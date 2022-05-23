package thoreros.libraries.preference.palette;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreferenceDialogFragmentCompat;

public class PalettePreferenceDialogFragment extends ListPreferenceDialogFragmentCompat {

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
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);
    }

    @Override
    protected void onPrepareDialogBuilder(@NonNull AlertDialog.Builder builder) {
        //super.onPrepareDialogBuilder(builder);
    }
}
