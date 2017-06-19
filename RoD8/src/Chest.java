import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Chest extends B2DSprite{

	public boolean isOpen;
	public boolean isTouched;
	private GameScreen gameScreen;
	private Texture texture;
	private Animation<TextureRegion> open;
	private Animation<TextureRegion> closed;
	
	public Chest(Body body, GameScreen gameScreen){
		
		super(body);
		isOpen = false;
		isTouched = false;
		this.gameScreen = gameScreen;
		//texture = gameScreen.textures.getTexture("chest");
		
		Texture texture = GameScreen.textures.getTexture("portal");
		
		TextureRegion[] sprites = new TextureRegion[14];
		sprites = TextureRegion.split(texture, 19, 14)[0];
		
		open = new Animation<TextureRegion>(0.07f, sprites[12]);
		closed = new Animation<TextureRegion>(0.07f, sprites[13]);
	}
	
	
	public void drawChest(SpriteBatch spriteBatch){
		
		spriteBatch.begin();
		if(isOpen){
	System.out.println(true);
			spriteBatch.draw(open.getKeyFrame(0, false), this.getBody().getPosition().x * 100 - 10, this.getBody().getPosition().y * 100 - 2, 0, 0, 19, 14, 1, 1, 0);
		}
		else{
			System.out.println(false);
			spriteBatch.draw(closed.getKeyFrame(0, false), this.getBody().getPosition().x * 100 - 10, this.getBody().getPosition().y * 100 - 2, 0, 0, 19, 14, 1, 1, 0);
		}
		
		spriteBatch.end();
	}
}
