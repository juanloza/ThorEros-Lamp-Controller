package thoreros.lamp.controller.Fragment;

import android.os.Bundle;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.SeekBarPreference;

import thoreros.lamp.controller.R;
import thoreros.libraries.preference.PreferenceFragment;
import thoreros.libraries.preference.colorpicker.ColorPickerPreference;
import thoreros.libraries.preference.colorpicker.ObservableColor;

public class PlainConfigFragment extends PreferenceFragment {
//    SeekBarPreference brightness;
//    ColorPickerPreference colorpicker;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.plain_config, rootKey);

        //Test change color value with external sekkbarPreference
//        colorpicker = findPreference("plain_color");
//        brightness = findPreference("plain_brighness");
//        assert brightness != null;
//        brightness.setOnPreferenceChangeListener((preference, newValue) -> {
//            ObservableColor color = new ObservableColor(colorpicker.getColor());
//            float convertedValue = (float) (Integer) newValue /0xff;
//            color.updateValue(convertedValue,null);
//            colorpicker.setColor(color.getColor());
//            return true;
//        });
    }
}
