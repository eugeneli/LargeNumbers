package li.eugene.largenumbers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

abstract class DrawableNumberActor extends Group
{
	private ShapeRenderer renderer;
	private BitmapFont font;
	private Long value;
	
	public DrawableNumberActor(long val)
	{ 
		renderer = new ShapeRenderer();
		font = new BitmapFont();
		value = new Long(val);
	}
	
	private float[] getCenteredFontPosition()
	{
		TextBounds bounds = font.getBounds(value.toString());
		float x = getX() + getWidth()/2 - bounds.width/2;
		float y = getY() + getHeight()/2 + bounds.height/2;
		return new float[] {x,y};
	}
}
