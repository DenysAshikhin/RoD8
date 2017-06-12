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
	Player player;

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
	MyContactListener contactListener;
	
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
		player.updateMovement();
		
		//monster movement update
		for(Monster m : monsterList){
			monsterMovement();
		}

		cam.position.set(player.getPosition().x * PPM, player.getPosition().y * PPM, 0);
		cam.update();
			
		tmr.setView(cam);
		tmr.render();

		spriteBatch.setProjectionMatrix(cam.combined);

		//Draw player
		player.drawPlayer();
		
		/**
		if(Math.random() < 0.1){
			createMonster();
		}
		*/
		
		//Draw monsters
		for(Monster m : monsterList){
			m.drawMonsters(spriteBatch);
		}

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
		//shape.setAs
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

