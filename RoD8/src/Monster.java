import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * The Class Player.
 */
public class Monster extends B2DSprite{

	private static final float DETECTION_RANGE = 200f;

	private static final float CRAB_RANGE = 28f;

	/** The num crystals. */
	private int numCrystals;
	
	/** The total crystals. */
	private int totalCrystals;

	private Animation<TextureRegion> runright;
	private Animation<TextureRegion> standingright;
	private Animation<TextureRegion> primaryright;
	private Animation<TextureRegion> deathright;

	private TextureRegion prevFrame;

	public float identifier;
	private int type;
	
	private int framesRun;
	public boolean killed = false;
	
	public float health = 1f;
	
	public int onGround;
	public int onWall;
	
	private float animTime;
	
	GameScreen gameScreen;
	
	/**
	 * Instantiates a new player.
	 *
	 * @param body the body
	 */
	public Monster(Body body, GameScreen gameScreen, float num, int type) {
		
		super(body);
		this.gameScreen = gameScreen;
		this.identifier = num;
		this.type = type;
		
		switch(this.type){
		case 1:
			createCrab();
			break;
		case 2:
			createLemurian();
			break;
		}
		
	}
	
	private void createCrab(){
		
		Texture texture = GameScreen.textures.getTexture("crab");
		TextureRegion[] sprites = new TextureRegion[4];
		
		sprites = TextureRegion.split(texture, 35, 32)[0];
		standingright = new Animation<TextureRegion>(0.07f, sprites[0]);
		runright = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3]});

		sprites = TextureRegion.split(texture, 35, 32)[1];
		primaryright = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3]});

		//sprites = TextureRegion.split(texture, 42, 32)[2];
		//deathright = new Animation<TextureRegion>(0.25f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3]});
	}
	
	private void createLemurian(){
		
		Texture texture = GameScreen.textures.getTexture("lemurian");
		TextureRegion[] sprites;
		
		sprites = new TextureRegion[3];
		sprites = TextureRegion.split(texture, 22, 28)[0];
		primaryright = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2]});

		sprites = new TextureRegion[6];
		sprites = TextureRegion.split(texture, 22, 28)[1];
		standingright = new Animation<TextureRegion>(0.07f, sprites[2]);
		runright = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5]});

		sprites = new TextureRegion[2];
		sprites = TextureRegion.split(texture, 28, 28)[2];
		deathright = new Animation<TextureRegion>(0.25f, new TextureRegion[]{sprites[0], sprites[1]});
	}
	/**
	 * Draw monsters.
	 */	
	public void drawMonsters(SpriteBatch spriteBatch){
		if(this.getBody().getFixtureList().size != 0){
			Fixture f = this.getBody().getFixtureList().peek();
			
			if(((String) f.getUserData()).contains("attack")){
				
				this.getBody().destroyFixture(f);
			}
		}else{
			this.setState(-1);
		}
		
		//spriteBatch.begin();
		switch(this.getState()){
		case 0: 

			if(this.getFacing()){
				
				spriteBatch.draw(standingright.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingright.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 1:

			if(this.getFacing()){
			
				spriteBatch.draw(standingright.getKeyFrame(animTime, false), this.getBody().getPosition().x * 100 - this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingright.getKeyFrame(animTime, false), this.getBody().getPosition().x * 100 + this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 2:

			spriteBatch.draw(runright.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
			break;
		case 3:

			spriteBatch.draw(runright.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, -GameScreen.SCALE, GameScreen.SCALE, 0);
			break;
		case 4:
		case 5:
			
			if (prevFrame != primaryright.getKeyFrame(animTime, true)){
				
				if(framesRun == 4){
					
					gameScreen.createLocalAttack(this, 5f, this.getFacing());
				}
				framesRun++;
				prevFrame = primaryright.getKeyFrame(animTime, true);
			}
			
			if (framesRun <= 4){
				
				if(this.getState() == 4){
					spriteBatch.draw(primaryright.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
				}else{
					spriteBatch.draw(primaryright.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, -GameScreen.SCALE, GameScreen.SCALE, 0);
				}
				
			}else{
				
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
	 * Update monster movement.
	 */
	public void monsterMovement(){
		if(this.getState() != -1){
			float range;
			range = (float) Math.sqrt(Math.pow(this.getPosition().x - GameScreen.player.getPosition().x, 2) + Math.pow(this.getPosition().y - GameScreen.player.getPosition().y, 2));
			
			if(range <= DETECTION_RANGE){
				if (this.getState() <= 3){
					
					if(range <= CRAB_RANGE/GameScreen.PPM){
						if(this.getPosition().x < GameScreen.player.getPosition().x){

							this.setFace(true);
							this.setState(4);
						}else if(this.getPosition().x > GameScreen.player.getPosition().x){

							this.setFace(false);
							this.setState(5);
						}
					}else{
						if ((this.getState() == 2 || this.getState() == 3) && onWall > 0){
							if(onGround > 0){		
						
								this.getBody().applyForceToCenter(0, 30, true);
							}
						}
						
						if(Math.abs(this.getPosition().x - GameScreen.player.getPosition().x) < 0.05){
							this.setState(0);

							if(this.getBody().getLinearVelocity().x > 0){
								this.getBody().applyForceToCenter(-10, 0, true);
							}
							if(this.getBody().getLinearVelocity().x < 0){
								this.getBody().applyForceToCenter(10, 0, true);
							}
						}else{
							
							if(this.getPosition().x > GameScreen.player.getPosition().x){
						
								this.setState(3);
								this.setFace(false);
						
								if(this.getBody().getLinearVelocity().x > -0.7f){
							
									this.getBody().applyLinearImpulse(new Vector2(-0.7f, 0f), this.getPosition(), true);
								}
							}
						
							if(this.getPosition().x < GameScreen.player.getPosition().x){
										
								this.setState(2);
								this.setFace(true);
						
								if(this.getBody().getLinearVelocity().x < 0.7f){
							
									this.getBody().applyLinearImpulse(new Vector2(0.7f, 0f), this.getPosition(), true);
								}
							}
						}
				
						if(!(onGround > 0)){
						
							this.setState(1);
							
						}
					}
				}else{
					this.getBody().setLinearVelocity(this.getBody().getLinearVelocity().x * 0.5f, this.getBody().getLinearVelocity().y);
					
					if(range > CRAB_RANGE/GameScreen.PPM){
						this.setState(1);
					}
				}
				
			}else{
				if(this.getBody().getLinearVelocity().x > 0){
					this.getBody().applyForceToCenter(-10, 0, true);
				}
				if(this.getBody().getLinearVelocity().x < 0){
					this.getBody().applyForceToCenter(10, 0, true);
				}
			}
		}		
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
