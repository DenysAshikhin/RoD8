import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {

	private TextureRegion[][] frames;
	private float time;
	private float delay;
	private int currentFrame;
	private int timesPlayed;
	private int state;
	
	public Animation(){}
	
	public Animation(TextureRegion[][] frames){
		
		this(frames, 1/12f);
	}
	
	public Animation(TextureRegion[][] frames, float delay){
		
		setFrames(frames, delay);
	}
	
	public void setFrames(TextureRegion[][] frames, float delay){
		
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
		timesPlayed = 0;
	}
	
	public void update(float dt){
		
		if(delay <= 0) return;
		time += dt;
		while(time >= delay){
			
			step();
		}
	}
	
	private void step(){
		
		time -= delay;
		currentFrame++;
		//System.out.println(currentFrame);
		if(currentFrame == frames[state].length){
			
			currentFrame = 0;
			timesPlayed++;
		}
	}
	
	public TextureRegion getFrame(int state){return frames[state][currentFrame];}
	public int getTimesPlayerd(){return timesPlayed;}
	public void setState(int state){this.state = state;}
}
