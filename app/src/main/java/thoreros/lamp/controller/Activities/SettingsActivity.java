package thoreros.lamp.controller.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

import thoreros.lamp.controller.Fragment.ServerSettingsFragment;
import thoreros.lamp.controller.R;
import thoreros.lamp.controller.databinding.SettingsActivityBinding;

public class SettingsActivity extends AppCompatActivity {
    SettingsActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new ServerSettingsFragment())
                    .commit();
        }
        setSupportActionBar(binding.toolbarLayout.toolbar);

        Button testButton = binding.btnTestConnection;
        testButton.setOnClickListener(view -> {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String server = prefs.getString("host_name","");
            String port = prefs.getString("port","");
            String url = "http://"+server;
            url = url.replaceAll("[/\\s]+$","");
            if(!port.equals("")){
                url+=":"+port;
            }
            url+="/ping";

            RequestQueue queue = Volley.newRequestQueue(this);
            // Request a string response from the provided URL.
            StringRequest pingRequest = new StringRequest(Request.Method.GET, url, response -> {
                if(response.equals("OK")){
                    Toast.makeText(this,R.string.ping_ok, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this,R.string.ping_fails, Toast.LENGTH_SHORT).show();
            }, error -> Toast.makeText(this,R.string.ping_fails, Toast.LENGTH_SHORT).show());
//            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
//                Toast.makeText(this,"Response: "+ response, Toast.LENGTH_SHORT).show();
//            },error -> {
//                Toast.makeText(this,error.getCause().toString(), Toast.LENGTH_SHORT).show();
//            });

            queue.add(pingRequest);
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}