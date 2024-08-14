package org.m2k.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.m2k.selection.SelectionManager;
import org.m2k.selection.WorldEditSelectionManager;

public class WandCommand implements CommandExecutor {

    private final SelectionManager selectionManager;

    public WandCommand(SelectionManager selectionManager) {
        this.selectionManager = selectionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only allow players to run this command.
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        // Inform player that they should use WorldEdit's wand if WorldEdit is enabled.
        if (selectionManager instanceof WorldEditSelectionManager) {
            player.sendMessage(ChatColor.RED + "WorldEdit is enabled. Use WorldEdit's wand to select regions." + ChatColor.RESET);
            return true;
        }

        // Give player the wand.
        player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
        player.sendMessage(ChatColor.DARK_AQUA + "Left click: select pos #1; Right click: select pos #2" + ChatColor.RESET);
        return true;
    }
}
