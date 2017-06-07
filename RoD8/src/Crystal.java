import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Crystal extends B2DSprite{

	private Animation<TextureRegion>crystalAnim;
	
	public Crystal(Body body){
		
		super(body);
		
		//Texture texture = GameScreen.textures.getTexture("crystal");
		//TextureRegion[] sprites = TextureRegion.split(texture, 16, 16)[0];
		crystalAnim = new Animation<TextureRegion>(0.07f, TextureRegion.split(GameScreen.textures.getTexture("crystal"), 16, 16)[0]);

		//setAnimation(sprites, 1/12f);
	}
	
	
	public Animation<TextureRegion> getAnim(){return crystalAnim;}
}
