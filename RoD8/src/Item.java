import java.util.Scanner;

public class Item {
	
	public static class pItem {
		String itemName;
		
		int count = 0;
		int healthChange = 0;
		int armourChange = 0;
		int speedChange = 0;
		double gravityMult = 1;
		int damageChange = 0;
		int areaDamageChange = 0;
		int jumpIncrease = 0;
		int jumpExtra = 0;
		boolean makeInvisible = false;
	}
	
	public static class aItem {
		String itemName;
		
		aItem next;
		
		public aItem(String itemname) {
			itemName = itemname;
			switch(itemname) {
			
			}
		}
	}
	
	public static class iItem {
		String itemName;
		int count = 1;
		//Image image;
		
		iItem next;
		
		public iItem(String itemname) {
			itemName = itemname;
			switch(itemname) {
			
			}
		}
	}
	
	public static class charStats {
		int health = 100;
		int armour = 10;
		int speed = 50;
		double gravity = 10;
		int damage = 25;
		int areaDamage = 0;
		int jumpStr = 20;
		int jumpCount = 1;
		boolean visible = false;
	}
	
	private static aItem activeRootNode;
	private static iItem imageRootNode;
	private static charStats stat = new charStats();		
	
	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		String itemname;
		iItem runner1;
		aItem runner2;

		LOOP: while(true){
			
			itemname = in.nextLine();
			if(itemname.equals("exit")) break LOOP;
			
			newItem(itemname);
			
			runner1 = imageRootNode;
			while(runner1 != null){
				System.out.println(runner1.itemName + "(x" + runner1.count + ")");
				runner1 = runner1.next;
			}
			
			runner2 = activeRootNode;
			while(runner2 != null){
				System.out.println(runner2.itemName);
				runner2 = runner2.next;
			}
			
			System.out.println();
			
			System.out.println("health: " + stat.health);
			System.out.println("armour: " + stat.armour);
			System.out.println("speed: " + stat.speed);
			System.out.println("gravity: " + stat.gravity);
			System.out.println("damage: " + stat.damage);
			System.out.println("areaDamage: " + stat.areaDamage);
			System.out.println("jumpStr: " + stat.jumpStr);
			System.out.println("jumpCount: " + stat.jumpCount);
			System.out.println("visible: " + stat.visible);
		}
		
		in.close();
	}
	
	public static String[] getActiveItems(){
		int count = 0;
		
		if(activeRootNode == null){
			return new String[0];
		}else{
			
			aItem runner = activeRootNode;
			while(runner != null){
				count++;
				runner = runner.next;
			}
			
			String[] items = new String[count];
			count = 0;
			
			runner = activeRootNode;
			while(runner.next != null){
				items[count] = runner.itemName;
				count++;
				runner = runner.next;
			}
			
			return items;
		}
	}
	
	public static void newItem(String itemName){
		pItem pBuild = new pItem();
		pBuild.itemName = itemName;
		imageAdd(new iItem(itemName));
		
		switch (itemName) {
		case "apple":
			pBuild.count = 1;
			pBuild.healthChange = 20;
			break;
			
		case "shield":
			pBuild.count = 1;
			pBuild.armourChange = 5;
			break;
			
		case "boots":
			pBuild.count = 1;
			pBuild.speedChange = 20;
			break;
			
		case "balloon":
			pBuild.count = 1;
			pBuild.gravityMult = 0.75;
			break;
			
		case "spring":
			pBuild.count = 1;
			pBuild.jumpIncrease = 5;
			break;
			
		case "wings":
			pBuild.count = 1;
			pBuild.jumpExtra = 1;
			break;
			
		}
		
		if(pBuild.count != 0) {
			updateStats(pBuild);
			return;
		}else{
			itemAdd(new aItem(itemName));
		}
	}
	
	private static void itemAdd(aItem insertion){
		aItem runner;
		
		if(activeRootNode == null){
			activeRootNode = insertion;
			return;
		}
		
		runner = activeRootNode;
		
		while(true){
			if(runner.next == null){
				runner.next = insertion;
				return;
			}else{
				if(insertion.itemName.equals(runner.itemName)){
					insertion.next = runner.next;
					runner.next = insertion;
					return;
				}else{
					runner = runner.next;
				}
			}
			
		} // end while
	}
	
	private static void imageAdd(iItem insertion){
		iItem runner;

		if(imageRootNode == null){
			imageRootNode = insertion;
			return;
		}

		runner = imageRootNode;

		while(true){
			if(insertion.itemName.equals(runner.itemName)){
				runner.count = runner.count + 1;
				return;
			}
			if(runner.next != null){
				runner = runner.next;
			}else{
				runner.next = insertion;
				return;
			}
			
		} // end while
	}
	
	private static void updateStats(pItem newItem){
		stat.health += newItem.healthChange;
		stat.armour += newItem.armourChange;
		stat.speed += newItem.count * newItem.speedChange;
		stat.gravity *= Math.pow(newItem.gravityMult, newItem.count);
		stat.damage += newItem.count * newItem.damageChange;
		stat.areaDamage += newItem.count * newItem.areaDamageChange;
		stat.jumpStr += newItem.count * newItem.jumpIncrease;
		stat.jumpCount += newItem.count * newItem.jumpExtra;
		if(stat.visible){
			stat.visible = !(newItem.makeInvisible);
		}
	}
}

