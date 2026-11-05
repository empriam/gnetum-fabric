package me.decce.gnetum;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PackedVanillaElements {
    public static final String PACKED_STATUS_BAR = "gnetum:status_bar";
    // Elements that the Forge version packed into a single cache entry (the status bar row)
    private static final List<String> PACKED = List.of(
            "minecraft:player_health", "minecraft:armor_level", "minecraft:food_level",
            "minecraft:mount_health", "minecraft:air_level");
    public static Set<String> set;

    public static void init() {
        set = new HashSet<>(PACKED);
    }

    public static String consider(String element) {
        if (set == null) {
            init();
        }
        return set.contains(element) ? PACKED_STATUS_BAR : element;
    }

    public static void reset() {
        set = null;
    }
}
