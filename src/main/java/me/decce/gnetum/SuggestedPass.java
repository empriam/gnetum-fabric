package me.decce.gnetum;

public class SuggestedPass {
    // Same grouping as the Forge version, expressed with the element IDs used by this port.
    public static int get(String name) {
        int pass = switch (name) {
            case "minecraft:spyglass", "minecraft:helmet", "minecraft:frostbite", "minecraft:portal", "minecraft:hotbar" -> 1;
            case "minecraft:boss_event_progress", "gnetum:status_bar", "minecraft:jump_bar", "minecraft:experience_bar" -> 2;
            default -> 3;
        };
        if (pass > Gnetum.config.numberOfPasses) pass = Gnetum.config.numberOfPasses;
        return pass;
    }
}
