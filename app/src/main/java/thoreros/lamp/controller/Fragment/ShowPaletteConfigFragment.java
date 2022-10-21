package thoreros.lamp.controller.Fragment;

import android.os.Bundle;

import androidx.preference.SeekBarPreference;

import thoreros.lamp.controller.R;
import thoreros.libraries.preference.PreferenceFragment;
import thoreros.libraries.preference.colorpicker.ColorPickerPreference;

public class ShowPaletteConfigFragment extends PreferenceFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.show_palette_config, rootKey);

        SeekBarPreference stripOffsetPref = findPreference("palette_stripOffset");
        assert stripOffsetPref != null;
        stripOffsetPref.setMin(getResources().getInteger(R.integer.byte_negative_max));
        stripOffsetPref.setDefaultValue(getResources().getInteger(R.integer.stripOffsetDefault));
    }
}
