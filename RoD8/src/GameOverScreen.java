import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * The Class GameOverScreen.
 */
public class GameOverScreen implements Screen{

	/** The game over banner. */
	Texture gameOverBanner;

	/** The score font. */
	BitmapFont scoreFont;

	/** The game. */
	SpaceGame game;

	/** The Constant BANNER_WIDTH. */
	private static final int BANNER_WIDTH = 350;
	
	/** The Constant BANNER_HEIGHT. */
	private static final int BANNER_HEIGHT = 100;
	
	
	/**
	 * Instantiates a new game over screen.
	 *
	 * @param game the game
	 */
	public GameOverScreen(SpaceGame game){
		
		this.game = game;
		
		//Load textures and fonts
		gameOverBanner = new Texture("game_over.png");
		scoreFont = new BitmapFont();
			

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

		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		
		game.scrollingBackground.updateAndRender(delta, game.batch);

		game.batch.draw(gameOverBanner, Gdx.graphics.getWidth()/2 - BANNER_WIDTH/2, Gdx.graphics.getHeight() - BANNER_HEIGHT - 15, BANNER_WIDTH, BANNER_HEIGHT);	

		GlyphLayout tryAgainLayout = new GlyphLayout(scoreFont, "Try Again");
		GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "Main Menu");
		
		float tryAgainX = SpaceGame.WIDTH / 2 - tryAgainLayout.width / 2;
		float tryAgainY = SpaceGame.HEIGHT / 2 - tryAgainLayout.height / 2;
		
		float mainMenuX = SpaceGame.WIDTH / 2 - mainMenuLayout.width / 2;
		float mainMenuY = SpaceGame.HEIGHT / 2 - mainMenuLayout.height/2 - tryAgainLayout.height - 20;
		
		float touchX = Gdx.input.getX(), touchY = SpaceGame.HEIGHT - Gdx.input.getY();
		
		//If user is clicking
		if (Gdx.input.isTouched()){//Episode 14
			//Try again
			if (touchX > tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY > tryAgainY && touchY < tryAgainY + tryAgainLayout.height){
				
				this.dispose();
				game.batch.end();
				game.setScreen(new CharacterScreen(game));
				return;
			}
			
			if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY && touchY < mainMenuY + mainMenuLayout.height){
				
				this.dispose();
				game.batch.end();
				game.setScreen(new MainMenu(game));
				return;
			}
		}
		
		scoreFont.draw(game.batch, tryAgainLayout, tryAgainX, tryAgainY);
		scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);
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