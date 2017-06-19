import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public class Chest extends B2DSprite{

	private boolean isOpen;
	public boolean isTouched;
	private GameScreen gameScreen;
	
	public Chest(Body body, GameScreen gameScreen){
		
		super(body);
		isOpen = false;
		isTouched = false;
		this.gameScreen = gameScreen;
	}
	
	
	public void drawChest(SpriteBatch spriteBatch){
		
		
	}
}
