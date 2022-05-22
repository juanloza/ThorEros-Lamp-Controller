package thoreros.lamp.controller.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
//import androidx.transition.TransitionInflater;
import android.transition.TransitionInflater;

import thoreros.lamp.controller.R;
import thoreros.libraries.preference.ColorPickerPreference;

public class PlainConfigFragment extends ModeConfigFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.plain_config, rootKey);
    }
}
