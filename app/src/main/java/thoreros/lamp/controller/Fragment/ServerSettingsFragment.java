package thoreros.lamp.controller.Fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import thoreros.lamp.controller.R;

public class ServerSettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.server_preferences, rootKey);
    }
}
