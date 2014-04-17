package net.gummycraft.eeh;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;


public class EggHunt extends JavaPlugin {
	static JavaPlugin plugin;
	static BukkitTask dropTask = null;
	
	@Override
    public void onEnable() {
		plugin = this;
		ConfMan.readConf( this );
		new EggListener( this );
    }	
	
	@Override
	public void onDisable() {
		EasterEgg.end();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ( sender.isOp() == false && sender.hasPermission("egghunt.run") == false )
			return(false);
		
		if ( args.length < 1 ) {
			sender.sendMessage("try /egghunt < start | stop | reload >");
		} else if ( args[0].equalsIgnoreCase("start")) {
			if ( EggListener.isActive() ) {
				sender.sendMessage("An Easter Egg hunt is already taking place");
			} else {
				ConfMan.startMsg();
				int cnt = ConfMan.startCount;
				for ( int  i = 0; i < cnt; i ++) {
					new EasterEgg();
				}		
				EggListener.setActive(true);
				dropTask = new RunnableEggHunt(this).runTaskTimer(this, 20, 20);
				sender.sendMessage("You started the hunt");
			}
		} else if ( args[0].equalsIgnoreCase("stop")) {
			if ( EggListener.isActive() ) {
				dropTask.cancel();
				ConfMan.gameOverMsg();
				EasterEgg.end();
				sender.sendMessage("Everything should be stopped");
			} else {
				sender.sendMessage("There is no Easter Egg hunt running, how can I stop?");
			}
		} else if ( args[0].equalsIgnoreCase("reload")) {
			if ( EggListener.isActive() ) {
				sender.sendMessage("Don't do this while an Easter Egg hunt is running!");
			} else {
				ConfMan.readConf( plugin );
				sender.sendMessage("Configuration is reloaded");
			}
		} else {
			sender.sendMessage("try /egghunt < start | stop | reload >");
		}
		return(true);
	}
}
