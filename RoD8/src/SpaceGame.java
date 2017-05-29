import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceGame extends Game{
	
	SpriteBatch batch;
	
	
	@Override
	public void create (){
		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
	}

	@Override
	public void render (){
		
		this.render();


	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
}
