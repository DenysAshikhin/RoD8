import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {

	public static final int SPEED = 500;
	private static Texture texture;
	private static float DEFAULT_Y = 40;
	
	float x,y;
	
	
	public boolean remove = false;
	
	public Bullet(float x){
		
		this.x = x;
		this.y = DEFAULT_Y;
		
		if (texture == null)
			texture = new Texture("bullet.png");
	}
	
	public void update(float deltatime){
		
		y += SPEED * deltatime;
	
		if (y > Gdx.graphics.getHeight()){
			remove = true;
		}
	}
	
	public void render(SpriteBatch batch){
		
		batch.draw(texture, x , y);
	}
	
}
