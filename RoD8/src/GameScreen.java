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

	private boolean debug = true;
		
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private OrthographicCamera b2dCam;
	private MyContactListener cl;
	
	private TiledMap tileMap;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;
	
	private Player player;
	private Array<Crystal> crystals;
	
	private HUD hud;
	
	float stateTime;

	private float stillTime;
	
	private OrthographicCamera cam;
	//private OrthographicCamera hudCam;

	BitmapFont scoreFont;
	

	CollisionRect playerRect;
	Texture healthTexture;
	SpriteBatch spriteBatch;
	
	public static final float PPM = 100;//Conversion of 100 pixels = 1 metre
	public static Content textures;
	
	public static final float PLAYER_WIDTH = 8f;
	public static final float PLAYER_HEIGHT = 20f;
	public static final float SCALE = 3f;
	
	//Filter Bits
	public static final short BIT_PLAYER = 2;
	public static final short BIT_RED = 4;
	public static final short BIT_GREEN = 8;
	public static final short BIT_BLUE = 16;
	public static final short BIT_CRYSTAL = 32;
	
	private MyContactListener contactListener;	

	Animation<TextureRegion> runLeft;
	Animation<TextureRegion> runRight;
	Animation<TextureRegion> jumpDefault;
	Animation<TextureRegion> jumpLeft;
	Animation<TextureRegion> standingLeft;
	Animation<TextureRegion> standingRight;
		
	SpaceGame game;
		
	
	public GameScreen(SpaceGame game){
		
		this.game = game;
		
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
		textures.loadTexture("commando_good.png", "commando");
		textures.loadTexture("bunny.png", "bunny");
		textures.loadTexture("crystal.png", "crystal");
		textures.loadTexture("hud.png", "hud");
		
		//Create palyer, tiles and crystals
		createPlayer();
		createTiles();
		createCrystals();
		
		
		//Temperoray loading of textures for commande animations
		Texture texture = GameScreen.textures.getTexture("commando");
		TextureRegion[][] sprites = new TextureRegion[3][8];
		
		sprites[0] = TextureRegion.split(texture, 7, 12)[0];
		sprites[1] = TextureRegion.split(texture, 7, 12)[1];
		sprites[2] = TextureRegion.split(texture, 7, 12)[2];
		
		TextureRegion[] run = new TextureRegion[8];
		
		for(int i = 1; i < sprites.length; i++){
			
			for(int j = 0; j < sprites[i].length; j++){
				
				run[j] = sprites[i][j];
			}
			
			switch(i){
		
			case 1:
				
				runRight = new Animation<TextureRegion>(0.07f, run);
				break;
			case 2:

				runLeft = new Animation<TextureRegion>(0.07f, run);
				break;
			}
			
			run = new TextureRegion[8];
		}
		
		standingRight = new Animation<TextureRegion>(0.07f, sprites[0][0]); 
		standingLeft = new Animation<TextureRegion>(0.07f, sprites[0][1]);
		jumpDefault = new Animation<TextureRegion>(0.07f, sprites[0][2]);
		jumpLeft = new Animation<TextureRegion>(0.07f, sprites[0][3]);
		spriteBatch = new SpriteBatch();
		stateTime = 0f;
	}
	
	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		/**Get rid of this to return to default tutorial game*/
				
		stateTime += Gdx.graphics.getDeltaTime();
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

		
		
		cam.position.set(player.getPosition().x * PPM, player.getPosition().y * PPM, 0);
		cam.update();

			
		tmr.setView(cam);
		tmr.render();

		spriteBatch.setProjectionMatrix(cam.combined);

		//Draw player
		this.drawPlayer(cam, spriteBatch);

		//Draw crystals
		for(int i = 0; i < crystals.size; i++){
			
			crystals.get(i).update(delta);
			crystals.get(i).render(spriteBatch);
		}

		
		if(debug){

			b2dCam.position.set(player.getPosition().x, player.getPosition().y, 0);
			b2dCam.update();
			b2dr.render(world, b2dCam.combined);
		}
	}
	
	private void updateMovement(){
		
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
			if(contactListener.isPlayerOnGround()){
				
				player.getBody().applyForceToCenter(0, 300, true);
				player.setState(1);
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.LEFT)){
				
			//spriteBatch.draw(run[1].getKeyFrame(stateTime, true), x, y, SHIP_WIDTH, SHIP_HEIGHT);
			player.setState(3);
			player.setFace(true);//CHANGE!!!
			if(player.getBody().getLinearVelocity().x > -2f){
				player.getBody().applyLinearImpulse(new Vector2(-1f, 0f), player.getPosition(), true);
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
						
			player.setState(2);
			player.setFace(false);
			if(player.getBody().getLinearVelocity().x < 2f){
					player.getBody().applyLinearImpulse(new Vector2(1f, 0f), player.getPosition(), true);
				}

		}
		
		if(!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT)){
			
			stillTime += Gdx.graphics.getDeltaTime();
			player.setState(0);
			player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x * 0.9f, player.getBody().getLinearVelocity().y);
		}

		if(!contactListener.isPlayerOnGround()){
			
			player.setState(1);
		}
	}
	
	private void drawPlayer(OrthographicCamera cam, SpriteBatch spriteBatch){
		
		spriteBatch.begin();
		
		switch(player.getState()){
		case 0: 		
			
			if(player.getFacing()){
				
				spriteBatch.draw(standingLeft.getKeyFrame(stateTime, false), player.getBody().getPosition().x * 100 - PLAYER_WIDTH * SCALE/2, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT * SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, SCALE, SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingRight.getKeyFrame(stateTime, false), player.getBody().getPosition().x * 100 - PLAYER_WIDTH * SCALE/2, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT * SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, SCALE, SCALE, 0);
			}
			break;
		case 1:
			
			if(player.getBody().getLinearVelocity().x >= 0){
			
				spriteBatch.draw(jumpDefault.getKeyFrame(stateTime, false), player.getBody().getPosition().x * 100 - PLAYER_WIDTH * SCALE/2, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT * SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, SCALE, SCALE, 0);
			}
			else{
				
				spriteBatch.draw(jumpLeft.getKeyFrame(stateTime, false), player.getBody().getPosition().x * 100 - PLAYER_WIDTH * SCALE/2, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT * SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, SCALE, SCALE, 0);
			}
			break;
		case 2:
			
			spriteBatch.draw(runRight.getKeyFrame(stateTime, true), player.getBody().getPosition().x * 100 - PLAYER_WIDTH * SCALE/2, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT * SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, SCALE, SCALE, 0);
			break;
		case 3:
			spriteBatch.draw(runLeft.getKeyFrame(stateTime, true), player.getBody().getPosition().x * 100 - PLAYER_WIDTH * SCALE/2, player.getBody().getPosition().y * 100 - PLAYER_HEIGHT * SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, SCALE, SCALE, 0);

			break;
		}
		
		spriteBatch.end();
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

