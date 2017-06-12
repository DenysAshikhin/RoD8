import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * The Class Player.
 */
public class Monster extends B2DSprite{

	/** The num crystals. */
	private int numCrystals;
	
	/** The total crystals. */
	private int totalCrystals;

	Animation<TextureRegion> runRightCrab;
	Animation<TextureRegion> standingRightCrab;
	Animation<TextureRegion> primaryRightCrab;
	Animation<TextureRegion> deathRightCrab;
	
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
	public void drawMonsters(SpriteBatch spriteBatch, float stateTime){
		
		spriteBatch.begin();
		
		switch(this.getState()){
		case 0: 		
			
			if(this.getFacing()){
				
				spriteBatch.draw(standingRightCrab.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - this.width * SCALE/2, this.getBody().getPosition().y * 100 - this.height * SCALE/2, 0, 0, this.width, this.height, SCALE, SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingRightCrab.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + this.width * SCALE/2, this.getBody().getPosition().y * 100 - this.height * SCALE/2, 0, 0, this.width, this.height, -SCALE, SCALE, 0);
			}
			break;
		case 1:
			
			if(this.getBody().getLinearVelocity().x >= 0){
			
				spriteBatch.draw(standingRightCrab.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 - this.width * SCALE/2, this.getBody().getPosition().y * 100 - this.height * SCALE/2, 0, 0, this.width, this.height, SCALE, SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingRightCrab.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 + this.width * SCALE/2, this.getBody().getPosition().y * 100 - this.height * SCALE/2, 0, 0, this.width, this.height, -SCALE, SCALE, 0);
			}
			break;
		case 2:
			
			spriteBatch.draw(runRightCrab.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - this.width * SCALE/2, this.getBody().getPosition().y * 100 - this.height * SCALE/2, 0, 0, this.width, this.height, SCALE, SCALE, 0);
			break;
		case 3:
			spriteBatch.draw(runRightCrab.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + this.width * SCALE/2, this.getBody().getPosition().y * 100 - this.height * SCALE/2, 0, 0, this.width, this.height, -SCALE, SCALE, 0);

			break;
		case 4:

			if (prevFrame != primaryRightCrab.getKeyFrame(stateTime, true)){
				
				framesRun++;
				prevFrame = primaryRightCrab.getKeyFrame(stateTime, true);
			}
		
			if (framesRun <= 5){
				
				spriteBatch.draw(primaryRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - 10, this.getBody().getPosition().y * 100 - this.height - 5, 0, 0, 18, this.height, SCALE, SCALE, 0);
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
