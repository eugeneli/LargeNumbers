package li.eugene.largenumbers;

import com.badlogic.gdx.ApplicationAdapter;

public class LargeNumbers extends ApplicationAdapter
{
	LNEngine engine;

	@Override
	public void create ()
	{
		engine = new LNEngine();
		engine.create();
	}

	@Override
	public void render ()
	{
		engine.render();
	}
}
