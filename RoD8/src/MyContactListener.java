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
		
		if(fa == null || fb == null){
			return;
		}
		else{
			
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			playerOnGround++;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			playerOnGround++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("mfoot")){
			monsterOnGround++;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("mfoot")){
			monsterOnGround++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("crystal")){
			
			bodiesToRemove.add(fa.getBody());
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("crystal")){
			
			bodiesToRemove.add(fb.getBody());
		}
		
		if(fa.getUserData() != null && ((String) fa.getUserData()).contains("bullet")){
			
			bodiesToRemove.add(fa.getBody());
			if(((String) fb.getUserData()).contains("monster")){
				
				for(Monster m : GameScreen.monsterList){

					if (m.identifier == Integer.parseInt(((String) fb.getUserData()).substring(((String) fb.getUserData()).indexOf(':') + 1, ((String) fb.getUserData()).length()))){
						
						bodiesToRemove.add(fb.getBody());
						GameScreen.monsterList.removeValue(m, true);
					}
				}
			}

		}
		if(fb.getUserData() != null && ((String) fb.getUserData()).contains("bullet")){

			bodiesToRemove.add(fb.getBody());
			
			if(((String) fa.getUserData()).contains("monster")){
				
				for(Monster m : GameScreen.monsterList){

					if (m.identifier == Integer.parseInt(((String) fa.getUserData()).substring(((String) fa.getUserData()).indexOf(':') + 1, ((String) fa.getUserData()).length()))){
						
						bodiesToRemove.add(fa.getBody());
						GameScreen.monsterList.removeValue(m, true);
					}
				}
			}

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
