

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class GameScreen implements Screen{

	int roll;
	float x,y;
	final float SPEED = 300;
	float stateTime;
	float rollTimer;
	float shootTimer;
	float asteroidSpawnTimer;
	Random random;
	
	private World world;
	private Box2DDebugRenderer b2dr;
	
	ArrayList<Bullet> bullets;
	ArrayList<Asteroid> asteroids;
	ArrayList<Explosion> explosions;
	
	BitmapFont scoreFont;
	
	float health = 1;// 0 = dead, 1 = full health
	
	int score;
	
	CollisionRect playerRect;
	
	Texture healthTexture;
	
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
		

		
		//Create platform
		BodyDef bdef = new BodyDef();
		bdef.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		bdef.type = BodyType.StaticBody;//Static don't move, unaffected by forces
										//Dyanamic - always get affected by forces
										//Kinematic - don't get affected by world froces, but can change velocities
		
		
		random = new Random();//Episode 11 for logic on timing
		asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
		
		asteroids = new ArrayList<Asteroid>();
		bullets = new ArrayList<Bullet>();
		explosions = new ArrayList<Explosion>();
		
		playerRect = new CollisionRect(0, 0, SHIP_WIDTH, SHIP_HEIGHT);
		
		scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
		
		score = 0;
		
		healthTexture = new Texture("blank.png");
		
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
	
		game.scrollingBackground.setSpeedFixed(false);
		game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
	}
	
	@Override
	public void show() {
		
		//b2dr = new Box2DDebugRenderer();
		world = new World(new Vector2(0, -9.8f), true);
	}

	@Override
	public void render(float delta) {
		

		
		//Update Asteroids
		ArrayList<Asteroid> asteroidsRemove = new ArrayList<Asteroid>();
		for(Asteroid asteroid : asteroids){
			
			asteroid.update(delta);
			if(asteroid.remove)
				asteroidsRemove.add(asteroid);
		}
		

		
		//Update Bullets
		ArrayList<Bullet> bulletsRemove = new ArrayList<Bullet>();
		for (Bullet bullet : bullets){
			
			bullet.update(delta);
			if(bullet.remove)
				bulletsRemove.add(bullet);
		}
		
		//Update Explosions
		ArrayList<Explosion> explosionsRemove = new ArrayList<Explosion>();
		for (Explosion explosion : explosions){
			
			explosion.update(delta);
			if (explosion.remove)
				explosionsRemove.add(explosion);
			
		}
		
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
		

		//After player moves, collision update
		playerRect.move(x, y);
		
		for(Asteroid asteroid : asteroids){
			
			if (asteroid.getCollisionRect().collidesWith(playerRect)){
				
				asteroidsRemove.add(asteroid);
				health -= 0.1;//Add f?
				//Check depleted health
				if (health <=0){
					
					this.dispose();
					game.setScreen(new GameOverScreen(game, score));
					return;
				}
			}
		}
		
		//After all updates, check collision
		for (Bullet bullet : bullets){
			
			for (Asteroid asteroid : asteroids){
				
				if (bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect())){
					
					asteroidsRemove.add(asteroid);
					bulletsRemove.add(bullet);
					explosions.add(new Explosion(asteroid.getX(), asteroid.getY()));
					score += 100;
				}
				
			}
		}
		
		bullets.removeAll(bulletsRemove);
		asteroids.removeAll(asteroidsRemove);
		explosions.removeAll(explosionsRemove);
		
		GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "" + score);
		
		stateTime += delta;
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		
		game.scrollingBackground.updateAndRender(delta, game.batch);
	
		for (Bullet bullet : bullets){
			
			bullet.render(game.batch);
		}
		
		for (Asteroid asteroid : asteroids){
			
			asteroid.render(game.batch);
		}
		
		for (Explosion explosion : explosions){
			
			explosion.render(game.batch);
		}
		
		
		//Draw Health
		if(health > 0.6f)
			game.batch.setColor(Color.GREEN);
		else if(health > 0.2f)
			game.batch.setColor(Color.ORANGE);
		else
			game.batch.setColor(Color.RED);
		game.batch.draw(healthTexture, 0, 0, Gdx.graphics.getWidth() * health, 5);	
		game.batch.setColor(Color.WHITE);
		
		game.batch.draw(rolls[roll].getKeyFrame(stateTime, true), x, y, SHIP_WIDTH, SHIP_HEIGHT);
		
		scoreFont.draw(game.batch, scoreLayout, Gdx.graphics.getWidth()/2 - scoreLayout.width/2, Gdx.graphics.getHeight() - scoreLayout.height / 2);

		
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

