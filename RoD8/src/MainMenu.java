import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class MainMenu implements Screen{

	SpaceGame game;
	
	private static int EXIT_BUTTON_WIDTH = 300;
	private static int EXIT_BUTTON_HEIGHT = 150;

	private static int PLAY_BUTTON_WIDTH = 330;
	private static int PLAY_BUTTON_HEIGHT = 150;
	
	private static final int PLAY_BUTTON_Y = 300;
	private static final int EXIT_BUTTON_Y = 100;
	
	Texture exitButttonActive;
	Texture exitButtonInactive;
	Texture playButtonActive;
	Texture playButtonInactive;
	
	
	public MainMenu(SpaceGame game){
	
		this.game = game;
		playButtonActive = new Texture("play_button_active.png");
		playButtonInactive = new Texture("play_button_inactive.png");
		exitButttonActive = new Texture("exit_button_active.png");
		exitButtonInactive = new Texture("exit_button_inactive.png");

		game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
	
	}
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0.25f, 0.25f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		
		game.scrollingBackground.updateAndRender(delta, game.batch);

		
		int x = SpaceGame.WIDTH/2 - EXIT_BUTTON_WIDTH/2;
		
		if (Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x && SpaceGame.HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && SpaceGame.HEIGHT - Gdx.input.getY() >  EXIT_BUTTON_Y ){
			
			game.batch.draw(exitButttonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
			if(Gdx.input.isTouched()){
				
				Gdx.app.exit();
			}
		}
		else{
			
			game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);

		}

		x = SpaceGame.WIDTH/2 - PLAY_BUTTON_WIDTH/2;
		if (Gdx.input.getX() < x + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x && SpaceGame.HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && SpaceGame.HEIGHT - Gdx.input.getY() >  PLAY_BUTTON_Y ){
			
			game.batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
			if(Gdx.input.isTouched()){
				
				game.setScreen(new GameScreen(game));
			}
		}
		else{
			
			game.batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
		}
		
		
		
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
