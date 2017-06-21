import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Mine extends B2DSprite{

	
	/** The is open. */
	public boolean isOpen;
	
	/** The is touched. */
	public boolean isTouched;
	
	/** The open. */
	private Animation<TextureRegion> anim;
	
	/**
	 * Instantiates a new chest.
	 *
	 * @param body the body
	 */
	public Mine(Body body){
		
		super(body);
		isOpen = false;
		isTouched = false;
		
		Texture texture = GameScreen.textures.getTexture("portal");
		
		TextureRegion[] sprites = new TextureRegion[12];
		sprites = TextureRegion.split(texture, 17, 10)[9];
		
		anim = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0],sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7], sprites[8], sprites[9], sprites[10], sprites[11]});
	}
	
	
	/**
	 * Draw chest.
	 *
	 * @param spriteBatch the sprite batch
	 */
	public void drawChest(SpriteBatch spriteBatch){
		
		spriteBatch.begin();

			spriteBatch.draw(anim.getKeyFrame(0, true), this.getBody().getPosition().x * 100 - 10, this.getBody().getPosition().y * 100 - 2, 0, 0, 19, 14, 1, 1, 0);
		spriteBatch.end();
	}
}
