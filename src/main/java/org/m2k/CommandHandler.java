package org.m2k;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CommandHandler implements CommandExecutor {

    private final PgmRegions plugin;
    private final PgmCoordinateConverter converter;

    public CommandHandler(PgmRegions plugin) {
        this.plugin = plugin;
        this.converter = new PgmCoordinateConverter();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("pwand")
                || command.getName().equalsIgnoreCase("/pwand")) {
                if (!plugin.shouldUseWorldEdit()){
                    if (player.isOp()) {
                        // Give the player a stone axe and display the instructions
                        player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
                        player.sendMessage(ChatColor.DARK_AQUA + "Left click: select pos #1; Right click: select pos #2" + ChatColor.RESET);
                    } else {
                        player.sendMessage(ChatColor.RED + "You must be opped to use this command." + ChatColor.RESET);
                    }

                } else {
                    player.sendMessage(ChatColor.RED + "WorldEdit is enabled. Use WorldEdit's wand to select regions." + ChatColor.RESET);
                }
                return true;
            }

            if (command.getName().equalsIgnoreCase("pregion") || command.getName().equalsIgnoreCase("pr")) {
                Vector[] points;

                if (plugin.isWorldEditEnabled()) {
                    points = plugin.getWorldEditSelection(player);
                    if (points == null) {
                        player.sendMessage(ChatColor.RED + "You must select both points with WorldEdit before using this command." + ChatColor.RESET);
                        return true;
                    }
                } else {
                    Vector point1 = plugin.getPoint1(player);
                    Vector point2 = plugin.getPoint2(player);

                    if (point1 != null && point2 != null) {
                        points = new Vector[]{point1, point2};
                    } else {
                        player.sendMessage(ChatColor.RED + "You must select both points before using this command." + ChatColor.RESET);
                        return true;
                    }
                }

                // Convert the coordinates using the PGMCoordinateConverter
                Vector[] pgmCoordinates = converter.convertCoordinates(points[0], points[1]);
                Vector minPoint = pgmCoordinates[0];
                Vector maxPoint = pgmCoordinates[1];

                // Create the coordinate text
                String coordinateText = "min=\\\"" + plugin.formatLocation(minPoint) + "\\\" max=\\\"" + plugin.formatLocation(maxPoint) + "\\\"";

                // Build the JSON message with hover and click events
                String jsonMessage = "{\"text\":\"" + coordinateText + "\","
                            + "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click here to copy!\"},"
                            + "\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + coordinateText + "\"}}";

                // Send the JSON message
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + jsonMessage);

                return true;
            }
        } else {
            sender.sendMessage("This command can only be run by a player.");
        }

        return false;
    }
}