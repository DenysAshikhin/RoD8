import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

/**
 * The listener interface for receiving myContact events.
 * The class that is interested in processing a myContact
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addMyContactListener<code> method. When
 * the myContact event occurs, that object's appropriate
 * method is invoked.
 *
 * @see MyContactEvent
 */
public class MyContactListener implements ContactListener{

	/** The player on ground. */
	private int playerOnGround;
	
	private int monsterOnGround;
	
	/** The bodies to remove. */
	private Array<Body> bodiesToRemove;
	
	/**
	 * Instantiates a new my contact listener.
	 */
	public MyContactListener(){
		
		super();
		bodiesToRemove = new Array<Body>();
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#beginContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	//Called when two fixtures start to collide
	@Override
	public void beginContact(Contact contact) {
	
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();//foot
		
		System.out.println(fa.getUserData());
		System.out.println(fb.getUserData());
		
		if(fa == null || fb == null){
			return;
		}
		else{
			
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			System.out.println("player on ground");
			playerOnGround++;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			System.out.println("player on ground");
			playerOnGround++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("mfoot")){
			System.out.println("monster on ground");
			monsterOnGround++;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("mfoot")){
			System.out.println("monster on ground");
			monsterOnGround++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("crystal")){
			
			bodiesToRemove.add(fa.getBody());
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("crystal")){
			
			bodiesToRemove.add(fb.getBody());
		}
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#endContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	//Called when two fixtures no longer colliding
	@Override
	public void endContact(Contact contact) {
		
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();//foot
		
		if(fa == null || fb == null) return;
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			
			playerOnGround--;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
		
			playerOnGround--;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("mfoot")){
			
			monsterOnGround--;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("mfoot")){
		
			monsterOnGround--;
		}
		
	}

	/**
	 * Gets the bodies to remove.
	 *
	 * @return the bodies to remove
	 */
	public Array<Body> getBodiesToRemove(){return bodiesToRemove;}
	
	/**
	 * Checks if is player on ground.
	 *
	 * @return true, if is player on ground
	 */
	public boolean isPlayerOnGround(){return playerOnGround > 0; }
	
	public boolean isMonsterOnGround(){return monsterOnGround > 0; }
	
	
	//Collision detection
	//preSolve
	//Collision handling
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#preSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.Manifold)
	 */
	//postSolve
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#postSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.ContactImpulse)
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {}

}
