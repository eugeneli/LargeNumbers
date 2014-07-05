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
	private NumberBox currentTallest;
	private long[] pointChanges = new long[]{0,0};
	private Queue<NumberBox> inactiveNumberBoxes = new LinkedList<NumberBox>();
	private SnapshotArray<Actor> numberBoxes = getChildren();
	
	private final static float BOX_CREATION_CHANCE = 0.1f;
	private static final float ACCELERATION = 0.0003f;
	
	private boolean failure = false;
	
	public NumberBoxGroup()
	{
		//Create 20 numberboxes for reuse
		for(int i = 0; i < 30; i++)
		{
			NumberBox nb = new NumberBox(0, 0, 0, 0, 0, 0);
			inactiveNumberBoxes.add(nb);
		}
	}
	
	public void update(long playerScore)
	{
		System.out.println(inactiveNumberBoxes.size());
		//Reset point changes
		pointChanges[0] = 0;
		pointChanges[1] = 0;
		
		if(canCreateRow())
			createRow(playerScore);
		
		for(Actor a: numberBoxes)
		{
			NumberBox nb = (NumberBox) a;
			
			speed += ACCELERATION;
			nb.setSpeed(speed);
			
			nb.recolor(playerScore);
			
			if(nb.getRectColor() == Color.GREEN)
			{
				if(nb.isShrinking() && !nb.isStale()) //Freshly clicked green
				{
					pointChanges[1] += nb.getValue();
					nb.setStale(true);
				}
				else if(!nb.isShrinking() && !nb.isStale() && nb.getTop() <= 0) //Missed green
				{
					pointChanges[0] += nb.getValue();
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
		
		int[] usedColumns = new int[]{-1,-1,-1,-1,-1}; //don't overlap boxes
		
		int randomColumn = randomColumn();
		usedColumns[randomColumn] = randomColumn;
		createBox(playerScore, randomColumn); //Always have at least one box per row
		
		for(int i = 0; i < 4; i++)
		{
			if(rand.nextFloat() < BOX_CREATION_CHANCE)
			{
				int col = randomColumn();
				if(usedColumns[col] == -1)
				{
					usedColumns[col] = col;
					
					createBox(playerScore, col);
				}
			}
		}
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
		long min = (long) (NumberBox.NUMBER_VALUE_RANGE_MIN * playerScore);
		long max = (long) (NumberBox.NUMBER_VALUE_RANGE_MAX * playerScore);
		
		long boxValue = (long) (min + rand.nextFloat() * max);
		if(boxValue == 0)
			boxValue = 1;
		
		//float boxRectWidth =  (boxValue/playerScore) * NumberBox.MAX_RECT_WIDTH;
		//if(boxRectWidth < NumberBox.MIN_RECT_WIDTH)
		float boxRectWidth = NumberBox.MIN_RECT_WIDTH;
		
		float boxRectHeight =  (boxValue/playerScore) * NumberBox.MAX_RECT_HEIGHT;
		if(boxRectHeight < NumberBox.MIN_RECT_HEIGHT)
			boxRectHeight = NumberBox.MIN_RECT_HEIGHT;
		
		/*float xPos = rand.nextFloat() * LNConstants.RES_WIDTH;
		if(xPos + boxRectWidth > LNConstants.RES_WIDTH)
			xPos = LNConstants.RES_WIDTH - boxRectWidth;*/
		
		float xPos = offset * NumberBox.MIN_RECT_WIDTH; //Add offset to spawn it in a different column
		
		float yPos = LNConstants.RES_HEIGHT;
		
		num.rebuild(boxValue, boxRectWidth, boxRectHeight, xPos, yPos, speed);
	    num.setTouchable(Touchable.enabled);
		addActor(num);
		
		if(currentTallest == null || num.getHeight() > currentTallest.getHeight())
			currentTallest = num;
	}
}
