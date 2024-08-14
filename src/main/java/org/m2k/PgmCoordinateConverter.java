package org.m2k;

import org.bukkit.util.Vector;

public class PgmCoordinateConverter {

    // The minimum location of a cuboid selection is always the bottom-most
    // and northwestern-most block position.
    public Vector getMinimumLocation(Vector point1, Vector point2) {
        int minX = Math.min(point1.getBlockX(), point2.getBlockX());
        int minY = Math.min(point1.getBlockY(), point2.getBlockY());
        int minZ = Math.min(point1.getBlockZ(), point2.getBlockZ());
        return new Vector(minX, minY, minZ);
    }

    // The maximum location of a cuboid selection is always the top-most
    // and southeastern-most block position.
    public Vector getMaximumLocation(Vector point1, Vector point2) {
        int maxX = Math.max(point1.getBlockX(), point2.getBlockX());
        int maxY = Math.max(point1.getBlockY(), point2.getBlockY());
        int maxZ = Math.max(point1.getBlockZ(), point2.getBlockZ());
        return new Vector(maxX, maxY, maxZ);
    }

    public Vector[] convertCoordinates(Vector point1, Vector point2) {
        Vector minLocation = getMinimumLocation(point1, point2);
        Vector maxLocation = getMaximumLocation(point1, point2);
        maxLocation.add(new Vector(1, 1, 1));
        return new Vector[]{minLocation, maxLocation};
    }
}