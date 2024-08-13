package org.m2k;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PgmRegions extends JavaPlugin implements Listener {

    private boolean isWorldEditEnabled = false;
    private boolean useWorldEdit = true;

    private final Map<UUID, Vector> point1Selections = new HashMap<>();
    private final Map<UUID, Vector> point2Selections = new HashMap<>();

    @Override
    public void onEnable() {
        // Load configuration
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        useWorldEdit = config.getBoolean("use-worldedit", true);
        getLogger().info("use-worldedit: " + useWorldEdit);

        // Check if WorldEdit is installed and "use-worldedit" config is true
        Plugin wePlugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (wePlugin instanceof WorldEditPlugin && useWorldEdit) {
            isWorldEditEnabled = true;
            getLogger().info("WorldEdit detected and enabled.");
        } else {
            isWorldEditEnabled = false;
            if (!useWorldEdit) {
                getLogger().info("WorldEdit has been disabled in the config. Using built-in region selection.");
            }
            else {
                getLogger().info("WorldEdit not found. Using built-in region selection.");
            }
        }

        // Send warning if use-worldedit config is true, but WorldEdit is not installed.
        if (!isWorldEditEnabled && useWorldEdit) {
            getLogger().warning("WorldEdit is not installed, defaulting to built-in selection system.");
        }

        // Register events and commands
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("pwand").setExecutor(new CommandHandler(this));
        this.getCommand("pregion").setExecutor(new CommandHandler(this));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Check if the player is in creative mode and holding a stone axe
        if (!shouldUseWorldEdit() && player.isOp() && player.getGameMode() == org.bukkit.GameMode.CREATIVE && player.getItemInHand().getType() == Material.STONE_AXE) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                // Set Point 1
                point1Selections.put(player.getUniqueId(), event.getClickedBlock().getLocation().toVector());
                sendSelectionMessage(player, "First position set to", point1Selections.get(player.getUniqueId()));
                event.setCancelled(true); // Prevent block breaking

            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                // Set Point 2
                point2Selections.put(player.getUniqueId(), event.getClickedBlock().getLocation().toVector());
                sendSelectionMessage(player, "Second position set to", point2Selections.get(player.getUniqueId()));
                event.setCancelled(true); // Prevent block placing
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove the player's selections when they log out
        UUID playerId = event.getPlayer().getUniqueId();
        point1Selections.remove(playerId);
        point2Selections.remove(playerId);
    }

    public boolean isWorldEditEnabled() {
        return isWorldEditEnabled;
    }

    public boolean shouldUseWorldEdit() {
        return useWorldEdit && isWorldEditEnabled;
    }

    private void sendSelectionMessage(Player player, String action, Vector point) {
        String message = action + " (" + formatLocation(point) + ")";

        Vector point1 = point1Selections.get(player.getUniqueId());
        Vector point2 = point2Selections.get(player.getUniqueId());

        if (point1 != null && point2 != null) {
            int blockCount = calculateBlockCount(point1, point2);
            message += " (" + blockCount + ")";
        }

        player.sendMessage(ChatColor.DARK_AQUA + message + ".");
    }

    public String formatLocation(Vector loc) {
        return String.format("%d, %d, %d", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    private int calculateBlockCount(Vector point1, Vector point2) {
        int xMin = Math.min(point1.getBlockX(), point2.getBlockX());
        int xMax = Math.max(point1.getBlockX(), point2.getBlockX());
        int yMin = Math.min(point1.getBlockY(), point2.getBlockY());
        int yMax = Math.max(point1.getBlockY(), point2.getBlockY());
        int zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
        int zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());

        int xSize = xMax - xMin + 1;
        int ySize = yMax - yMin + 1;
        int zSize = zMax - zMin + 1;

        return xSize * ySize * zSize;
    }

    public Vector[] getWorldEditSelection(Player player) {
        if (!isWorldEditEnabled) {
            return null;
        }

        WorldEditPlugin wePlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        Selection selection = wePlugin.getSelection(player);

        if (selection != null) {
            Vector point1 = selection.getMinimumPoint().toVector();
            Vector point2 = selection.getMaximumPoint().toVector();
            return new Vector[]{point1, point2};
        }

        return null;
    }

    public Vector getPoint1(Player player) {
        return point1Selections.get(player.getUniqueId());
    }

    public Vector getPoint2(Player player) {
        return point2Selections.get(player.getUniqueId());
    }
}