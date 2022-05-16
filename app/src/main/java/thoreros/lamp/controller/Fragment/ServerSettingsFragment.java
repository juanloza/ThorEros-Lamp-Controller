package thoreros.lamp.controller.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

import thoreros.lamp.controller.R;

public class ServerSettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.server_preferences, rootKey);
    }
}
