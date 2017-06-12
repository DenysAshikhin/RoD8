import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * The Class Player.
 */
public class Monster extends B2DSprite{

	/** The num crystals. */
	private int numCrystals;
	
	/** The total crystals. */
	private int totalCrystals;
	
	/**
	 * Instantiates a new player.
	 *
	 * @param body the body
	 */
	public Monster(Body body) {
		
		super(body);

		Texture texture = GameScreen.textures.getTexture("crab");
		
		TextureRegion[][] sprites = new TextureRegion[3][8];
		
		sprites[0] = TextureRegion.split(texture, 8, 12)[0];
		sprites[1] = TextureRegion.split(texture, 8, 12)[1];
		sprites[2] = TextureRegion.split(texture, 8, 12)[2];
		
		setAnimation(sprites, 1/12f);
	}

	/**
	 * Collect crystal.
	 */
	public void collectCrystal(){numCrystals++;}
	
	/**
	 * Gets the num crystals.
	 *
	 * @return the num crystals
	 */
	public int getNumCrystals(){ return numCrystals; }	
	
	/**
	 * Sets the total crystals.
	 *
	 * @param i the new total crystals
	 */
	public void setTotalCrystals(int i){totalCrystals = i;}
	
	/**
	 * Gets the total crystals.
	 *
	 * @return the total crystals
	 */
	public int getTotalCrystals(){return totalCrystals;}

}
