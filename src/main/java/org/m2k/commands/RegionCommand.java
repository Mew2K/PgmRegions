package org.m2k.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.m2k.PgmCoordinateConverter;
import org.m2k.selection.SelectionManager;

public class RegionCommand implements CommandExecutor {
    private final PgmCoordinateConverter converter;

    private final SelectionManager selectionManager;

    public RegionCommand(SelectionManager selectionManager) {
        this.selectionManager = selectionManager;
        this.converter = new PgmCoordinateConverter();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        Vector[] points = selectionManager.getSelection(player);

        if (points == null) {
            player.sendMessage(ChatColor.RED + "You must select both points before using this command." + ChatColor.RESET);
            return true;
        }

        sendFormattedRegionMessage(converter.convertCoordinates(points[0], points[1]), player);
        return true;
    }

    private void sendFormattedRegionMessage(Vector[] pgmCoordinates, Player player) {
        Vector minPoint = pgmCoordinates[0];
        Vector maxPoint = pgmCoordinates[1];

        // Create the coordinate text
        String coordinateText = "min=\\\"" + selectionManager.formatLocation(minPoint) + "\\\" max=\\\"" + selectionManager.formatLocation(maxPoint) + "\\\"";

        // Build the JSON message with hover and click events
        String jsonMessage = "{\"text\":\"" + coordinateText + "\","
                + "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click here to copy!\"},"
                + "\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + coordinateText + "\"}}";

        // Send the JSON message
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + jsonMessage);
    }
}
