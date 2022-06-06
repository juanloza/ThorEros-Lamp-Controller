package thoreros.libraries.preference.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;
import androidx.preference.PreferenceViewHolder;

import java.util.Objects;

import thoreros.libraries.preference.R;

public class ColorPickerPreference extends DialogPreference {
    private final String selectNoneButtonText;
    private static final Integer defaultColor=Color.TRANSPARENT;
    private final String noneSelectedSummaryText;
    private final boolean mShowValue;
    private final boolean mValueMaxIfHidden;
    private final boolean mShowAlpha;
    private final boolean mShowHex;
    private final boolean mShowPreview;

    public ColorPickerPreference(Context context) {
        this(context, null);
    }
    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorPicker, 0, 0);
            selectNoneButtonText = a.getString(R.styleable.ColorPicker_selectNoneButtonText);
            noneSelectedSummaryText = a.getString(R.styleable.ColorPicker_noneSelectedSummaryText);
            mShowValue = a.getBoolean(R.styleable.ColorPicker_showValue, true);
            mValueMaxIfHidden = a.getBoolean(R.styleable.ColorPicker_valueMaxIfHidden, true);
            mShowAlpha = a.getBoolean(R.styleable.ColorPicker_showAlpha, true);
            mShowHex = a.getBoolean(R.styleable.ColorPicker_showHex, true);
            mShowPreview = a.getBoolean(R.styleable.ColorPicker_showPreview, true);
        }
        else {
            selectNoneButtonText = null;
            noneSelectedSummaryText = null;
            mShowValue = true;
            mValueMaxIfHidden = true;
            mShowAlpha = true;
            mShowHex = true;
            mShowPreview = true;
        }

        this.setWidgetLayoutResource(R.layout.colorpicker_widget);
    }


    @Nullable
    public CharSequence getSelectNoneButtonText() {
        return selectNoneButtonText;
    }

    @Override
    public CharSequence getSummary() {
        return (noneSelectedSummaryText != null && !hasPersistedColor())
                ? noneSelectedSummaryText
                : super.getSummary();
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        //Set widgetColor
        View widget = holder.itemView;
        ImageView imageContainer = widget.findViewById(R.id.imageWidgetContainer);
        ColorDrawable circle = (ColorDrawable)imageContainer.getDrawable();
        int color = getPersistedColor();
        circle.setColor(color);
        super.onBindViewHolder(holder);
    }

    @Override
    protected Object onGetDefaultValue(@NonNull TypedArray a, int index) {
        int passedDefaultColor = defaultColor;
        if (a.peekValue(index) != null) {
            int type = a.peekValue(index).type;
            if (type == TypedValue.TYPE_STRING) {
                try{
                    passedDefaultColor = Color.parseColor(standardiseColorDigits(a.getString(index)));
                }catch (Exception ignored){ //Already set default color
                }
            }
            else if (TypedValue.TYPE_FIRST_COLOR_INT <= type && type <= TypedValue.TYPE_LAST_COLOR_INT) {
                passedDefaultColor = a.getColor(index, defaultColor);
            }
            else if (TypedValue.TYPE_FIRST_INT <= type && type <= TypedValue.TYPE_LAST_INT) {
                passedDefaultColor = a.getInt(index, defaultColor);
            }
        }
        return passedDefaultColor;
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        super.setDefaultValue(defaultValue);
        //defaultColor = parseDefaultValue(defaultValue);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        super.onSetInitialValue(defaultValue);
        if (defaultValue == null) {
            defaultValue = defaultColor;
        }
        setColor(getPersistedInt((int)defaultValue));
    }
//#FF0000
//#99FF0000
    private static String standardiseColorDigits(String s) {
        if (s.length()>0 && s.charAt(0) == '#' && s.length() <= "#argb".length()) {
            // Convert #[a]rgb to #[aa]rrggbb
            StringBuilder ss = new StringBuilder("#");
            for (int i = 1; i < s.length(); ++i) {
                ss.append(s.charAt(i));
                ss.append(s.charAt(i));
            }
            return ss.toString();
        }
        else {
            return s;
        }
    }

    private boolean hasPersistedColor(){
        return getSharedPreferences()!=null && getSharedPreferences().contains(getKey());
    }

    public int getPersistedColor(){
        return getPersistedInt(defaultColor);
    }

    private void removeSetting() {
        if (shouldPersist()) {
            Objects.requireNonNull(getSharedPreferences())
                    .edit()
                    .remove(getKey())
                    .apply();
        }
    }

    public void setColor(@Nullable Integer color) {
        if (color == null) {
            removeSetting();
        }
        else {
            persistInt(color);
        }
        notifyChanged();
    }

    @Nullable
    public Integer getColor() {
        return hasPersistedColor() ? getPersistedColor() : null;
    }

    public boolean isValueVisible() {
        return mShowValue;
    }

    public boolean isAlphaVisible() {
        return mShowAlpha;
    }

    public boolean isHexVisible() {
        return mShowHex;
    }

    public boolean isPreviewVisible() {
        return mShowPreview;
    }

    public boolean useMaxValueIfHidden() {
        return mValueMaxIfHidden;
    }
}
