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

	/** The money. */
	public float money;

	/** The max health. */
	public float maxHealth = 100f;

	/** The health. */
	public float health = maxHealth;
	
	/** The gold gain. */
	public float goldGain = 0.02f;

	/** The gold leech. */
	public float goldLeech = 10f;

	/** The Health steal. */
	public float HealthSteal = 0f;

	/** The Health leech. */
	public float HealthLeech = 0f;
	
	/** The total jumps. */
	public int totalJumps = 1;
	
	/** The jump count. */
	public int jumpCount;
	
	/** The attack time. */
	public float attackTime = 0.07f;
	
	/** The move speed. */
	public float moveSpeed = 1f;
	
	/** The knockback chance. */
	public float knockbackChance = 0f;
	
	/** The mortar chance. */
	public float mortarChance = 0f;

	/** The Constant PLAYER_WIDTH. */
	public static float PLAYER_WIDTH = 8f;

	/** The Constant PLAYER_HEIGHT. */
	public static float PLAYER_HEIGHT = 20f;

	/** The run right. */
	Animation<TextureRegion> runRight;

	/** The jump right. */
	Animation<TextureRegion> jumpRight;

	/** The standing right. */
	Animation<TextureRegion> standingRight;

	/** The climbing. */
	Animation<TextureRegion> climbing;

	/** The primary right. */
	Animation<TextureRegion> primaryRight;

	/** The secondary right. */
	Animation<TextureRegion> secondaryRight;

	/** The tertiary right. */
	Animation<TextureRegion> tertiaryRight;

	/** The quaternary right. */
	Animation<TextureRegion> quaternaryRight;
	
	/** The drone idle. */
	Animation<TextureRegion> droneIdle;
	
	/** The drone scan. */
	Animation<TextureRegion> droneScan;
	
	/** The Constant DETECTION_RANGE. */
	private static final float DETECTION_RANGE = 1.7f;
	
	/** The second CD. */
	public long secondCD;
	
	/** The third CD. */
	public long thirdCD;
	
	/** The fourth CD. */
	public long fourthCD;
	
	/** The second used. */
	public long secondUsed;
	
	/** The third used. */
	public long thirdUsed;
	
	/** The fourth used. */
	public long fourthUsed;
	
	/** The marked mob. */
	Monster markedMob;
	
	/** The anim time. */
	private float animTime;
	
	/** The frames run. */
	private int framesRun;
	
	/** The type. */
	private int type;
	
	/** The prev frame. */
	private TextureRegion prevFrame = null;
	
	/** The game screen. */
	private GameScreen gameScreen;

	
	/**
	 * Instantiates a new player.
	 *
	 * @param body the body
	 * @param gameScreen the game screen
	 * @param type the type
	 */
	public Player(Body body, GameScreen gameScreen, int type){//Add a type int later to determing which animations will be loaded in
		
		super(body);
		
		secondCD = 3000;
		thirdCD = 6000;
		fourthCD = 10000;
		
		markedMob = null;
		
		this.gameScreen = gameScreen;
		
		this.type = type;
		
		money = 0;
		
		switch(this.type){
			
		case 1:
			
			secondCD = 2000;
			thirdCD = 4000;
			fourthCD = 6000;
			this.createCommando();
			break;
		case 2:
			
			secondCD = 3000;
			thirdCD = 6000;
			fourthCD = 10000;
			this.createSniper();
			break;
		}
		
		framesRun = 0;
	}
	
	/**
	 * Draw commando.
	 *
	 * @param spriteBatch the sprite batch
	 * @param stateTime the state time
	 */
	private void drawCommando(SpriteBatch spriteBatch, float stateTime){
		
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
					
					gameScreen.createBullet("bullet:10.00", this.getFacing());
					
					if(Math.random() < this.mortarChance){
						gameScreen.createMortar(20f, this.getFacing());
					}
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
					
					gameScreen.createBullet("ray:20.00", this.getFacing());
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
					
					gameScreen.createBullet("bullet:15.00", this.getFacing());
				}
				else if(framesRun == 3 || framesRun ==7 || framesRun == 11){
					
					gameScreen.createBullet("bullet:15.00", !this.getFacing());
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
	 * Draw sniper.
	 *
	 * @param spriteBatch the sprite batch
	 * @param stateTime the state time
	 */
	private void drawSniper(SpriteBatch spriteBatch, float stateTime){
		
		switch(this.getState()){
		
		case 0:
			
			spriteBatch.draw(climbing.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH * 1.5f, PLAYER_HEIGHT * 1.5f, GameScreen.SCALE, GameScreen.SCALE, 0);
			break;
		case 1: 		
			
			if(this.getFacing()){
				
				spriteBatch.draw(standingRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE - 1, 0, 0, 20, PLAYER_HEIGHT * 1.5f, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE - 1, 0, 0, 20, PLAYER_HEIGHT * 1.5f, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 2:
			
			if(this.getBody().getLinearVelocity().x >= 0){
			
				spriteBatch.draw(jumpRight.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, 20, PLAYER_HEIGHT * 1.5f, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(jumpRight.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, 20, PLAYER_HEIGHT * 1.5f, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 3:
			
			if(this.getFacing())
				spriteBatch.draw(runRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, 20, PLAYER_HEIGHT * 1.5f, GameScreen.SCALE, GameScreen.SCALE, 0);
			else
				spriteBatch.draw(runRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, 20, PLAYER_HEIGHT * 1.5f, -GameScreen.SCALE, GameScreen.SCALE, 0);

			break;
		case 4:

			if (prevFrame != primaryRight.getKeyFrame(animTime, true)){
				
				if(framesRun == 2){
					
					gameScreen.createBullet("bullet:15.00", this.getFacing());
					
					if(Math.random() < this.mortarChance){
						gameScreen.createMortar(20f, this.getFacing());
					}
				}
				framesRun++;
				prevFrame = primaryRight.getKeyFrame(animTime, true);
			}
	
			if (framesRun <= 6){

				if(this.getFacing())
					spriteBatch.draw(primaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - (PLAYER_WIDTH/2 * GameScreen.SCALE), this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE- 2, 0, 0, 56, 33, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(primaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + (PLAYER_WIDTH/2 * GameScreen.SCALE), this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE - 2, 0, 0, 56, 33, -GameScreen.SCALE, GameScreen.SCALE, 0);
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
				

				framesRun++;
				prevFrame = secondaryRight.getKeyFrame(animTime, true);
			}

			if (framesRun <= 12){
				
				if(this.getFacing())
					spriteBatch.draw(secondaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - 1 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, 23, 39, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(secondaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - 1 -  (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, 23, 39, -GameScreen.SCALE, GameScreen.SCALE, 0);				
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
				
				if(framesRun == 1){
					
					gameScreen.createBullet("ray:40.00", this.getFacing());
				}
				framesRun++;
				prevFrame = tertiaryRight.getKeyFrame(animTime, true);
			}

			if (framesRun <=  6){
			
				if(this.getFacing())
					spriteBatch.draw(tertiaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - 20, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 1) * GameScreen.SCALE, 0, 0, 94, 39, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(tertiaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + 20, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 1) * GameScreen.SCALE, 0, 0, 94, 39, -GameScreen.SCALE, GameScreen.SCALE, 0);

			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			break;
		}	
		
		if(markedMob == null){
		
			if(this.getFacing()){
			
				spriteBatch.draw(droneIdle.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - 20, this.getBody().getPosition().y * 100 + 10, 0, 0, 15, 15, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
		
				spriteBatch.draw(droneIdle.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + 20, this.getBody().getPosition().y * 100 + 10, 0, 0, 15, 15, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
		}else{
			
			spriteBatch.draw(droneScan.getKeyFrame(stateTime, true), markedMob.getBody().getPosition().x * 100 - 20, markedMob.getBody().getPosition().y * 100 + 10, 0, 0, 15, 15, GameScreen.SCALE, GameScreen.SCALE, 0);

		}
		
		
		//spriteBatch.end();
	}
	
	/**
	 * Framesrun needs to be changed depending on the character/class.
	 *
	 * @param spriteBatch the sprite batch
	 * @param stateTime the state time
	 */
	public void drawPlayer(SpriteBatch spriteBatch, float stateTime){
			
		//spriteBatch.begin();
		
		switch(this.type){
		
		case 1:
				drawCommando(spriteBatch, stateTime);
				break;
		case 2:
				drawSniper(spriteBatch, stateTime);
				break;
		case 3:
				
				break;
		}
	}
	
	/**
	 * Update player movement.
	 */
	public void updateMovement(){
		
		if (this.getState() <= 3){
			
			if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
			
				if(this.jumpCount > 0){		
			
					if(gameScreen.phase == 1){
						
						gameScreen.phase = 2;
					}
					this.getBody().applyLinearImpulse(new Vector2(0f, 2.8f), this.getPosition(), true);
					
					this.setState(2);
					
					this.jumpCount -= 1;
				}
			}
		
			if (Gdx.input.isKeyPressed(Keys.UP) && gameScreen.contactListener.isPlayerOnLadder()){
				
				if(gameScreen.phase == 2){
					
					gameScreen.phase = 3;
				}
				this.setState(0);
				this.getBody().setLinearVelocity(new Vector2(0f, 1.5f));
			}
			
			if(Gdx.input.isKeyPressed(Keys.LEFT)){	
		
				if(gameScreen.phase == 0){
					
					gameScreen.phase = 1;
				}
				this.setState(3);
				this.setFace(false);//CHANGE!!!
		
				if(this.getBody().getLinearVelocity().x > -moveSpeed)
					this.getBody().applyLinearImpulse(new Vector2(-moveSpeed / 2, 0f), this.getPosition(), true);
			}
		
			if(Gdx.input.isKeyPressed(Keys.RIGHT)){
						
				if(gameScreen.phase == 0){
					
					gameScreen.phase = 1;
				}
				this.setState(3);
				this.setFace(true);
		
				if(this.getBody().getLinearVelocity().x < moveSpeed)			
					this.getBody().applyLinearImpulse(new Vector2(moveSpeed / 2, 0f), this.getPosition(), true);
			}
		
			if(!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT) && gameScreen.contactListener.isPlayerOnGround() == true){
			
				this.setState(1);
			}
	
			if(gameScreen.contactListener.isPlayerOnGround() == false && gameScreen.contactListener.isPlayerOnLadder() == false){
			
				this.setState(2);
			}
			
			if(Gdx.input.isKeyPressed(Keys.A)){
			
				if(gameScreen.phase == 3){
					
					gameScreen.phase = 4;
				}
				this.setState(4);
			}
			
			if(Gdx.input.isKeyPressed(Keys.S) && ((System.currentTimeMillis() - secondUsed) >= secondCD)){
				
				if(gameScreen.phase == 3){
					
					gameScreen.phase = 4;
				}
				this.setState(5);
				
				if(this.type == 2){
					
					if(this.getFacing() && gameScreen.contactListener.isPlayerOnGround())
						this.getBody().applyLinearImpulse(new Vector2(-6f, 0f), this.getPosition(), true);
					else if(!this.getFacing() && gameScreen.contactListener.isPlayerOnGround())
						this.getBody().applyLinearImpulse(new Vector2(6f, 0f), this.getPosition(), true);
				}
				secondUsed = System.currentTimeMillis();
			}
			
			if(Gdx.input.isKeyPressed(Keys.D) && ((System.currentTimeMillis() - thirdUsed) >= thirdCD)){
				
				if(gameScreen.phase == 3){
					
					gameScreen.phase = 4;
				}
				this.setState(6);
				
				if(this.type == 1){
					
					if(this.getFacing() && gameScreen.contactListener.isPlayerOnGround())
						this.getBody().applyLinearImpulse(new Vector2(0.75f, 0f), this.getPosition(), true);
					else if(!this.getFacing() && gameScreen.contactListener.isPlayerOnGround())
						this.getBody().applyLinearImpulse(new Vector2(-0.75f, 0f), this.getPosition(), true);
				}
					
				thirdUsed = System.currentTimeMillis();
			}
			
			if(Gdx.input.isKeyPressed(Keys.F) && ((System.currentTimeMillis() - fourthUsed) >= fourthCD)){
				
				if(gameScreen.phase == 3){
					
					gameScreen.phase = 4;
				}
				
				if(this.type == 1)
					this.setState(7);
				else if(this.type == 2){
					
					markMob();
				}
				
				fourthUsed = System.currentTimeMillis();
			}
		}
		
		if(gameScreen.contactListener.isPlayerOnGround() && this.getState() != 6)	
			
			this.getBody().setLinearVelocity(this.getBody().getLinearVelocity().x * 0.9f, this.getBody().getLinearVelocity().y);
	}
	
	/**
	 * Mark mob.
	 */
	private void markMob(){
			
		if(markedMob != null){
			
			markedMob.isMarked = false;
			markedMob = null;
		}
			for (Monster m : GameScreen.monsterList){
				
				float range = (float) Math.sqrt(Math.pow(m.getPosition().x - this.getPosition().x, 2) + Math.pow(m.getPosition().y - this.getPosition().y, 2));
				if(range <= DETECTION_RANGE){
					
					if(markedMob == null){
						
						markedMob = m;
						m.isMarked = true;
					}
						if(m.health > markedMob.health){
							
							markedMob.isMarked = false;
							markedMob = m;
							m.isMarked = true;
						}
					}
				}
	}
	
	/**
	 * Increase max health.
	 *
	 * @param increase the increase
	 */
	public void increaseMaxHealth(float increase){
		
		this.maxHealth += increase;
		this.health += increase;
	}
	
	/**
	 * Increase gold gain.
	 *
	 * @param increase the increase
	 */
	public void increaseGoldGain(float increase){
		this.goldGain += increase;
	}
	
	/**
	 * Increase gold leech.
	 *
	 * @param increase the increase
	 */
	public void increaseGoldLeech(float increase){
		this.goldLeech += increase;
	}
	
	/**
	 * Increase health steal.
	 *
	 * @param increase the increase
	 */
	public void increaseHealthSteal(float increase){
		this.HealthSteal += increase;
	}
	
	/**
	 * Increase health leech.
	 *
	 * @param increase the increase
	 */
	public void increaseHealthLeech(float increase){
		this.HealthLeech += increase;
	}
	
	/**
	 * Increase jumps.
	 *
	 * @param increase the increase
	 */
	public void increaseJumps(int increase){
		this.totalJumps += 1;
	}
	
	/**
	 * Increase attack speed.
	 *
	 * @param increase the increase
	 */
	public void increaseAttackSpeed(float increase){
		this.attackTime -= increase;
		
		Texture texture;
		TextureRegion[] sprites;
		
		switch(this.type){
		case 1:
			texture = GameScreen.textures.getTexture("commando");

			sprites = new TextureRegion[5];
			sprites = TextureRegion.split(texture, 18, 13)[2];
			primaryRight = new Animation<TextureRegion>(attackTime, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4]});
			break;
		case 2:
			
			texture = GameScreen.textures.getTexture("sniper");

			sprites = new TextureRegion[6];
			sprites = TextureRegion.split(texture, 62, 14)[2];
			primaryRight = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5]});
			break;
			
		}
	}
	
	/**
	 * Increase move speed.
	 *
	 * @param increase the increase
	 */
	public void increaseMoveSpeed(float increase){
		this.moveSpeed += increase;
	}
	
	/**
	 * Increase knockback chance.
	 *
	 * @param increase the increase
	 */
	public void increaseKnockbackChance(float increase){
		this.knockbackChance += increase;
	}
	
	/**
	 * Increase mortar chance.
	 *
	 * @param increase the increase
	 */
	public void increaseMortarChance(float increase){
		this.mortarChance += increase;
	}
	
	/**
	 * Increase anim time.
	 *
	 * @param value the value
	 */
	public void increaseAnimTime(float value){animTime += value;}

	/**
	 * Creates the commando.
	 */
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
		primaryRight = new Animation<TextureRegion>(attackTime, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4]});
		
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

	/**
	 * Creates the sniper.
	 */
	private void createSniper(){
		
		Texture texture = GameScreen.textures.getTexture("sniper");
		TextureRegion[] sprites = new TextureRegion[4];
		
		sprites = TextureRegion.split(texture, 17, 14)[0];
		standingRight = new Animation<TextureRegion>(0.07f, sprites[0]);
		jumpRight = new Animation<TextureRegion>(0.07f, sprites[1]);
	
		sprites = new TextureRegion[5];
		sprites = TextureRegion.split(texture, 12, 12)[0];
		climbing = new Animation<TextureRegion>(0.5f, new TextureRegion[]{sprites[3], sprites[4]});
	
		
		sprites = new TextureRegion[8];
		sprites = TextureRegion.split(texture, 17, 14)[1];
		runRight = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7]});
	
		sprites = new TextureRegion[6];
		sprites = TextureRegion.split(texture, 62, 14)[2];
		primaryRight = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5]});
		
		sprites = new TextureRegion[12];
		sprites = TextureRegion.split(texture, 19, 20)[4];
		secondaryRight = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7], sprites[8], sprites[9], sprites[10], sprites[11]});
		
		sprites = new TextureRegion[9];
		sprites = TextureRegion.split(texture, 90, 18)[6];
		tertiaryRight = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5]});
		
		//Not Needed
		sprites = new TextureRegion[15];
		sprites = TextureRegion.split(texture, 40, 13)[5];
		quaternaryRight = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7], sprites[8], sprites[9], sprites[10], sprites[11], sprites[12], sprites[13], sprites[14]});
		
		sprites = new TextureRegion[1];
		sprites = TextureRegion.split(texture, 21, 11)[13];
		droneIdle = new Animation<TextureRegion>(0.07f, sprites[0]);
		
		sprites = new TextureRegion[2];
		sprites = TextureRegion.split(texture, 21, 11)[13];
		droneScan = new Animation<TextureRegion>(0.5f, new TextureRegion[]{sprites[1], sprites[2]});
		
	}

}