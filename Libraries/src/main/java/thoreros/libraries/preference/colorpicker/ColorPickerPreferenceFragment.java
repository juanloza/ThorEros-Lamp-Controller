package thoreros.libraries.preference.colorpicker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceDialogFragmentCompat;

public class ColorPickerPreferenceFragment extends PreferenceDialogFragmentCompat {

	public static ColorPickerPreferenceFragment newInstance(String prefKey) {
		ColorPickerPreferenceFragment fragment = new ColorPickerPreferenceFragment();
		Bundle bundle = new Bundle(1);
		bundle.putString(PreferenceDialogFragmentCompat.ARG_KEY, prefKey);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	protected void onPrepareDialogBuilder(@NonNull AlertDialog.Builder builder) {
		super.onPrepareDialogBuilder(builder);
		((ColorPickerPreference)getPreference()).prepareDialogBuilder(builder);
	}

	@Override
	public void onDialogClosed(boolean b) {
	}
}
