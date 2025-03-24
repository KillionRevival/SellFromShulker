package co.killionrevival.sellfromshulker;

import org.bukkit.plugin.java.JavaPlugin;

import co.killionrevival.killioncommons.KillionUtilities;
import co.killionrevival.killioncommons.util.console.ConsoleUtil;
import lombok.Getter;

public class SellFromShulker extends JavaPlugin {
    private final String pluginName = "SellFromShulker";

    @Getter
    private static JavaPlugin plugin;
    @Getter
    private static KillionUtilities killionUtilities;
    @Getter
    private static ConsoleUtil myLogger;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        plugin = this;
        killionUtilities = new KillionUtilities(this);
        myLogger = killionUtilities.getConsoleUtil();
        myLogger.sendSuccess(this.pluginName + " has been enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        myLogger.sendSuccess(this.pluginName + " has been disabled.");
    }
}