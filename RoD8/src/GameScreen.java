

import java.util.ArrayList;
import java.util.Random;

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
	float shootTimer;
	float asteroidSpawnTimer;
	Random random;
	
	ArrayList<Bullet> bullets;
	ArrayList<Asteroid> asteroids;
	
	public static final float SHIP_ANIMATION_SPEED = 0.5f;
	public static final int SHIP_WIDTH_PIXEL = 17;
	public static final int SHIP_HEIGHT_PIXEL = 32;
	public static final int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
	public static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;
	public static final float ROLL_TIME_SWITCH_TIME = 0.12f;
	public static final float SHOOT_WAIT_TIME = 0.3f;
	public static final float MIN_ASTEROID_SPAWN_TIME = 0.3f;
	public static final float MAX_ASTEROID_SPAWN_TIME = 0.6f;
	
	
	Animation<TextureRegion>[] rolls;
		
	SpaceGame game;
	
	public GameScreen(SpaceGame game){
		
		this.game = game;
		y = 15;
		x = SpaceGame.WIDTH / 2 - SHIP_WIDTH/2;
		
		random = new Random();//Episode 11 for logic on timing
		asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
		
		asteroids = new ArrayList<Asteroid>();
		bullets = new ArrayList<Bullet>();
		
		roll = 2;
		shootTimer = 0;
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
		shootTimer += delta;
		if(Gdx.input.isKeyPressed(Keys.SPACE) && shootTimer >= SHOOT_WAIT_TIME){
			
			shootTimer = 0;
			
			int offset = 4;
			if (roll == 1 || roll == 3)//Slight Tilt
				offset = 8;
			
			if (roll == 0 || roll == 4)//Full Tilt
				offset = 16;
			
			
			bullets.add(new Bullet(x + offset));
			bullets.add(new Bullet(x + SHIP_WIDTH - offset));
		}
		
		
		//Asteroid Logic
		asteroidSpawnTimer -= delta;
		if (asteroidSpawnTimer <= 0){
			
			asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
			asteroids.add(new Asteroid(random.nextInt(Gdx.graphics.getWidth() - Asteroid.WIDTH)));
		}
		
		//Update Asteroids
		ArrayList<Asteroid> asteroidsRemove = new ArrayList<Asteroid>();
		for(Asteroid asteroid : asteroids){
			
			asteroid.update(delta);
			if(asteroid.remove)
				asteroidsRemove.add(asteroid);
		}
		asteroids.remove(asteroidsRemove);

		
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
				
				rollTimer += ROLL_TIME_SWITCH_TIME;
				roll--;					
			}

		}else{
			
			if (roll < 2){
				
				//Update roll
				rollTimer += Gdx.graphics.getDeltaTime();
				if (rollTimer > ROLL_TIME_SWITCH_TIME && roll != 4){		
					
					rollTimer += ROLL_TIME_SWITCH_TIME;
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
				
				rollTimer -= ROLL_TIME_SWITCH_TIME;
				roll++;				
			}
		}
		else{
			
			if (roll > 2){
				
				rollTimer -= Gdx.graphics.getDeltaTime();
				if (rollTimer < -ROLL_TIME_SWITCH_TIME && roll != 0){
					
					rollTimer -= ROLL_TIME_SWITCH_TIME;
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
		
		for (Asteroid asteroid : asteroids){
			
			asteroid.render(game.batch);
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

