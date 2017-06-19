import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Teleporter extends B2DSprite{

	public boolean isTouched;
	public boolean isActivated;

	private GameScreen gameScreen;
	Body body;
	private Animation<TextureRegion> available;
	private Animation<TextureRegion> unavailable;
	
	public Teleporter(Body body, GameScreen gameSceen){
		super(body);

		isTouched = false;
		isActivated = false;
		this.body = body;
		this.gameScreen = gameScreen;
		
		Texture texture = GameScreen.textures.getTexture("portal");
		
		TextureRegion[] sprites = new TextureRegion[2];
		sprites = TextureRegion.split(texture, 82, 27)[0];
		
		available = new Animation<TextureRegion>(0.07f, sprites[0]);
		unavailable = new Animation<TextureRegion>(0.07f, sprites[1]);
	}

	
	public void drawChest(SpriteBatch spriteBatch, boolean value){
		
		spriteBatch.begin();
		if(value){
			spriteBatch.draw(unavailable.getKeyFrame(0, false), this.getBody().getPosition().x * 100 - 40, this.getBody().getPosition().y * 100 - 2, 0, 0, 82, 27, 1, 1, 0);
		}
		else{
			spriteBatch.draw(available.getKeyFrame(0, false), this.getBody().getPosition().x * 100 - 40, this.getBody().getPosition().y * 100 - 2, 0, 0, 82, 27, 1, 1, 0);
		}
		
		spriteBatch.end();
	}
}
