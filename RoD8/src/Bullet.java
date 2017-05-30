import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {

	public static final int SPEED = 500;
	private static Texture texture;
	private static float DEFAULT_Y = 40;
	private static final int WIDTH = 3;
	private static final int HEIGHT = 12;
	
	float x,y;
	
	CollisionRect rect;
	
	public boolean remove = false;
	
	public Bullet(float x){
		
		this.x = x;
		this.y = DEFAULT_Y;
		
		this.rect = new CollisionRect(x, y, WIDTH, HEIGHT);
		
		if (texture == null)
			texture = new Texture("bullet.png");
	}
	
	public void update(float deltatime){
		
		y += SPEED * deltatime;
	
		if (y > Gdx.graphics.getHeight()){
			remove = true;
		}
		
		rect.move(x, y);
	}
	
	public void render(SpriteBatch batch){
		
		batch.draw(texture, x , y);
	}
	
	public CollisionRect getCollisionRect(){return rect;}
}
