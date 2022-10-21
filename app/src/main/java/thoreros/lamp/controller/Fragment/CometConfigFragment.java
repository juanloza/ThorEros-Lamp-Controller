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

public class CometConfigFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.comet_config, rootKey);

        SwitchPreference prefRandomColor = findPreference("comet_random_color");
        assert prefRandomColor != null;
        prefRandomColor.setOnPreferenceChangeListener(this);
        this.onPreferenceChange(prefRandomColor, prefRandomColor.isChecked());

        SwitchPreference prefRandomDecay = findPreference("comet_random_decay");
        assert prefRandomDecay != null;
        prefRandomDecay.setOnPreferenceChangeListener(this);
        this.onPreferenceChange(prefRandomDecay, prefRandomDecay.isChecked());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //this.getListView().setOverScrollMode(ListView.OVER_SCROLL_NEVER);
    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
        if(Objects.equals(preference.getKey(), "comet_random_color")){
            if(newValue == null){
                return true;
            }
            if((Boolean)newValue){
                ColorPickerPreference colorPref = findPreference("comet_color");
                assert colorPref != null;
                //colorPref.setColor(0xff000000);
                colorPref.setVisible(false);

                PalettePreference palettePref = findPreference("comet_palette");
                assert palettePref != null;
                palettePref.setVisible(true);
            }else{
                ColorPickerPreference colorPref = findPreference("comet_color");
                assert colorPref != null;
                colorPref.setVisible(true);

                PalettePreference palettePref = findPreference("comet_palette");
                assert palettePref != null;
                palettePref.setVisible(false);
            }
            return true;
        }else if(Objects.equals(preference.getKey(), "comet_random_decay")){
            if(newValue == null){
                return true;
            }
            if((Boolean)newValue){
                SeekBarPreference decayProbabilityPref = findPreference("comet_decay_probability");
                assert decayProbabilityPref != null;
                decayProbabilityPref.setVisible(true);
            }else{
                SeekBarPreference decayProbabilityPref = findPreference("comet_decay_probability");
                assert decayProbabilityPref != null;
                decayProbabilityPref.setVisible(false);
            }
            return true;
        }
        return false;
    }
}
