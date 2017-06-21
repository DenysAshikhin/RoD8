import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

// TODO: Auto-generated Javadoc
/**
 * The Class Player.
 */
public class Monster extends B2DSprite{

	/** The identifier. */
	public float identifier;

	/** The type. */
	public int type;

	/** The killed. */
	public boolean killed = false;

	/** The health. */
	public float health;

	/** The jump height. */
	public float jumpHeight;

	/** The on ground. */
	public int onGround;

	/** The on wall. */
	public int onWall;

	/** The can hurdle. */
	public int canHurdle = 1;
	
	/** The is marked. */
	public boolean isMarked;

	/** The game screen. */
	GameScreen gameScreen;

	/** The runright. */
	private Animation<TextureRegion> runright;
	
	/** The standingright. */
	private Animation<TextureRegion> standingright;
	
	/** The primaryright. */
	private Animation<TextureRegion> primaryright;
	
	/** The secondaryright. */
	private Animation<TextureRegion> secondaryright;

	/** The prev frame. */
	private TextureRegion prevFrame;
	
	/** The frames run. */
	private int framesRun;
	
	/** The jump strength. */
	private float jumpStrength;
	
	/** The attack range. */
	private float attackRange;

	/** The primary attack frames. */
	private int primaryAttackFrames;
	
	/** The secondary attack frames. */
	private int secondaryAttackFrames;
	
	/** The anim time. */
	private float animTime;
	
	/** The Constant DETECTION_RANGE. */
	private static final float DETECTION_RANGE = 200f;

	/**
	 * Instantiates a new monster.
	 *
	 * @param body the body
	 * @param gameScreen the game screen
	 * @param num the num
	 * @param type the type
	 */
	public Monster(Body body, GameScreen gameScreen, float num, int type) {
	
		super(body);
		this.isMarked = false;
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
		case 3:
			createGiant();
			break;
		case 4:
			createGolem();
			break;
		case 5:
			createCastle();
			break;
		}

		
		this.width = GameScreen.MONSTER_WIDTH[type];
		this.height = GameScreen.MONSTER_HEIGHT[type];
		
		this.jumpHeight = (float) (Math.pow(jumpStrength, 2)/(2 * 9.81f));
		this.attackRange = this.width / 2;
	}
	
	/**
	 * Draw monsters.
	 *
	 * @param spriteBatch the sprite batch
	 * @param stateTime the state time
	 */	
	public void drawMonsters(SpriteBatch spriteBatch, float stateTime){
		
		if(this.getBody().getFixtureList().size != 0){
			
			Fixture f = this.getBody().getFixtureList().peek();
			
			if(((String) f.getUserData()).contains("attack")){
				
				this.getBody().destroyFixture(f);
			}
		}else{
			
			this.setState(-1);
		}
		
		switch(this.getState()){
		case 0: 
			framesRun = 0;
			animTime = 0;
			prevFrame = null;
	
			if(this.getFacing()){
				
				spriteBatch.draw(standingright.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingright.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 1:
			framesRun = 0;
			animTime = 0;
			prevFrame = null;
	
			if(this.getFacing()){
			
				spriteBatch.draw(standingright.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 - this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingright.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 + this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 2:
			framesRun = 0;
			animTime = 0;
			prevFrame = null;
			
			spriteBatch.draw(runright.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
			break;
		case 3:
			framesRun = 0;
			animTime = 0;
			prevFrame = null;
	
			spriteBatch.draw(runright.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, -GameScreen.SCALE, GameScreen.SCALE, 0);
			break;
		case 4:
		case 5:
			
			if (prevFrame != primaryright.getKeyFrame(animTime, true)){
				
				if(framesRun == (primaryAttackFrames - 1)){
					
					if(this.type == 4 || this.type == 5){
						gameScreen.createLocalAttack(this, 5f, this.getFacing(), true);
					}else{
						gameScreen.createLocalAttack(this, 5f, this.getFacing(), false);
					}
				}
				framesRun++;
				prevFrame = primaryright.getKeyFrame(animTime, true);
			}
			
			if (framesRun <= primaryAttackFrames){
				
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
		case 6:
		case 7:
			
			if (prevFrame != secondaryright.getKeyFrame(animTime, true)){
				
				if(framesRun == 4){
					
					gameScreen.createLocalAttack(this, 5f, this.getFacing(), false);
				}
				framesRun++;
				prevFrame = secondaryright.getKeyFrame(animTime, true);
			}
			
			if (framesRun <= secondaryAttackFrames){
				
				if(this.getState() == 6){
					spriteBatch.draw(secondaryright.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
				}else{
					spriteBatch.draw(secondaryright.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + this.width * GameScreen.SCALE/2, this.getBody().getPosition().y * 100 - this.height * GameScreen.SCALE/2, 0, 0, this.width, this.height, -GameScreen.SCALE, GameScreen.SCALE, 0);
				}
				
			}else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			break;
		}
	}

	/**
	 * Update monster movement.
	 */
	public void monsterMovement(){
		
		if(this.getState() != -1){
			float range = (float) Math.sqrt(Math.pow(this.getPosition().x - GameScreen.player.getPosition().x, 2) + Math.pow(this.getPosition().y - GameScreen.player.getPosition().y, 2));
			
				if(range <= DETECTION_RANGE){
					
					if (this.getState() <= 3){
						
						if(range <= attackRange/GameScreen.PPM){
							if(this.getPosition().x < GameScreen.player.getPosition().x){
	
								this.setFace(true);
								
								if(this.type == 5){
									this.setState(2 * ((int) (Math.random() * 2)) + 4);
								}else{
									this.setState(4);
								}
							}else if(this.getPosition().x > GameScreen.player.getPosition().x){
	
								this.setFace(false);
								
								if(this.type == 5){
									this.setState(2 * ((int) (Math.random() * 2)) + 5);
								}else{
									this.setState(5);
								}
							}
						}else{
							
							if ((this.getState() == 2 || this.getState() == 3) && onWall > 0){
								
								if(onGround > 0 && canHurdle > 0){		
							
									this.getBody().applyLinearImpulse(new Vector2(0, jumpStrength), this.getPosition(), true);
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
							
									if(this.getBody().getLinearVelocity().x > -0.5f){
								
										this.getBody().applyLinearImpulse(new Vector2(-0.3f, 0f), this.getPosition(), true);
									}
								}
							
								if(this.getPosition().x < GameScreen.player.getPosition().x){
											
									this.setState(2);
									this.setFace(true);
							
									if(this.getBody().getLinearVelocity().x < 0.5f){
								
										this.getBody().applyLinearImpulse(new Vector2(0.3f, 0f), this.getPosition(), true);
									}
								}
							}
					
							if(!(onGround > 0)){
							
								this.setState(1);
							}
						}
					}else{
						
						this.getBody().setLinearVelocity(this.getBody().getLinearVelocity().x * 0.5f, this.getBody().getLinearVelocity().y);
						
						if(range > this.attackRange/GameScreen.PPM){
							
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
			
		}else{
			
			GameScreen.monsterList.removeValue(this, true);
		}
	}

	/**
	 * Increase anim time.
	 *
	 * @param value the value
	 */
	public void increaseAnimTime(float value){animTime += value;}

	/**
	 * Creates the crab.
	 */
	private void createCrab(){
		this.health = 90f;
		this.jumpStrength = 0f;
		this.primaryAttackFrames = 4;
		
		Texture texture = GameScreen.textures.getTexture("crab");
		TextureRegion[] sprites;
		
		sprites = new TextureRegion[4];
		sprites = TextureRegion.split(texture, 35, 32)[0];
		standingright = new Animation<TextureRegion>(0.07f, sprites[0]);
		runright = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3]});

		sprites = new TextureRegion[primaryAttackFrames];
		sprites = TextureRegion.split(texture, 35, 32)[1];
		primaryright = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3]});
	}
	
	/**
	 * Creates the lemurian.
	 */
	private void createLemurian(){
		this.health = 60f;
		this.jumpStrength = 1.3f;
		this.primaryAttackFrames = 3;
		
		Texture texture = GameScreen.textures.getTexture("lemurian");
		TextureRegion[] sprites;
		
		sprites = new TextureRegion[primaryAttackFrames];
		sprites = TextureRegion.split(texture, 22, 28)[0];
		primaryright = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2]});

		sprites = new TextureRegion[6];
		sprites = TextureRegion.split(texture, 22, 28)[1];
		standingright = new Animation<TextureRegion>(0.07f, sprites[2]);
		runright = new Animation<TextureRegion>(0.08f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5]});
	}
	
	/**
	 * Creates the giant.
	 */
	private void createGiant(){
		this.health = 120f;
		this.jumpStrength = 0f;
		this.primaryAttackFrames = 4;
		
		Texture texture = GameScreen.textures.getTexture("giant");
		TextureRegion[] sprites;

		sprites = new TextureRegion[4];
		sprites = TextureRegion.split(texture, 48, 69)[0];
		standingright = new Animation<TextureRegion>(0.07f, sprites[3]);
		runright = new Animation<TextureRegion>(0.08f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3]});
		
		sprites = new TextureRegion[primaryAttackFrames];
		sprites = TextureRegion.split(texture, 80, 69)[1];
		primaryright = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3]});
	}
	
	/**
	 * Creates the golem.
	 */
	private void createGolem(){
		this.health = 80f;
		this.jumpStrength = 0f;
		this.primaryAttackFrames = 5;
		
		Texture texture = GameScreen.textures.getTexture("golem");
		TextureRegion[] sprites;

		sprites = new TextureRegion[7];
		sprites = TextureRegion.split(texture, 22, 33)[0];
		standingright = new Animation<TextureRegion>(0.07f, sprites[0]);
		runright = new Animation<TextureRegion>(0.08f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6]});
		
		sprites = new TextureRegion[primaryAttackFrames];
		sprites = TextureRegion.split(texture, 42, 33)[1];
		primaryright = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4]});
	}
	
	/**
	 * Creates the castle.
	 */
	private void createCastle(){
		this.health = 1000f;
		this.jumpStrength = 0f;
		this.primaryAttackFrames = 8;
		this.secondaryAttackFrames = 10;
		
		Texture texture = GameScreen.textures.getTexture("castle");
		TextureRegion[] sprites;

		sprites = new TextureRegion[6];
		sprites = TextureRegion.split(texture, 82, 119)[0];
		standingright = new Animation<TextureRegion>(0.07f, sprites[5]);
		runright = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5]});

		sprites = new TextureRegion[primaryAttackFrames];
		sprites = TextureRegion.split(texture, 180, 119)[1];
		primaryright = new Animation<TextureRegion>(0.11f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7]});
		
		sprites = new TextureRegion[secondaryAttackFrames];
		sprites = TextureRegion.split(texture, 110, 119)[2];
		secondaryright = new Animation<TextureRegion>(0.12f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7], sprites[8], sprites[9]});
	}
}
