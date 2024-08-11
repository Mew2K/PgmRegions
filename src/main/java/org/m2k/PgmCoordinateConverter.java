package org.m2k;

import org.bukkit.Location;

public class PgmCoordinateConverter {

    // The minimum location of a cuboid selection is always the bottom-most
    // and northwestern-most block position.
    public Location getMinimumLocation(Location point1, Location point2) {
        int minX = Math.min(point1.getBlockX(), point2.getBlockX());
        int minY = Math.min(point1.getBlockY(), point2.getBlockY());
        int minZ = Math.min(point1.getBlockZ(), point2.getBlockZ());

        return new Location(point1.getWorld(), minX, minY, minZ);
    }

    // The maximum location of a cuboid selection is always the top-most
    // and southeastern-most block position.
    public Location getMaximumLocation(Location point1, Location point2) {
        int maxX = Math.max(point1.getBlockX(), point2.getBlockX());
        int maxY = Math.max(point1.getBlockY(), point2.getBlockY());
        int maxZ = Math.max(point1.getBlockZ(), point2.getBlockZ());

        return new Location(point1.getWorld(), maxX, maxY, maxZ);
    }

    public Location[] convertCoordinates(Location point1, Location point2) {
        Location minLocation = getMinimumLocation(point1, point2);
        Location maxLocation = getMaximumLocation(point1, point2);
        maxLocation.add(1, 1, 1);
        return new Location[]{minLocation, maxLocation};
    }
}