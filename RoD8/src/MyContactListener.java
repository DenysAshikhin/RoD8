import java.util.HashSet;

import com.badlogic.gdx.math.Vector2;
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
	
	private int monsterOnWall;
	
	/** The bodies to remove. */
	private Array<Body> bodiesToRemove;
	private HashSet<Body> bodyToRemove;
	
	/**
	 * Instantiates a new my contact listener.
	 */
	public MyContactListener(){
		
		super();
		bodiesToRemove = new Array<Body>();
		bodyToRemove = new HashSet<Body>();
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#beginContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	//Called when two fixtures start to collide
	@Override
	public void beginContact(Contact contact) {
	
		Fixture fa = contact.getFixtureA();		
		Fixture fb = contact.getFixtureB();//foot
		
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
		
		if(fa.getUserData() != null && fa.getUserData().equals("mwall")){
			monsterOnWall++;
		}

		if(fb.getUserData() != null && fb.getUserData().equals("mwall")){
			monsterOnWall++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("crystal")){
			
			//bodiesToRemove.add(fa.getBody());
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("crystal")){
			
			//bodiesToRemove.add(fb.getBody());
		}
		
		
		if(fa.getUserData() != null && ((String)fa.getUserData()).contains("ray")){
			
			if(fb.getUserData() != null && fb.getUserData().equals("ground"))
				bodyToRemove.add(fa.getBody());
			
			if(fb.getUserData() != null && ((String)fb.getUserData()).contains("monster")){

				for(Monster m : GameScreen.monsterList){
					float damage = Float.parseFloat(((String) fb.getUserData()).substring(((String) fb.getUserData()).indexOf(':') + 1, ((String) fb.getUserData()).length()));
					if (m.identifier == damage){

						m.health -= damage/10;
						if((fa.getBody().getLinearVelocity().x) < 0)
							fb.getBody().applyLinearImpulse(new Vector2(-4f, 0f), fb.getBody().getPosition(), true);
						if((fa.getBody().getLinearVelocity().x) > 0)
							fb.getBody().applyLinearImpulse(new Vector2(4f, 0f), fb.getBody().getPosition(), true);
					}	
				}	
			}
		}
		
		if(fb.getUserData() != null && ((String)fb.getUserData()).contains("ray")){
			
			if(fa.getUserData() != null && fa.getUserData().equals("ground"))
				bodyToRemove.add(fb.getBody());
			if(fa.getUserData() != null && ((String)fa.getUserData()).contains("monster")){
				
				for(Monster m : GameScreen.monsterList){
					float damage = Float.parseFloat(((String) fa.getUserData()).substring(((String) fa.getUserData()).indexOf(':') + 1, ((String) fa.getUserData()).length()));
					if (m.identifier == damage){

						m.health -= damage/10;
						if ((fb.getBody().getLinearVelocity().x) <0)
							fa.getBody().applyLinearImpulse(new Vector2(-4f, 0f), fa.getBody().getPosition(), true);
						if((fb.getBody().getLinearVelocity().x) > 0)
							fa.getBody().applyLinearImpulse(new Vector2(4f, 0f), fa.getBody().getPosition(), true);					
					}
				}	
			}		
		}
		
		if(fa.getUserData() != null && ((String) fa.getUserData()).contains("bullet")){
	
			bodyToRemove.add(fa.getBody());
			
			if(fb.getUserData() != null && ((String) fb.getUserData()).contains("monster")){
				
				for(Monster m : GameScreen.monsterList){
					float damage = Float.parseFloat(((String) fb.getUserData()).substring(((String) fb.getUserData()).indexOf(':') + 1, ((String) fb.getUserData()).length()));

					if (m.identifier == damage){
						
						bodyToRemove.add(fb.getBody());
						m.killed = true;
						GameScreen.removeMobs.add(m);
					}
				}
			}
		}
		if(fb.getUserData() != null && ((String) fb.getUserData()).contains("bullet")){

			bodyToRemove.add(fb.getBody());

			if(fa.getUserData() != null && ((String) fa.getUserData()).contains("monster")){
				
				for(Monster m : GameScreen.monsterList){

					if (m.identifier == Float.parseFloat(((String) fa.getUserData()).substring(((String) fa.getUserData()).indexOf(':') + 1, ((String) fa.getUserData()).length()))){
						
						bodyToRemove.add(fa.getBody());
						m.killed = true;
						GameScreen.removeMobs.add(m);
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
	
	public HashSet<Body> getBodyToRemove(){return bodyToRemove;}
	
	/**
	 * Checks if is player on ground.
	 *
	 * @return true, if is player on ground
	 */
	public boolean isPlayerOnGround(){return playerOnGround > 0; }
	
	public boolean isMonsterOnGround(){return monsterOnGround > 0; }

	public boolean isMonsterOnWall(){return monsterOnWall > 0; }
	
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
