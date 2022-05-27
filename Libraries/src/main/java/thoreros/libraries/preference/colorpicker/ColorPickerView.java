/*
 * Copyright (C) 2015 Martin Stone
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package thoreros.libraries.preference.colorpicker;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import thoreros.libraries.preference.R;

public class ColorPickerView extends FrameLayout {

	private final ValueView valueView;
	private final AlphaView alphaView;
	private final EditText hexEdit;
	private final ObservableColor observableColor = new ObservableColor(0);
	private final SwatchView swatchView;

	public ColorPickerView(Context context) {
		this(context, null);
	}

	public ColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.picker, this);

		swatchView = findViewById(R.id.swatchView);
		swatchView.observeColor(observableColor);

		HueSatView hueSatView = findViewById(R.id.hueSatView);
		hueSatView.observeColor(observableColor);

		valueView = findViewById(R.id.valueView);
		valueView.observeColor(observableColor);

		alphaView = findViewById(R.id.alphaView);
		alphaView.observeColor(observableColor);

		hexEdit = findViewById(R.id.hexEdit);
		HexEdit.setUpListeners(hexEdit, observableColor);

		applyAttributes(attrs);

		// We get all our state saved and restored for free,
		// thanks to the EditText and its listeners!
	}

	private void applyAttributes(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ColorPicker, 0, 0);
			showValue(a.getBoolean(R.styleable.ColorPicker_showValue, true), a.getBoolean(R.styleable.ColorPicker_valueMaxIfHidden, true));
			showAlpha(a.getBoolean(R.styleable.ColorPicker_showAlpha, true));
			showHex(a.getBoolean(R.styleable.ColorPicker_showHex, true));
			showPreview(a.getBoolean(R.styleable.ColorPicker_showPreview, true));
		}
	}

	/** Returns the color selected by the user */
	public int getColor() {
		return observableColor.getColor();
	}

	/**
     *  Sets the original color swatch and the current color to the specified value.<br>
     *  Using dark colors may lead to a loss of color information caused by the ARGB color space.
     *  Use {@link #setColor(int, float, float, float)} instead to avoid this.
     */
	public void setColor(int color) {
		setOriginalColor(color);
		setCurrentColor(color);
	}

	/**
	 * Sets the original color swatch and the current color to the specified value.
	 *
	 * <ul>
	 *   <li>Alpha [0...255]</li>
	 *   <li>Hue [0...360)</li>
	 *   <li>Saturation [0...1]</li>
	 *   <li>Brightness [0...1]</li>
	 * </ul>
	 */	public void setColor(int alpha, float hue, float sat, float bri) {
		setOriginalColor(alpha, hue, sat, bri);
		setCurrentColor(alpha, hue, sat, bri);
	}

	/** Sets the original color swatch without changing the current color. */
	public void setOriginalColor(int color) {
		swatchView.setOriginalColor(color);
	}

	/** Sets the original color swatch without changing the current color. */
	public void setOriginalColor(int alpha, float hue, float sat, float bri) {
        int color = Color.HSVToColor(alpha, new float[]{hue, sat, bri});
		swatchView.setOriginalColor(color);
	}

	/** Updates the current color without changing the original color swatch. */
	public void setCurrentColor(int color) {
		observableColor.updateColor(color, null);
	}

	/** Updates the current color without changing the original color swatch. */
	public void setCurrentColor(int alpha, float hue, float sat, float bri) {
		observableColor.updateColor(alpha, hue, sat, bri, null);
	}

	public void showValue(boolean showValue, boolean valueMaxIfHidden) {
		valueView.setVisibility(showValue ? View.VISIBLE : View.GONE);
		if(!showValue && valueMaxIfHidden){
			observableColor.updateValue(1, this.valueView);
		}
	}

	public void showAlpha(boolean showAlpha) {
		alphaView.setVisibility(showAlpha ? View.VISIBLE : View.GONE);
		HexEdit.setShowAlphaDigits(hexEdit, showAlpha);
	}

	public void addColorObserver(ColorObserver observer) {
		observableColor.addObserver(observer);
	}

	public void showHex(boolean showHex) {
		hexEdit.setVisibility(showHex ? View.VISIBLE : View.GONE);
	}

	public void showPreview(boolean showPreview) {
		swatchView.setVisibility(showPreview ? View.VISIBLE : View.GONE);
	}
}
