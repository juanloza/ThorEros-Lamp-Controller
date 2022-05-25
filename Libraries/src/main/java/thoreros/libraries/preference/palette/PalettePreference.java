package thoreros.libraries.preference.palette;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceViewHolder;

import thoreros.libraries.preference.R;

public class PalettePreference extends ListPreference {
    //CharSequence[] mPreviewThumbnails;
    Drawable[] mEntryImages;
    int[] mEntryImagesResIds;
    private Drawable mSelectedImage;
    private int mSelectedImageResId;

    public PalettePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.PalettePreference, 0, 0);

        int thumbResId = a.getResourceId(R.styleable.PalettePreference_entryPreviewThumbnails,0);
        TypedArray thumbRes = a.getResources().obtainTypedArray(thumbResId);
        mEntryImages = new Drawable[thumbRes.length()];
        mEntryImagesResIds = new int[thumbRes.length()];
        for (int i=0; i<thumbRes.length(); i++){
            mEntryImages[i]=thumbRes.getDrawable(i);
            mEntryImagesResIds[i]=thumbRes.getResourceId(i,0);
        }
        thumbRes.length();
        thumbRes.recycle();
        a.recycle();

        this.setLayoutResource(R.layout.palette_preference);
    }

    public Drawable[] getEntryImages() {
        return mEntryImages;
    }
    public int[] getEntryImagesResIds() {
        return mEntryImagesResIds;
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        final ImageView selectedImage = (ImageView) holder.findViewById(R.id.palette_image);

        mSelectedImage.setDither(true);
        selectedImage.setImageDrawable(mSelectedImage);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        if(value != null) {
            int idx = this.findIndexOfValue(value);
            mSelectedImageResId = mEntryImagesResIds[idx];
            mSelectedImage = mEntryImages[idx];
        }
    }

}