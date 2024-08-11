package org.m2k;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PgmRegions extends JavaPlugin implements Listener {
    private final Map<UUID, Location> point1Selections = new HashMap<>();
    private final Map<UUID, Location> point2Selections = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        this.getCommand("pwand").setExecutor(new CommandHandler(this));
        this.getCommand("pregion").setExecutor(new CommandHandler(this));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Check if the player is in creative mode and holding a stone axe
        if (player.isOp() && player.getGameMode() == org.bukkit.GameMode.CREATIVE && player.getItemInHand().getType() == Material.STONE_AXE) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                // Set Point 1
                point1Selections.put(player.getUniqueId(), event.getClickedBlock().getLocation());
                sendSelectionMessage(player, "First position set to", point1Selections.get(player.getUniqueId()));
                event.setCancelled(true); // Prevent block breaking

            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                // Set Point 2
                point2Selections.put(player.getUniqueId(), event.getClickedBlock().getLocation());
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

    private void sendSelectionMessage(Player player, String action, Location point) {
        String message = action + " " + formatLocation(point);

        Location point1 = point1Selections.get(player.getUniqueId());
        Location point2 = point2Selections.get(player.getUniqueId());

        if (point1 != null && point2 != null) {
            int blockCount = calculateBlockCount(point1, point2);
            message += " (" + blockCount + ")";
        }

        player.sendMessage(ChatColor.DARK_AQUA + message + ".");
    }

    private String formatLocation(Location loc) {
        return String.format("(%d, %d, %d)", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    private int calculateBlockCount(Location point1, Location point2) {
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

    public Location getPoint1(Player player) {
        return point1Selections.get(player.getUniqueId());
    }

    public Location getPoint2(Player player) {
        return point2Selections.get(player.getUniqueId());
    }
}