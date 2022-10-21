package thoreros.lamp.controller.Fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreference;

import java.util.Objects;

import thoreros.lamp.controller.R;
import thoreros.libraries.preference.PreferenceFragment;
import thoreros.libraries.preference.colorpicker.ColorPickerPreference;
import thoreros.libraries.preference.palette.PalettePreference;

public class StarSparkleConfigFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.star_sparkle_config, rootKey);

        SwitchPreference prefRandomColor = findPreference("star_random_color");
        assert prefRandomColor != null;
        prefRandomColor.setOnPreferenceChangeListener(this);
        this.onPreferenceChange(prefRandomColor, prefRandomColor.isChecked());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
        if(Objects.equals(preference.getKey(), "star_random_color")){
            if(newValue == null){
                return true;
            }
            if((Boolean)newValue){
                ColorPickerPreference colorPref = findPreference("star_color");
                assert colorPref != null;
                //colorPref.setColor(0xff000000);
                colorPref.setVisible(false);

                PalettePreference palettePref = findPreference("star_palette");
                assert palettePref != null;
                palettePref.setVisible(true);
            }else{
                ColorPickerPreference colorPref = findPreference("star_color");
                assert colorPref != null;
                colorPref.setVisible(true);

                PalettePreference palettePref = findPreference("star_palette");
                assert palettePref != null;
                palettePref.setVisible(false);
            }
            return true;
        }
        return false;
    }
}
