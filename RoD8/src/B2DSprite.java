import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class B2DSprite {

	protected Body body;
	protected Animation animation;
	protected float width = 8;
	protected float height = 12;
	private int state;

	
	public B2DSprite(Body body){
		
		this.body = body;
		animation = new Animation();
	}

	public void setAnimation(TextureRegion[][] reg, float delay){
		
		animation.setFrames(reg, delay);
	}
	
	public void update(float dt){
		animation.update(dt);
	}
	
	public void render(SpriteBatch sb){
		sb.begin();
		sb.draw(animation.getFrame(state), body.getPosition().x * 100 - width / 2, body.getPosition().y * 100 - height / 2, 0, 0, width, height, 4, 4, 0);
		sb.end();
	}
	
	public Body getBody(){return body;}
	public Vector2 getPosition(){return body.getPosition();}
	public float getWidth(){return width;}
	public float getHeight(){return height;}
	public void setState(int i){
		state = i;
		animation.setState(state);
	}
	public int getState(){return state;}
	
}
