package thoreros.lamp.controller.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import java.util.Objects;

import thoreros.lamp.controller.R;
import thoreros.libraries.preference.PreferenceFragment;
import thoreros.libraries.preference.palette.PalettePreference;

public class MainConfigFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {
    ListPreference listEffect;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.mode_config, rootKey);

        listEffect = findPreference("lamp_mode");
        assert listEffect != null;
        listEffect.setOnPreferenceChangeListener(this);
        if (savedInstanceState == null) {
            onPreferenceChange(listEffect, listEffect.getValue());
        }
    }


    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
        if(Objects.equals(preference.getKey(), "lamp_mode")){
            if(newValue == null){
                return true;
            }
            PreferenceFragment destFragment = null;
            String[] textModes = getResources().getStringArray(R.array.modo_entries);
            String textMode = textModes[Integer.parseInt((String)newValue)];
            switch (textMode){
                case "Test":
                    Fragment fragment = getParentFragmentManager().findFragmentByTag("mode_config");
                    //No config, so remove config fragment if exists
                    if(fragment != null){
                        getParentFragmentManager().beginTransaction()
                                .remove(fragment)
                                .setCustomAnimations(
                                        R.anim.slide_in,  // enter
                                        R.anim.slide_out,  // exit
                                        R.anim.slide_in,   // popEnter
                                        R.anim.slide_out  // popExit
                                ).commit();
                    }
                    return true;
                case "Color plano": //Color plano
                    destFragment = new PlainConfigFragment();
                    break;
                case "Paleta de color":
                    destFragment = new ShowPaletteConfigFragment();
                    break;
                case "Fuego":
                    destFragment = new FireConfigFragment();
                    break;
                case "Cometa":
                    destFragment = new CometConfigFragment();
                    break;
                case "Estrellas":
                    destFragment = new StarSparkleConfigFragment();
                    break;
                case "Pelotas":
                    break;
            }
            if(destFragment != null){
                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.slide_out,  // exit
                                R.anim.slide_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.mode_config, destFragment,"mode_config")
                        .commit();
            }else{
                Toast.makeText(this.getContext(), "Option not supported yet", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return false;
    }
}
