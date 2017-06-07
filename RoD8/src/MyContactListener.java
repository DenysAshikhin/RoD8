import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class MyContactListener implements ContactListener{

	private int playerOnGround;
	private Array<Body> bodiesToRemove;
	
	public MyContactListener(){
		
		super();
		bodiesToRemove = new Array<Body>();
	}
	
	//Called when two fixtures start to collide
	@Override
	public void beginContact(Contact contact) {
	
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();//foot
			
		if(fa == null || fb == null) return;

		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			
			playerOnGround++;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
		
			playerOnGround++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("crystal")){
			
			bodiesToRemove.add(fa.getBody());
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("crystal")){
			
			bodiesToRemove.add(fb.getBody());
		}
	}

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
		
	}

	public Array<Body> getBodiesToRemove(){return bodiesToRemove;}
	public boolean isPlayerOnGround(){return playerOnGround > 0; }
	
	
	//Collision detection
	//preSolve
	//Collision handling
	//postSolve
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {}

}
