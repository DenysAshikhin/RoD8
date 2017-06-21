import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// TODO: Auto-generated Javadoc
/**
 * The Class ScrollingBackground.
 */
public class ScrollingBackground {

	/** The Constant DEFAULT_SPEED. */
	public static final int DEFAULT_SPEED = 80;
	
	/** The Constant ACCELERATION. */
	public static final int ACCELERATION = 50;
	
	/** The Constant GOAL_REACHED_ACCELERATION. */
	public static final int GOAL_REACHED_ACCELERATION = 200;
	
	/** The image. */
	Texture image;
	
	/** The y 2. */
	float y1, y2;
	
	/** The speed. */
	int speed;//In pixels / second
	
	/** The goal speed. */
	int goalSpeed;
	
	/** The image scale. */
	float imageScale;
	
	/** The speed fixed. */
	boolean speedFixed = true;

	/**
	 * Instantiates a new scrolling background.
	 */
	public ScrollingBackground(){
		
		image = new Texture("stars_background.png");
		
		y1 = 0;
		y2 = image.getHeight();
		imageScale = 0;
		speed = 0;
		goalSpeed = DEFAULT_SPEED;
		
		
	}
	
	
	/**
	 * Update and render.
	 *
	 * @param deltaTime the delta time
	 * @param batch the batch
	 */
	public void updateAndRender(Float deltaTime, SpriteBatch batch){
		//Speed adjustment to reach goal
		
		if (speed < goalSpeed){
			speed += GOAL_REACHED_ACCELERATION * deltaTime;
			
			if (speed > goalSpeed)
				speed = goalSpeed;
		}
		else if(speed < goalSpeed){
				speed -= GOAL_REACHED_ACCELERATION * deltaTime;
				
				if (speed < goalSpeed)
					speed = goalSpeed;
		}
		
		if (!speedFixed)
			speed += ACCELERATION * deltaTime;
		y1 -= speed * deltaTime;
		y2 -= speed * deltaTime;
		
		//Checking for image offscreen and move it back up
		if (y1 + image.getHeight() * imageScale <= 0)
			y1 = y2 + image.getHeight() * imageScale;
		
		if (y2 + image.getHeight() * imageScale <= 0)
			y2 = y1 + image.getHeight() * imageScale;
		
		batch.draw(image, 0, y1, Gdx.graphics.getWidth(), image.getHeight() * imageScale);
		batch.draw(image, 0, y2, Gdx.graphics.getWidth(), image.getHeight() * imageScale);
		
	}
	
	/**
	 * Resize.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public void resize(int width, int height){
		imageScale = width / image.getWidth();
	}
	
	/**
	 * Sets the speed.
	 *
	 * @param goalSpeed the new speed
	 */
	public void setSpeed (int goalSpeed){this.goalSpeed = goalSpeed;}
	
	/**
	 * Sets the speed fixed.
	 *
	 * @param speedFixed the new speed fixed
	 */
	public void setSpeedFixed(boolean speedFixed){this.speedFixed = speedFixed;}
	}
