package net.gummycraft.eeh;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * And yes, I know this is kinda of crappy, but I just rushed moving things that were hard coded for a 
 * one time use plugin to able to use a config file, and since Easter is in a few hours I just moved it
 * this way. Deal with it, it works and won't break, just not elegant.
 */
public class ConfMan {
	static int startCount = 6;
	static int moreToDrop = 60;
	static int interDelay = 1;
	static int despawnTime = 120;
	static boolean broadcastFinds = true;
	static boolean individualFinds = false;
	static String startMessage = null;
	static String lastDropMessage = null;
	static String gameOverMessage = null;
	static World world = null;	
	static int lowX = -1723;
	static int spanX = 148;
	static int lowZ = -155;
	static int spanZ = 180;
	static int setY = 92;
	static List<String> prizeMessages = new ArrayList<String>();
	static List<String> prizeCommands = new ArrayList<String>();
	
	
	static public void startMsg() {
		if ( startMessage != null && startMessage.length() > 1 )
			Bukkit.broadcastMessage( ChatColor.translateAlternateColorCodes('&',  startMessage));
	}
	static public void lastDropMsg() {
		if ( lastDropMessage != null && lastDropMessage.length() > 1 )
			Bukkit.broadcastMessage( ChatColor.translateAlternateColorCodes('&',  lastDropMessage));
	}
	static public void gameOverMsg() {
		if ( gameOverMessage != null && gameOverMessage.length() > 1 )
			Bukkit.broadcastMessage( ChatColor.translateAlternateColorCodes('&',  gameOverMessage));
	}
	
	
	/*
	 * just reads in with sane defaults....if you really fuck up the config things may go real bad
	 * there is almost no bounds checking
	 */
	static public void readConf(JavaPlugin plugin) {
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		FileConfiguration cnf = plugin.getConfig();

		prizeMessages = new ArrayList<String>();
		prizeCommands = new ArrayList<String>();
		
		startCount = cnf.getInt("StartCount", 6);
		moreToDrop = cnf.getInt("MoreToDrop", 60);
		interDelay = cnf.getInt("InterDelay", 60);
		despawnTime = cnf.getInt("DespawnTime", 120);
		broadcastFinds = cnf.getBoolean("BroadcastFinds", true);
		individualFinds = cnf.getBoolean("IndividualFinds", false);
		startMessage = cnf.getString("StartMessage", null);
		lastDropMessage = cnf.getString("LastDropMessage", null);
		gameOverMessage = cnf.getString("GameOverMessage", null);

		// get where to drop this crap
		String worldName = cnf.getString("WorldName", null);
		world = null;
		if ( worldName != null ) {
			world = Bukkit.getWorld(worldName);
		}
		if ( world == null ) 
			world = Bukkit.getWorlds().get(0);
		
		int x1 = cnf.getInt("Corner1x");
		int x2 = cnf.getInt("Corner2x");
		int z1 = cnf.getInt("Corner1z");
		int z2 = cnf.getInt("Corner2z");
		lowX = Math.min(x1,  x2);
		spanX = Math.max(x2, x2) - lowX;
		lowZ = Math.min(z1,  z2);
		spanZ = Math.max(z2, z2) - lowZ;
		setY = cnf.getInt("DropYLevel", 100);
		
		// read in all the prizes
		if ( cnf.getString("prizes") != null ) {
			ConfigurationSection prizes = cnf.getConfigurationSection("prizes");
			if ( prizes != null ) {
				Set<String> pzList = prizes.getKeys(false);
				if ( pzList != null && pzList.size() > 0 ) {
					for ( String pzName : pzList ) {
						ConfigurationSection onePz = prizes.getConfigurationSection(pzName);
						String msg = onePz.getString("msg", null);
						String cmd = onePz.getString("cmd", null);
						if ( msg != null && cmd != null ) {
							addPrize(msg, cmd);
						}
					}
				}
			}
		}
		
		// if nothing got loaded, just load some crap to give away
		if ( prizeMessages.isEmpty() ) {
			addPrize("a piece of dirt", "give {pname} dirt 1");
			addPrize("a piece of cobble", "give {pname} cobble 1");
			addPrize("a carrot", "give {pname} carrot_item 1");
			addPrize("some xp", "xp 25 {pname}");
		}
		return;			
	}
	
	
	static void addPrize(String msg, String cmd) {
		prizeMessages.add(msg);
		prizeCommands.add(cmd);
	}
	static int getNumOfPrizes() {
		return( prizeMessages.size() );
	}
	static String getPrizeMsg(int num) {
		return( prizeMessages.get(num) );
	}
	static String getPrizeCmd(int num) {
		return( prizeCommands.get(num) );
	}
}
