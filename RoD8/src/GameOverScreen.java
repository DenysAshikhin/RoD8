import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

public class GameOverScreen implements Screen{

	private static final int BANNER_WIDTH = 350;
	private static final int BANNER_HEIGHT = 100;
	
	
	Texture gameOverBanner;
	BitmapFont scoreFont;
	SpaceGame game;
	int score, highscore;
	
	public GameOverScreen(SpaceGame game, int score){
		
		this.game = game;
		this.score = score;
		
		//Get highscore from save file
		Preferences prefs = Gdx.app.getPreferences("SpaceGame");//Can be found in user folder, .prefs 
		this.highscore = prefs.getInteger("highscore", 0);
	
		//Check if score beats highscore
		if (score > highscore){
			
			prefs.putInteger("highscore", score);
			prefs.flush();
		}
		
		//Load textures and fonts
		gameOverBanner = new Texture("game_over.png");
		scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
			
	}
	
	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
	
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		
		game.batch.draw(gameOverBanner, Gdx.graphics.getWidth()/2 - BANNER_WIDTH/2, Gdx.graphics.getHeight() - BANNER_HEIGHT - 15, BANNER_WIDTH, BANNER_HEIGHT);
		
		GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "Score: \n" + score, Color.WHITE, 0, Align.left, false);
		GlyphLayout highscoreLayout = new GlyphLayout(scoreFont, "Highscore: \n" + highscore, Color.WHITE, 0, Align.left, false);

		scoreFont.draw(game.batch, scoreLayout, Gdx.graphics.getWidth()/2 - scoreLayout.width/2, Gdx.graphics.getHeight() - BANNER_HEIGHT - 15 * 2);
		scoreFont.draw(game.batch, highscoreLayout, Gdx.graphics.getWidth()/2 - highscoreLayout.width/2, Gdx.graphics.getHeight() - BANNER_HEIGHT - scoreLayout.height - 15 * 3);

		GlyphLayout tryAgainLayout = new GlyphLayout(scoreFont, "Try Again");
		GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "Main Menu");
		
		float tryAgainX = Gdx.graphics.getWidth()/2 - tryAgainLayout.width / 2;
		float tryAgainY = Gdx.graphics.getHeight() /2 - tryAgainLayout.height/2;
		
		float mainMenuX = Gdx.graphics.getWidth()/2 - mainMenuLayout.width / 2;
		float mainMenuY = Gdx.graphics.getHeight() /2 - mainMenuLayout.height/2 - tryAgainLayout.height - 15;
		
		float touchX = Gdx.input.getX(), touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
		
		//If user is clicking
		if (Gdx.input.isTouched()){//Episode 14
			//Try again
			if (touchX > tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY > tryAgainY - tryAgainLayout.height && touchY < tryAgainY){
				
				this.dispose();
				game.batch.end();
				game.setScreen(new GameScreen(game));
				return;
			}
			
			if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY){
				
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

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}



}
