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
	int itemType;
	
	int itemNum;

	private Animation<TextureRegion> image;
	
	public Item(Body body, GameScreen gamescreen, String item, float identity){
		
		super(body);
		
		gameScreen = gamescreen;
		this.type = item;
		this.identifier = identity;
		
		Texture texture = GameScreen.textures.getTexture("items");
		TextureRegion[] sprites;
		
		this.width = 32f;
		this.height = 32f;
		
		switch(type){
		case "root":
			itemType = 1;
			sprites = new TextureRegion[10];
			sprites = TextureRegion.split(texture, 32, 32)[0];
			image = new Animation<TextureRegion>(0.07f, sprites[itemType]);
			break;
		}
	}
	
	public void getItem(){
		switch(itemType){
		case 1: //root
			GameScreen.player.increaseMaxHealth(10);
			break;
		}
	}
	
	public void writeItem(SpriteBatch spritebatch){
		
		spritebatch.draw(image.getKeyFrame(gameScreen.stateTime, false), this.itemNum * 36, 100, this.width, this.height);
	}
	
	public void drawItem(SpriteBatch spritebatch){
		
		spritebatch.draw(image.getKeyFrame(gameScreen.stateTime, false), this.getPosition().x * 100 + this.width * GameScreen.SCALE / 2, this.getPosition().y * 100 - this.height * GameScreen.SCALE / 2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
	}
}