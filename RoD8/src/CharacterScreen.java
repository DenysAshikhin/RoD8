import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * The Class MainMenu.
 */
public class CharacterScreen implements Screen{

	/** The game. */
	SpaceGame game;
	
	BitmapFont scoreFont;

	
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
	private static int EXIT_BUTTON_WIDTH = 300;
	
	/** The exit button height. */
	private static int EXIT_BUTTON_HEIGHT = 300;

	/** The play button width. */
	private static int PLAY_BUTTON_WIDTH = 300;
	
	/** The play button height. */
	private static int PLAY_BUTTON_HEIGHT = 300;
	
	/** The Constant PLAY_BUTTON_Y. */
	private static final int PLAY_BUTTON_X = 800;
	
	/** The Constant EXIT_BUTTON_Y. */
	private static final int EXIT_BUTTON_X = 300;
	
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
	
		scoreFont = new BitmapFont();
		scoreFont.getData().setScale(0.5f);
		
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
		GlyphLayout guiLayout = new GlyphLayout(scoreFont, "Hold the up arrow to climb ladders.");

		int y = SpaceGame.HEIGHT/2 - EXIT_BUTTON_HEIGHT/2;
		
		if (Gdx.input.getY() < y + EXIT_BUTTON_HEIGHT && Gdx.input.getY() > y && Gdx.input.getX() < EXIT_BUTTON_X + EXIT_BUTTON_WIDTH && Gdx.input.getX() >  EXIT_BUTTON_X){
			
			game.batch.draw(commandoActive, EXIT_BUTTON_X, y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
			scoreFont.getData().setScale(2);
			guiLayout = new GlyphLayout(scoreFont, "Quick, weak attacks.");
			
			scoreFont.draw(game.batch, guiLayout, 190, 200);
			if(Gdx.input.isTouched()){

				
				game.setScreen(new GameScreen(game, 1));
			}
		}
		else{
			
			game.batch.draw(commandoInactive, EXIT_BUTTON_X, y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);

		}
		
		y = SpaceGame.HEIGHT/2 - PLAY_BUTTON_HEIGHT/2;
		
		if (Gdx.input.getY() < y + PLAY_BUTTON_HEIGHT && Gdx.input.getY() > y && Gdx.input.getX() < PLAY_BUTTON_X + PLAY_BUTTON_WIDTH && Gdx.input.getX() >  PLAY_BUTTON_X){
			
			game.batch.draw(sniperActive, PLAY_BUTTON_X, y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
			scoreFont.getData().setScale(2);
			guiLayout = new GlyphLayout(scoreFont, "Powerful, slow attacks.");
			
			scoreFont.draw(game.batch, guiLayout, 190, 200);
			if(Gdx.input.isTouched()){
				
				game.setScreen(new GameScreen(game, 2));
			}
		}
		else{
			
			game.batch.draw(sniperInactive, PLAY_BUTTON_X, y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
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
