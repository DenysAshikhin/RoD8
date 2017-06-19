import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
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
	public World world;
	
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
	public static Player player;

	/** The monsters. */
	public static Array<Monster> monsterList = new Array<Monster>();

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
	
	public static final float CRAB_WIDTH = 40f;
	
	public static final float CRAB_HEIGHT = 40f;
	
	public static final float LEMURIAN_WIDTH = 18f;
	
	public static final float LEMURIAN_HEIGHT = 18f;
	
	/** The Constant SCALE. */
	public static final float SCALE = 1f;
	
	/** The Constant BIT_PLAYER. */
	//Filter Bits
	public static final short BIT_PLAYER = 2;
	
	/** The Constant BIT_RED. */
	public static final short BIT_GROUND = 4;
	
	/** The Constant BIT_GREEN. */
	public static final short BIT_AIR = 8;
	
	/** The Constant BIT_BLUE. */
	public static final short BIT_LADDER = 16;
	
	/** The Constant BIT_CRYSTAL. */
	public static final short BIT_CHEST = 32;
	
	/** The Constant BIT_MONSTER. */
	public static final short BIT_MONSTER = 64;
	
	public static final short BIT_BULLET = 128;
	
	public static final short BIT_MONSTER_SENSOR = 256;
	
	public static final short BIT_CRAB_ATTACK = 512;

	/** The contact listener. */

	public MyContactListener contactListener;
	
	private float framesRun;
	private float animTime;
	private TextureRegion prevFrame = null;
	private int monsterNum;
	public static Array<Monster> removeMobs = new Array<Monster>();
	
	public static HashSet<Chest> chests;
		
	private Texture blank;
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
		textures.loadTexture("Monster 2 Final.png", "lemurian");
		textures.loadTexture("whitepixel.png", "blank");
		textures.loadTexture("chestandteleporter.png", "portal");
		
	               	          
		crab = textures.getTexture("crab");
		crystal = textures.getTexture("crystal");
		blank = textures.getTexture("blank");

		monsterNum = 0;
		monsterList.ordered = false;
		
		chests = new HashSet<Chest>();

		
		//Create player, tiles and crystals
		createPlayer();
		createTiles();
		createCrystals();
		createChests();
		
		scoreFont = new BitmapFont();
		scoreFont.getData().setScale(0.5f);
		
		spriteBatch = new SpriteBatch();
		stateTime = 0f;
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
			
			player.increaseAnimTime(delta);	
		}
		
		world.step(delta, 6, 2);
		
		//Remove crystals
		//Array<Body> bodies = contactListener.getBodiesToRemove();
		HashSet<Body> bodies = contactListener.getBodyToRemove();
		
		for (Body body : bodies){
		
				world.destroyBody(body);
		}
		bodies.clear();

		for(Monster j : removeMobs){
			
			monsterList.removeValue(j, true);
		}
		removeMobs.clear();
		
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//movement update
		player.updateMovement();
		
		cam.position.set(player.getPosition().x * PPM, player.getPosition().y * PPM, 0);
		cam.update();
		spriteBatch.setProjectionMatrix(cam.combined);
	
		tmr.setView(cam);
		tmr.render();
		
		spriteBatch.begin();

		for(Monster m : monsterList){
			
			if(m.getState() > 3){
				m.increaseAnimTime(delta);
			}
			m.monsterMovement();
			m.drawMonsters(spriteBatch, stateTime);
			
			spriteBatch.setColor(Color.GREEN);
			spriteBatch.draw(blank, m.getBody().getPosition().x * PPM - 12, m.getBody().getPosition().y * PPM + 20, 24 * m.health, 3);
			spriteBatch.setColor(Color.WHITE);
		}

		//Draw player
		player.drawPlayer(spriteBatch, stateTime);

		//Draw crystals
		for(int i = 0; i < crystals.size; i++){
			
			spriteBatch.draw(crystals.get(i).getAnim().getKeyFrame(stateTime, true), crystals.get(i).getBody().getPosition().x * PPM - 8, crystals.get(i).getBody().getPosition().y * PPM - 8);
		}
	
		spriteBatch.end();		
	
		for(Chest chest : chests){
			
			chest.drawChest(spriteBatch);
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.L)){
			
			createMonster();
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.E)){
			
			if(player.money >= 500){
				
				for(Chest chest : chests){
					
					if (chest.isTouched && !chest.isOpen){
						
						chest.isOpen = true;
						player.money -= 500;
					}
				}
			}
		}

		Matrix4 uiMatrix = cam.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, 500, 500);
		spriteBatch.setProjectionMatrix(uiMatrix);
		spriteBatch.begin();
		//hudBatch.begin();
		
		GlyphLayout guiLayout = new GlyphLayout(scoreFont, "Gold: " + player.money);

		scoreFont.draw(spriteBatch, guiLayout, 5, 490);
		
		spriteBatch.setColor(Color.BLACK);
		spriteBatch.draw(blank, 100, 50, 300, 10);
		
		spriteBatch.setColor(Color.GREEN);
		spriteBatch.draw(blank, 100, 50, 300 * player.health, 10);
		spriteBatch.setColor(Color.WHITE);

		guiLayout = new GlyphLayout(scoreFont, "Health: " + player.health * 100 + "%");
		spriteBatch.end();
		//hudBatch.end();
		/**
		if(Math.random() < 0.01){
			createMonster();
		}
		*/
		if(debug){

			b2dCam.position.set(player.getPosition().x, player.getPosition().y, 0);
			b2dCam.update();
			b2dr.render(world, b2dCam.combined);
		}
	}
	
	/**
	 * Creates the player.
	 */
	private void createPlayer(){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		//Create Player
		bdef.position.set(12, 24);
		bdef.type = BodyType.DynamicBody;
		//bdef.linearVelocity.set(1f, 0);
		Body body = world.createBody(bdef);
		
		shape.setAsBox(
				((Player.PLAYER_WIDTH * SCALE) / 2) / PPM, 
				((Player.PLAYER_HEIGHT * SCALE) / 2) / PPM);
	//	shape.setAs
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_PLAYER;
		fdef.filter.maskBits = BIT_GROUND | BIT_CHEST | BIT_BULLET | BIT_CRAB_ATTACK | BIT_LADDER;
		body.createFixture(fdef).setUserData("player");
		
		//Create Player
		player = new Player(body, this, 1);

		player.setState(1);
		
		//Create foot sensor
		shape.setAsBox(
				(((Player.PLAYER_WIDTH - 2) / 2) * SCALE) / PPM, 
				(((Player.PLAYER_HEIGHT / 7) / 2) * SCALE) / PPM, 
				new Vector2(0, -(Player.PLAYER_HEIGHT / 2 * SCALE) / PPM),
				0);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_PLAYER;
		fdef.filter.maskBits = BIT_GROUND | BIT_LADDER;	
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");
	}
	
	public void createBullet(String identifier, boolean value){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		//Create Player
		//x set at X 100
		//y set at X 100
		bdef.position.set((player.getBody().getPosition().x * 100) / PPM, (player.getBody().getPosition().y * 100) / PPM);
		bdef.type = BodyType.DynamicBody;
		
		if(value){
			
			bdef.linearVelocity.set(5f, 0);
		}
		else{
			
			bdef.linearVelocity.set(-5f, 0);
		}
		bdef.bullet = true;
		Body body = world.createBody(bdef);
		body.setGravityScale(0);
		shape.setAsBox(1 / PPM, 1 / PPM);
	//	shape.setAs
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_BULLET;
		fdef.filter.maskBits = BIT_GROUND | BIT_MONSTER;
		if(identifier.contains("ray")){
			fdef.isSensor = true; 
		}
		body.createFixture(fdef).setUserData(identifier);
	}
	
	
	/**
	 * Creates monsters.
	 */
	private void createMonster(){
		
		int monsterType = (int) (Math.random() * 2) + 1;
		float width = 0;
		float height = 0;
		switch(monsterType){
		case 1:
			width = CRAB_WIDTH;
			height = CRAB_HEIGHT;
			break;
		case 2:
			width = LEMURIAN_WIDTH;
			height = LEMURIAN_HEIGHT;
			break;
		}
		
		BodyDef b1def = new BodyDef();
		FixtureDef f1def = new FixtureDef();
		PolygonShape shape1 = new PolygonShape();
		
		//get crystal spawn point
		Vector2 position = crystals.random().getPosition();
		
		//Create Monster
		b1def.position.set(position);
		b1def.type = BodyType.DynamicBody;
		
		Body body1 = world.createBody(b1def);
		
		shape1.setAsBox(
				((width * SCALE) / 2) / PPM, 
				((height * SCALE) / 2) / PPM);
		//shape.setAs
		f1def.shape = shape1;
		f1def.filter.categoryBits = BIT_MONSTER;
		f1def.filter.maskBits = BIT_GROUND | BIT_BULLET;

		body1.createFixture(f1def).setUserData("monster:" + monsterNum);

		//Create Monster
		monsterList.add(new Monster(body1, this, monsterNum, monsterType));
		//monsterList.add(new Monster(body1, this, monsterNum, 2));
		monsterList.peek().setState(1);
		
		//Create foot sensor
		
		shape1.setAsBox(
				(((width - 2) / 2) * SCALE) / PPM, 
				(((height / 7) / 2) * SCALE) / PPM, 
				new Vector2(0, -(height / 2 * SCALE) / PPM),
				0);
		f1def.shape = shape1;
		f1def.filter.categoryBits = BIT_MONSTER;
		f1def.filter.maskBits = BIT_GROUND;	
		f1def.isSensor = true;
		body1.createFixture(f1def).setUserData("mfoot");
		
		//Create jump sensor
		shape1.setAsBox(
				(((width + 5) / 2) * SCALE) / PPM, 
				(((height / 7) / 2) * SCALE) / PPM,
				new Vector2(0, (float) ((((-height / 2)) * SCALE / PPM) + ((monsterList.peek().jumpHeight) - 0.05))),
				0);
		f1def.shape = shape1;
		f1def.filter.categoryBits = BIT_MONSTER;
		f1def.filter.maskBits = BIT_GROUND;	
		f1def.isSensor = true;
		body1.createFixture(f1def).setUserData("mjump");
		
		//Create wall sensor
		shape1.setAsBox(
				(((width + 2) / 2) * SCALE) / PPM, 
				(((height - 2) / 2) * SCALE) / PPM);
		f1def.shape = shape1;
		f1def.filter.categoryBits = BIT_MONSTER;
		f1def.filter.maskBits = BIT_GROUND;	
		f1def.isSensor = true;
		body1.createFixture(f1def).setUserData("mwall");
		
		monsterNum ++;
	}
	

	public void createLocalAttack(Monster m, float damage, boolean value){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.position.set((m.getBody().getPosition().x * 100) / PPM, (m.getBody().getPosition().y * 100) / PPM);
		bdef.type = BodyType.StaticBody;
		
		if(value){

			shape.setAsBox((m.width / 2) / PPM, m.height / PPM, new Vector2((m.width / 4) * SCALE / PPM, 0), 0);
		}
		else{

			shape.setAsBox((m.width / 2) / PPM, m.height / PPM, new Vector2(-(m.width / 4) * SCALE / PPM, 0), 0);
		}
		Body body = m.getBody();
		body.setGravityScale(0);
	//	shape.setAs
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_CRAB_ATTACK;
		fdef.filter.maskBits = BIT_PLAYER;
		body.createFixture(fdef).setUserData("attack:" + damage);
	}
	
	/**
	 * Creates the tiles.
	 */
	private void createTiles(){
		
		 tileMap = new TmxMapLoader().load("first_stage_map.tmx");
		 tmr = new OrthogonalTiledMapRenderer(tileMap);

		 tileSize = (int) tileMap.getProperties().get("tilewidth");
		 
		 TiledMapTileLayer layer;
		 layer = (TiledMapTileLayer) tileMap.getLayers().get("air");
		 createLayer(layer, BIT_AIR, "air");
		 layer = (TiledMapTileLayer) tileMap.getLayers().get("ground");
		 createLayer(layer, BIT_GROUND, "ground");
		 layer = (TiledMapTileLayer) tileMap.getLayers().get("ladder");
		 createLayer(layer, BIT_LADDER, "ladder");
	}
	

	/**
	 * Creates the layer.
	 *
	 * @param layer the layer
	 * @param bits the bits
	 */
	private void createLayer(TiledMapTileLayer layer, short bits, String userData){
		
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
				
				 if(bits != BIT_AIR){
					 ChainShape chainShape = new ChainShape();
					 
					 //if(bits == BIT_GROUND){
						 
						 Vector2[] vertices = new Vector2[4];
						 vertices[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);//Bottom left corner
						 vertices[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
						 vertices[2] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);//Upper right corner
						 vertices[3] = new Vector2(tileSize / 2 / PPM, -tileSize / 2 / PPM);
						 chainShape.createChain(vertices);
					 //}
					 /*else{
					
						 Vector2[] vertices = new Vector2[6];
						 vertices[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);//Bottom left corner
						 vertices[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
						 vertices[2] = new Vector2(-tileSize / 16 / PPM, -tileSize / 2 / PPM);

						 vertices[3] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);//Upper right corner
						 vertices[4] = new Vector2(tileSize / 2 / PPM, -tileSize / 2 / PPM);
						 vertices[5] = new Vector2(tileSize / 20 / PPM, -tileSize / 1 / PPM);
						 chainShape.createChain(vertices);
					 }*/
					
					 if(bits == BIT_LADDER)
						 fdef.isSensor = true;
					 else
						 fdef.isSensor = false;
					 
					 fdef.friction = 0;
					 fdef.filter.categoryBits = bits;
					 fdef.shape = chainShape;
					 switch(bits){
					 case BIT_AIR:
						 break;
					 case BIT_LADDER:
						 fdef.filter.maskBits = BIT_PLAYER;
						 break;
					 case BIT_GROUND:
						 fdef.filter.maskBits = BIT_PLAYER | BIT_MONSTER | BIT_BULLET;
						 break;
					 }
				 
					 world.createBody(bdef).createFixture(fdef).setUserData(userData);
				 }
				 

				 /**Have the ground message be a variable passed in depending on what tile is being imported*/
			 }
		 }
	}
	
	private void createChests(){
				
		MapLayer layer = tileMap.getLayers().get("chest");
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		for (MapObject mapObject : layer.getObjects()){
			
			bdef.type = BodyType.StaticBody;
			float x = mapObject.getProperties().get("x", Float.class)/ PPM;
			float y = mapObject.getProperties().get("y", Float.class)/ PPM;

			bdef.position.set(x,y);
			PolygonShape squareShape = new PolygonShape();
			squareShape.setAsBox(15 / PPM, 10 / PPM);
			
			fdef.shape = squareShape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = BIT_CHEST;
			fdef.filter.maskBits = BIT_PLAYER;
			
			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("chest");
			
			Chest c = new Chest(body, this);
			chests.add(c);

			body.setUserData(c);
		}
	}
	
	/**
	 * Creates the crystals.
	 */
	private void createCrystals(){
		
		crystals = new Array<Crystal>();
		
		MapLayer layer = tileMap.getLayers().get("spawner");
		
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
			fdef.filter.categoryBits = BIT_CHEST;
			fdef.filter.maskBits = BIT_PLAYER;
			
			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("crystal");
			
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

