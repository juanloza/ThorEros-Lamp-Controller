package thoreros.lamp.controller.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import thoreros.lamp.controller.Fragment.MainConfigFragment;
import thoreros.lamp.controller.R;
import thoreros.lamp.controller.databinding.ModeConfigActivityBinding;

public class ModeConfigActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected ModeConfigActivityBinding binding;
    protected RequestQueue queue;
    protected String baseUrl;
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    Boolean requestRunning;
    StringRequest queuedRequest;
    protected enum RequestType {
        CHANGE_MODE_CONFIG,
        CHANGE_SINGLE_PARAM
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ModeConfigActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarLayout.toolbar);

        progressBar = binding.progressbar;
        requestRunning = false;

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_config, new MainConfigFragment())
                    .commit();
        }
        setSupportActionBar(binding.toolbarLayout.toolbar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        binding.btnSend2.setOnClickListener(view -> sendConfigRequest(RequestType.CHANGE_MODE_CONFIG,null));

        String server = sharedPreferences.getString("host_name","");
        String port = sharedPreferences.getString("port","");
        baseUrl = "http://"+server;
        baseUrl = baseUrl.replaceAll("[/\\s]+$","");
        if(!port.equals("")){
            baseUrl +=":"+port;
        }
        //TODO: sync config
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //TODO: sync config
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btn_send){
            sendConfigRequest(RequestType.CHANGE_MODE_CONFIG,null);
            return true;
        }else if(item.getItemId() == R.id.btn_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(Objects.equals(key, "lamp_enabled")) {
            sendConfigRequest(RequestType.CHANGE_SINGLE_PARAM, key);
        }
    }

    protected String getRequestUrl(RequestType requestType, int requestMode, @Nullable String paramKey){
        String url = this.baseUrl;
        switch (requestType){
            case CHANGE_SINGLE_PARAM:
                url+="/setSingleParam";
                break;
            case CHANGE_MODE_CONFIG:
                url+="/setModeConfig";
                break;
        }
        if(requestMode==Request.Method.GET){
            StringBuilder encodedParams = new StringBuilder();
            try {
                for (Map.Entry<String, String> entry : getConfigRequestParams(paramKey).entrySet()) {
                    if (entry.getKey() == null || entry.getValue() == null) {
                        throw new IllegalArgumentException(
                                String.format(
                                        "Request#getParams() or Request#getPostParams() returned a map "
                                                + "containing a null key or value: (%s, %s). All keys "
                                                + "and values must be non-null.",
                                        entry.getKey(), entry.getValue()));
                    }
                    encodedParams.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    encodedParams.append('=');
                    encodedParams.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                    encodedParams.append('&');
                }
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException("Encoding not supported: UTF-8", uee);
            }
            url+="?"+ encodedParams;
        }
        return url;
    }

    protected void sendConfigRequest(RequestType requestType, @Nullable String paramKey){
        progressBar.setVisibility(View.VISIBLE);
//        int mode = Integer.parseInt(sharedPreferences.getString("lamp_mode", "0"));
//        String[] modes = getResources().getStringArray(R.array.modo_entries);
//        String prefTxt = "Modo: "+modes[mode]+"\n";
//        switch (mode) {
//            case 0:
//                prefTxt += "Cooling: " + sharedPreferences.getInt("fire_cooling", 0) + "\n";
//                prefTxt += "Sparking: " + sharedPreferences.getInt("fire_sparking", 0) + "\n";
//                String[] palettes = getResources().getStringArray(R.array.palettes_entries);
//                int palette = Integer.parseInt(sharedPreferences.getString("fire_palette", "0"));
//                prefTxt += "Paleta de color: " + palettes[palette] + "\n";
//                break;
//            case 1:
//                int color = sharedPreferences.getInt("plain_color", 0);
//                prefTxt += "Color: #" + String.format("%06x", color & 0x00ffffff) + "\n";
//                prefTxt += "Brightness: #" + sharedPreferences.getInt("plain_brighness", 255) + "\n";
//                prefTxt += "Leds encendidos: #" + sharedPreferences.getInt("plain_num_leds", 4) + "\n";
//                break;
//        }

        if(queue==null){
            queue = Volley.newRequestQueue(this);
        }
        int requestMode = Request.Method.GET;
        String requestUrl = getRequestUrl(requestType,requestMode, paramKey);
        StringRequest configRequest = new StringRequest(requestMode, requestUrl, response -> {
            if(response.equals("OK")){
                Toast.makeText(this,"Datos enviados", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                requestRunning=false;
                runQueuedRequest();
                return;
            }
            Toast.makeText(this,response, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            requestRunning=false;
            runQueuedRequest();
        }, error -> {
            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            requestRunning=false;
            runQueuedRequest();
        });

        if(requestRunning){  //Another request is running
            queuedRequest = configRequest;
        }else{
            requestRunning=true;
            queue.add(configRequest);
        }
        //Toast.makeText(this, prefTxt, Toast.LENGTH_LONG).show();
    }

    protected void runQueuedRequest(){
        if(queuedRequest != null){
            if(queue==null){
                queue = Volley.newRequestQueue(this);
            }
            progressBar.setVisibility(View.VISIBLE);
            requestRunning=true;
            queue.add(queuedRequest);
            queuedRequest = null;

        }
    }

    protected Map<String,String> getConfigRequestParams(@Nullable String paramKey){
        Map<String,String> params = new HashMap<>();
        //Single param mode
        if(paramKey != null){
            Map<String, ?> allConfig = sharedPreferences.getAll();
            params.put("param",paramKey);
            Object value = allConfig.get(paramKey);
            assert value != null;
            if(value.getClass() == Boolean.class){
                value = (Boolean)value?"1":"0";
            }else{
                value = value.toString();
            }
            params.put("value",(String)value);
            return params;
        }

        //All mode params
        boolean enabled = sharedPreferences.getBoolean("lamp_enabled",false);
        params.put("enabled",enabled ? "1":"0");
        String modo = sharedPreferences.getString("lamp_mode", "0");
        params.put("mode",modo);
        switch (modo){
            case "0"://Test
                break;
            case "1"://Color plano
                params.put("color", String.valueOf(sharedPreferences.getInt("plain_color", 0)));
                params.put("brightness", String.valueOf(sharedPreferences.getInt("plain_brightness", 255)));
                params.put("numleds", String.valueOf(sharedPreferences.getInt("plain_num_leds", 4)));
                break;
            case "2"://Fuego
                params.put("cooling", String.valueOf(sharedPreferences.getInt("fire_cooling", 0)));
                params.put("sparking", String.valueOf(sharedPreferences.getInt("fire_sparking", 0)));
                params.put("palette", sharedPreferences.getString("fire_palette", "0"));
                params.put("brightness", String.valueOf(sharedPreferences.getInt("fire_brightness", 255)));
                break;
        }
        return params;
    }
}