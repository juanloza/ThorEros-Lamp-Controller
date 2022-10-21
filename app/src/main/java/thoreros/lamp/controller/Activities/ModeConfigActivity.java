package thoreros.lamp.controller.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import thoreros.lamp.controller.Fragment.MainConfigFragment;
import thoreros.lamp.controller.R;
import thoreros.lamp.controller.databinding.ModeConfigActivityBinding;

public class ModeConfigActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected ModeConfigActivityBinding binding;
    protected SharedPreferences sharedPreferences;
    protected ProgressBar progressBar;
    protected RequestQueue queue;
    protected Boolean requestRunning;
    protected StringRequest queuedRequest;
    protected String baseUrl;
    protected Map<String, String> paramsMapping =  new HashMap<>();

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

        this.getServerConfig();

        //Only need to put params who need to single update on the fly
        paramsMapping.put("lamp_enabled", "enabled");
        paramsMapping.put("lamp_mode", "mode");

        paramsMapping.put("plain_color", "color");
        paramsMapping.put("plain_brightness", "brightness");
        paramsMapping.put("plain_num_leds", "numleds");

        paramsMapping.put("palette_palette", "palette");
        paramsMapping.put("palette_reverse", "reverse");
        paramsMapping.put("palette_brightness", "brightness");
        paramsMapping.put("palette_scale", "scale");
        paramsMapping.put("palette_speed", "speed");
        paramsMapping.put("palette_step", "step");
        paramsMapping.put("palette_stripOffset", "stripoffset");

        paramsMapping.put("fire_cooling", "cooling");
        paramsMapping.put("fire_sparking", "sparking");
        paramsMapping.put("fire_palette", "palette");
        paramsMapping.put("fire_brightness", "brightness");

        paramsMapping.put("comet_bgcolor", "bgcolor");
        paramsMapping.put("comet_bgbrightness", "bgbrightness");
        paramsMapping.put("comet_color", "color");
        paramsMapping.put("comet_palette", "palette");
        paramsMapping.put("comet_brightness", "brightness");
        paramsMapping.put("comet_size", "size");
        paramsMapping.put("comet_new_random", "newprobability");
        paramsMapping.put("comet_random_decay", "randomDecay");
        paramsMapping.put("comet_decay_probability", "decayProbability");
        paramsMapping.put("comet_decay", "decay");
        paramsMapping.put("comet_max_total", "maxtotal");
        paramsMapping.put("comet_max_strip", "maxstrip");
        paramsMapping.put("comet_speed", "speed");

        paramsMapping.put("star_bgcolor", "bgcolor");
        paramsMapping.put("star_bgbrightness", "bgbrightness");
        paramsMapping.put("star_color", "color");
        paramsMapping.put("star_palette", "palette");
        paramsMapping.put("star_brightness", "brightness");
        paramsMapping.put("star_probability", "starprobability");
        paramsMapping.put("star_fade_amount", "fadeStar");
        paramsMapping.put("star_falling", "fallstars");

        paramsMapping.put("balls_bgcolor", "bgcolor");
        paramsMapping.put("balls_bgbrightness", "bgbrightness");
        paramsMapping.put("balls_color", "color");
        paramsMapping.put("balls_palette", "palette");
        paramsMapping.put("balls_brightness", "brightness");
        paramsMapping.put("balls_gravity", "gravity");
        paramsMapping.put("balls_probability", "newprobability");
        paramsMapping.put("balls_max_total", "maxtotal");
        paramsMapping.put("balls_max_strip", "maxstrip");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.getServerConfig();
    }

    private void getServerConfig() {
        String server = sharedPreferences.getString("host_name","");
        String port = sharedPreferences.getString("port","");
        baseUrl = "http://"+server;
        baseUrl = baseUrl.replaceAll("[/\\s]+$","");
        if(!port.equals("")){
            baseUrl +=":"+port;
        }
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
        if(paramsMapping.containsKey(key)){
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

        if(queue==null){
            queue = Volley.newRequestQueue(this);
        }
        int requestMode = Request.Method.GET;
        String requestUrl = getRequestUrl(requestType,requestMode, paramKey);
        StringRequest configRequest = new StringRequest(requestMode, requestUrl, response -> {
            if(response.equals("OK")){
                //Toast.makeText(this,"Datos enviados", Toast.LENGTH_SHORT).show();
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
            if(!paramsMapping.containsKey(paramKey)){
                return params;
            }
            params.put("param",paramsMapping.get(paramKey));
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
        String[] textModes = getResources().getStringArray(R.array.modo_entries);
        String textMode = textModes[Integer.parseInt(modo)];
        params.put("mode",modo);
        switch (textMode){
            case "Test":
                break;
            case "Color plano":
                params.put("color", String.valueOf(sharedPreferences.getInt("plain_color", 0)));
                params.put("brightness", String.valueOf(sharedPreferences.getInt("plain_brightness", 255)));
                params.put("numleds", String.valueOf(sharedPreferences.getInt("plain_num_leds", 4)));
                break;
            case "Paleta de color":
                params.put("palette", sharedPreferences.getString("palette_palette", "0"));
                boolean reverse = sharedPreferences.getBoolean("palette_reverse",false);
                params.put("reverse",reverse ? "1":"0");
                params.put("brightness", String.valueOf(sharedPreferences.getInt("palette_brightness", 255)));
                params.put("scale", String.valueOf(sharedPreferences.getInt("palette_scale", 255)));
                params.put("speed", String.valueOf(sharedPreferences.getInt("palette_speed", 127)));
                params.put("step", String.valueOf(sharedPreferences.getInt("palette_step", 1)));
                params.put("stripoffset", String.valueOf(sharedPreferences.getInt("palette_stripOffset", -20)));
                break;
            case "Fuego":
                params.put("cooling", String.valueOf(sharedPreferences.getInt("fire_cooling", 0)));
                params.put("sparking", String.valueOf(sharedPreferences.getInt("fire_sparking", 0)));
                params.put("palette", sharedPreferences.getString("fire_palette", "0"));
                params.put("brightness", String.valueOf(sharedPreferences.getInt("fire_brightness", 255)));
                break;
            case "Cometa"://Cometa
                params.put("bgcolor", String.valueOf(sharedPreferences.getInt("comet_bgcolor", 0)));
                params.put("bgbrightness", String.valueOf(sharedPreferences.getInt("comet_bgbrightness", 255)));
                boolean cometRandomColor = sharedPreferences.getBoolean("comet_random_color", false);
                if(cometRandomColor){
                    params.put("color", String.valueOf(0xff000000)); //Color when random is black
                    params.put("palette", sharedPreferences.getString("comet_palette", "0"));
                }else{
                    params.put("color", String.valueOf(sharedPreferences.getInt("comet_color", 0)));
                }
                params.put("brightness", String.valueOf(sharedPreferences.getInt("comet_brightness", 255)));
                params.put("size", String.valueOf(sharedPreferences.getInt("comet_size", 5)));
                params.put("newprobability", String.valueOf(sharedPreferences.getInt("comet_new_random", 5)));
                boolean randomDecay = sharedPreferences.getBoolean("comet_random_decay",false);
                params.put("randomdecay",randomDecay ? "1":"0");
                if(randomDecay){
                    params.put("decayprobability", String.valueOf(sharedPreferences.getInt("comet_decay_probability", 128)));
                }
                params.put("decay", String.valueOf(sharedPreferences.getInt("comet_decay", 64)));
                params.put("maxtotal", String.valueOf(sharedPreferences.getInt("comet_max_total", 20)));
                params.put("maxstrip", String.valueOf(sharedPreferences.getInt("comet_max_strip", 2)));
                params.put("speed", String.valueOf(sharedPreferences.getInt("comet_speed", 60)));
                break;
            case "Estrellas":
                params.put("bgcolor", String.valueOf(sharedPreferences.getInt("star_bgcolor", 0)));
                params.put("bgbrightness", String.valueOf(sharedPreferences.getInt("star_bgbrightness", 255)));
                boolean randomStarColor = sharedPreferences.getBoolean("star_random_color", false);
                if(randomStarColor){
                    params.put("color", String.valueOf(0xff000000)); //Color when random is black
                    params.put("palette", sharedPreferences.getString("star_palette", "0"));
                }else{
                    params.put("color", String.valueOf(sharedPreferences.getInt("star_color", 0)));
                }
                params.put("brightness", String.valueOf(sharedPreferences.getInt("star_brightness", 255)));

                params.put("starprobability", String.valueOf(sharedPreferences.getInt("star_probability", 15)));
                params.put("fadeStar", String.valueOf(sharedPreferences.getInt("star_fade_amount", 16)));
                boolean fallStars = sharedPreferences.getBoolean("star_falling",false);
                params.put("fallstars",fallStars ? "1":"0");
                if(fallStars){
                    params.put("speed", String.valueOf(sharedPreferences.getInt("star_falling_speed", 32)));
                }
                break;
            case "Pelotas":
                params.put("bgcolor", String.valueOf(sharedPreferences.getInt("balls_bgcolor", 0)));
                params.put("bgbrightness", String.valueOf(sharedPreferences.getInt("balls_bgbrightness", 255)));
                boolean ballsRandomColor = sharedPreferences.getBoolean("balls_random_color", false);
                if(ballsRandomColor){
                    params.put("color", String.valueOf(0xff000000)); //Color when random is black
                    params.put("palette", sharedPreferences.getString("balls_palette", "0"));
                }else{
                    params.put("color", String.valueOf(sharedPreferences.getInt("balls_color", 0)));
                }
                params.put("brightness", String.valueOf(sharedPreferences.getInt("balls_brightness", 255)));
                params.put("gravity", String.valueOf(sharedPreferences.getInt("balls_gravity", 255)));
                params.put("newprobability", String.valueOf(sharedPreferences.getInt("balls_probability", 5)));
                params.put("maxtotal", String.valueOf(sharedPreferences.getInt("balls_max_total", 255)));
                params.put("maxstrip", String.valueOf(sharedPreferences.getInt("balls_max_strip", 255)));
                break;
        }
        return params;
    }
}