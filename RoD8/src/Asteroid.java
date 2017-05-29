import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Asteroid {

	public static final int SPEED = 250;
	private static Texture texture;
	public static int WIDTH = 40;
	public static int HEIGHT = 16;
	
	float x,y;
	
	
	public boolean remove = false;
	
	public Asteroid(float x){
		
		this.x = x;
		this.y = Gdx.graphics.getHeight();
		
		if (texture == null)
			texture = new Texture("asteroid.png");
	}
	
	public void update(float deltatime){
		
		y -= SPEED * deltatime;
	
		if (y < - HEIGHT){
			remove = true;
		}
	}
	
	public void render(SpriteBatch batch){
		
		batch.draw(texture, x , y);
	}
	
}