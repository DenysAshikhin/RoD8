import java.util.ArrayList;

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


/**
 * The Class GameScreen.
 */
public class GameScreen implements Screen{

	/** The debug. */
	private boolean debug = true;
		
	/** The world. */
	private World world;
	
	/** The b 2 dr. */
	private Box2DDebugRenderer b2dr;
	
	/** The b 2 d cam. */
	private OrthographicCamera b2dCam;
	
	/** The tile map. */
	private TiledMap tileMap;
	
	/** The tile size. */
	private float tileSize;
	
	/** The tiled map renderer. */
	private OrthogonalTiledMapRenderer tmr;
	
	/** The player. */
	private Player player;

	/** The monsters. */
	private Array<Monster> monsterList = new Array<Monster>();

	/** The crystals. */
	private Array<Crystal> crystals;
		
	/** The state time. */
	float stateTime;
	
	/** The cam. */
	private OrthographicCamera cam;
	//private OrthographicCamera hudCam;

	/** The score font. */
	BitmapFont scoreFont;

	/** The health texture. */
	Texture healthTexture;
	
	/** The sprite batch. */
	SpriteBatch spriteBatch;
	
	/** The Constant PPM. */
	public static final float PPM = 100;//Conversion of 100 pixels = 1 metre
	
	/** The textures. */
	public static Content textures;
	
	/** The Constant PLAYER_WIDTH. */
	public static final float PLAYER_WIDTH = 8f;
	
	/** The Constant PLAYER_HEIGHT. */
	public static final float PLAYER_HEIGHT = 20f;
	
	public static final float CRAB_WIDTH = 40f;
	
	public static final float CRAB_HEIGHT = 40f;
	
	public static final float CRAB_RANGE = 20f;
	
	public static final float DETECTION_RANGE = 200f;
	
	/** The Constant SCALE. */
	public static final float SCALE = 1f;
	
	/** The Constant BIT_PLAYER. */
	//Filter Bits
	public static final short BIT_PLAYER = 2;
	
	/** The Constant BIT_RED. */
	public static final short BIT_RED = 4;
	
	/** The Constant BIT_GREEN. */
	public static final short BIT_GREEN = 8;
	
	/** The Constant BIT_BLUE. */
	public static final short BIT_BLUE = 16;
	
	/** The Constant BIT_CRYSTAL. */
	public static final short BIT_CRYSTAL = 32;
	
	/** The Constant BIT_MONSTER. */
	public static final short BIT_MONSTER = 64;
	
	/** The contact listener. */
	private MyContactListener contactListener;	

	Animation<TextureRegion> runRight;
	Animation<TextureRegion> jumpRight;
	Animation<TextureRegion> standingRight;
	Animation<TextureRegion> climbing;
	Animation<TextureRegion> primaryRight;
	Animation<TextureRegion> secondaryRight;
	Animation<TextureRegion> tertiaryRight;
	Animation<TextureRegion> quaternaryRight;

	Animation<TextureRegion> runRightCrab;
	Animation<TextureRegion> standingRightCrab;
	Animation<TextureRegion> primaryRightCrab;
	Animation<TextureRegion> deathRightCrab;
	
	private float framesRun;
	private float animTime;
	private TextureRegion prevFrame = null;
	
	/** The crab. */
	Texture crab;
	
	/** The crystal. */
	Texture crystal;
		
	
	/**
	 * Instantiates a new game screen.
	 *
	 * @param game the game
	 */
	@SuppressWarnings("static-access")
	public GameScreen(SpaceGame game){
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, game.WIDTH, game.HEIGHT);
		
		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, game.WIDTH / PPM, game.HEIGHT / PPM);
		
		spriteBatch = new SpriteBatch();
		
		world = new World(new Vector2(0, -9.81f), true);
		contactListener = new MyContactListener();
		world.setContactListener(contactListener);	
		b2dr = new Box2DDebugRenderer();
		
		//Load textures (temp)
		textures = new Content();
		textures.loadTexture("commando_final.png", "commando");
		textures.loadTexture("bunny.png", "bunny");
		textures.loadTexture("crystal.png", "crystal");
		textures.loadTexture("hud.png", "hud");
		textures.loadTexture("Monster Crab.png", "crab");
		
		crab = textures.getTexture("crab");
		crystal = textures.getTexture("crystal");
		
		//Create player, tiles and crystals
		createPlayer();
		createTiles();
		createCrystals();

		createMonster();
		
		//Temporary loading of textures for commando animations
		Texture texture = textures.getTexture("commando");
		TextureRegion[] sprites = new TextureRegion[4];
		
		sprites = TextureRegion.split(texture, 7, 13)[0];
		standingRight = new Animation<TextureRegion>(0.07f, sprites[0]);
		jumpRight = new Animation<TextureRegion>(0.07f, sprites[1]);

		sprites = TextureRegion.split(texture, 7, 13)[0];
		climbing = new Animation<TextureRegion>(0.07f, sprites[3]);
		climbing = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[3], sprites[4]});

		sprites = new TextureRegion[8];
		sprites = TextureRegion.split(texture, 7, 13)[1];
		runRight = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7]});
	
		sprites = new TextureRegion[5];
		sprites = TextureRegion.split(texture, 18, 13)[2];
		primaryRight = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4]});
		
		sprites = new TextureRegion[5];
		sprites = TextureRegion.split(texture, 33, 13)[3];
		secondaryRight = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4]});
		
		sprites = new TextureRegion[9];
		sprites = TextureRegion.split(texture, 12, 13)[4];
		tertiaryRight = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7], sprites[8]});
		
		sprites = new TextureRegion[15];
		sprites = TextureRegion.split(texture, 40, 13)[5];
		quaternaryRight = new Animation<TextureRegion>(0.07f,
				new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7], sprites[8], sprites[9], sprites[10], sprites[11], sprites[12], sprites[13], sprites[14]});
		
		spriteBatch = new SpriteBatch();
		stateTime = 0f;			
		framesRun = 0;
		
		//Temporary loading of textures for crab animations
		Texture texturecrab = textures.getTexture("crab");
		TextureRegion[] spritescrab = new TextureRegion[4];
		
		spritescrab = TextureRegion.split(texturecrab, 36, 32)[0];
		standingRightCrab = new Animation<TextureRegion>(0.07f, spritescrab[0]);
		runRightCrab = new Animation<TextureRegion>(0.07f, new TextureRegion[]{spritescrab[0], spritescrab[1], spritescrab[2], spritescrab[3]});

		spritescrab = TextureRegion.split(texturecrab, 36, 32)[1];
		primaryRightCrab = new Animation<TextureRegion>(0.07f, new TextureRegion[]{spritescrab[0], spritescrab[1], spritescrab[2], spritescrab[3]});

		spritescrab = TextureRegion.split(texturecrab, 42, 32)[2];
		deathRightCrab = new Animation<TextureRegion>(0.07f, new TextureRegion[]{spritescrab[0], spritescrab[1], spritescrab[2], spritescrab[3]});
		
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
		
		//stateTime += Gdx.graphics.getDeltaTime();
		stateTime += delta;
		if(player.getState() > 3){
			
			animTime += delta;	
		}
		
		world.step(delta, 6, 2);
		
		//Remove crystals
		Array<Body> bodies = contactListener.getBodiesToRemove();
		for (Body body : bodies){
			
			player.collectCrystal();
			crystals.removeValue((Crystal) body.getUserData(), true);
			world.destroyBody(body);
		}
		bodies.clear();
				
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//movement update
		updateMovement();
		
		//monster movement update
		monsterMovement();

		cam.position.set(player.getPosition().x * PPM, player.getPosition().y * PPM, 0);
		cam.update();
			
		tmr.setView(cam);
		tmr.render();

		spriteBatch.setProjectionMatrix(cam.combined);

		//Draw player
		this.drawPlayer();
		
		/**
		if(Math.random() < 0.1){
			createMonster();
		}
		*/
		
		//Draw monsters
		this.drawMonsters();

		spriteBatch.begin();
		//Draw crystals
		for(int i = 0; i < crystals.size; i++){
			
			spriteBatch.draw(crystals.get(i).getAnim().getKeyFrame(stateTime, true), crystals.get(i).getBody().getPosition().x * PPM - 8, crystals.get(i).getBody().getPosition().y * PPM - 8);
		}
		spriteBatch.end();
		
		if(debug){

			b2dCam.position.set(player.getPosition().x, player.getPosition().y, 0);
			b2dCam.update();
			b2dr.render(world, b2dCam.combined);
		}
	}
	
	/**
	 * Update player movement.
	 */
	private void updateMovement(){
		
		if (player.getState() <= 3){
			
			if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
			
				if(contactListener.isPlayerOnGround()){		
			
					player.getBody().applyForceToCenter(0, 300, true);	
					player.setState(1);	
				}
			}
		
			if(Gdx.input.isKeyPressed(Keys.LEFT)){	
		
				player.setState(3);
				player.setFace(false);//CHANGE!!!
		
				if(player.getBody().getLinearVelocity().x > -2f){
			
					player.getBody().applyLinearImpulse(new Vector2(-1f, 0f), player.getPosition(), true);
				}
			}
		
			if(Gdx.input.isKeyPressed(Keys.RIGHT)){
						
				player.setState(2);
				player.setFace(true);
		
				if(player.getBody().getLinearVelocity().x < 2f){
			
					player.getBody().applyLinearImpulse(new Vector2(1f, 0f), player.getPosition(), true);

				}
			}
		
			if(!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT)){
			
				player.setState(0);
				player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x * 0.9f, player.getBody().getLinearVelocity().y);
	
			}
	
			if(contactListener.isPlayerOnGround() == false){
			
				player.setState(1);
				
			}
			
			if(Gdx.input.isKeyPressed(Keys.A)){
			
				player.setState(4);

			}
			
			if(Gdx.input.isKeyPressed(Keys.S)){
				
				player.setState(5);

			}
			
			if(Gdx.input.isKeyPressed(Keys.D)){
				
				player.setState(6);

			}
			
			if(Gdx.input.isKeyPressed(Keys.F)){
				
				player.setState(7);

			}
		}		
	}
	
	/**
	 * Update monster movement.
	 */
	private void monsterMovement(){
		
		float range;
		
		for(Monster m : monsterList){
			
			range = (float) Math.sqrt(Math.pow(m.getPosition().x - m.getPosition().x, 2) + Math.pow(m.getPosition().y - m.getPosition().y, 2));
			
			if(range <= DETECTION_RANGE){
				
				if (m.getState() <= 3){
					
					if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
					
						if(contactListener.isMonsterOnGround()){		
					
							m.getBody().applyForceToCenter(0, 300, true);	
							m.setState(1);	
						}
					}
				
					if(m.getPosition().x > m.getPosition().x){	
				
						m.setState(3);
						m.setFace(false);//CHANGE!!!
				
						if(m.getBody().getLinearVelocity().x > -2f){
					
							m.getBody().applyLinearImpulse(new Vector2(-1f, 0f), m.getPosition(), true);
						}
					}
				
					if(m.getPosition().x < m.getPosition().x){
								
						m.setState(2);
						m.setFace(true);
				
						if(m.getBody().getLinearVelocity().x < 2f){
					
							m.getBody().applyLinearImpulse(new Vector2(1f, 0f), m.getPosition(), true);

						}
					}
			
					if(contactListener.isMonsterOnGround() == false){
					
						m.setState(1);
						
					}
					
					if(range <= CRAB_RANGE){
						//attack
					}
				}
				
			}else{
				
				m.setState(0);
				m.getBody().setLinearVelocity(m.getBody().getLinearVelocity().x * 0.9f, m.getBody().getLinearVelocity().y);
			
			}
		}
		
	}
	
	private void drawPlayer(){
		
		spriteBatch.begin();
		
		switch(player.getState()){
		case 0: 		
			
			if(player.getFacing()){
				
				spriteBatch.draw(standingRight.getKeyFrame(stateTime, true), player.getBody().getPosition().x * 100 - PLAYER_WIDTH * SCALE/2, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT * SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, SCALE, SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingRight.getKeyFrame(stateTime, true), player.getBody().getPosition().x * 100 + PLAYER_WIDTH * SCALE/2, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT * SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, -SCALE, SCALE, 0);
			}
			break;
		case 1:
			
			if(player.getBody().getLinearVelocity().x >= 0){
			
				spriteBatch.draw(jumpRight.getKeyFrame(stateTime, false), player.getBody().getPosition().x * 100 - PLAYER_WIDTH * SCALE/2, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT * SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, SCALE, SCALE, 0);
			}
			else{
				
				spriteBatch.draw(jumpRight.getKeyFrame(stateTime, false), player.getBody().getPosition().x * 100 + PLAYER_WIDTH * SCALE/2, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT * SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, -SCALE, SCALE, 0);
			}
			break;
		case 2:
			
			spriteBatch.draw(runRight.getKeyFrame(stateTime, true), player.getBody().getPosition().x * 100 - PLAYER_WIDTH * SCALE/2, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT * SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, SCALE, SCALE, 0);
			break;
		case 3:
			spriteBatch.draw(runRight.getKeyFrame(stateTime, true), player.getBody().getPosition().x * 100 + PLAYER_WIDTH * SCALE/2, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT * SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, -SCALE, SCALE, 0);

			break;
		case 4:

			if (prevFrame != primaryRight.getKeyFrame(animTime, true)){
				
				framesRun++;
				prevFrame = primaryRight.getKeyFrame(animTime, true);
			}
		
			if (framesRun <= 5){

				spriteBatch.draw(primaryRight.getKeyFrame(animTime, true), player.getBody().getPosition().x * 100 - 10, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT - 5, 0, 0, 18, PLAYER_HEIGHT, SCALE, SCALE, 0);
			}
			else{
				
				player.setState(0);
				framesRun = 0;
				animTime = 0;
			}
			break;
		case 5:
			
			if (prevFrame != secondaryRight.getKeyFrame(animTime, true)){
				
				framesRun++;
				prevFrame = secondaryRight.getKeyFrame(animTime, true);
			}

			if (framesRun <= 5){
				
				spriteBatch.draw(secondaryRight.getKeyFrame(animTime, true), player.getBody().getPosition().x * 100 - 10, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT - 5, 0, 0, 33, PLAYER_HEIGHT, SCALE, SCALE, 0);
			}
			else{
				
				player.setState(0);
				framesRun = 0;
				animTime = 0;
			}
			
			break;
		case 6:
					
			if (prevFrame != tertiaryRight.getKeyFrame(animTime, true)){
				
				framesRun++;
				prevFrame = tertiaryRight.getKeyFrame(animTime, true);
			}

			if (framesRun <=  9){
			
				spriteBatch.draw(tertiaryRight.getKeyFrame(animTime, true), player.getBody().getPosition().x * 100 - 12, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT - 5, 0, 0, 12, PLAYER_HEIGHT, SCALE, SCALE, 0);
			}
			else{
				
				player.setState(0);
				framesRun = 0;
				animTime = 0;
			}
			break;
		case 7:

			
			if (prevFrame != quaternaryRight.getKeyFrame(animTime, true)){
				
				framesRun++;
				prevFrame = quaternaryRight.getKeyFrame(animTime, true);
			}

			if (framesRun <= 15){
				
				spriteBatch.draw(quaternaryRight.getKeyFrame(animTime, true), player.getBody().getPosition().x * 100 - 40, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT - 5, 0, 0, 40, PLAYER_HEIGHT, SCALE, SCALE, 0);
			}
			else{
				
				player.setState(0);
				framesRun = 0;
				animTime = 0;
			}
			break;
		}
		
		spriteBatch.end();
	}
	
	/**
	 * Draw monsters.
	 */
	private void drawMonsters(){
		
		spriteBatch.begin();
		
		for(Monster m : monsterList){
			switch(m.getState()){
			case 0: 		
				
				if(m.getFacing()){
					
					spriteBatch.draw(standingRightCrab.getKeyFrame(stateTime, true), m.getBody().getPosition().x * 100 - m.width * SCALE/2, m.getBody().getPosition().y * 100 - m.height * SCALE/2, 0, 0, m.width, m.height, SCALE, SCALE, 0);
				}
				else{
					
					spriteBatch.draw(standingRightCrab.getKeyFrame(stateTime, true), m.getBody().getPosition().x * 100 + m.width * SCALE/2, m.getBody().getPosition().y * 100 - m.height * SCALE/2, 0, 0, m.width, m.height, -SCALE, SCALE, 0);
				}
				break;
			case 1:
				
				if(m.getBody().getLinearVelocity().x >= 0){
				
					spriteBatch.draw(standingRightCrab.getKeyFrame(stateTime, false), m.getBody().getPosition().x * 100 - m.width * SCALE/2, m.getBody().getPosition().y * 100 - m.height * SCALE/2, 0, 0, m.width, m.height, SCALE, SCALE, 0);
				}
				else{
					
					spriteBatch.draw(standingRightCrab.getKeyFrame(stateTime, false), m.getBody().getPosition().x * 100 + m.width * SCALE/2, m.getBody().getPosition().y * 100 - m.height * SCALE/2, 0, 0, m.width, m.height, -SCALE, SCALE, 0);
				}
				break;
			case 2:
				
				spriteBatch.draw(runRightCrab.getKeyFrame(stateTime, true), m.getBody().getPosition().x * 100 - m.width * SCALE/2, m.getBody().getPosition().y * 100 - m.height * SCALE/2, 0, 0, m.width, m.height, SCALE, SCALE, 0);
				break;
			case 3:
				spriteBatch.draw(runRightCrab.getKeyFrame(stateTime, true), m.getBody().getPosition().x * 100 + m.width * SCALE/2, m.getBody().getPosition().y * 100 - m.height * SCALE/2, 0, 0, m.width, m.height, -SCALE, SCALE, 0);

				break;
			case 4:

				if (prevFrame != primaryRightCrab.getKeyFrame(stateTime, true)){
					
					framesRun++;
					prevFrame = primaryRightCrab.getKeyFrame(stateTime, true);
				}
			
				if (framesRun <= 5){
					
					spriteBatch.draw(primaryRight.getKeyFrame(stateTime, true), m.getBody().getPosition().x * 100 - 10, m.getBody().getPosition().y * 100 - m.height - 5, 0, 0, 18, m.height, SCALE, SCALE, 0);
				}
				else{
					
					m.setState(0);
					framesRun = 0;
				}
				break;
			}
		}
		
		spriteBatch.end();
	}
	
	/**
	 * Creates the player.
	 */
	private void createPlayer(){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		//Create Player
		bdef.position.set(90 / PPM, 100 / PPM);
		bdef.type = BodyType.DynamicBody;
		//bdef.linearVelocity.set(1f, 0);
		Body body = world.createBody(bdef);
		
		shape.setAsBox(
				((PLAYER_WIDTH * SCALE) / 2) / PPM, 
				((PLAYER_HEIGHT * SCALE) / 2) / PPM);
	//	shape.setAs
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_PLAYER;
		fdef.filter.maskBits = BIT_RED | BIT_CRYSTAL;
		body.createFixture(fdef).setUserData("player");
		
		
		//Create Player
		player = new Player(body);
		player.setState(1);
		
		//Create foot sensor
		shape.setAsBox(
				(((PLAYER_WIDTH - 2) / 2) * SCALE) / PPM, 
				(((PLAYER_HEIGHT / 7) / 2) * SCALE) / PPM, 
				new Vector2(0, -(PLAYER_HEIGHT / 2 * SCALE) / PPM),
				0);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_PLAYER;
		fdef.filter.maskBits = BIT_RED;	
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");
	}
	
	/**
	 * Creates monsters.
	 */
	private void createMonster(){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		//get crystal spawn point
		Vector2 position = crystals.random().getPosition();
		
		//Create Player
		//bdef.position.set(position);
		bdef.position.set(player.getPosition());
		bdef.type = BodyType.DynamicBody;
		
		bdef.linearVelocity.set(1f, 0);
		
		Body body = world.createBody(bdef);
		
		shape.setAsBox(
				((CRAB_WIDTH * SCALE) / 2) / PPM, 
				((CRAB_HEIGHT * SCALE) / 2) / PPM);
	//	shape.setAs
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_MONSTER;
		fdef.filter.maskBits = BIT_RED;
		body.createFixture(fdef).setUserData("monster");
		
		
		//Create Player
		monsterList.add(new Monster(body));
		monsterList.peek().setState(1);
		
		//Create foot sensor
		shape.setAsBox(
				(((CRAB_WIDTH - 2) / 2) * SCALE) / PPM, 
				(((CRAB_HEIGHT / 7) / 2) * SCALE) / PPM, 
				new Vector2(0, -(CRAB_HEIGHT / 2 * SCALE) / PPM),
				0);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_MONSTER;
		fdef.filter.maskBits = BIT_RED;	
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("mfoot");
	}
	
	/**
	 * Creates the tiles.
	 */
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
	
	/**
	 * Creates the layer.
	 *
	 * @param layer the layer
	 * @param bits the bits
	 */
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
				 fdef.filter.maskBits = BIT_PLAYER | BIT_MONSTER;
				 
				 world.createBody(bdef).createFixture(fdef);
			 }
		 }
	}
	
	/**
	 * Creates the crystals.
	 */
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

/**Group index filtering - look it up later*/

