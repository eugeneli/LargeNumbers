package li.eugene.largenumbers.models;

import li.eugene.largenumbers.util.NumberFormatter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class NumberBox extends Actor
{
	private Long value;
	private BitmapFont font;
	private ShapeRenderer renderer;
	private float speed;
	private boolean stale; //Stale means its value has already been added/counted to player score
	private boolean shrinking;
	private boolean valid;
	private Color color = Color.GREEN;
	private Color fontColor = Color.BLACK;
	
	public static final int MIN_RECT_WIDTH = 235;
	public static final int MIN_RECT_HEIGHT = 175;
	public static final int MAX_RECT_WIDTH = 235;
	public static final int MAX_RECT_HEIGHT = 300;
	
	private static final float SHRINK_SPEED = 60f;
	
	public NumberBox(long val, float w, float h, float x, float y, float sp)
	{
		value = new Long(val);
		
		setWidth(w);
		setHeight(h);
		setX(x);
		setY(y);
		
		speed = sp;
		shrinking = false;
		stale = false;
		valid = true;
		
		renderer = new ShapeRenderer();
		font = new BitmapFont();
		font.scale(1.2f);
		
		addListener(numberBoxTapListener);
	}
	
	public void rebuild(long val, float w, float h, float x, float y, float sp)
	{
		value = new Long(val);
		
		setWidth(w);
		setHeight(h);
		setX(x);
		setY(y);
		
		speed = sp;
		shrinking = false;
		stale = false;
		valid = true;
	}
	
	public void setSpeed(float sp) { speed = sp; }
	
	public long getValue() { return value.longValue(); }
	
	public Color getRectColor() { return color; }
	
	public boolean isShrinking() { return shrinking; }
	public boolean isValid() { return valid; }
	public void setValid(boolean b) { valid = b; }
	public boolean isStale() { return stale; }
	public void setStale(boolean b) { stale = b; }
	
	public void recolor(long playerScore)
	{
		if(playerScore <= 1 && value == 1)
		{
			color = Color.GREEN;
			fontColor = Color.BLACK;
		}
		else
		{
			if(playerScore > value)
			{
				color = Color.GREEN;
				fontColor = Color.BLACK;
			}
			else
			{
				color = Color.RED;
				fontColor = Color.WHITE;
			}
		}
	}
	
	private float[] getCenteredFontPosition()
	{
		TextBounds bounds = font.getBounds(NumberFormatter.shorten(value));
		float x = getX() + getWidth()/2 - bounds.width/2;
		float y = getY() + getHeight()/2 + bounds.height/2;
		return new float[] {x,y};
	}

	private void resetBounds()
	{
		setBounds(getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
    public void draw(Batch batch, float alpha)
	{
		batch.end();
		
		//Draw shape
		renderer.setProjectionMatrix(batch.getProjectionMatrix()); //set projection matrix to be same as the camera's. same as camera.getProjectionMatrix()
		renderer.begin(ShapeType.Filled);
	    renderer.setColor(color);
	    renderer.rect(getX(), getY(), getWidth(), getHeight());
	    renderer.end();
	    
	    batch.begin();
	    
	    //Draw number
	  	font.setColor(fontColor);
	  	float[] fontPos = getCenteredFontPosition();
	  	font.draw(batch, NumberFormatter.shorten(value), fontPos[0] , fontPos[1]);
    }
    
    @Override
    public void act(float delta)
    {
    	moveBy(0, -speed);
    	if(shrinking)
    	{
    		setHeight(getHeight() - SHRINK_SPEED);
    		setY(getY() + SHRINK_SPEED);
    	}
    	resetBounds();
    }
    
    private InputListener numberBoxTapListener = new InputListener()
	{
	    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
	    {
	        shrinking = true;
	        return true;
	    }
	};
}
