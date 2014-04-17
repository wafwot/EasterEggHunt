package net.gummycraft.eeh;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class EggListener implements Listener {
	static boolean active = false;
	
	EggListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	private void pickSomthingUp(PlayerPickupItemEvent event) {
		if ( !active )
			return;
		Item item = event.getItem();
		if ( EasterEgg.isThisMine( item, event.getPlayer() ) ) {
			event.setCancelled(true);
			item.remove();
		}
	}
	 
	static public void setActive(boolean setting) {
		active = setting;
	}
	static public boolean isActive() {
		return(active);
	}
	
}
