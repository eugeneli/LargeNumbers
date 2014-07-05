package li.eugene.largenumbers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerManager extends Actor
{
	private ShapeRenderer renderer = new ShapeRenderer();
	private BitmapFont font = new BitmapFont();
	private Long score = new Long(0);
	private long negativeGoal = 0;
	private long positiveGoal = 0;
	
	private int POINT_CHANGE_RATE = (int) Math.ceil(.99f * positiveGoal + 1);
	private final static float PLAYER_SCORE_HEIGHT = 100;
	
	public PlayerManager()
	{
		font.setScale(5);
		setX(0);
		setY(LNConstants.RES_HEIGHT - PLAYER_SCORE_HEIGHT);
		setWidth(LNConstants.RES_WIDTH);
		setHeight(PLAYER_SCORE_HEIGHT);
	}
	
	public void setGoals(long[] goals)
	{
		negativeGoal += goals[0];
		positiveGoal += goals[1];
	}
	
	public Long getScore()
	{
		return score.longValue();
	}
	
	@Override
    public void draw(Batch batch, float alpha)
	{
		batch.end();

		//Draw shape
		renderer.setProjectionMatrix(batch.getProjectionMatrix()); //set projection matrix to be same as the camera's. same as camera.getProjectionMatrix()
		renderer.begin(ShapeType.Filled);
	    renderer.setColor(LNConstants.SEMITRANSPARENT_BLACK);
	    renderer.rect(0, LNConstants.RES_HEIGHT, LNConstants.RES_WIDTH, PLAYER_SCORE_HEIGHT);
	    renderer.end();
	    
	    batch.begin();
	    
	    //Draw number
	  	font.setColor(Color.WHITE);
	  	float[] fontPos = getCenteredFontPosition();
	  	font.draw(batch, score.toString(), fontPos[0] , fontPos[1]);
    }
	
	private float[] getCenteredFontPosition()
	{
		TextBounds bounds = font.getBounds(score.toString());
		float x = getX() + getWidth()/2 - bounds.width/2;
		float y = getY() + getHeight()/2 + bounds.height/2;
		return new float[] {x,y};
	}
    
    @Override
    public void act(float delta)
    {
    	System.out.println(positiveGoal);
    	if(negativeGoal > 0)
    	{
    		score -= POINT_CHANGE_RATE;
    		negativeGoal -= POINT_CHANGE_RATE;
    	}
    	
    	if(positiveGoal > 0)
    	{
    		score += POINT_CHANGE_RATE;
    		positiveGoal -= POINT_CHANGE_RATE;
    	}
    }
}
