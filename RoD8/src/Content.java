import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;

/**
 * The Class Content.
 */
public class Content {

	/** The textures. */
	private HashMap<String, Texture> textures;
	
	/**
	 * Instantiates a new content.
	 */
	public Content(){
		
		textures = new HashMap<String, Texture>();
	}
	
	/**
	 * Load texture.
	 *
	 * @param path the path
	 * @param key the key
	 */
	public void loadTexture(String path, String key){
		
		Texture texture = new Texture(path);
		textures.put(key, texture);
	}
	
	/**
	 * Gets the texture.
	 *
	 * @param key the key
	 * @return the texture
	 */
	public Texture getTexture(String key){
		return textures.get(key);
	}
	
	/**
	 * Dispose texture.
	 *
	 * @param key the key
	 */
	public void disposeTexture(String key){
		Texture tex = textures.get(key);
		if (tex != null) tex.dispose();
	}
}
