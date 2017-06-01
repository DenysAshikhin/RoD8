import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Crystal extends B2DSprite{

	public Crystal(Body body){
		
		super(body);
		
		Texture texture = GameScreen.textures.getTexture("crystal");
		TextureRegion[] sprites = TextureRegion.split(texture, 16, 16)[0];
		
		//setAnimation(sprites, 1/12f);
	}
}