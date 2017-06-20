import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The Class Animation.
 */
public class Animation {

	/** The frames. */
	private TextureRegion[][] frames;
	
	/** The time. */
	private float time;
	
	/** The delay. */
	private float delay;
	
	/** The current frame. */
	private int currentFrame;
	
	/** The times played. */
	private int timesPlayed;
	
	/** The state. */
	private int state;
	
	/**
	 * Instantiates a new animation.
	 */
	public Animation(){}
	
	/**
	 * Instantiates a new animation.
	 *
	 * @param frames the frames
	 */
	public Animation(TextureRegion[][] frames){
		
		this(frames, 1/12f);
	}
	
	/**
	 * Instantiates a new animation.
	 *
	 * @param frames the frames
	 * @param delay the delay
	 */
	public Animation(TextureRegion[][] frames, float delay){
		
		setFrames(frames, delay);
	}
	
	/**
	 * Sets the frames.
	 *
	 * @param frames the frames
	 * @param delay the delay
	 */
	public void setFrames(TextureRegion[][] frames, float delay){
		
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
		timesPlayed = 0;
	}
	
	/**
	 * Update.
	 *
	 * @param dt the dt
	 */
	public void update(float dt){
		
		if(delay <= 0) return;
		time += dt;
		while(time >= delay){
			
			step();
		}
	}
	
	/**
	 * Gets the frame.
	 *
	 * @param state the state
	 * @return the frame
	 */
	public TextureRegion getFrame(int state){return frames[state][currentFrame];}
	
	/**
	 * Gets the times playerd.
	 *
	 * @return the times playerd
	 */
	public int getTimesPlayerd(){return timesPlayed;}
	
	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(int state){this.state = state;}

	/**
	 * Step.
	 */
	private void step(){
		
		time -= delay;
		currentFrame++;
		//System.out.println(currentFrame);
		if(currentFrame == frames[state].length){
			
			currentFrame = 0;
			timesPlayed++;
		}
	}
}
