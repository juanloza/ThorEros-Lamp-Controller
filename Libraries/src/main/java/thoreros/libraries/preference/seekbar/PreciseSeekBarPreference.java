package thoreros.libraries.preference.seekbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SeekBarPreference;

import thoreros.libraries.preference.R;

public class PreciseSeekBarPreference extends SeekBarPreference {
    public int minSeekbarValue;
    public int maxSeekbarValue;

    @SuppressLint("PrivateResource")
    public PreciseSeekBarPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(
                attrs, androidx.preference.R.styleable.SeekBarPreference, 0, 0);

        minSeekbarValue = a.getInt(androidx.preference.R.styleable.SeekBarPreference_min, 0);
        maxSeekbarValue = a.getInt(androidx.preference.R.styleable.SeekBarPreference_android_max, 100);

        a.recycle();

        this.setLayoutResource(R.layout.precise_seekbar_preference);

    }

    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        final TextView textViewValue = (TextView) holder.findViewById(R.id.seekbar_value);
//        final EditText editTextValue = (EditText) holder.findViewById(R.id.seekbar_value_precise);

        //textViewValue.setVisibility(View.GONE);
        textViewValue.setOnClickListener(v -> {
            AlertDialog.Builder alert;
            alert = new AlertDialog.Builder(getContext());
            alert.setTitle(getTitle());
            //boite.setIcon(AppCompatResources.getDrawable(R.drawable.warning_shield_96px));


            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setText(textViewValue.getText());
            alert.setView(input);

            alert.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                if(input.getText().toString().isEmpty()){
                    return;
                }
                int value = Integer.parseInt(input.getText().toString());
                if(value > maxSeekbarValue){
                    value = maxSeekbarValue;
                }
                if(value < minSeekbarValue){
                    value = minSeekbarValue;
                }
                setValue(value);
            });
//                boite.setNegativeButton("NON", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //whatever action
//                    }
//                });
            alert.setNeutralButton("Cancelar", (dialogInterface, i) -> {
                //whatever action
            });
            alert.show();
            input.requestFocus();
        });

//        editTextValue.setVisibility(View.GONE);
//        editTextValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                int j = 2;
//            }
//        });
//        editTextValue.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(s.toString().isEmpty()){
//                    return;
//                }
//                int value = Integer.parseInt(s.toString());
//                if(value > maxSeekbarValue){
//                    value = maxSeekbarValue;
//                }
//                if(value < minSeekbarValue){
//                    value = minSeekbarValue;
//                }
//                setValue(value);
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                int j=2;
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                int j=2;
//            }
//        });
    }
}