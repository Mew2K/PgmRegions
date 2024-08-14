package org.m2k;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.m2k.commands.RegionCommand;
import org.m2k.commands.WandCommand;
import org.m2k.selection.DefaultSelectionManager;
import org.m2k.selection.SelectionManager;
import org.m2k.selection.WorldEditSelectionManager;

public class PgmRegions extends JavaPlugin implements Listener {
    private boolean useWorldEditConfig = true;

    @Override
    public void onEnable() {
        // Load configuration
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        useWorldEditConfig = config.getBoolean("use-worldedit", true);
        SelectionManager selectionManager = chooseSelectionManager();

        // Register events and commands
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("pwand").setExecutor(new WandCommand(selectionManager));
        this.getCommand("pregion").setExecutor(new RegionCommand(selectionManager));
    }

    private SelectionManager chooseSelectionManager() {
        Plugin wePlugin = Bukkit.getPluginManager().getPlugin("WorldEdit");

        // Use WorldEdit selection system if plugin is found and config enables it.
        if (wePlugin instanceof WorldEditPlugin && useWorldEditConfig) {
            getLogger().info("WorldEdit detected and enabled");
            return new WorldEditSelectionManager();
        }

        // Determine cause for not using WorldEdit selection system.
        if (!useWorldEditConfig) {
            getLogger().info("WorldEdit has been disabled in the config. Using built-in region selection");
        }
        else {
            getLogger().info("WorldEdit not found. Using built-in region selection");
        }

        // Fall-back to built-in selection system.
        return new DefaultSelectionManager(this);
    }
}