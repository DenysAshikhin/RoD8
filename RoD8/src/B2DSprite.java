import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

// TODO: Auto-generated Javadoc
/**
 * The Class B2DSprite.
 */
public class B2DSprite {

	/** The body. */
	protected Body body;
	
	/** The animation. */
	protected Animation animation;
	
	/** The width. */
	protected float width = 36;
	
	/** The height. */
	protected float height = 32;
	
	/** The state. */
	private int state;
	
	/** The facing. */
	private boolean facing;

	
	/**
	 * Instantiates a new b 2 D sprite.
	 *
	 * @param body the body
	 */
	public B2DSprite(Body body){
		
		this.body = body;
		animation = new Animation();
	}

	/**
	 * Sets the animation.
	 *
	 * @param reg the reg
	 * @param delay the delay
	 */
	public void setAnimation(TextureRegion[][] reg, float delay){
		
		animation.setFrames(reg, delay);
	}
	
	/**
	 * Update.
	 *
	 * @param dt the dt
	 */
	public void update(float dt){
		animation.update(dt);
	}
	
	/**
	 * Render.
	 *
	 * @param sb the sb
	 */
	public void render(SpriteBatch sb){
		sb.begin();
		sb.draw(animation.getFrame(state), body.getPosition().x * 100 - width / 2, body.getPosition().y * 100 - height / 2, 0, 0, width, height, 4, 4, 0);
		sb.end();
	}
	
	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public Body getBody(){return body;}
	
	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Vector2 getPosition(){return body.getPosition();}
	
	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth(){return width;}
	
	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight(){return height;}
	
	/**
	 * Sets the state.
	 *
	 * @param i the new state
	 */
	public void setState(int i){
		state = i;
		animation.setState(state);
	}
	
	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public int getState(){return state;}
	
	/**
	 * Sets the face.
	 *
	 * @param value the new face
	 */
	public void setFace(boolean value){facing = value;}
	
	/**
	 * Gets the facing.
	 *
	 * @return the facing
	 */
	public boolean getFacing(){return facing;}
	
	/**
	 * Sets the body.
	 *
	 * @param body the new body
	 */
	public void setBody(Body body){this.body = body;}
	
}
