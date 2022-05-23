package thoreros.libraries.preference.palette;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import thoreros.libraries.preference.R;

public class PalettePreference extends ListPreference {
    //CharSequence[] mPreviewThumbnails;
    Drawable[] mPreviewThumbnails;

    public PalettePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.PalettePreference, 0, 0);

        int thumbResId = a.getResourceId(R.styleable.PalettePreference_entryPreviewThumbnails,0);
        TypedArray thumbRes = a.getResources().obtainTypedArray(thumbResId);
        mPreviewThumbnails = new Drawable[thumbRes.length()];
        for (int i=0; i<thumbRes.length(); i++){
            mPreviewThumbnails[i]=thumbRes.getDrawable(i);
        }
        thumbRes.length();
        thumbRes.recycle();

        a.recycle();

        this.setDialogLayoutResource(R.layout.palette_preference);
    }

    @Override
    protected void onClick() {
        super.onClick();
    }
}
