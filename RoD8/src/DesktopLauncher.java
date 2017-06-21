import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * The Class DesktopLauncher.
 */
public class DesktopLauncher {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main (String[] args) {
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.foregroundFPS = 60;    
		//config.width = SpaceGame.WIDTH * SpaceGame.WIDTH;
		//config.height = SpaceGame.HEIGHT * SpaceGame.WIDTH;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		config.width = (int) screenSize.getWidth();
		config.height = (int) screenSize.getHeight();
		config.resizable = false;
		new LwjglApplication(new SpaceGame(), config);
		
		
	}
}
