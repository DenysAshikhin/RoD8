import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * The Class Item.
 */
public class Item extends B2DSprite{
	
	public float identifier;

	GameScreen gameScreen;
	
	String type;
	int itemNum;

	private Animation<TextureRegion> image;
	
	public Item(Body body, GameScreen gamescreen, String item, float identity){
		
		super(body);
		
		gameScreen = gamescreen;
		this.type = item;
		this.identifier = identity;
		
		Texture texture = GameScreen.textures.getTexture("items");
		TextureRegion[] sprites;
		
		this.width = 16f;
		this.height = 16f;
		
		switch(type){
		case "root":
			itemNum = 1;
			sprites = new TextureRegion[1];
			sprites = TextureRegion.split(texture, 32, 32)[0];
			image = new Animation<TextureRegion>(0.07f, sprites[0]);
			break;
		}
	}
	
	public void getItem(){
		switch(itemNum){
		case 1: //root

			GameScreen.player.increaseMaxHealth(10);
			break;
		}
	}
	
	public void drawItem(SpriteBatch spritebatch){
		switch(itemNum){
		case 1: //root
			spritebatch.draw(image.getKeyFrame(gameScreen.stateTime, false), this.getPosition().x * 100 + this.width * GameScreen.SCALE / 2, this.getPosition().y * 100 - this.height * GameScreen.SCALE / 2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
			break;
		}
	}
}