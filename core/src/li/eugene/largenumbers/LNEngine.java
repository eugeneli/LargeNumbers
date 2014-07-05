package li.eugene.largenumbers;

import java.util.Random;

import li.eugene.largenumbers.models.NumberBox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class LNEngine
{
	private Stage stage;
	private PlayerManager playerManager;
	private NumberBoxGroup nbGroup;

	public void create()
	{
	    stage = new Stage(new ExtendViewport(LNConstants.RES_WIDTH, LNConstants.RES_HEIGHT));
	    Gdx.input.setInputProcessor(stage);
	    
	    nbGroup = new NumberBoxGroup();
	    stage.addActor(nbGroup);
	    
	    playerManager = new PlayerManager();
	    stage.addActor(playerManager);
	}

	public void resize (int width, int height)
	{
	    stage.getViewport().update(width, height, true);
	}
	
	public void update()
	{
		playerManager.setGoals(nbGroup.getPointChanges()); //Get point changes before updating
		nbGroup.update(playerManager.getScore());
	}

	public void render()
	{
		update();
		
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    Gdx.gl.glClearColor(1, 1, 1, 1);
	    stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();
	}

	public void dispose() {
	    stage.dispose();
	}
}
