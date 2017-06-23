import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * The Class Mine.
 */
public class Mine extends B2DSprite{
	
	/** The open. */
	private Animation<TextureRegion> anim;
	
	/**
	 * Instantiates a new chest.
	 *
	 * @param body the body
	 */
	public Mine(Body body){
		
		super(body);
		
		Texture texture = GameScreen.textures.getTexture("mine");
		
		TextureRegion[] sprites = new TextureRegion[12];
		sprites = TextureRegion.split(texture, 17, 16)[0];
		
		anim = new Animation<TextureRegion>(0.04f, new TextureRegion[]{sprites[0],sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7], sprites[8], sprites[9], sprites[10], sprites[11]});
	}
	
	
	/**
	 * Draw chest.
	 *
	 * @param spriteBatch the sprite batch
	 * @param stateTime the state time
	 */
	public void drawMine(SpriteBatch spriteBatch, float stateTime){
		
		spriteBatch.draw(anim.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - 10, this.getBody().getPosition().y * 100 - 2, 0, 0, 19, 14, GameScreen.SCALE, GameScreen.SCALE, 0);
	}
}
