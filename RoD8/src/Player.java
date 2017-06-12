import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * The Class Player.
 */
public class Player extends B2DSprite{

	/** The num crystals. */
	private int numCrystals;
	
	/** The total crystals. */
	private int totalCrystals;
	
	private float animTime;
	
	private SpriteBatch spriteBat;
	
	/**
	 * Instantiates a new player.
	 *
	 * @param body the body
	 */
	public Player(Body body){
		
		super(body);
		animTime = 0;

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
