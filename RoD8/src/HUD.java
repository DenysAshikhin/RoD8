import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HUD {

	private Player player;
	
	private TextureRegion[] blocks;
	
	public HUD(Player player){
		
		this.player = player;
		
		Texture texture = GameScreen.textures.getTexture("hud");
		
		blocks = new TextureRegion[3];
		
		for(int i = 0; i < blocks.length; i++){
			
			blocks[i] = new TextureRegion(texture, 32 + i * 16, 0, 16, 16);
		}
	}
	
	public void render(SpriteBatch sb){
		
		short bits = player.getBody().getFixtureList().first().getFilterData().maskBits;
		
		sb.begin();
		if((GameScreen.BIT_RED & bits) != 0){
			sb.draw(blocks[0], 4, 200);
		}
		if((GameScreen.BIT_GREEN & bits) != 0){
			sb.draw(blocks[1], 4, 200);			
		}
		if((GameScreen.BIT_BLUE & bits) != 0){
			sb.draw(blocks[2], 4, 200);
		}
		sb.end();
	}
}
