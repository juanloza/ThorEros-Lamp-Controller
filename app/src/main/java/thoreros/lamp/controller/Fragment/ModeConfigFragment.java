package thoreros.lamp.controller.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import thoreros.lamp.controller.R;
import thoreros.libraries.preference.ColorPickerPreference;

public class ModeConfigFragment extends PreferenceFragmentCompat {
    protected String mName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fire_config, rootKey);
    }

    @Override
    public void onDisplayPreferenceDialog(@NonNull Preference preference) {
        if (preference instanceof ColorPickerPreference) {
            ((ColorPickerPreference) preference).showDialog(this, 0);
        } else super.onDisplayPreferenceDialog(preference);
    }

    public String getName(){
        return mName;
    }
}
