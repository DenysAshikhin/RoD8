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
public class Monster extends B2DSprite{

	private static final float DETECTION_RANGE = 200f;

	private static final float CRAB_RANGE = 20f;

	/** The num crystals. */
	private int numCrystals;
	
	/** The total crystals. */
	private int totalCrystals;

	private Animation<TextureRegion> runRightCrab;
	private Animation<TextureRegion> standingRightCrab;
	private Animation<TextureRegion> primaryRightCrab;
	private Animation<TextureRegion> deathRightCrab;

	private TextureRegion prevFrame;

	private int framesRun;
	
	GameScreen gameScreen;
	
	/**
	 * Instantiates a new player.
	 *
	 * @param body the body
	 */
	public Monster(Body body) {
		
		super(body);

		//Temporary loading of textures for crab animations
		Texture texturecrab = GameScreen.textures.getTexture("crab");
		TextureRegion[] spritescrab = new TextureRegion[4];
		
		spritescrab = TextureRegion.split(texturecrab, 36, 32)[0];
		standingRightCrab = new Animation<TextureRegion>(0.07f, spritescrab[0]);
		runRightCrab = new Animation<TextureRegion>(0.07f, new TextureRegion[]{spritescrab[0], spritescrab[1], spritescrab[2], spritescrab[3]});

		spritescrab = TextureRegion.split(texturecrab, 36, 32)[1];
		primaryRightCrab = new Animation<TextureRegion>(0.07f, new TextureRegion[]{spritescrab[0], spritescrab[1], spritescrab[2], spritescrab[3]});

		spritescrab = TextureRegion.split(texturecrab, 42, 32)[2];
		deathRightCrab = new Animation<TextureRegion>(0.07f, new TextureRegion[]{spritescrab[0], spritescrab[1], spritescrab[2], spritescrab[3]});
		
	}
	/**
	 * Draw monsters.
	 */	
	public void drawMonsters(SpriteBatch spriteBatch){
		
		spriteBatch.begin();
		
		switch(this.getState()){
		case 0: 		
			
			if(this.getFacing()){
				
				spriteBatch.draw(standingRightCrab.getKeyFrame(gameScreen.stateTime, true), this.getBody().getPosition().x * 100 - this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingRightCrab.getKeyFrame(gameScreen.stateTime, true), this.getBody().getPosition().x * 100 + this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 1:
			
			if(this.getBody().getLinearVelocity().x >= 0){
			
				spriteBatch.draw(standingRightCrab.getKeyFrame(gameScreen.stateTime, false), this.getBody().getPosition().x * 100 - this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingRightCrab.getKeyFrame(gameScreen.stateTime, false), this.getBody().getPosition().x * 100 + this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 2:
			
			spriteBatch.draw(runRightCrab.getKeyFrame(gameScreen.stateTime, true), this.getBody().getPosition().x * 100 - this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
			break;
		case 3:
			spriteBatch.draw(runRightCrab.getKeyFrame(gameScreen.stateTime, true), this.getBody().getPosition().x * 100 + this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, -GameScreen.SCALE, GameScreen.SCALE, 0);

			break;
		case 4:

			if (prevFrame != primaryRightCrab.getKeyFrame(gameScreen.stateTime, true)){
				
				framesRun++;
				prevFrame = primaryRightCrab.getKeyFrame(gameScreen.stateTime, true);
			}
		
			if (framesRun <= 5){
				
				spriteBatch.draw(primaryRightCrab.getKeyFrame(gameScreen.stateTime, true), this.getBody().getPosition().x * 100 - 10, this.getBody().getPosition().y * 100 - this.height - 5, 0, 0, 18, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				this.setState(0);
				framesRun = 0;
			}
			break;
		}
		
		spriteBatch.end();
	}
	
	/**
	 * Update monster movement.
	 */
	private void monsterMovement(){
		
		float range;
		
		range = (float) Math.sqrt(Math.pow(this.getPosition().x - gameScreen.player.getPosition().x, 2) + Math.pow(this.getPosition().y - gameScreen.player.getPosition().y, 2));
		
		if(range <= DETECTION_RANGE){
			
			if (this.getState() <= 3){
				
				if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
				
					if(gameScreen.contactListener.isMonsterOnGround()){		
				
						this.getBody().applyForceToCenter(0, 300, true);	
						this.setState(1);	
					}
				}
			
				if(this.getPosition().x > this.getPosition().x){	
			
					this.setState(3);
					this.setFace(false);//CHANGE!!!
			
					if(this.getBody().getLinearVelocity().x > -2f){
				
						this.getBody().applyLinearImpulse(new Vector2(-1f, 0f), this.getPosition(), true);
					}
				}
			
				if(this.getPosition().x < this.getPosition().x){
							
					this.setState(2);
					this.setFace(true);
			
					if(this.getBody().getLinearVelocity().x < 2f){
				
						this.getBody().applyLinearImpulse(new Vector2(1f, 0f), this.getPosition(), true);

					}
				}
		
				if(gameScreen.contactListener.isMonsterOnGround() == false){
				
					this.setState(1);
					
				}
				
				if(range <= CRAB_RANGE){
					//attack
				}
			}
			
		}else{
			
			this.setState(0);
			this.getBody().setLinearVelocity(this.getBody().getLinearVelocity().x * 0.9f, this.getBody().getLinearVelocity().y);
		
		}
		
	}

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
