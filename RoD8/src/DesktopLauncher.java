import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

// TODO: Auto-generated Javadoc
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
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		
	//	WIDTH = 
		//HEIGHT = 
		config.width = gd.getDisplayMode().getWidth();
		config.height = gd.getDisplayMode().getHeight();
		config.resizable = false;
		new LwjglApplication(new SpaceGame(), config);
		
		
	}
}
