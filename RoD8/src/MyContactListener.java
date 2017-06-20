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
	
	public HashSet<Body> bodyToRemove;

	/** The player on ground. */
	private int playerOnGround;
	
	private int playerOnLadder;
	
	/** The bodies to remove. */
	private Array<Body> bodiesToRemove;
	/**
	 * Instantiates a new my contact listener.
	 */
	public MyContactListener(){
		
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
	
	public HashSet<Body> getBodyToRemove(){return bodyToRemove;}
	
	/**
	 * Checks if is player on ground.
	 *
	 * @return true, if is player on ground
	 */
	public boolean isPlayerOnGround(){return playerOnGround > 0; }
	
	public boolean isPlayerOnLadder(){return playerOnLadder > 0;}
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#beginContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	//Called when two fixtures start to collide
	@Override
	public void beginContact(Contact contact) {
	
		Fixture fa = contact.getFixtureA();		
		Fixture fb = contact.getFixtureB();//foot
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			
			if(fb.getUserData() != null && fb.getUserData().equals("ground"))
				playerOnGround++;
		
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			
			if(fa.getUserData() != null && fa.getUserData().equals("ground"))
				playerOnGround++;
	
		}
		
		
		if(fa.getUserData() != null && fa.getUserData().equals("portal")){
			
			GameScreen.teleporter.isTouched = true;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("portal")){
			
			GameScreen.teleporter.isTouched = true;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("chest")){
						
			for(Chest chest : GameScreen.chests){
				
				if(chest.getBody() == fa.getBody()){
					
					chest.isTouched = true;
				}
			}
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("chest")){
			
			for(Chest chest : GameScreen.chests){
				
				if(chest.getBody() == fb.getBody()){
					
					chest.isTouched = true;
				}
			}
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("ladder"))
			playerOnLadder++;
		
		else if(fa.getUserData() != null && fa.getUserData().equals("ladder"))
			playerOnLadder++;
		
	
		
		if(fa.getUserData() != null && fa.getUserData().equals("mfoot")){
			
			if(fb.getUserData() != null && fb.getUserData().equals("ground")){
				
				for(Monster m : GameScreen.monsterList){
					
					if(m.identifier == Float.parseFloat(((String) fa.getBody().getFixtureList().first().getUserData()).substring(((String) fa.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fa.getBody().getFixtureList().first().getUserData()).length()))){
						
						m.onGround++;
					}
				}
			}
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("mfoot")){
	
			if(fa.getUserData() != null && fa.getUserData().equals("ground")){
	
				for(Monster m : GameScreen.monsterList){
					
					if(m.identifier == Float.parseFloat(((String) fb.getBody().getFixtureList().first().getUserData()).substring(((String) fb.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fb.getBody().getFixtureList().first().getUserData()).length()))){
						
						m.onGround++;
					}
				}
			}
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("mwall")){
			
			for(Monster m : GameScreen.monsterList){
				
				if(m.identifier == Float.parseFloat(((String) fa.getBody().getFixtureList().first().getUserData()).substring(((String) fa.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fa.getBody().getFixtureList().first().getUserData()).length()))){
					
					m.onWall++;
				}
			}
		}
	
		if(fb.getUserData() != null && fb.getUserData().equals("mwall")){
			
			for(Monster m : GameScreen.monsterList){
				
				if(m.identifier == Float.parseFloat(((String) fb.getBody().getFixtureList().first().getUserData()).substring(((String) fb.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fb.getBody().getFixtureList().first().getUserData()).length()))){
					
					m.onWall++;
				}
			}
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("mjump")){
			
			if(fb.getUserData() != null && fb.getUserData().equals("ground")){
				
				for(Monster m : GameScreen.monsterList){
					
					if(m.identifier == Float.parseFloat(((String) fa.getBody().getFixtureList().first().getUserData()).substring(((String) fa.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fa.getBody().getFixtureList().first().getUserData()).length()))){
						
						m.canHurdle--;
					}
				}
			}
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("mjump")){
			
			if(fa.getUserData() != null && fa.getUserData().equals("ground")){
				
				for(Monster m : GameScreen.monsterList){
					
					if(m.identifier == Float.parseFloat(((String) fb.getBody().getFixtureList().first().getUserData()).substring(((String) fb.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fb.getBody().getFixtureList().first().getUserData()).length()))){
						
						m.canHurdle--;
					}
				}
			}
		}
		
		if(fa.getUserData() != null & ((String) fa.getUserData()).contains("item")){
			
			for(Item i : GameScreen.floatingItemList){
				
				if(i.identifier == Float.parseFloat(((String) fa.getBody().getFixtureList().first().getUserData()).substring(((String) fa.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fa.getBody().getFixtureList().first().getUserData()).length()))){
					System.out.println(GameScreen.player.health + ", " + GameScreen.player.maxHealth);
					
					i.getItem();		
					
					System.out.println(GameScreen.player.health + ", " + GameScreen.player.maxHealth);
	
					bodyToRemove.add(i.getBody());
					GameScreen.removeItems.add(i);
				}
			}
		}
		
		if(fb.getUserData() != null & ((String) fb.getUserData()).contains("item")){
			
			for(Item i : GameScreen.floatingItemList){
				
				if(i.identifier == Float.parseFloat(((String) fb.getBody().getFixtureList().first().getUserData()).substring(((String) fb.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fb.getBody().getFixtureList().first().getUserData()).length()))){
					System.out.println(GameScreen.player.health + ", " + GameScreen.player.maxHealth);
					
					i.getItem();
					
					System.out.println(GameScreen.player.health + ", " + GameScreen.player.maxHealth);
					
					bodyToRemove.add(i.getBody());
					GameScreen.removeItems.add(i);
				}
			}
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
					
					float damage = Float.parseFloat(((String) fa.getUserData()).substring(((String) fa.getUserData()).indexOf(':') + 1, ((String) fa.getUserData()).length()));
					
					if (m.identifier == Float.parseFloat(((String) fb.getUserData()).substring(((String) fb.getUserData()).indexOf(':') + 1, ((String) fb.getUserData()).length()))){
	
						m.health -= damage;
						
						if((fa.getBody().getLinearVelocity().x) < 0)
							fb.getBody().applyLinearImpulse(new Vector2(-4f, 0f), fb.getBody().getPosition(), true);
						if((fa.getBody().getLinearVelocity().x) > 0)
							fb.getBody().applyLinearImpulse(new Vector2(4f, 0f), fb.getBody().getPosition(), true);
						
						if(m.health <= 0){
							
							bodyToRemove.add(fb.getBody());
							m.killed = true;
							GameScreen.removeMobs.add(m);	
							GameScreen.player.money += 100;
	
						}
					}
				}	
			}
		}
		
		if(fb.getUserData() != null && ((String)fb.getUserData()).contains("ray")){
			
			if(fa.getUserData() != null && fa.getUserData().equals("ground"))
				bodyToRemove.add(fb.getBody());
			if(fa.getUserData() != null && ((String)fa.getUserData()).contains("monster")){
				
				for(Monster m : GameScreen.monsterList){
					
					float damage = Float.parseFloat(((String) fb.getUserData()).substring(((String) fb.getUserData()).indexOf(':') + 1, ((String) fb.getUserData()).length()));
					
					if (m.identifier == Float.parseFloat(((String) fa.getUserData()).substring(((String) fa.getUserData()).indexOf(':') + 1, ((String) fa.getUserData()).length()))){
	
						m.health -= damage;
						
						if ((fb.getBody().getLinearVelocity().x) <0)
							fa.getBody().applyLinearImpulse(new Vector2(-4f, 0f), fa.getBody().getPosition(), true);
						if((fb.getBody().getLinearVelocity().x) > 0)
							fa.getBody().applyLinearImpulse(new Vector2(4f, 0f), fa.getBody().getPosition(), true);					
					}
	
					if (m.health <= 0){
						
						bodyToRemove.add(fa.getBody());
						m.killed = true;
						GameScreen.removeMobs.add(m);	
						GameScreen.player.money += 100;
					
					}
				}	
			}		
		}
		
		if(fa.getUserData() != null && ((String) fa.getUserData()).contains("bullet")){
	
			bodyToRemove.add(fa.getBody());
			
			if(fb.getUserData() != null && ((String) fb.getUserData()).contains("monster")){
				
				for(Monster m : GameScreen.monsterList){
	
					float damage = Float.parseFloat(((String) fa.getUserData()).substring(((String) fa.getUserData()).indexOf(':') + 1, ((String) fa.getUserData()).length()));
					
					if (m.identifier == Float.parseFloat(((String) fb.getUserData()).substring(((String) fb.getUserData()).indexOf(':') + 1, ((String) fb.getUserData()).length()))){
						
						m.health -= damage;
						
						if(m.health <= 0){
							
							bodyToRemove.add(fb.getBody());
							m.killed = true;
							GameScreen.removeMobs.add(m);
							GameScreen.player.money += 100;
	
						}
					}
				}
			}
		}
		
		if(fb.getUserData() != null && ((String) fb.getUserData()).contains("bullet")){
	
			bodyToRemove.add(fb.getBody());
	
			if(fa.getUserData() != null && ((String) fa.getUserData()).contains("monster")){
				
				for(Monster m : GameScreen.monsterList){
				
					float damage = Float.parseFloat(((String) fb.getUserData()).substring(((String) fb.getUserData()).indexOf(':') + 1, ((String) fb.getUserData()).length()));
					
					
					if (m.identifier == Float.parseFloat(((String) fa.getUserData()).substring(((String) fa.getUserData()).indexOf(':') + 1, ((String) fa.getUserData()).length()))){
						
						m.health -= damage;
						
						if(m.health <= 0){
							
							bodyToRemove.add(fa.getBody());
							m.killed = true;
							GameScreen.removeMobs.add(m);
							GameScreen.player.money += 100;
	
						}
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
		
		if(fa.getUserData() != null && fa.getUserData().equals("portal")){
			
			GameScreen.teleporter.isTouched = false;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("portal")){
			
			GameScreen.teleporter.isTouched = false;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("chest")){
			
			for(Chest chest : GameScreen.chests){
				
				if(chest.getBody() == fa.getBody()){
					
					chest.isTouched = false;
				}
			}
		}
	
		if(fb.getUserData() != null && fb.getUserData().equals("chest")){
			
			for(Chest chest : GameScreen.chests){
				
				if(chest.getBody() == fb.getBody()){
					
					chest.isTouched = false;
				}
			}
		}
				
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			
			if(fb.getUserData() != null && fb.getUserData().equals("ground"))
				playerOnGround--;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
		
			if(fa.getUserData() != null && fa.getUserData().equals("ground"))
				playerOnGround--;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("ladder"))
			playerOnLadder--;
		
		if(fb.getUserData() != null && fb.getUserData().equals("ladder"))
			playerOnLadder--;
		
		if(fa.getUserData() != null && fa.getUserData().equals("mfoot")){
	
			if(fb.getUserData() != null && fb.getUserData().equals("ground")){
	
				for(Monster m : GameScreen.monsterList){
					
					if(m.identifier == Float.parseFloat(((String) fa.getBody().getFixtureList().first().getUserData()).substring(((String) fa.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fa.getBody().getFixtureList().first().getUserData()).length()))){
						
						m.onGround--;
					}
				}
			}
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("mfoot")){
	
			if(fa.getUserData() != null && fa.getUserData().equals("ground")){
	
				for(Monster m : GameScreen.monsterList){
					
					if(m.identifier == Float.parseFloat(((String) fb.getBody().getFixtureList().first().getUserData()).substring(((String) fb.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fb.getBody().getFixtureList().first().getUserData()).length()))){
						
						m.onGround--;
					}
				}
			}
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("mwall")){
			
			for(Monster m : GameScreen.monsterList){
				
				if(m.identifier == Float.parseFloat(((String) fa.getBody().getFixtureList().first().getUserData()).substring(((String) fa.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fa.getBody().getFixtureList().first().getUserData()).length()))){
					
					m.onWall--;
				}
			}
		}
	
		if(fb.getUserData() != null && fb.getUserData().equals("mwall")){
			
			for(Monster m : GameScreen.monsterList){
				
				if(m.identifier == Float.parseFloat(((String) fb.getBody().getFixtureList().first().getUserData()).substring(((String) fb.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fb.getBody().getFixtureList().first().getUserData()).length()))){
					
					m.onWall--;
				}
			}
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("mjump")){
			
			if(fb.getUserData() != null && fb.getUserData().equals("ground")){
				
				for(Monster m : GameScreen.monsterList){
					
					if(m.identifier == Float.parseFloat(((String) fa.getBody().getFixtureList().first().getUserData()).substring(((String) fa.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fa.getBody().getFixtureList().first().getUserData()).length()))){
						
						m.canHurdle++;
					}
				}
			}
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("mjump")){
			
			if(fa.getUserData() != null && fa.getUserData().equals("ground")){
				
				for(Monster m : GameScreen.monsterList){
					
					if(m.identifier == Float.parseFloat(((String) fb.getBody().getFixtureList().first().getUserData()).substring(((String) fb.getBody().getFixtureList().first().getUserData()).indexOf(':') + 1, ((String) fb.getBody().getFixtureList().first().getUserData()).length()))){
						
						m.canHurdle++;
					}
				}
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
