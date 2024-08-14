package org.m2k.selection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;
import org.m2k.PgmRegions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Manages selections using the built-in selection system.
public class DefaultSelectionManager extends SelectionManager implements Listener {
    private final Map<UUID, Vector> point1Selections = new HashMap<>();
    private final Map<UUID, Vector> point2Selections = new HashMap<>();

    public DefaultSelectionManager(PgmRegions plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public Vector[] getSelection(Player player) {

        Vector point1 = getPoint1(player);
        Vector point2 = getPoint2(player);

        if (point1 == null || point2 == null) {
            return null;
        }

        return new Vector[]{point1, point2};
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Check if the player is in creative mode and holding a stone axe
        if (!hasAccess(player) || !holdingTool(player)) { return; }

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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove the player's selections when they log out
        UUID playerId = event.getPlayer().getUniqueId();
        point1Selections.remove(playerId);
        point2Selections.remove(playerId);
    }



    private void sendSelectionMessage(Player player, String action, Vector point) {
        String message = action + " (" + formatLocation(point) + ")";

        Vector point1 = point1Selections.get(player.getUniqueId());
        Vector point2 = point2Selections.get(player.getUniqueId());

        // Append the volume of the selection if both points are set.
        if (point1 != null && point2 != null) {
            int blockCount = calculateBlockCount(point1, point2);
            message += " (" + blockCount + ")";
        }

        player.sendMessage(ChatColor.DARK_AQUA + message + ".");
    }


    private boolean hasAccess(Player player) {
        return (player.hasPermission("pgmregions.pwand") || player.isOp()) && player.getGameMode() == GameMode.CREATIVE;
    }

    private boolean holdingTool(Player player) {
        return player.getItemInHand().getType() == Material.STONE_AXE;
    }

    public Vector getPoint1(Player player) {
        return point1Selections.get(player.getUniqueId());
    }

    public Vector getPoint2(Player player) {
        return point2Selections.get(player.getUniqueId());
    }
}
