package co.killionrevival.REPLACE_ME;

import org.bukkit.plugin.java.JavaPlugin;

import co.killionrevival.killioncommons.KillionUtilities;
import co.killionrevival.killioncommons.util.console.ConsoleUtil;

public class Plugin extends JavaPlugin {
    private final String pluginName = "REPLACE_ME";
    private static JavaPlugin plugin;
    private static KillionUtilities killionUtilities;
    private static ConsoleUtil logger;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        plugin = this;
        killionUtilities = new KillionUtilities(this);
        logger = killionUtilities.getConsoleUtil();
        logger.sendSuccess(this.pluginName + " has been enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logger.sendSuccess(this.pluginName + " has been disabled.");
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static ConsoleUtil getMyLogger() {
        return logger;
    }
}