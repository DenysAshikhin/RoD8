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
	
	int type;
	
	int itemNum;

	private Animation<TextureRegion> image;
	
	public Item(Body body, GameScreen gamescreen, int item, float identity){
		
		super(body);
		
		gameScreen = gamescreen;
		this.type = item;
		this.identifier = identity;
		
		Texture texture = GameScreen.textures.getTexture("items");
		TextureRegion[] sprites;
		
		this.width = 32f;
		this.height = 32f;
		
		sprites = new TextureRegion[10];
		sprites = TextureRegion.split(texture, 32, 32)[0];
		image = new Animation<TextureRegion>(0.07f, sprites[type - 1]);
	}
	
	public void getItem(){
		switch(type){
		case 1: //root
			GameScreen.player.increaseMaxHealth(10);
			break;
		case 2: //piggy bank
			GameScreen.player.increaseGoldGain(0.02f);
			break;
		case 3: //smart shopper
			GameScreen.player.increaseGoldLeech(2f);
			break;
		case 4: //infusion
			GameScreen.player.increaseHealthSteal(2f);
			break;
		case 5: //leech
			GameScreen.player.increaseHealthLeech(0.1f);
		}
	}
	
	public void writeItem(SpriteBatch spritebatch){
		
		spritebatch.draw(image.getKeyFrame(gameScreen.stateTime, false), this.itemNum * 20, 80, this.width, this.height);
	}
	
	public void drawItem(SpriteBatch spritebatch){
		
		spritebatch.draw(image.getKeyFrame(gameScreen.stateTime, false), this.getPosition().x * 100 + this.width * GameScreen.SCALE / 2, this.getPosition().y * 100 - this.height * GameScreen.SCALE / 2, 0, 0, this.width, this.height, GameScreen.SCALE, GameScreen.SCALE, 0);
	}
}