package thoreros.lamp.controller.Fragment;

import android.os.Bundle;
import thoreros.lamp.controller.R;

public class PlainConfigFragment extends ModeConfigFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.plain_config, rootKey);
    }
}
