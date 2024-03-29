import java.util.HashSet;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
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
	
	/** The body to remove. */
	public HashSet<Body> bodyToRemove;

	/** The explosion to add. */
	public Body explosionToAdd;

	/** The player on ground. */
	private int playerOnGround;
	
	/** The player in lava. */
	private int playerInLava;
	
	/** The player on ladder. */
	private int playerOnLadder;
	
	/** The bodies to remove. */
	private Array<Body> bodiesToRemove;
	
	/** The filter. */
	private Filter filter;
	
	/**
	 * Instantiates a new my contact listener.
	 *
	 * @param gameScreen the game screen
	 */
	public MyContactListener(GameScreen gameScreen){
		super();
		
		bodiesToRemove = new Array<Body>();
		bodyToRemove = new HashSet<Body>();
		
	}
	

	
	/**
	 * Gets the bodies to remove.
	 *
	 * @return the bodies to remove
	 */
	public Array<Body> getBodiesToRemove(){return bodiesToRemove;}
	
	/**
	 * Gets the body to remove.
	 *
	 * @return the body to remove
	 */
	public HashSet<Body> getBodyToRemove(){return bodyToRemove;}
	
	/**
	 * Checks if is player on ground.
	 *
	 * @return true, if is player on ground
	 */
	public boolean isPlayerOnGround(){return playerOnGround > 0; }
	
	/**
	 * Checks if is player on ladder.
	 *
	 * @return true, if is player on ladder
	 */
	public boolean isPlayerOnLadder(){return playerOnLadder > 0;}
	
	/**
	 * Checks if is player in lava.
	 *
	 * @return true, if is player in lava
	 */
	public boolean isPlayerInLava(){return playerInLava > 0;}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#beginContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	//Called when two fixtures start to collide
	@Override
	public void beginContact(Contact contact) {
	
		Fixture fa = contact.getFixtureA();		
		Fixture fb = contact.getFixtureB();//foot
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			
			if(fb.getUserData() != null && fb.getUserData().equals("ground")){
				
				playerOnGround++;
				GameScreen.player.jumpCount = GameScreen.player.totalJumps;
			}
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			
			if(fa.getUserData() != null && fa.getUserData().equals("ground")){
				
				playerOnGround++;
				GameScreen.player.jumpCount = GameScreen.player.totalJumps;
			}
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("lava")){

			if(fb.getBody().getUserData() instanceof Player){
				
				playerInLava++;
			}
			else{
				
				((Monster)fb.getBody().getUserData()).isInLava ++;
			}
		}

		if(fb.getUserData() != null && fb.getUserData().equals("lava")){

			if(fa.getBody().getUserData() instanceof Player){
				
				playerInLava++;
			}
			else{
				
				((Monster)fa.getBody().getUserData()).isInLava ++;
			}
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("launcher")){
				
			fb.getBody().applyLinearImpulse(new Vector2(0,8.7f), fb.getBody().getPosition(), true);
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("launcher")){
				
			fa.getBody().applyLinearImpulse(new Vector2(0, 8.7f), fa.getBody().getPosition(), true);
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("portal")){
			
			GameScreen.teleporter.isTouched = true;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("portal")){
			
			GameScreen.teleporter.isTouched = true;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("chest")){
				
			((Chest)fa.getBody().getUserData()).isTouched = true;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("chest")){
			
			((Chest)fb.getBody().getUserData()).isTouched = true;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("ladder")){

			playerOnLadder++;
		}
		else if(fa.getUserData() != null && fa.getUserData().equals("ladder")){
			
			playerOnLadder++;
		}
	
		
		if(fa.getUserData() != null && fa.getUserData().equals("mfoot")){
			
			if(fb.getUserData() != null && fb.getUserData().equals("ground")){
				
				((Monster)fa.getBody().getUserData()).onGround++;
			}
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("mfoot")){
	
			if(fa.getUserData() != null && fa.getUserData().equals("ground")){
	
				((Monster)fb.getBody().getUserData()).onGround++;
			}
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("mwall")){
			
			((Monster)fa.getBody().getUserData()).onWall++;
		}
	
		if(fb.getUserData() != null && fb.getUserData().equals("mwall")){
		
			((Monster)fb.getBody().getUserData()).onWall++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("mjump")){
			
			if(fb.getUserData() != null && fb.getUserData().equals("ground")){
				
				((Monster)fa.getBody().getUserData()).canHurdle--;
			}
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("mjump")){
			
			if(fa.getUserData() != null && fa.getUserData().equals("ground")){
				
				((Monster)fb.getBody().getUserData()).canHurdle--;
			}
		}
		
		if(fa.getUserData() != null & ((String) fa.getUserData()).contains("item")){
			
			
			bodyToRemove.add(fa.getBody());
			((Item)fa.getBody().getUserData()).getItem();
			GameScreen.transitionItems.add((Item)fa.getBody().getUserData());
		}
		
		if(fb.getUserData() != null & ((String) fb.getUserData()).contains("item")){
			
			bodyToRemove.add(fb.getBody());
			((Item)fb.getBody().getUserData()).getItem();
			GameScreen.transitionItems.add((Item)fb.getBody().getUserData());
		}
	
		if(fa.getUserData() != null && ((String) fa.getUserData()).contains("attack")){
			
			if(fa.getBody().getPosition().x < fb.getBody().getPosition().x){
				
				fb.getBody().applyLinearImpulse(new Vector2(1f, 0f), fb.getBody().getPosition(), true);
			}else{
				
				fb.getBody().applyLinearImpulse(new Vector2(-1f, 0f), fb.getBody().getPosition(), true);
			}
			
			float damage = Float.parseFloat(((String) fa.getUserData()).substring(((String) fa.getUserData()).indexOf(':') + 1, ((String) fa.getUserData()).length()));
	
			GameScreen.player.health -= damage;
	
			((String) fa.getUserData()).replaceAll(Float.toString(damage), "0.0");
		}
		
		if(fb.getUserData() != null && ((String) fb.getUserData()).contains("attack")){
			
			if(fb.getBody().getPosition().x < fa.getBody().getPosition().x){
				
				fa.getBody().applyLinearImpulse(new Vector2(1f, 0f), fa.getBody().getPosition(), true);
			}else{
				
				fa.getBody().applyLinearImpulse(new Vector2(-1f, 0f), fa.getBody().getPosition(), true);
			}
			
			float damage = Float.parseFloat(((String) fb.getUserData()).substring(((String) fb.getUserData()).indexOf(':') + 1, ((String) fb.getUserData()).length()));
	
			GameScreen.player.health -= damage;
	
			((String) fb.getUserData()).replaceAll(Float.toString(damage), "0.0");
		}
		
		if(fa.getUserData() != null && ((String)fa.getUserData()).contains("ray")){
			
			if(fb.getUserData() != null && fb.getUserData().equals("ground"))
				bodyToRemove.add(fa.getBody());
			
			if(fb.getUserData() != null && ((String)fb.getUserData()).contains("monster")){
	
				Monster m = (Monster)fb.getBody().getUserData();
					
				float damage = Float.parseFloat(((String) fa.getUserData()).substring(((String) fa.getUserData()).indexOf(':') + 1, ((String) fa.getUserData()).length()));
				
				
				if(m.isMarked)
					damage *= 2;
				
				m.health -= damage;
				
				float leech = GameScreen.player.HealthLeech * damage;
				
				if(GameScreen.player.health < GameScreen.player.maxHealth){
					
					if((GameScreen.player.maxHealth - GameScreen.player.health) > leech){
						GameScreen.player.health += leech;
					}
					else{
						GameScreen.player.health = GameScreen.player.maxHealth;
					}
				}
				
				if((fa.getBody().getLinearVelocity().x) < 0)
					fb.getBody().applyLinearImpulse(new Vector2(-4f, 0f), fb.getBody().getPosition(), true);
				if((fa.getBody().getLinearVelocity().x) > 0)
					fb.getBody().applyLinearImpulse(new Vector2(4f, 0f), fb.getBody().getPosition(), true);
			}
		}
		
		if(fb.getUserData() != null && ((String)fb.getUserData()).contains("ray")){
			
			if(fa.getUserData() != null && fa.getUserData().equals("ground"))
				bodyToRemove.add(fb.getBody());
			if(fa.getUserData() != null && ((String)fa.getUserData()).contains("monster")){
					
				Monster m = (Monster)fa.getBody().getUserData();
				
				float damage = Float.parseFloat(((String) fb.getUserData()).substring(((String) fb.getUserData()).indexOf(':') + 1, ((String) fb.getUserData()).length()));

				if(m.isMarked)
					damage *= 2;
				
				m.health -= damage;

				float leech = GameScreen.player.HealthLeech * damage;
				
				if(GameScreen.player.health < GameScreen.player.maxHealth){
					
					if((GameScreen.player.maxHealth - GameScreen.player.health) > leech){
						GameScreen.player.health += leech;
					}
					else{
						GameScreen.player.health = GameScreen.player.maxHealth;
					}
				}
				
				if ((fb.getBody().getLinearVelocity().x) < 0)
					fa.getBody().applyLinearImpulse(new Vector2(-4f, 0f), fa.getBody().getPosition(), true);
				if((fb.getBody().getLinearVelocity().x) > 0)
					fa.getBody().applyLinearImpulse(new Vector2(4f, 0f), fa.getBody().getPosition(), true);
			}		
		}
		
		if(fa.getUserData() != null && ((String) fa.getUserData()).contains("bullet")){
	
			bodyToRemove.add(fa.getBody());
			
			if(fb.getUserData() != null && ((String) fb.getUserData()).contains("monster")){
				
				Monster m = (Monster)fb.getBody().getUserData();
				
				float damage = Float.parseFloat(((String) fa.getUserData()).substring(((String) fa.getUserData()).indexOf(':') + 1, ((String) fa.getUserData()).length()));
					
				if(m.isMarked)
					damage *= 2;
				
				m.health -= damage;

				float leech = GameScreen.player.HealthLeech * damage;
				
				if(GameScreen.player.health < GameScreen.player.maxHealth){
					
					if((GameScreen.player.maxHealth - GameScreen.player.health) > leech){
						GameScreen.player.health += leech;
					}
					else{
						GameScreen.player.health = GameScreen.player.maxHealth;
					}
				}
				
				if(Math.random() < GameScreen.player.knockbackChance){
					if ((fb.getBody().getLinearVelocity().x) < 0)
						fa.getBody().applyLinearImpulse(new Vector2(-3f, 0f), fa.getBody().getPosition(), true);
					if((fb.getBody().getLinearVelocity().x) > 0)
						fa.getBody().applyLinearImpulse(new Vector2(3f, 0f), fa.getBody().getPosition(), true);		
				}
			}
		}
		
		if(fb.getUserData() != null && ((String) fb.getUserData()).contains("bullet")){
	
			bodyToRemove.add(fb.getBody());
	
			if(fa.getUserData() != null && ((String) fa.getUserData()).contains("monster")){
				
				Monster m = (Monster)fa.getBody().getUserData();
				
				float damage = Float.parseFloat(((String) fb.getUserData()).substring(((String) fb.getUserData()).indexOf(':') + 1, ((String) fb.getUserData()).length()));
				
				if(m.isMarked)
					damage *= 2;
				
				m.health -= damage;

				float leech = GameScreen.player.HealthLeech * damage;
				
				if(GameScreen.player.health < GameScreen.player.maxHealth){
					if((GameScreen.player.maxHealth - GameScreen.player.health) > leech){
						GameScreen.player.health += leech;
					}else{
						GameScreen.player.health = GameScreen.player.maxHealth;
					}
				}
				
				if(Math.random() < GameScreen.player.knockbackChance){
					if ((fb.getBody().getLinearVelocity().x) < 0)
						fa.getBody().applyLinearImpulse(new Vector2(-3f, 0f), fa.getBody().getPosition(), true);
					if((fb.getBody().getLinearVelocity().x) > 0)
						fa.getBody().applyLinearImpulse(new Vector2(3f, 0f), fa.getBody().getPosition(), true);		
				}
			}
		}
		
		if(fa.getUserData() != null && ((String) fa.getUserData()).contains("mine")){

			if(fb.getUserData() != null && fb.getUserData().equals("ground")){

				fa.getBody().setGravityScale(0);
				fa.getBody().setLinearVelocity(new Vector2(0, 0));
				
				filter = fa.getFilterData();
				filter.maskBits = GameScreen.BIT_MONSTER;
				fa.setFilterData(filter);
			}else{
				
				bodyToRemove.add(fa.getBody());
				
				GameScreen.removeMines.add((Mine) fa.getBody().getUserData()); 
					
				Monster m = (Monster)fb.getBody().getUserData();
				
				float damage = Float.parseFloat(((String) fa.getUserData()).substring(((String) fa.getUserData()).indexOf(':') + 1, ((String) fa.getUserData()).length()));
					
				m.health -= damage;
				
				if (fb.getBody().getPosition().x < fa.getBody().getPosition().x){
					fb.getBody().applyLinearImpulse(new Vector2(-3f, 0f), fb.getBody().getPosition(), true);
				}else{
					fb.getBody().applyLinearImpulse(new Vector2(3f, 0f), fb.getBody().getPosition(), true);	
				}
			}
		}
		
		if(fb.getUserData() != null && ((String) fb.getUserData()).contains("mine")){
			
			if(fa.getUserData() != null && fa.getUserData().equals("ground")){

				fb.getBody().setGravityScale(0);
				fb.getBody().setLinearVelocity(new Vector2(0, 0));
				
				filter = fb.getFilterData();
				filter.maskBits = GameScreen.BIT_MONSTER;
				fb.setFilterData(filter);
			}else{

				bodyToRemove.add(fb.getBody());

				GameScreen.removeMines.add((Mine) fb.getBody().getUserData()); 
				
				Monster m = (Monster)fa.getBody().getUserData();
				
				float damage = Float.parseFloat(((String) fb.getUserData()).substring(((String) fb.getUserData()).indexOf(':') + 1, ((String) fb.getUserData()).length()));
					
				m.health -= damage;
				
				if (fa.getBody().getPosition().x < fb.getBody().getPosition().x){
					fa.getBody().applyLinearImpulse(new Vector2(-3f, 0f), fa.getBody().getPosition(), true);
				}else{
					fa.getBody().applyLinearImpulse(new Vector2(3f, 0f), fa.getBody().getPosition(), true);	
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
		
		if(fa.getUserData() != null && fa.getUserData().equals("portal")){
			
			GameScreen.teleporter.isTouched = false;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("portal")){
			
			GameScreen.teleporter.isTouched = false;
		}

		if(fa.getUserData() != null && fa.getUserData().equals("lava")){

			if(fb.getBody().getUserData() instanceof Player){
				
				playerInLava--;
			}
			else{
				
				((Monster)fb.getBody().getUserData()).isInLava --;
			}
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("lava")){

			if(fa.getBody().getUserData() instanceof Player){
				
				playerInLava--;
			}			
			else{
				
				((Monster)fa.getBody().getUserData()).isInLava --;
			}
		}		
		
		if(fa.getUserData() != null && fa.getUserData().equals("chest")){
			
			
			((Chest)fa.getBody().getUserData()).isTouched = false;
		}
	
		if(fb.getUserData() != null && fb.getUserData().equals("chest")){
			
			((Chest)fb.getBody().getUserData()).isTouched = false;
		}
				
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			
			if(fb.getUserData() != null && fb.getUserData().equals("ground"))
				playerOnGround--;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
		
			if(fa.getUserData() != null && fa.getUserData().equals("ground"))
				playerOnGround--;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("ladder")){
		
			playerOnLadder--;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("ladder")){
		
			playerOnLadder--;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("mfoot")){
	
			if(fb.getUserData() != null && fb.getUserData().equals("ground")){
				
				((Monster)fa.getBody().getUserData()).onGround--;
			}

		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("mfoot")){
	
			if(fa.getUserData() != null && fa.getUserData().equals("ground")){
				
				((Monster)fb.getBody().getUserData()).onGround--;
			}
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("mwall")){
			
			((Monster)fa.getBody().getUserData()).onWall++;
		}
	
		if(fb.getUserData() != null && fb.getUserData().equals("mwall")){
			
			
			((Monster)fb.getBody().getUserData()).onWall++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("mjump")){
			
			if(fb.getUserData() != null && fb.getUserData().equals("ground")){
				
				((Monster)fa.getBody().getUserData()).canHurdle++;
			}
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("mjump")){
			
			if(fb.getUserData() != null && fb.getUserData().equals("ground")){
				
				((Monster)fb.getBody().getUserData()).canHurdle++;
			}
		}
	}

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
