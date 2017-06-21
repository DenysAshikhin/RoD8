import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

// TODO: Auto-generated Javadoc
/**
 * The Class Teleporter.
 */
public class Teleporter extends B2DSprite{

	/** The is touched. */
	public boolean isTouched;
	
	/** The is active. */
	public boolean isActive;
	
	/** The was activated. */
	public boolean wasActivated;
	
	/** The is finished. */
	public boolean isFinished;

	/** The body. */
	Body body;
	
	/** The available. */
	private Animation<TextureRegion> available;
	
	/** The unavailable. */
	private Animation<TextureRegion> unavailable;
	
	/**
	 * Instantiates a new teleporter.
	 *
	 * @param body the body
	 */
	public Teleporter(Body body){
		super(body);

		isTouched = false;
		isActive = false;
		wasActivated = false;
		isFinished = false;
		this.body = body;
		
		Texture texture = GameScreen.textures.getTexture("portal");
		
		TextureRegion[] sprites = new TextureRegion[2];
		sprites = TextureRegion.split(texture, 82, 27)[0];
		
		available = new Animation<TextureRegion>(0.07f, sprites[0]);
		unavailable = new Animation<TextureRegion>(0.07f, sprites[1]);
	}

	
	/**
	 * Draw portal.
	 *
	 * @param spriteBatch the sprite batch
	 * @param value the value
	 */
	public void drawPortal(SpriteBatch spriteBatch, boolean value){
		
		spriteBatch.begin();
		if(value){
			spriteBatch.draw(available.getKeyFrame(0, false), this.getBody().getPosition().x * 100 - 40, this.getBody().getPosition().y * 100 - 2, 0, 0, 82, 27, 1, 1, 0);
		}
		else{
			spriteBatch.draw(unavailable.getKeyFrame(0, false), this.getBody().getPosition().x * 100 - 40, this.getBody().getPosition().y * 100 - 2, 0, 0, 82, 27, 1, 1, 0);
		}
		
		spriteBatch.end();
	}
}
