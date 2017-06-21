import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// TODO: Auto-generated Javadoc
/**
 * The Class SpaceGame.
 */
public class SpaceGame extends Game{
	
	/** The batch. */
	public SpriteBatch batch;
	
	/** The scrolling background. */
	public ScrollingBackground scrollingBackground;
	
	/** The Constant WIDTH. */
	public static final int WIDTH = 320;
	
	/** The Constant HEIGHT. */
	public static final int HEIGHT = 240;
	
	/** The Constant SCALE. */
	public static final int SCALE = 2;
	
	/** The cam. */
	private OrthographicCamera cam;
//	private StretchViewport viewPort;
	
	/* (non-Javadoc)
 * @see com.badlogic.gdx.ApplicationListener#create()
 */
@Override
	public void create (){
		
		scrollingBackground = new ScrollingBackground();
		batch = new SpriteBatch();
		
		cam = new OrthographicCamera();
		//viewPort = new StretchViewport(WIDTH, HEIGHT, cam);
		//viewPort.apply();
		cam.setToOrtho(false, WIDTH, HEIGHT);

		this.setScreen(new GameScreen(this));
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#render()
	 */
	@Override
	public void render (){
	
		batch.setProjectionMatrix(cam.combined);

		super.render();
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#resize(int, int)
	 */
	@Override
	public void resize(int width, int height){
		
		//this.scrollingBackground.resize(width, height);
		//viewPort.update(width, height);
		super.resize(width, height);
	}
	
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#dispose()
	 */
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
	
}
