package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Input.Keys;

public class GameScreen implements Screen{

	Texture img;
	float x,y;
	final float SPEED = 120;
	
	SpaceGame game;
	
	public GameScreen(SpaceGame game){
		
	}
	
	@Override
	public void show() {

		
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isKeyPressed(Keys.UP)){
			
			y += SPEED*Gdx.graphics.getDeltaTime();
		}
		
		if(Gdx.input.isKeyPressed(Keys.LEFT)){
			
			x -= SPEED*Gdx.graphics.getDeltaTime();
		}
		
		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			
			y -= SPEED*Gdx.graphics.getDeltaTime();
		}
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			
			x += SPEED*Gdx.graphics.getDeltaTime();
		}
		
		batch.begin();
		batch.draw(img, x, y);
		batch.end();
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
