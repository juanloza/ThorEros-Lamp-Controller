package thoreros.lamp.controller.Fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;

import thoreros.lamp.controller.R;
import thoreros.libraries.preference.PreferenceFragment;

public class ModeConfigFragment extends PreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fire_config, rootKey);
    }
}
