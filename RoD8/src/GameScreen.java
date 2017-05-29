

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Input.Keys;

public class GameScreen implements Screen{

	int roll;
	float x,y;
	final float SPEED = 300;
	float stateTime;
	float rollTimer;
	
	ArrayList<Bullet> bullets;
	
	public static final float SHIP_ANIMATION_SPEED = 0.5f;
	public static final int SHIP_WIDTH_PIXEL = 17;
	public static final int SHIP_HEIGHT_PIXEL = 32;
	public static final int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
	public static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;
	public static final float ROLL_TIME_SWITCH_TIME = 0.12f;
	
	Animation<TextureRegion>[] rolls;
		
	SpaceGame game;
	
	public GameScreen(SpaceGame game){
		
		this.game = game;
		y = 15;
		x = SpaceGame.WIDTH / 2 - SHIP_WIDTH/2;
		
		bullets = new ArrayList<Bullet>();
		
		roll = 2;
		rollTimer = 0;
		rolls = new Animation[5];//Figure this underline later
		TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("ship.png"), SHIP_WIDTH_PIXEL, SHIP_HEIGHT_PIXEL);
	
		rolls[0] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[2]);//All left
		rolls[1] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[1]);
		rolls[2] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[0]);//No tilt
		rolls[3] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[3]);
		rolls[4] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[4]);//All right
	
	}
	
	@Override
	public void show() {


	}

	@Override
	public void render(float delta) {

		//Shooting code
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
			
			bullets.add(new Bullet(x + 4));
			bullets.add(new Bullet(x + SHIP_WIDTH -4));
		}
		
		//Update Bullets
		ArrayList<Bullet> bulletsRemove = new ArrayList<Bullet>();
		for (Bullet bullet : bullets){
			
			bullet.update(delta);
			if(bullet.remove)
				bulletsRemove.add(bullet);
		}
		
		bullets.remove(bulletsRemove);
		
		
		//Movement code
		if(Gdx.input.isKeyPressed(Keys.LEFT)){
			
			if(x >= 0)
				x -= SPEED * Gdx.graphics.getDeltaTime();
			
			//Update roll of key was just pressed
			if(Gdx.input.isKeyJustPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT) && roll != 0){
				
				rollTimer = 0;
				roll--;
			}
			//Update roll
			rollTimer -= Gdx.graphics.getDeltaTime();
			if (rollTimer < -ROLL_TIME_SWITCH_TIME && roll != 0){
				
				rollTimer = 0;
				roll--;					
			}

		}else{
			
			if (roll < 2){
				
				//Update roll
				rollTimer += Gdx.graphics.getDeltaTime();
				if (rollTimer > ROLL_TIME_SWITCH_TIME && roll != 4){		
					
					rollTimer = 0;
					roll++;
				}
			}	
		}
		
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			
			if (x + SHIP_WIDTH <= Gdx.graphics.getWidth()){
				
				x += SPEED*Gdx.graphics.getDeltaTime();
			}
			
			
			//Update roll of key was just pressed
			if(Gdx.input.isKeyJustPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT) && roll != 4){
				
				rollTimer = 0;
				roll++;
			}
			
			//Update roll
			rollTimer += Gdx.graphics.getDeltaTime();
			if (rollTimer > ROLL_TIME_SWITCH_TIME && roll !=4){
				
				rollTimer = 0;
				roll++;				
			}
		}
		else{
			
			if (roll > 2){
				
				rollTimer -= Gdx.graphics.getDeltaTime();
				if (rollTimer < -ROLL_TIME_SWITCH_TIME && roll != 0){
					
					rollTimer = 0;
					roll--;				
				}
			}
		}
		
		stateTime += delta;
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		game.batch.begin();
		
		for (Bullet bullet : bullets){
			
			bullet.render(game.batch);
		}
		game.batch.draw(rolls[roll].getKeyFrame(stateTime, true), x, y, SHIP_WIDTH, SHIP_HEIGHT);
		
		
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




/*		if(Gdx.input.isKeyPressed(Keys.UP)){

y += SPEED*Gdx.graphics.getDeltaTime();
}

if(Gdx.input.isKeyPressed(Keys.DOWN)){

y -= SPEED*Gdx.graphics.getDeltaTime();
}*/

