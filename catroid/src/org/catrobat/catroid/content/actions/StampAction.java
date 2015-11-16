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
			Log.v("Stamp2", "scaleX:" + (x + sprite.look.getLookData().getPixmap().getWidth()*scale*0.5));
			Log.v("Stamp2", "scaleY:" + (y + sprite.look.getLookData().getPixmap().getHeight()*scale*0.5));
			x = (int) (x + sprite.look.getLookData().getPixmap().getWidth()*(1-scale)*0.5);
			y = (int) (y + sprite.look.getLookData().getPixmap().getHeight()*(1-scale)*0.5);
		}
		else if(scale > 1) {
			x = (int) (x - sprite.look.getLookData().getPixmap().getWidth()*(scale-1)*0.5);
			y = (int) (y - sprite.look.getLookData().getPixmap().getHeight()*(scale-1)*0.5);
		}


		Log.v("Stamp2", "newX:" + x);
		Log.v("Stamp2", "newY:" + y);

		Log.v("Stamp2", "rotation:" +sprite.look.getDirectionInUserInterfaceDimensionUnit());

		Log.v("Stamp", "TextureRegionWidth: " + sprite.look.getLookData().getTextureRegion().getRegionWidth());
		Log.v("Stamp", "TextureRegionHeight: " + sprite.look.getLookData().getTextureRegion().getRegionHeight());



		Pixmap rotateMap = this.sprite.look.getLookData().getPixmap();

		if(sprite.look.getDirectionInUserInterfaceDimensionUnit() != 90) {
			Log.v("Stamp","Rotation vorgenommen!!");

			rotateMap = rotatePixmap(this.sprite.look.getLookData().getPixmap(),
					sprite.look.getDirectionInUserInterfaceDimensionUnit());

			double xCorrection = (rotateMap.getWidth() - this.sprite.look.getLookData().getPixmap().getWidth())/2;
			double yCorrection = (rotateMap.getHeight() - this.sprite.look.getLookData().getPixmap().getHeight())/2;


			Log.v("Stamp", "rotateMapWidth: " + rotateMap.getWidth());
			Log.v("Stamp", "spriteMapWidth: " + this.sprite.look.getLookData().getPixmap().getWidth());

			Log.v("Stamp", "xCorr: " + xCorrection);
			Log.v("Stamp", "yCorr: " + yCorrection);

			xCorrection = (rotateMap.getWidth() - this.sprite.look.getLookData().getPixmap().getWidth())*scale/2;
			yCorrection = (rotateMap.getHeight() - this.sprite.look.getLookData().getPixmap().getHeight())
					*scale/2;

			Log.v("Stamp", "xCorr2: " + xCorrection);
			Log.v("Stamp", "yCorr2: " + yCorrection);

			//if(rotateMap.getWidth()>this.sprite.look.getLookData().getPixmap().getWidth()) {
				//Log.v("Stamp", "rotateMap.getWidth()>>>>>>this.sprite.look.getLookData().getPixmap().getWidth()");
				x -= xCorrection;
			//}
			/*
			else {
				x += xCorrection;
				Log.v("Stamp", "ELSE rotateMap.getWidth()>>>>>>this.sprite.look.getLookData().getPixmap().getWidth()");
			}
			*/

			//if(rotateMap.getHeight()>this.sprite.look.getLookData().getPixmap().getHeight()) {
				//Log.v("Stamp", "rotateMap.getWidth()>>>>this.sprite.look.getLookData().getPixmap().getWidth()");
				y -= yCorrection;
			//}
			/*
			else {
				Log.v("Stamp", "ELSE rotateMap.getWidth()>>>>this.sprite.look.getLookData().getPixmap().getWidth()");
				y += yCorrection;
			}
			*/

		}


		Log.v("Stamp","rotatemapwidth:" + rotateMap.getWidth());
		Log.v("Stamp","rotatemapHeight:" + rotateMap.getHeight());

		Log.v("Stamp","rotatemapwidth2:" + rotateMap.getWidth()/100*(int)(sprite.look.getSizeInUserInterfaceDimensionUnit()));
		Log.v("Stamp","rotatemapHeight2:" + rotateMap.getHeight()/100*(int)(sprite.look.getSizeInUserInterfaceDimensionUnit
				()));

		//pixmap1.drawPixmap(this.sprite.look.getLookData().getPixmap(),
		pixmap1.drawPixmap(rotateMap,
		0,
		0,
		//sprite.look.getLookData().getPixmap().getWidth(),
		//sprite.look.getLookData().getPixmap().getHeight(),
				rotateMap.getWidth(),
				rotateMap.getHeight(),
		x,
		y,
		//sprite.look.getLookData().getPixmap().getWidth()/100*(int)(sprite.look.getSizeInUserInterfaceDimensionUnit()),
		//sprite.look.getLookData().getPixmap().getHeight()/100*(int)(sprite.look.getSizeInUserInterfaceDimensionUnit
				(int)((float)rotateMap.getWidth()/100*(sprite.look.getSizeInUserInterfaceDimensionUnit())),
				(int)((float)rotateMap.getHeight()/100*(sprite.look.getSizeInUserInterfaceDimensionUnit
				())));

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

		angle = angle-90;

		Float polyRotation = angle;

		angle = 360 - angle;

		poly.setRotation(polyRotation);

		Rectangle rect = poly.getBoundingRectangle();

		Log.v("StampRot", "RectWidth:" + rect.getWidth());
		Log.v("StampRot", "RectHeight:" + rect.getHeight());

		//final int width = ((int)rect.getWidth());
		//final int height = ((int) rect.getHeight());

		final int width = ((int) src.getWidth());
		final int height = ((int) src.getHeight());

		Log.v("StampFormat","format: " + src.getFormat().toString());
		Log.v("StampFormat","formatValues:" + src.getFormat().values());

		final int newWidth = (int) rect.getWidth();
		final int newHeight = (int) rect.getHeight();
		final int newCenterX = newWidth / 2;
		final int newCenterY = newHeight / 2;

		Pixmap rotated = new Pixmap(newWidth, newHeight, src.getFormat());

		for(int i = 0; i<rotated.getWidth(); i++) {
			for(int j = 0; j<rotated.getHeight(); j++) {
				rotated.drawPixel(i,j,Color.RED);
			}
		}

		final double radians = Math.toRadians(angle), cos = Math.cos(radians), sin = Math.sin(radians);


		Log.v("Stamp", "NewWidth: " + newWidth);
		Log.v("Stamp", "NewHeight: " + newHeight);
		Log.v("Stamp", "NewCenterX: " + newCenterX);
		Log.v("Stamp", "NewCenterY: " + newCenterY);


		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final int
						centerx = width/2, centery = height / 2,
						m = x - centerx,
						n = y - centery,
						j = ((int) (m * cos + n * sin)) + newCenterX,
						k = ((int) (n * cos - (m * sin))) + newCenterY;
				//Log.v("Stamp", "m: " + m);
				//Log.v("Stamp", "n: " + n);
				//rotated.drawPixel(x,y, Color.RED);
				if (j >= 0 && j < newWidth && k >= 0 && k < newHeight){
					//rotated.drawPixel(x, y, src.getPixel(k, j));
					rotated.drawPixel(j, k, src.getPixel(x, y));
				}
				else if(j < 0) {
					Log.v("Stamp", "------------ OUT OF BOUNDS j<0 -----------#x="+x+"##y="+y+"##j"+j);
				}
				else if(j > newWidth) {
					Log.v("Stamp", "------------ OUT OF BOUNDS j > newWidth -----------#x="+x+"##y="+y+"##j"+j);
				}
				else if(k < 0) {
					Log.v("Stamp", "------------ OUT OF BOUNDS k < 0 -----------#x="+x+"##y="+y+"##k"+k);
				}
				else if(k > newHeight) {
					Log.v("Stamp", "------------ OUT OF BOUNDS k > newHeight ------------#x="+x+"##y="+y+"##k"+k);
				}
			}
		}
		return rotated;

	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

}
