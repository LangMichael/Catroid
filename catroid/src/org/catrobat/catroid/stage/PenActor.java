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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.content.Sprite;

public class PenActor extends Actor {
	private int previousPosX;
	private int previousPosY;
	private Color color = Color.BLACK;
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
			previousPosX = (int) sprite.look.getXInUserInterfaceDimensionUnit();
			previousPosY = (int) sprite.look.getYInUserInterfaceDimensionUnit();
			initialize = false;
			return;
		}

		if (previousPosX != (int) sprite.look.getXInUserInterfaceDimensionUnit() || previousPosY != (int) sprite.look
				.getYInUserInterfaceDimensionUnit()) {
			Pixmap background = ProjectManager.getInstance().getCurrentProject().getSpriteList().get(0)
					.look.getLookData().getPixmap();
			background.setColor(color);

			int oldX = background.getWidth() / 2 + previousPosX;
			int oldY = background.getHeight() / 2 - previousPosY;
			int newX = background.getWidth() / 2 + (int) sprite.look.getXInUserInterfaceDimensionUnit();
			int newY = background.getHeight() / 2 - (int) sprite.look.getYInUserInterfaceDimensionUnit();

			background.drawLine(oldX, oldY, newX, newY);
			ProjectManager.getInstance().getCurrentProject().getSpriteList().get(0)
					.look.getLookData().getTextureRegion().getTexture().draw(background, 0, 0);

			previousPosX = (int) sprite.look.getXInUserInterfaceDimensionUnit();
			previousPosY = (int) sprite.look.getYInUserInterfaceDimensionUnit();
		}
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
}
