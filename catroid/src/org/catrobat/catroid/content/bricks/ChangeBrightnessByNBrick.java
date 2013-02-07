/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content.bricks;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.ui.ScriptActivity;
import org.catrobat.catroid.ui.dialogs.BrickTextDialog;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeBrightnessByNBrick implements Brick, OnClickListener {
	private static final long serialVersionUID = 1L;
	private double changeBrightness;
	private Sprite sprite;

	private transient View view;
	private transient View prototype;

	public ChangeBrightnessByNBrick() {

	}

	public ChangeBrightnessByNBrick(Sprite sprite, double changeBrightness) {
		this.sprite = sprite;
		this.changeBrightness = changeBrightness;
	}

	@Override
	public int getRequiredResources() {
		return NO_RESOURCES;
	}

	@Override
	public void execute() {
		sprite.costume.changeBrightnessValueBy((float) (this.changeBrightness / 100));
	}

	@Override
	public Sprite getSprite() {
		return this.sprite;
	}

	public double getChangeBrightness() {
		return changeBrightness;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter adapter) {

		view = View.inflate(context, R.layout.brick_change_brightness, null);

		TextView textX = (TextView) view.findViewById(R.id.brick_change_brightness_prototype_text_view);
		EditText editX = (EditText) view.findViewById(R.id.brick_change_brightness_edit_text);
		editX.setText(String.valueOf(changeBrightness));

		textX.setVisibility(View.GONE);
		editX.setVisibility(View.VISIBLE);

		editX.setOnClickListener(this);

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		return setDefaultValues(context);
	}

	@Override
	public Brick clone() {
		return new ChangeBrightnessByNBrick(getSprite(), getChangeBrightness());
	}

	@Override
	public View setDefaultValues(Context context) {
		prototype = View.inflate(context, R.layout.brick_change_brightness, null);
		TextView textChangeBrightness = (TextView) prototype
				.findViewById(R.id.brick_change_brightness_prototype_text_view);
		textChangeBrightness.setText(changeBrightness + "");
		return prototype;
	}

	@Override
	public void onClick(View view) {
		ScriptActivity activity = (ScriptActivity) view.getContext();

		BrickTextDialog editDialog = new BrickTextDialog() {
			@Override
			protected void initialize() {
				input.setText(String.valueOf(changeBrightness));
				input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
						| InputType.TYPE_NUMBER_FLAG_SIGNED);
				input.setSelectAllOnFocus(true);
			}

			@Override
			protected boolean handleOkButton() {
				try {
					changeBrightness = Double.parseDouble(input.getText().toString());
				} catch (NumberFormatException exception) {
					Toast.makeText(getActivity(), R.string.error_no_number_entered, Toast.LENGTH_SHORT).show();
				}

				return true;
			}
		};

		editDialog.show(activity.getSupportFragmentManager(), "dialog_change_brightness_brick");
	}
}
