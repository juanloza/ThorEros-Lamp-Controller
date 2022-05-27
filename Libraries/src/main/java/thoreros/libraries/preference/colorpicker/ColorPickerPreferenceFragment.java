package thoreros.libraries.preference.colorpicker;

import android.graphics.Color;
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

		ColorPickerPreference pref = (ColorPickerPreference)getPreference();
		final ColorPickerView picker = new ColorPickerView(getContext());
		picker.setColor(pref.getPersistedColor());
		picker.showValue(pref.isValueVisible(), pref.useMaxValueIfHidden());
		picker.showAlpha(pref.isAlphaVisible());
		picker.showHex(pref.isHexVisible());
		picker.showPreview(pref.isPreviewVisible());
		builder.setTitle(null)
				.setView(picker)
				.setPositiveButton(pref.getPositiveButtonText(), (dialog, which) -> {
					final int color = picker.getColor();
					if (pref.callChangeListener(color)) {
						pref.setColor(color);
					}
				});
		if (pref.getSelectNoneButtonText() != null) {
			builder.setNeutralButton(pref.getSelectNoneButtonText(), (dialog, which) -> {
				if (pref.callChangeListener(null)) {
					pref.setColor(null);
				}
			});
		}
	}

	@Override
	public void onDialogClosed(boolean b) {
	}
}
