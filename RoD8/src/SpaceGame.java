import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceGame extends Game{
	
	public SpriteBatch batch;
	public ScrollingBackground scrollingBackground;
	
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	private OrthographicCamera cam;
//	private StretchViewport viewPort;
	
	@Override
	public void create (){
		
		//scrollingBackground = new ScrollingBackground();
		batch = new SpriteBatch();
		
		cam = new OrthographicCamera();
		//viewPort = new StretchViewport(WIDTH, HEIGHT, cam);
		//viewPort.apply();
		cam.setToOrtho(false, WIDTH, HEIGHT);
	
		this.setScreen(new GameScreen(this));
	}

	@Override
	public void render (){
	
		batch.setProjectionMatrix(cam.combined);

		super.render();
	}
	
	public void resize(int width, int height){
		
		//this.scrollingBackground.resize(width, height);
		//viewPort.update(width, height);
		super.resize(width, height);
	}
	
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
	
}
