package org.m2k.selection;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class SelectionManager {
    public abstract Vector[] getSelection(Player player);

    public String formatLocation(Vector loc) {
        return String.format("%d, %d, %d", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public int calculateBlockCount(Vector point1, Vector point2) {
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
}
