package thoreros.lamp.controller.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

import thoreros.lamp.controller.R;
import thoreros.libraries.preference.ColorPickerPreference;

public class MainConfigFragment extends PreferenceFragmentCompat
        implements PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback,
        Preference.OnPreferenceChangeListener {
        ListPreference listEffect;
        PreferenceCategory catFuego;
        PreferenceCategory catColorPlano;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main_config, rootKey);

        catFuego = findPreference("FireConfig");
        catColorPlano = findPreference("PlainColorConfig");

        listEffect = findPreference("lamp_mode");
        assert listEffect != null;
        listEffect.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onDisplayPreferenceDialog(@NonNull Preference preference) {
        if (preference instanceof ColorPickerPreference) {
            ((ColorPickerPreference) preference).showDialog(this, 0);
        } else super.onDisplayPreferenceDialog(preference);
    }


    @Override
    public boolean onPreferenceDisplayDialog(@NonNull PreferenceFragmentCompat caller, @NonNull Preference pref) {
        return false;
    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
        if(Objects.equals(preference.getKey(), "lamp_mode")){
            switch (Integer.parseInt((String)newValue)){
                case 0:
                    catFuego.setVisible(true);
                    catColorPlano.setVisible(false);
                    break;
                case 1:
                    catFuego.setVisible(false);
                    catColorPlano.setVisible(true);
                    break;
            }
            return true;
        }
        return false;
    }
}
