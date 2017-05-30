import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class SpaceGame extends Game{
	
	public SpriteBatch batch;
	public ScrollingBackground scrollingBackground;
	
	public static final int WIDTH = 500;
	public static final int HEIGHT = 650;
	
	private OrthographicCamera cam;
	private StretchViewport viewPort;
	


	
	@Override
	public void create (){
		
		scrollingBackground = new ScrollingBackground();
		batch = new SpriteBatch();
		
		cam = new OrthographicCamera();
		viewPort = new StretchViewport(WIDTH, HEIGHT, cam);
		viewPort.apply();
		cam.position.set(WIDTH/2, HEIGHT/2, 0);
		cam.update();
		

		
		//img = new Texture("badlogic.jpg");
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render (){
	
		batch.setProjectionMatrix(cam.combined);

		super.render();
	}
	
	public void resize(int width, int height){
		
		this.scrollingBackground.resize(width, height);
		viewPort.update(width, height);
		super.resize(width, height);
	}
	
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
}
