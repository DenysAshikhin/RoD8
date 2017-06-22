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


		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		GlyphLayout guiLayout;
		
		float x;
		float y;
		float width;
		float height;
		
		width = SpaceGame.WIDTH / 5;
		height = SpaceGame.WIDTH / 3;
		
		x = SpaceGame.WIDTH / 5;
		y = SpaceGame.HEIGHT / 4;
		
		if (Gdx.input.getY() < y + height && Gdx.input.getY() > y && Gdx.input.getX() < x + width && Gdx.input.getX() > x){
			
			game.batch.draw(commandoActive, x, y, width, height);
			scoreFont.getData().setScale(2);
			guiLayout = new GlyphLayout(scoreFont, "Quick, weak attacks.");
			scoreFont.draw(game.batch, guiLayout, x, y - 20);

			guiLayout = new GlyphLayout(scoreFont, "* Basic light attacks.");
			scoreFont.draw(game.batch, guiLayout, x + 40, y - 50);
			
			guiLayout = new GlyphLayout(scoreFont, "* Knock-back, medium damage.");
			scoreFont.draw(game.batch, guiLayout, x + 40, y - 80);

			guiLayout = new GlyphLayout(scoreFont, "* Roll to safety.");
			scoreFont.draw(game.batch, guiLayout, x + 40, y - 110);

			guiLayout = new GlyphLayout(scoreFont, "* Rapid fire in both directions.");
			scoreFont.draw(game.batch, guiLayout, x + 40, y - 140);
			
			if(Gdx.input.isTouched()){

				
				game.setScreen(new GameScreen(game, 1));
			}
		}
		else{
			
			game.batch.draw(commandoInactive, x, y, width, height);

		}

		x = 3 * SpaceGame.WIDTH / 5;
		y = SpaceGame.HEIGHT / 4;
		
		if (Gdx.input.getY() < y + height && Gdx.input.getY() > y && Gdx.input.getX() < x + width && Gdx.input.getX() > x){
			
			game.batch.draw(sniperActive, x, y, width, height);
			scoreFont.getData().setScale(2);
			guiLayout = new GlyphLayout(scoreFont, "Powerful, slow attacks.");			
			scoreFont.draw(game.batch, guiLayout, x, y - 20);

			guiLayout = new GlyphLayout(scoreFont, "* Medium light attack.");
			scoreFont.draw(game.batch, guiLayout, x + 40, y - 50);
			
			guiLayout = new GlyphLayout(scoreFont, "* Leap backwards to safety.");
			scoreFont.draw(game.batch, guiLayout, x + 40, y - 80);

			guiLayout = new GlyphLayout(scoreFont, "* Powerful knockback.");
			scoreFont.draw(game.batch, guiLayout, x + 40, y - 110);

			guiLayout = new GlyphLayout(scoreFont, "* Mark an enemy for double damage.");
			scoreFont.draw(game.batch, guiLayout, x + 40, y - 140);
			if(Gdx.input.isTouched()){
				
				game.setScreen(new GameScreen(game, 2));
			}
		}
		else{
			
			game.batch.draw(sniperInactive, x, y, width, height);
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
