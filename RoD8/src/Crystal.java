import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

// TODO: Auto-generated Javadoc
/**
 * The Class Crystal.
 */
public class Crystal extends B2DSprite{

	/** The crystal anim. */
	private Animation<TextureRegion>crystalAnim;
	
	/**
	 * Instantiates a new crystal.
	 *
	 * @param body the body
	 */
	public Crystal(Body body){
		
		super(body);
		
		//Texture texture = GameScreen.textures.getTexture("crystal");
		//TextureRegion[] sprites = TextureRegion.split(texture, 16, 16)[0];
		crystalAnim = new Animation<TextureRegion>(0.07f, TextureRegion.split(GameScreen.textures.getTexture("crystal"), 16, 16)[0]);

		//setAnimation(sprites, 1/12f);
	}
	
	
	/**
	 * Gets the anim.
	 *
	 * @return the anim
	 */
	public Animation<TextureRegion> getAnim(){return crystalAnim;}
}
