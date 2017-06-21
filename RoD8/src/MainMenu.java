import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

/**
 * The Class MainMenu.
 */
public class MainMenu implements Screen{

	/** The game. */
	SpaceGame game;
	
	/** The exit buttton active. */
	Texture exitButttonActive;

	/** The exit button inactive. */
	Texture exitButtonInactive;

	/** The play button active. */
	Texture playButtonActive;

	/** The play button inactive. */
	Texture playButtonInactive;

	/** The exit button width. */
	private static int EXIT_BUTTON_WIDTH = 300;
	
	/** The exit button height. */
	private static int EXIT_BUTTON_HEIGHT = 150;

	/** The play button width. */
	private static int PLAY_BUTTON_WIDTH = 33;
	
	/** The play button height. */
	private static int PLAY_BUTTON_HEIGHT = 15;
	
	/** The Constant PLAY_BUTTON_Y. */
	private static final int PLAY_BUTTON_Y = 30;
	
	/** The Constant EXIT_BUTTON_Y. */
	private static final int EXIT_BUTTON_Y = 125;
	
	/**
	 * Instantiates a new main menu.
	 *
	 * @param game the game
	 */
	public MainMenu(SpaceGame game){
	
		this.game = game;
		
		playButtonActive = new Texture("play_button_active.png");
		playButtonInactive = new Texture("play_button_inactive.png");
		exitButttonActive = new Texture("exit_button_active.png");
		exitButtonInactive = new Texture("exit_button_inactive.png");

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
			
	
			game.batch.draw(exitButttonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
		}
		else{
			
			game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);

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
