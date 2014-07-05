package li.eugene.largenumbers.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import li.eugene.largenumbers.LargeNumbers;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Large Numbers";
		//config.height = 1920;
		//config.width = 1080;
		new LwjglApplication(new LargeNumbers(), config);
	}
}
