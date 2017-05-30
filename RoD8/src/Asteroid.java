import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Asteroid {

	public static final int SPEED = 250;
	private static Texture texture;
	public static int WIDTH = 40;
	public static int HEIGHT = 16;
	
	float x,y;
	
	CollisionRect rect;

	public boolean remove = false;
	
	public Asteroid(float x){
		
		this.x = x;
		this.y = Gdx.graphics.getHeight();
		
		this.rect = new CollisionRect(x, y, WIDTH, HEIGHT);

		
		if (texture == null)
			texture = new Texture("asteroid.png");
	}
	
	public void update(float deltatime){
		
		y -= SPEED * deltatime;
	
		if (y < - HEIGHT){
			remove = true;
		}
		
		rect.move(x, y);
	}
	
	public void render(SpriteBatch batch){
		
		batch.draw(texture, x , y);
	}
	
	public CollisionRect getCollisionRect(){return rect;}
	
}