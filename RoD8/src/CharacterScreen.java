import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

// TODO: Auto-generated Javadoc
/**
 * The Class MainMenu.
 */
public class CharacterScreen implements Screen{

	/** The game. */
	SpaceGame game;
	
	/** The exit buttton active. */
	Texture commandoActive;

	/** The exit button inactive. */
	Texture commandoInactive;

	/** The play button active. */
	Texture sniperActive;

	/** The play button inactive. */
	Texture sniperInactive;

	public Object scrollingBackground;

	/** The exit button width. */
	private static int EXIT_BUTTON_WIDTH = 150;
	
	/** The exit button height. */
	private static int EXIT_BUTTON_HEIGHT = 300;

	/** The play button width. */
	private static int PLAY_BUTTON_WIDTH = 150;
	
	/** The play button height. */
	private static int PLAY_BUTTON_HEIGHT = 300;
	
	/** The Constant PLAY_BUTTON_Y. */
	private static final int PLAY_BUTTON_Y = 300;
	
	/** The Constant EXIT_BUTTON_Y. */
	private static final int EXIT_BUTTON_Y = 125;
	
	/**
	 * Instantiates a new main menu.
	 *
	 * @param game the game
	 */
	public CharacterScreen(SpaceGame game){
	
		this.game = game;
		
		commandoActive = new Texture("commandoOn.png");
		commandoInactive = new Texture("commandoOff.png");
		sniperActive = new Texture("sniperOn.png");
		sniperInactive = new Texture("sniperOff.png");

		game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
	
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {

		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		
		
		
		int x = SpaceGame.WIDTH/2 - EXIT_BUTTON_WIDTH/2;
		
		System.out.println(Gdx.input.getX() + ", " + Gdx.input.getY());
		System.out.println(x + EXIT_BUTTON_WIDTH);

		if (Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x && SpaceGame.HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && SpaceGame.HEIGHT - Gdx.input.getY() >  EXIT_BUTTON_Y){
			game.batch.draw(commandoActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
			if(Gdx.input.isTouched()){
				
			 
			}
		}
		else{
			
			game.batch.draw(commandoInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);

		}
		x = SpaceGame.WIDTH/2 - PLAY_BUTTON_WIDTH/2;
		if (Gdx.input.getX() < x + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x && SpaceGame.HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && SpaceGame.HEIGHT - Gdx.input.getY() >  PLAY_BUTTON_Y){
			game.batch.draw(sniperActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
			if(Gdx.input.isTouched()){
				
				game.setScreen(new GameScreen(game));
			}
		}
		else{
			
			game.batch.draw(sniperInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);

		}
		

		game.batch.end();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		
	}

}
