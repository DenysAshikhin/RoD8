import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * The Class Player.
 */
public class Player extends B2DSprite{

	/** The num crystals. */
	private int numCrystals;
	
	/** The total crystals. */
	private int totalCrystals;
	
	private float animTime;
	private int framesRun;
	
	/** The Constant PLAYER_WIDTH. */
	public static final float PLAYER_WIDTH = 8f;
	
	/** The Constant PLAYER_HEIGHT. */
	public static final float PLAYER_HEIGHT = 20f;
	
	Animation<TextureRegion> runRight;
	Animation<TextureRegion> jumpRight;
	Animation<TextureRegion> standingRight;
	Animation<TextureRegion> climbing;
	Animation<TextureRegion> primaryRight;
	Animation<TextureRegion> secondaryRight;
	Animation<TextureRegion> tertiaryRight;
	Animation<TextureRegion> quaternaryRight;
	
	public float health = 100f;
	
	private int type;
	private TextureRegion prevFrame = null;
	public int money;
	private GameScreen gameScreen;
	
	/**
	 * Instantiates a new player.
	 *
	 * @param body the body
	 */
	public Player(Body body, GameScreen gameScreen, int type){//Add a type int later to determing which animations will be loaded in
		
		super(body);
		
		this.gameScreen = gameScreen;
		
		this.type = type;
		
		money = 0;
		
		switch(this.type){
			
		case 1:
			this.createCommando();
			break;
		case 2:
			this.createSniper();
			break;
		}
		
		framesRun = 0;
	}
	
	private void createCommando(){
		
		Texture texture = GameScreen.textures.getTexture("commando");
		TextureRegion[] sprites = new TextureRegion[4];
		
		sprites = TextureRegion.split(texture, 7, 13)[0];
		standingRight = new Animation<TextureRegion>(0.07f, sprites[0]);
		jumpRight = new Animation<TextureRegion>(0.07f, sprites[1]);

		sprites = new TextureRegion[2];
		sprites = TextureRegion.split(texture, 7, 13)[0];
		climbing = new Animation<TextureRegion>(0.25f, new TextureRegion[]{sprites[2], sprites[3]});

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
		
	}
	
	private void createSniper(){
		
		Texture texture = GameScreen.textures.getTexture("sniper");
		TextureRegion[] sprites = new TextureRegion[4];
		
		sprites = TextureRegion.split(texture, 7, 13)[0];
		standingRight = new Animation<TextureRegion>(0.07f, sprites[0]);
		jumpRight = new Animation<TextureRegion>(0.07f, sprites[1]);

		
		sprites = new TextureRegion[2];
		sprites = TextureRegion.split(texture, 7, 13)[0];
		climbing = new Animation<TextureRegion>(0.5f, new TextureRegion[]{sprites[2], sprites[3]});

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
		
	}
	/**Framesrun needs to be changed depending on the character/class*/
	public void drawPlayer(SpriteBatch spriteBatch, float stateTime){
			
		//spriteBatch.begin();
		
		switch(this.getState()){
		
		case 0:
			
			spriteBatch.draw(climbing.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 3) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			break;
		case 1: 		
			
			if(this.getFacing()){
				
				spriteBatch.draw(standingRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 3) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 3) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 2:
			
			if(this.getBody().getLinearVelocity().x >= 0){
			
				spriteBatch.draw(jumpRight.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(jumpRight.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 3:
			
			if(this.getFacing())
				spriteBatch.draw(runRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			else
				spriteBatch.draw(runRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);

			break;
		case 4:

			if (prevFrame != primaryRight.getKeyFrame(animTime, true)){
				
				if(framesRun == 0 || framesRun == 3){
					
					gameScreen.createBullet("bullet:1.00", this.getFacing());
				}
				framesRun++;
				prevFrame = primaryRight.getKeyFrame(animTime, true);
			}
	
			if (framesRun <= 5){

				if(this.getFacing())
					spriteBatch.draw(primaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - (PLAYER_WIDTH/2 * GameScreen.SCALE), this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 18, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(primaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + (PLAYER_WIDTH/2 * GameScreen.SCALE), this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 18, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			break;
		case 5:
			
			if (prevFrame != secondaryRight.getKeyFrame(animTime, true)){
				
				if(framesRun == 1){
					
					gameScreen.createBullet("ray:1.00", this.getFacing());
				}
				framesRun++;
				prevFrame = secondaryRight.getKeyFrame(animTime, true);
			}

			if (framesRun <= 5){
				
				if(this.getFacing())
					spriteBatch.draw(secondaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 33, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(secondaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 33, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);				
			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			
			break;
		case 6:
					
			if (prevFrame != tertiaryRight.getKeyFrame(animTime, true)){
				
				framesRun++;
				prevFrame = tertiaryRight.getKeyFrame(animTime, true);
			}

			if (framesRun <=  9){
			
				if(this.getFacing())
					spriteBatch.draw(tertiaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - GameScreen.SCALE * PLAYER_WIDTH/1.5f, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 12, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(tertiaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + GameScreen.SCALE * PLAYER_WIDTH/1.5f, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 12, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);

			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			break;
		case 7:
			
			if (prevFrame != quaternaryRight.getKeyFrame(animTime, true)){
				
				if(framesRun == 1 || framesRun == 5 || framesRun == 9){
					
					gameScreen.createBullet("bullet:1.00", this.getFacing());
				}
				else if(framesRun == 3 || framesRun ==7 || framesRun == 11){
					
					gameScreen.createBullet("bullet:1.00", !this.getFacing());
				}
				framesRun++;
				prevFrame = quaternaryRight.getKeyFrame(animTime, true);
			}

			if (framesRun <= 15){
				
				if(this.getFacing())
					spriteBatch.draw(quaternaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH * 2.2f * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 40, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(quaternaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH * 2.2f * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 40, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			break;
		}	
		
		//spriteBatch.end();
	}
	
	/**
	 * Update player movement.
	 */
	public void updateMovement(){
		
		if (this.getState() <= 3){
			
			if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
			
				if(gameScreen.contactListener.isPlayerOnGround()){		
			
					this.getBody().applyForceToCenter(0, 300, true);	
					this.setState(2);	
				}
			}
		
			if (Gdx.input.isKeyPressed(Keys.UP) && gameScreen.contactListener.isPlayerOnLadder()){
				
				this.setState(0);
				this.getBody().setLinearVelocity(new Vector2(0f, 1.5f));
			}
			
			if(Gdx.input.isKeyPressed(Keys.LEFT)){	
		
				this.setState(3);
				this.setFace(false);//CHANGE!!!
		
				if(this.getBody().getLinearVelocity().x > -2f)
					this.getBody().applyLinearImpulse(new Vector2(-1f, 0f), this.getPosition(), true);
			}
		
			if(Gdx.input.isKeyPressed(Keys.RIGHT)){
						
				this.setState(3);
				this.setFace(true);
		
				if(this.getBody().getLinearVelocity().x < 2f)			
					this.getBody().applyLinearImpulse(new Vector2(1f, 0f), this.getPosition(), true);
			}
		
			if(!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT) && gameScreen.contactListener.isPlayerOnGround() == true){
			
				this.setState(1);
			}
	
			if(gameScreen.contactListener.isPlayerOnGround() == false && gameScreen.contactListener.isPlayerOnLadder() == false){
			
				this.setState(2);
			}
			
			if(Gdx.input.isKeyPressed(Keys.A)){
			
				this.setState(4);
			}
			
			if(Gdx.input.isKeyPressed(Keys.S)){
				
				this.setState(5);
			}
			
			if(Gdx.input.isKeyPressed(Keys.D)){
				
				this.setState(6);
				
				if(this.getFacing() && gameScreen.contactListener.isPlayerOnGround())
					this.getBody().applyLinearImpulse(new Vector2(0.75f, 0f), this.getPosition(), true);
				else if(!this.getFacing() && gameScreen.contactListener.isPlayerOnGround())
					this.getBody().applyLinearImpulse(new Vector2(-0.75f, 0f), this.getPosition(), true);					
			}
			
			if(Gdx.input.isKeyPressed(Keys.F)){
				
				this.setState(7);
			}
		}
		if(gameScreen.contactListener.isPlayerOnGround() && this.getState() != 6)	
			this.getBody().setLinearVelocity(this.getBody().getLinearVelocity().x * 0.9f, this.getBody().getLinearVelocity().y);
		
	}
	
	public void increaseAnimTime(float value){animTime += value;}
	
	
	/**
	 * Collect crystal.
	 */
	public void collectCrystal(){numCrystals++;}
	
	/**
	 * Gets the num crystals.
	 *
	 * @return the num crystals
	 */
	public int getNumCrystals(){ return numCrystals; }	
	
	/**
	 * Sets the total crystals.
	 *
	 * @param i the new total crystals
	 */
	public void setTotalCrystals(int i){totalCrystals = i;}
	
	/**
	 * Gets the total crystals.
	 *
	 * @return the total crystals
	 */
	public int getTotalCrystals(){return totalCrystals;}
}