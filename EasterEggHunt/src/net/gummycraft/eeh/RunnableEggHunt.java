package net.gummycraft.eeh;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RunnableEggHunt extends BukkitRunnable {
	private final JavaPlugin plugin;
	int toDrop;
	int secondsBetween;
	int despawnTime;
	
	public RunnableEggHunt(JavaPlugin plugin) {
		this.plugin = plugin;
		this.toDrop = ConfMan.moreToDrop;
		this.secondsBetween = ConfMan.interDelay;
		this.despawnTime = ConfMan.despawnTime;
	}
	
	int loopCounter = 0;
	public void run() {
		loopCounter++;
		if ( toDrop > 0 ) {
			if ( loopCounter == secondsBetween ) {
				toDrop--;
				if ( toDrop == 0 ) {
					ConfMan.lastDropMsg();
				}
				new EasterEgg();
				loopCounter = 0;
				return;
			}
		} else if ( loopCounter == despawnTime ) {
			ConfMan.gameOverMsg();
			EasterEgg.end();
			this.cancel();
		}
	}

}
