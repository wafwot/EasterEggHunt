package net.gummycraft.eeh;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EasterEgg {
	int			pzNum;
	Item		item = null;
	static List<EasterEgg> eggList = new ArrayList<EasterEgg>();
	
	
	EasterEgg() {
		short num = getIDNum();
		
		pzNum = getPrizeNumber();
		Location loc = getDropLoc();
		int eggType = getEggType();
		ItemStack is = new ItemStack(383, 1, (short) num, (byte) eggType );
		this.item = loc.getWorld().dropItem(loc, is);
		eggList.add( this );
	}
	

	public static boolean isThisMine(Item checkItem, Player p) {
		for ( EasterEgg ee : eggList ) {
			if ( ee.item.equals( checkItem ) ) {
				String msg = ConfMan.getPrizeMsg(ee.pzNum);
				if ( ConfMan.broadcastFinds )
					Bukkit.broadcastMessage( c() + "Winner! " + c() + p.getName() + c() + " got " + msg);
				if ( ConfMan.individualFinds )
					p.sendMessage( c() + "you got " + c() + msg );
				
				msg = ConfMan.getPrizeCmd(ee.pzNum).replace("{pname}",  p.getName() );
				Bukkit.dispatchCommand( Bukkit.getConsoleSender(), msg );
				Bukkit.broadcastMessage("<" + msg + ">");
				// since we just remove it then leave the loop, concurrent modification won't happen
				eggList.remove(ee);
				return(true);
			}
		}
		return(false);
	}

	
	// removes any entities that are left
	static public void end() {
		for ( EasterEgg ee : eggList ) {
			ee.item.remove();
		}
		eggList = new ArrayList<EasterEgg>();
		EggListener.setActive(false);
	}
	
	
	// this finds a number that is not being used...mainly to keep spigot from stacking prizes
	static private short getIDNum() {
		return( (short) (Math.random() * 32000) );
	}

	// this gets a prize number
	static private int getPrizeNumber() {
		return( (int) (Math.random() * ConfMan.getNumOfPrizes() ));
	}

	// gets an egg type for this easter egg
	static int nums[] = { 50,51,52,53,54,55,56,57,58,59,60,61,62,64,65,66,90,91,92,93,94,95,96,98,100,120 }; 
	static private int getEggType() {
		int id = nums[ (int) (Math.random() * nums.length) ];
		return(id);
	}
	
	
	World world = Bukkit.getWorlds().get(0);
	private Location getDropLoc() {
		
		// the watchDog is just to "help" people when they mess up defining a rectangle.
		// it will return NPE, and that's fine. At least it doesn't halt the minecraft main thread, right?
		int watchDog = 0;
		do {
			int randomX = (int) ( (Math.random() * ConfMan.spanX) + ConfMan.lowX);
			int randomZ = (int) ( (Math.random() * ConfMan.spanZ) + ConfMan.lowZ);
				
			Location l = new Location(world, randomX, ConfMan.setY, randomZ);
			Block b = l.getBlock(); 
			// just be sure this is air...and that it will land on something decent
			
			if ( b.getType() == Material.AIR ) {
				while ( b.getType() == Material.AIR ) {
					b = b.getRelative(BlockFace.DOWN);
				}
				if ( isSuitable(b.getType())) {
					return(l);
				}
			}
		} while ( ++watchDog < 20 );		
		return(null);
	}
	
	
	// gets a random color string
	static String colors[] = { ChatColor.AQUA.toString(), ChatColor.GREEN.toString(), ChatColor.LIGHT_PURPLE.toString(), ChatColor.YELLOW.toString() };
	private static String c() {
		int id = (int) (Math.random() * colors.length);
		return(colors[id]);
	}
	
	
	// checks if material os good for an egg to land on
	static private boolean isSuitable(Material mat) {
		switch ( mat ) {
			case CROPS:
			case DEAD_BUSH:				
			case GRASS:
			case DIRT:
			case FENCE:
			case FENCE_GATE:
			case FLOWER_POT:
			case GRAVEL:
			case HAY_BLOCK:
			case IRON_FENCE:
			case LEAVES:
			case LEAVES_2:
			case LONG_GRASS:
			case NETHER_FENCE:
			case NETHER_STALK:
			case NETHER_WARTS:
			case RED_MUSHROOM:
			case BROWN_MUSHROOM:
			case RED_ROSE:
			case SAND:
			case SAPLING:
			case SKULL_ITEM:
			case SOIL:
			case SUGAR_CANE:
			case WATER_LILY:
			case YELLOW_FLOWER:
			case DOUBLE_PLANT:
				return(true);
			default:
				break;
		}
		return(false);
	}
}
