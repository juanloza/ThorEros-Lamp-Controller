package thoreros.lamp.controller.Fragment;

import android.os.Bundle;
import thoreros.lamp.controller.R;

public class FireConfigFragment extends ModeConfigFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fire_config, rootKey);
    }
}
