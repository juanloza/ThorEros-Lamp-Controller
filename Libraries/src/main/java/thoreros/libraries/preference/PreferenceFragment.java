package thoreros.libraries.preference;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import thoreros.libraries.preference.colorpicker.ColorPickerPreference;
import thoreros.libraries.preference.colorpicker.ColorPickerPreferenceFragment;
import thoreros.libraries.preference.palette.PalettePreference;
import thoreros.libraries.preference.palette.PalettePreferenceDialogFragment;

public abstract class PreferenceFragment extends PreferenceFragmentCompat {

    @SuppressWarnings("deprecation")
    @Override
    public void onDisplayPreferenceDialog(@NonNull Preference preference) {
        final DialogFragment f;
        if (getParentFragmentManager().findFragmentByTag(preference.getKey()) != null) {
            return;
        }
        if (preference instanceof ColorPickerPreference) {
            f = ColorPickerPreferenceFragment.newInstance(preference.getKey());
        } else if (preference instanceof PalettePreference) {
            f = PalettePreferenceDialogFragment.newInstance(preference.getKey());
        }else{
            super.onDisplayPreferenceDialog(preference);
            return;
        }
        f.setTargetFragment(this, 0);
        f.show(getParentFragmentManager(), preference.getKey());
    }
}
