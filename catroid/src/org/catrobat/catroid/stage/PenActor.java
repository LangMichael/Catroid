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

import android.graphics.PointF;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.XmlHeader;

public class PenActor extends Actor {
	private boolean initialize = true;
	private FrameBuffer buffer;

	public PenActor() {
		XmlHeader header = ProjectManager.getInstance().getCurrentProject().getXmlHeader();
		buffer = new FrameBuffer(Pixmap.Format.RGBA8888, header.virtualScreenWidth, header.virtualScreenHeight, false);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		buffer.begin();
		for (Sprite sprite : ProjectManager.getInstance().getCurrentProject().getSpriteList()) {
			drawLinesForSprite(sprite);
		}
		buffer.end();

		batch.end();
		TextureRegion region = new TextureRegion(buffer.getColorBufferTexture());
		region.flip(false, true);
		Image image = new Image(region);
		image.setX(- buffer.getWidth() / 2);
		image.setY(- buffer.getHeight() / 2);
		batch.begin();
		image.draw(batch, parentAlpha);
	}

	private void drawLinesForSprite(Sprite sprite) {
		float x = sprite.look.getXInUserInterfaceDimensionUnit();
		float y = sprite.look.getYInUserInterfaceDimensionUnit();

		if (initialize) {
			sprite.previousPoint = new PointF(x, y);
			initialize = false;
			return;
		}

		if (sprite.previousPoint.x != sprite.look.getX() || sprite.previousPoint.y != sprite.look.getY()) {
			ShapeRenderer renderer = StageActivity.stageListener.shapeRenderer;
			renderer.setColor(sprite.penColor);

			renderer.begin(ShapeRenderer.ShapeType.Filled);
			if (sprite.penDown) {
				renderer.circle(sprite.previousPoint.x, sprite.previousPoint.y, sprite.penSize / 2);
				renderer.rectLine(sprite.previousPoint.x, sprite.previousPoint.y, x, y, sprite.penSize);
				renderer.circle(x, y, sprite.penSize / 2);
			}
			renderer.end();


			sprite.previousPoint.x = x;
			sprite.previousPoint.y = y;
		}
	}
}
