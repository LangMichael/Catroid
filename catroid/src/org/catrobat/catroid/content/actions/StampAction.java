/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2014 The Catrobat Team
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
package org.catrobat.catroid.content.actions;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.content.Look;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.FormulaElement;
import org.catrobat.catroid.formulaeditor.InterpretationException;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class StampAction extends TemporalAction {

	private Sprite sprite;

	@Override
	protected void update(float delta) {
		Log.v("Stamp", "do update");
		Project project = ProjectManager.getInstance().getCurrentProject();
		Sprite background = project.getSpriteList().get(0);

		LookData lookData = background.look.getLookData();
		Pixmap pixmap1 = lookData.getPixmap();

		float xCoord =  sprite.look.getLookData().getPixmap().getWidth() / 2f;// + sprite.look.getWidth()/2f;
		float yCoord =  sprite.look.getLookData().getPixmap().getHeight() / 2f;// - sprite.look.getHeight();
		Log.v("Stamp1", "Größe1:"+ sprite.look.getLookData().getPixmap().getWidth());
		Log.v("Stamp1", "Größe2:"+ sprite.look.getSizeInUserInterfaceDimensionUnit());
		Log.v("Stamp1", "Anzahl Looks:" + sprite.getLookDataList().size());



		int x = pixmap1.getWidth()/2-(int)xCoord + (int)this.sprite.look.getXInUserInterfaceDimensionUnit();
		int y = pixmap1.getHeight()/2-(int)yCoord - (int)this.sprite.look.getYInUserInterfaceDimensionUnit();

		//pixmap1.drawPixmap(this.sprite.look.getLookData().getPixmap(), x, y);

		Log.v("Stamp2", "Originalbreite:" + sprite.look.getLookData().getPixmap().getWidth());
		Log.v("Stamp2", "Originalhöhe:" + sprite.look.getLookData().getPixmap().getHeight());

		Log.v("Stamp2", "x:" + x);
		Log.v("Stamp2", "y:" + y);

		double scale = sprite.look.getSizeInUserInterfaceDimensionUnit()/100;

		Log.v("Stamp2", "scale:" + scale);

		if (scale < 1) {
			x = (int) (x + sprite.look.getLookData().getPixmap().getWidth()*scale*0.5);
			y = (int) (y + sprite.look.getLookData().getPixmap().getHeight()*scale*0.5);
		}
		else if(scale > 1) {
			scale = scale - 1;
			x = (int) (x - sprite.look.getLookData().getPixmap().getWidth()*scale*0.5);
			y = (int) (y - sprite.look.getLookData().getPixmap().getHeight()*scale*0.5);
		}

		Log.v("Stamp2", "newX:" + x);
		Log.v("Stamp2", "newY:" + y);

		Log.v("Stamp2", "rotation:" +sprite.look.getDirectionInUserInterfaceDimensionUnit());

		Log.v("Stamp", "TextureRegionWidth: " + sprite.look.getLookData().getTextureRegion().getRegionWidth());
		Log.v("Stamp", "TextureRegionHeight: " + sprite.look.getLookData().getTextureRegion().getRegionHeight());

		//Float testF = sprite.look.getDirectionInUserInterfaceDimensionUnit() -90;
		//Log.v("Stamp2", "rotation2:" +testF);

		Pixmap testMap = rotatePixmap(this.sprite.look.getLookData().getPixmap(), sprite.look.getDirectionInUserInterfaceDimensionUnit());


		//pixmap1.drawPixmap(this.sprite.look.getLookData().getPixmap(),
		pixmap1.drawPixmap(testMap,
		0,
		0,
		sprite.look.getLookData().getPixmap().getWidth(),
		sprite.look.getLookData().getPixmap().getHeight(),
		x,
		y,
		sprite.look.getLookData().getPixmap().getWidth()/100*(int)(sprite.look.getSizeInUserInterfaceDimensionUnit()),
		sprite.look.getLookData().getPixmap().getHeight()/100*(int)(sprite.look.getSizeInUserInterfaceDimensionUnit()));

		lookData.setPixmap(pixmap1);

		lookData.setTextureRegion();

		background.look.setLookData(lookData);

	}

	public Pixmap rotatePixmap (Pixmap src, float angle){

		Polygon poly = new Polygon(new float[]{
				0,0,
				src.getWidth(),0,
				src.getWidth(), src.getHeight(),
				0,src.getHeight()
		});

		Float polyRotation = angle - 90;

		poly.setRotation(polyRotation);

		Rectangle rect = poly.getBoundingRectangle();

		Log.v("StampRot", "RectWidth:" + rect.getWidth());
		Log.v("StampRot", "RectHeight:" + rect.getHeight());

		//final int width = ((int)rect.getWidth());
		//final int height = ((int) rect.getHeight());

		final int width = ((int)rect.getWidth()*2);
		final int height = ((int) rect.getWidth()*2);

		Pixmap rotated = new Pixmap(width, height, src.getFormat());

		final double radians = Math.toRadians(angle), cos = Math.cos(radians), sin = Math.sin(radians);


		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final int
						centerx = width/2, centery = height / 2,
						m = x - centerx,
						n = y - centery,
						j = ((int) (m * cos + n * sin)) + centerx,
						k = ((int) (n * cos - m * sin)) + centery;
				if (j >= 0 && j < width && k >= 0 && k < height){
					rotated.drawPixel(x, y, src.getPixel(k, j));
				}
			}
		}
		return rotated;

	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

}
