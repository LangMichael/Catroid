/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.stage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Sprite;

import java.io.ByteArrayOutputStream;

public class PenActor extends Actor {
	private float previousPosX;
	private float previousPosY;
	private int color = Color.BLACK;
	private Sprite sprite;
	private boolean initialize = true;
	private int strokeWidth = 4;

	public PenActor(Sprite sprite) {
		this.sprite = sprite;
		setZIndex(ProjectManager.getInstance().getCurrentProject().getSpriteList().size());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (initialize) {
			previousPosX = sprite.look.getX();
			previousPosY = sprite.look.getY();
			initialize = false;
			return;
		}

		if (previousPosX != sprite.look.getX() || previousPosY != sprite.look.getY()) {
			getImageToDraw().draw(batch, parentAlpha);
			previousPosX = sprite.look.getX();
			previousPosY = sprite.look.getY();
		}
	}

	private Image getImageToDraw() {
		int width = Math.abs((int) (previousPosX - sprite.look.getX()));
		int height = Math.abs((int) (previousPosY - sprite.look.getY()));
		if (width == 0) {
			width = strokeWidth;
		}
		if (height == 0) {
			height = strokeWidth;
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(color);
		canvas.drawLine(0, 0, width, height, paint);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] bytes = stream.toByteArray();

		Image image = new Image(new Texture(new Pixmap(bytes, 0, bytes.length)));
		image.setX(previousPosX);
		image.setY(previousPosY);
		return image;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
}
