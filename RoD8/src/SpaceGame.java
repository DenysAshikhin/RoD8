import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceGame extends Game{
	
	public SpriteBatch batch;
	
	public static final int WIDTH = 500;
	public static final int HEIGHT = 650;
	
	@Override
	public void create (){
		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render (){
	
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
}
