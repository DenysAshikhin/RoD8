import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	
	public static void main (String[] args) {
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.foregroundFPS = 60;    
		config.width = SpaceGame.WIDTH * SpaceGame.WIDTH;
		config.height = SpaceGame.HEIGHT * SpaceGame.WIDTH;
		config.resizable = false;
		new LwjglApplication(new SpaceGame(), config);
		
		
	}
}
