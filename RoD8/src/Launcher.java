import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

// TODO: Auto-generated Javadoc
/**
 * The Class Launcher.
 */
public class Launcher extends B2DSprite{

	/** The launcher anim. */
	Animation<TextureRegion> launcherAnim;

	/**
	 * Instantiates a new launcher.
	 *
	 * @param body the body
	 */
	public Launcher(Body body) {
		super(body);
			
		Texture texture = GameScreen.textures.getTexture("launcher");
		
		TextureRegion[] sprites = new TextureRegion[5];
			
		sprites = TextureRegion.split(texture, 32, 63)[0];
		launcherAnim = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3]});	
	}

	
	
	/**
	 * Draw launcher.
	 *
	 * @param spriteBatch the sprite batch
	 * @param delta the delta
	 */
	public void drawLauncher(SpriteBatch spriteBatch, float delta){
		
		spriteBatch.begin();
		spriteBatch.draw(launcherAnim.getKeyFrame(delta, true), this.getBody().getPosition().x * 100 - 16, this.getBody().getPosition().y * 100 - 2, 0, 0, 32, 75, 1, 1, 0);
		spriteBatch.end();
	}
}
