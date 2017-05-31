

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen{

	private boolean debug = false;
	
	int roll;
	float x,y;
	final float SPEED = 300;
	float stateTime;
	float rollTimer;
	float shootTimer;
	float asteroidSpawnTimer;
	Random random;
	
	private float stillTime;
	
	private World world;
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	private OrthographicCamera b2dCam;
	
	ArrayList<Bullet> bullets;
	ArrayList<Asteroid> asteroids;
	ArrayList<Explosion> explosions;
	Array<Crystal> crystals;
	
	BitmapFont scoreFont;
	
	float health = 1f;// 0 = dead, 1 = full health
	
	int score;
	
	CollisionRect playerRect;
	
	Texture healthTexture;
	
	SpriteBatch spriteBatch;
	
	public static final float SHIP_ANIMATION_SPEED = 0.5f;
	public static final int SHIP_WIDTH_PIXEL = 17;
	public static final int SHIP_HEIGHT_PIXEL = 32;
	public static final int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
	public static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;
	public static final float ROLL_TIME_SWITCH_TIME = 0.12f;
	public static final float SHOOT_WAIT_TIME = 0.3f;
	public static final float MIN_ASTEROID_SPAWN_TIME = 0.3f;
	public static final float MAX_ASTEROID_SPAWN_TIME = 0.6f;
	public static final float PPM = 100;//Conversion of 100 pixels = 1 metre
	public static Content textures;
	
	
	//Filter Bits
	public static final short BIT_PLAYER = 2;
	public static final short BIT_RED = 4;
	public static final short BIT_GREEN = 8;
	public static final short BIT_BLUE = 16;
	public static final short BIT_CRYSTAL = 32;
	
	private Player player;
	private HUD hud;
	private MyContactListener contactListener;
	
	private int direction;
	
	Animation<TextureRegion>[] rolls;
	Animation<TextureRegion>[] run;
		
	SpaceGame game;
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	private float tileSize;
	
	
	public GameScreen(SpaceGame game){
		
		this.game = game;
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		 hudCam = new OrthographicCamera();
		 hudCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(cam.combined);
		
		world = new World(new Vector2(0, -9.81f), true);

		contactListener = new MyContactListener();
		
		//Collision set up?
		 world.setContactListener(contactListener);	
		 
		stillTime = 0;
		 
		y = 15;
		x = SpaceGame.WIDTH / 2 - SHIP_WIDTH/2;		
		
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
	
		
		textures = new Content();
		textures.loadTexture("commando_good.png", "commando");
		
		//Texture texture = textures.getTexture("commando");
		//TextureRegion[] sprites = TextureRegion.split(texture, 8, 12)[1];
		//System.out.println(sprites.length);
		//run = new Animation[3];
		//run[1] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, sprites);
		
		
		/*TextureRegion[][] playerMoveSheet = TextureRegion.split(new Texture("commando_good.png"), 8, 12);
		run = new Animation[3];
		
		run[0] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, playerMoveSheet[0]);//Still
		run[1] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, playerMoveSheet[1]);//Right
		run[2] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, playerMoveSheet[2]);//Left*/
		
		
		game.scrollingBackground.setSpeedFixed(false);
		game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
	
		
		
		
		
		/////////////////////
		

		textures.loadTexture("bunny.png", "bunny");
		textures.loadTexture("crystal.png", "crystal");
		textures.loadTexture("hud.png", "hud");
	
		
		createPlayer();
		createTiles();
		createCrystals();
		
		/**Setting up the camera and world*/
		
		
		b2dr = new Box2DDebugRenderer();
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		//Set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM);

		 //Set up hud
		 hud = new HUD(player);
		 
		
		/***/	
	}
	
	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		/**Get rid of this to return to default tutorial game*/
		
		stateTime += delta;
		
		world.step(delta, 6, 2);
		
		//Remove crystals
		Array<Body> bodies = contactListener.getBodiesToRemove();
		for (Body body : bodies){
			
			player.collectCrystal();
			crystals.removeValue((Crystal) body.getUserData(), true);
			world.destroyBody(body);
		}
		bodies.clear();
		
		player.update(delta);
		
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//spriteBatch.begin();
		
		//game.batch.begin();
		
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
			if(contactListener.isPlayerOnGround()){
				
				player.getBody().applyForceToCenter(0, 300, true);
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.LEFT)){
				
			//spriteBatch.draw(run[1].getKeyFrame(stateTime, true), x, y, SHIP_WIDTH, SHIP_HEIGHT);
			player.setState(2);
			if(player.getBody().getLinearVelocity().x > -2f){
				player.getBody().applyLinearImpulse(new Vector2(-1f, 0f), player.getPosition(), true);
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			
			//spriteBatch.draw(run[2].getKeyFrame(stateTime, true), x, y, 8, 12)''
			
			player.setState(1);
				if(player.getBody().getLinearVelocity().x < 2f){
					player.getBody().applyLinearImpulse(new Vector2(1f, 0f), player.getPosition(), true);
				}

		}
		
		if(!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT)){
			
			stillTime += Gdx.graphics.getDeltaTime();
			direction = 0;
			player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x * 0.9f, player.getBody().getLinearVelocity().y);
		}


		cam.position.set(player.getPosition().x * PPM, player.getPosition().y * PPM, 0);
		cam.update();
		spriteBatch.setProjectionMatrix(cam.combined);
		//spriteBatch.draw(run[direction].getKeyFrame(stateTime, true), player.getBody().getPosition().x * 100 - 4/2, player.getBody().getPosition().y * 100 - 6 / 2, 8, 12);

		//spriteBatch.begin();
		//Draw player
		player.render(spriteBatch);
		
			//int width = run[1].getKeyFrame(delta).getRegionWidth();
			
			//int height = run[1].getKeyFrame(delta).getRegionHeight();
			
			//spriteBatch.draw(rolls[1].getKeyFrame(stateTime, true), player.getBody().getPosition().x * 100 - width/2, player.getBody().getPosition().y * 100 - height / 2, 0, 0, width, height, 4, 4, 0);
		
		
		//Draw crystals
		for(int i = 0; i < crystals.size; i++){
			
			crystals.get(i).update(delta);
			crystals.get(i).render(spriteBatch);
		}
		
		tmr.setView(cam);
		tmr.render();

		//draw hud
		//spriteBatch.setProjectionMatrix(hudCam.combined);
		//hud.render(spriteBatch);
		


		
		if(debug){
			
			b2dCam.position.set(player.getPosition().x * PPM, player.getPosition().y * PPM, 0);
			b2dCam.update();
			b2dr.render(world, b2dCam.combined);;
		}
		
		/**Get rid of this to return to default tutorial game*/
	
	}
	
	private void createPlayer(){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		//Create Player
		bdef.position.set(90 / PPM, 100 / PPM);
		bdef.type = BodyType.DynamicBody;
		//bdef.linearVelocity.set(1f, 0);
		Body body = world.createBody(bdef);
		
		shape.setAsBox(15 / PPM, 15 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_PLAYER;
		fdef.filter.maskBits = BIT_RED | BIT_CRYSTAL;
		body.createFixture(fdef).setUserData("player");
		
		
		//Create Player
		player = new Player(body);
		player.setState(1);
		
		//Create foot sensor
		shape.setAsBox(13 / PPM,  6 / PPM, new Vector2(0, -14 / PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_PLAYER;
		fdef.filter.maskBits = BIT_RED;	
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");
	}
	
	private void createTiles(){
		
		 tileMap = new TmxMapLoader().load("tester3.tmx");
		 tmr = new OrthogonalTiledMapRenderer(tileMap);

		 tileSize = (int) tileMap.getProperties().get("tilewidth");
		 
		 TiledMapTileLayer layer;
		 layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
		 createLayer(layer, BIT_RED);
		 layer = (TiledMapTileLayer) tileMap.getLayers().get("green");
		 createLayer(layer, BIT_GREEN);
		 layer = (TiledMapTileLayer) tileMap.getLayers().get("blue");
		 createLayer(layer, BIT_BLUE);
	}
	
	private void createLayer(TiledMapTileLayer layer, short bits){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		 //Go through all cells in layer
		 for(int row = 0; row < layer.getHeight(); row++){
			 
			 for(int col = 0; col < layer.getWidth(); col++){
				 
				 //Get cell
				 Cell cell = layer.getCell(col, row);
				 
				 //Check if cell exists
				 if (cell == null) continue;
				 if (cell.getTile() == null) continue;
				 
				 //Create a body + fixture from cell
				 
				 bdef.type = BodyType.StaticBody;//Episode 5
				 bdef.position.set((col + 0.5f) * tileSize / PPM, (row + 0.5f) * tileSize / PPM);

				 ChainShape chainShape = new ChainShape();
				 Vector2[] vertices = new Vector2[4];
				 vertices[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);//Bottom left corner
				 vertices[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
				 vertices[2] = new Vector2(+tileSize / 2 / PPM, tileSize / 2 / PPM);//Upper right corner
				 vertices[3] = new Vector2(tileSize /2 / PPM, -tileSize /2 / PPM);
				 chainShape.createChain(vertices);
				 fdef.isSensor = false;
				 fdef.friction = 0;
				 fdef.shape = chainShape;
				 fdef.filter.categoryBits = bits;
				 fdef.filter.maskBits = BIT_PLAYER;
				 
				 world.createBody(bdef).createFixture(fdef);
			 }
		 }
	}
	
	private void createCrystals(){
		
		crystals = new Array<Crystal>();
		
		MapLayer layer = tileMap.getLayers().get("crystals");
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		
		for (MapObject mapObject : layer.getObjects()){
			
			bdef.type = BodyType.StaticBody;
			float x = mapObject.getProperties().get("x", Float.class)/ PPM;
			float y = mapObject.getProperties().get("y", Float.class)/ PPM;

			bdef.position.set(x,y);
			CircleShape circleShape = new CircleShape();
			circleShape.setRadius(8 / PPM);
			
			fdef.shape = circleShape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = BIT_CRYSTAL;
			fdef.filter.maskBits = BIT_PLAYER;
			
			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("crystal");;
			
			Crystal c = new Crystal(body);
			crystals.add(c);
			
			body.setUserData(c);
		}
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

/**Group index filtering - look it up later*/

/*


The following is for render:	//Update Asteroids
		/*ArrayList<Asteroid> asteroidsRemove = new ArrayList<Asteroid>();
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


//Create platform
		BodyDef bdef = new BodyDef();
		bdef.position.set(200 / PPM, (Gdx.graphics.getHeight() - 100) / PPM);
		bdef.type = BodyType.StaticBody;//Static don't move, unaffected by forces
										//Dyanamic - always get affected by forces
										//Kinematic - don't get affected by world forces, but can change velocities
		Body body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(100 / PPM, 10 / PPM);//1/2 width and height
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_GROUND;
		fdef.filter.maskBits = BIT_PLAYER;
		body.createFixture(fdef).setUserData("ground");




 * Ball code:
 * //Create ball
		bdef.position.set(230/PPM, 10 / PPM);
		body = world.createBody(bdef);
		
		CircleShape cshape = new CircleShape();
		cshape.setRadius(25 / PPM);
		fdef.shape = cshape;
		fdef.filter.maskBits = BIT_GROUND;
		fdef.filter.categoryBits = BIT_BALL;

		body.createFixture(fdef).setUserData("ball");
		
		cam = new OrthographicCamera();
		cam.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
 */

/*		if(Gdx.input.isKeyPressed(Keys.UP)){

y += SPEED*Gdx.graphics.getDeltaTime();
}

if(Gdx.input.isKeyPressed(Keys.DOWN)){

y -= SPEED*Gdx.graphics.getDeltaTime();
}*/

