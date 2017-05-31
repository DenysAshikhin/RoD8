import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Player extends B2DSprite{

	private int numCrystals;
	private int totalCrystals;
	
	public Player(Body body){
		
		super(body);

		Texture texture = GameScreen.textures.getTexture("commando");
		TextureRegion[][] sprites = new TextureRegion[3][8];
		
		sprites[0] = TextureRegion.split(texture, 8 ,12)[0];
		sprites[1] = TextureRegion.split(texture, 8, 12)[1];
		sprites[2] = TextureRegion.split(texture, 8 ,12)[2];
		
		setAnimation(sprites, 1/12f);
	}
	
	public void collectCrystal(){numCrystals++;}
	public int getNumCrystals(){ return numCrystals; }	
	public void setTotalCrystals(int i){totalCrystals = i;}
	public int getTotalCrystals(){return totalCrystals;}

}
