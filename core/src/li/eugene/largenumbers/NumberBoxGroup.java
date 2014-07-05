package li.eugene.largenumbers;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import li.eugene.largenumbers.models.NumberBox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.SnapshotArray;

public class NumberBoxGroup extends Group
{
	private float speed = 5f;
	private Random rand = new Random();
	private boolean failure = false;
	private NumberBox currentTallest;
	private long[] pointChanges = new long[]{0,0};
	private Queue<NumberBox> inactiveNumberBoxes = new LinkedList<NumberBox>();
	private SnapshotArray<Actor> numberBoxes = getChildren();
	private float chanceToCreateSmaller = 0.5f;
	
	private final static float BOX_CREATION_CHANCE = 0.25f;
	private final static float CHANCE_TO_CREATE_SMALLER_REDUCTION = 0.9f;
	private final static float ACCELERATION = 0.0002f;
	private final static float MAX_SPEED = 20f;
	
	private final static float SMALLER_VALUE_RANGE_MIN = 0.1f;
	private final static float SMALLER_VALUE_RANGE_MAX = 0.45f;
	private final static float GREATER_VALUE_RANGE_MIN = 8f;
	private final static float GREATER_VALUE_RANGE_MAX = 10f;
	
	public NumberBoxGroup()
	{
		//Create numberboxes for reuse
		for(int i = 0; i < 50; i++)
		{
			NumberBox nb = new NumberBox(0, 0, 0, 0, 0, 0);
			inactiveNumberBoxes.add(nb);
		}
	}
	
	public void update(long playerScore)
	{
		//Reset point changes
		pointChanges[0] = 0;
		pointChanges[1] = 0;
		
		if(canCreateRow())
			createRow(playerScore);
		
		for(Actor a: numberBoxes)
		{
			NumberBox nb = (NumberBox) a;
			
			if(speed <= MAX_SPEED)
			{
				speed += ACCELERATION;
				nb.setSpeed(speed);
			}
			
			nb.recolor(playerScore);
			
			if(nb.getRectColor() == Color.GREEN)
			{
				if(nb.isShrinking() && !nb.isStale()) //Freshly clicked green
				{
					pointChanges[1] += nb.getValue()/2 + 1;
					nb.setStale(true);
				}
				else if(!nb.isShrinking() && !nb.isStale() && nb.getTop() <= 0) //Missed green
				{
					pointChanges[0] += nb.getValue()/2 + 1;
					nb.setStale(true);
					nb.setValid(false);
				}
			}
			else
			{
				if(nb.isShrinking() && !nb.isStale()) //Freshly clicked red
				{
					failure = true;
					nb.setValid(false);
				}
				else if(!nb.isShrinking() && !nb.isStale() && nb.getTop() <= 0) //Missed red
				{
					nb.setValid(false);
				}
			}
			
			if(nb.getHeight() < NumberBox.MIN_RECT_HEIGHT * 0.3f)
				nb.setValid(false);
			
			if(!nb.isValid())
			{
				nb.remove();
				inactiveNumberBoxes.add(nb);
			}
		}
	}
	
	public boolean failed() { return failure; }
	
	public long[] getPointChanges() { return pointChanges; }

	public float getSpeed() { return speed; }
	
	public boolean canCreateRow()
	{
		if(currentTallest == null)
			return true;
		else
			return currentTallest.getTop() <= LNConstants.RES_HEIGHT;
	}
	
	public void createRow(long playerScore)
	{
		currentTallest = null;

		for(int i = 0; i < 5; i++)
			if(rand.nextFloat() < BOX_CREATION_CHANCE) createBox(playerScore, i);
		
		chanceToCreateSmaller = 0.5f;
	}
	
	private int randomColumn()
	{
		float random = rand.nextFloat();
		if(random <= 0.2) 					   return 0;
		else if(random > 0.2 && random <= 0.4) return 1;
		else if(random > 0.4 && random <= 0.6) return 2;
		else if(random > 0.6 && random <= 0.8) return 3;
		else				 				   return 4;
	}
	
	public void createBox(long playerScore, int offset)
	{
		NumberBox num = inactiveNumberBoxes.remove();
		playerScore = playerScore == 0 ? 1 : playerScore;
		
		float randFloat = rand.nextFloat();
		long boxValue;
		if(randFloat < chanceToCreateSmaller)
		{
			float randomMultiplierInRange = SMALLER_VALUE_RANGE_MIN + rand.nextFloat() * SMALLER_VALUE_RANGE_MAX;
			boxValue = (long) (randomMultiplierInRange * playerScore);
			chanceToCreateSmaller *= CHANCE_TO_CREATE_SMALLER_REDUCTION; //Decrease chance to create a smaller box by half each time
		}
		else
		{
			float randomMultiplierInRange = GREATER_VALUE_RANGE_MIN + rand.nextFloat() * GREATER_VALUE_RANGE_MAX;
			boxValue = (long) (randomMultiplierInRange * playerScore);
		}
		
		if(boxValue == 0)
			boxValue = rand.nextBoolean() ? 1 : 2;

		float xPos = offset * NumberBox.MIN_RECT_WIDTH; //Add offset to spawn it in a different column	
		float yPos = LNConstants.RES_HEIGHT;
		
		num.rebuild(boxValue, NumberBox.MIN_RECT_WIDTH, NumberBox.MAX_RECT_HEIGHT, xPos, yPos, speed);
	    num.setTouchable(Touchable.enabled);
		addActor(num);
		
		if(currentTallest == null || num.getHeight() > currentTallest.getHeight())
			currentTallest = num;
	}
}
