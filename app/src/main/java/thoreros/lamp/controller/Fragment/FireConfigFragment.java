package thoreros.lamp.controller.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
//import androidx.transition.TransitionInflater;
import android.transition.TransitionInflater;

//import com.github.koston.preference.ColorPreferenceFragmentCompat;

import java.util.Objects;

import thoreros.lamp.controller.R;
import thoreros.libraries.preference.ColorPickerPreference;

public class FireConfigFragment extends ModeConfigFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fire_config, rootKey);
    }
}
