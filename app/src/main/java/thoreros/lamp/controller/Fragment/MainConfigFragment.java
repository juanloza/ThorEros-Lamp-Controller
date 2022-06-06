package thoreros.lamp.controller.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import java.util.Objects;

import thoreros.lamp.controller.R;
import thoreros.libraries.preference.PreferenceFragment;

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
            switch (Integer.parseInt((String)newValue)){
                case 0:
                    destFragment = new FireConfigFragment();
                    break;
                case 1:
                    destFragment = new PlainConfigFragment();
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
                        .replace(R.id.mode_config, destFragment)
                        .commit();
            }else{
                Toast.makeText(this.getContext(), "Option not supported yet", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return false;
    }
}
