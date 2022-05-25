package thoreros.libraries.preference.palette;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreferenceDialogFragmentCompat;

public class PalettePreferenceDialogFragment extends ListPreferenceDialogFragmentCompat {

    private static final String SAVE_STATE_INDEX = "PalettePreferenceDialogFragment.index";
    private static final String SAVE_STATE_ENTRIES = "PalettePreferenceDialogFragment.entries";
    private static final String SAVE_STATE_ENTRY_VALUES =
            "PalettePreferenceDialogFragment.entryValues";
    private static final String SAVE_STATE_ENTRY_IMAGES =
            "PalettePreferenceDialogFragment.entryImages";
    private static final String SAVE_STATE_ENTRY_IMAGES_RESOURCES =
            "PalettePreferenceDialogFragment.entryImagesResIds";

    int mClickedDialogEntryIndex;
    protected CharSequence[] mEntries;
    protected CharSequence[] mEntryValues;
    protected Drawable[] mEntryImages;
    protected int[] mEntryImagesResIds;

    @NonNull
    public static PalettePreferenceDialogFragment newInstance(String key) {
        final PalettePreferenceDialogFragment fragment =
                new PalettePreferenceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            final PalettePreference preference = getPalettePreference();

            if (preference.getEntries() == null || preference.getEntryValues() == null) {
                throw new IllegalStateException(
                        "ListPreference requires an entries array and an entryValues array.");
            }

            this.mClickedDialogEntryIndex = preference.findIndexOfValue(preference.getValue());
            mEntries = preference.getEntries();
            mEntryValues = preference.getEntryValues();
            mEntryImagesResIds = preference.getEntryImagesResIds();
        } else {
            mClickedDialogEntryIndex = savedInstanceState.getInt(SAVE_STATE_INDEX, 0);
            mEntries = savedInstanceState.getCharSequenceArray(SAVE_STATE_ENTRIES);
            mEntryValues = savedInstanceState.getCharSequenceArray(SAVE_STATE_ENTRY_VALUES);
            mEntryImagesResIds = savedInstanceState.getIntArray(SAVE_STATE_ENTRY_IMAGES_RESOURCES);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_STATE_INDEX, mClickedDialogEntryIndex);
        outState.putCharSequenceArray(SAVE_STATE_ENTRIES, mEntries);
        outState.putCharSequenceArray(SAVE_STATE_ENTRY_VALUES, mEntryValues);
        outState.putIntArray(SAVE_STATE_ENTRY_IMAGES_RESOURCES, mEntryImagesResIds);
    }

    private PalettePreference getPalettePreference() {
        return (PalettePreference) getPreference();
    }

    @Override
    protected void onPrepareDialogBuilder(@NonNull AlertDialog.Builder builder) {
        ListAdapter adapter = new PaletteAdapter(
                getContext(), mEntries, mEntryImagesResIds, mClickedDialogEntryIndex,
                thoreros.libraries.preference.R.layout.palette_item, android.R.id.text1, android.R.id.button1);
        builder.setAdapter(adapter, null);
        super.onPrepareDialogBuilder(builder);
    }
}
