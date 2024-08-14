package org.m2k.selection;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

// Manages selections using WorldEdit's selection system.
public class WorldEditSelectionManager extends SelectionManager {

    private final WorldEditPlugin wePlugin;

    public WorldEditSelectionManager() {
        wePlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
    }

    @Override
    public Vector[] getSelection(Player player) {
        Selection selection = wePlugin.getSelection(player);

        if (selection == null) {
            return null;
        }

        Vector point1 = selection.getMinimumPoint().toVector();
        Vector point2 = selection.getMaximumPoint().toVector();
        return new Vector[]{point1, point2};
    }
}
