package thoreros.lamp.controller.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import thoreros.lamp.controller.Fragment.MainConfigFragment;
import thoreros.lamp.controller.R;
import thoreros.lamp.controller.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected MainActivityBinding binding;
    protected RequestQueue queue;
    protected String url;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarLayout.toolbar);


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_config, new MainConfigFragment())
                    .commit();
        }
        setSupportActionBar(binding.toolbarLayout.toolbar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        String server = sharedPreferences.getString("host_name","");
        String port = sharedPreferences.getString("port","");
        url = "http://"+server;
        url = url.replaceAll("[/\\s]+$","");
        if(!port.equals("")){
            url+=":"+port;
        }
        url+="/setConfig";
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
            int mode = Integer.parseInt(sharedPreferences.getString("lamp_mode", "0"));
            String[] modes = getResources().getStringArray(R.array.modo_entries);
            String prefTxt = "Modo: "+modes[mode]+"\n";
            switch (mode) {
                case 0:
                    prefTxt += "Cooling: " + sharedPreferences.getInt("fire_cooling", 0) + "\n";
                    prefTxt += "Sparking: " + sharedPreferences.getInt("fire_sparking", 0) + "\n";
                    String[] palettes = getResources().getStringArray(R.array.palettes_entries);
                    int palette = Integer.parseInt(sharedPreferences.getString("fire_palette", "0"));
                    prefTxt += "Paleta de color: " + palettes[palette] + "\n";
                    break;
                case 1:
                    int color = sharedPreferences.getInt("plain_color", 0);
                    prefTxt += "Color: #" + String.format("%06x", color & 0x00ffffff);
                    break;
            }

            if(queue==null){
                queue = Volley.newRequestQueue(this);
            }
            StringRequest configRequest = new StringRequest(Request.Method.POST, url, response -> {
                if(response.equals("OK")){
                    Toast.makeText(this,"Datos enviados", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this,R.string.ping_fails, Toast.LENGTH_SHORT).show();
            }, error -> Toast.makeText(this,R.string.ping_fails, Toast.LENGTH_SHORT).show())
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return getConfigRequestParams();
                }
            };
            Toast.makeText(this, prefTxt, Toast.LENGTH_LONG).show();
            return true;
        }else if(item.getItemId() == R.id.btn_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected Map<String,String> getConfigRequestParams(){
        Map<String,String> params = new HashMap<>();
        String modo = sharedPreferences.getString("lamp_mode", "0");
        params.put("mode",modo);
        switch (modo){
            case "0":
                params.put("cooling", String.valueOf(sharedPreferences.getInt("fire_cooling", 0)));
                params.put("sparking", String.valueOf(sharedPreferences.getInt("fire_sparking", 0)));
                params.put("palette", sharedPreferences.getString("fire_palette", "0"));
                break;
            case "1":
                params.put("color", String.valueOf(sharedPreferences.getInt("plain_color", 0)));
                break;
        }
        return params;
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Toast.makeText(this, sharedPreferences.getAll().toString(), Toast.LENGTH_SHORT).show();
    }
}