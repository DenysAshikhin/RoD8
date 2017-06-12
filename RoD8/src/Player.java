import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

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
	
	
	private TextureRegion prevFrame = null;

	private GameScreen gameScreen;
	
	/**
	 * Instantiates a new player.
	 *
	 * @param body the body
	 */
	public Player(Body body, GameScreen gameScreen){
		
		super(body);
		
		this.gameScreen = gameScreen;
		
		//Temporary loading of textures for commando animations
		Texture texture = GameScreen.textures.getTexture("commando");
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
		
		framesRun = 0;
		

	}
	
	
	
	public void drawPlayer(SpriteBatch spriteBatch, float stateTime){
		
		spriteBatch.begin();
		
		switch(this.getState()){
		case 0: 		
			
			if(this.getFacing()){
				
				spriteBatch.draw(standingRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - PLAYER_HEIGHT * GameScreen.SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - PLAYER_HEIGHT * GameScreen.SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 1:
			
			if(this.getBody().getLinearVelocity().x >= 0){
			
				spriteBatch.draw(jumpRight.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 - PLAYER_WIDTH * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - PLAYER_HEIGHT * GameScreen.SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(jumpRight.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 + PLAYER_WIDTH * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - PLAYER_HEIGHT * GameScreen.SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 2:
			
			spriteBatch.draw(runRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - PLAYER_HEIGHT * GameScreen.SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			break;
		case 3:
			spriteBatch.draw(runRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - PLAYER_HEIGHT * GameScreen.SCALE/2, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);

			break;
		case 4:

			if (prevFrame != primaryRight.getKeyFrame(animTime, true)){
				
				framesRun++;
				prevFrame = primaryRight.getKeyFrame(animTime, true);
			}
		
			if (framesRun <= 5){

				spriteBatch.draw(primaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - 10, this.getBody().getPosition().y * 100 - PLAYER_HEIGHT - 5, 0, 0, 18, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				this.setState(0);
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
				
				spriteBatch.draw(secondaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - 10, this.getBody().getPosition().y * 100 - PLAYER_HEIGHT - 5, 0, 0, 33, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				this.setState(0);
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
			
				spriteBatch.draw(tertiaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - 12, this.getBody().getPosition().y * 100 - PLAYER_HEIGHT - 5, 0, 0, 12, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				this.setState(0);
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
				
				spriteBatch.draw(quaternaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - 40, this.getBody().getPosition().y * 100 - PLAYER_HEIGHT - 5, 0, 0, 40, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
			}
			break;
		}
		
		spriteBatch.end();		
	}
	
	
	
	/**
	 * Update player movement.
	 */
	private void updateMovement(){
		
		if (this.getState() <= 3){
			
			if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
			
				if(gameScreen.contactListener.isPlayerOnGround()){		
			
					this.getBody().applyForceToCenter(0, 300, true);	
					this.setState(1);	
				}
			}
		
			if(Gdx.input.isKeyPressed(Keys.LEFT)){	
		
				this.setState(3);
				this.setFace(false);//CHANGE!!!
		
				if(this.getBody().getLinearVelocity().x > -2f){
			
					this.getBody().applyLinearImpulse(new Vector2(-1f, 0f), this.getPosition(), true);
				}
			}
		
			if(Gdx.input.isKeyPressed(Keys.RIGHT)){
						
				this.setState(2);
				this.setFace(true);
		
				if(this.getBody().getLinearVelocity().x < 2f){
			
					this.getBody().applyLinearImpulse(new Vector2(1f, 0f), this.getPosition(), true);

				}
			}
		
			if(!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT)){
			
				this.setState(0);
				this.getBody().setLinearVelocity(this.getBody().getLinearVelocity().x * 0.9f, this.getBody().getLinearVelocity().y);
	
			}
	
			if(gameScreen.contactListener.isPlayerOnGround() == false){
			
				this.setState(1);
				
			}
			
			if(Gdx.input.isKeyPressed(Keys.A)){
			
				this.setState(4);

			}
			
			if(Gdx.input.isKeyPressed(Keys.S)){
				
				this.setState(5);

			}
			
			if(Gdx.input.isKeyPressed(Keys.D)){
				
				this.setState(6);

			}
			
			if(Gdx.input.isKeyPressed(Keys.F)){
				
				this.setState(7);

			}
		}		
	}
	
	//public void resetAnimTime(){animTime = 0;}
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