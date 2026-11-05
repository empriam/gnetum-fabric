package me.decce.gnetum;

import com.mojang.blaze3d.platform.InputConstants;
import me.decce.gnetum.gui.ConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public final class Gnetum implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "gnetum";
    public static final String HAND_ELEMENT = "gnetum:minecraft_hand";
    public static final String OTHER_MODS = "gnetum_unknown";

    public static final FpsCounter FPS_COUNTER = new FpsCounter();
    public static GnetumConfig config;
    public static PassManager passManager;
    public static UncachedElements uncachedElements;
    public static String currentElement;
    public static ElementType currentElementType;

    public static long lastSwapNanos;
    public static boolean rendering;
    public static boolean renderingCanceled;

    private static KeyMapping configMapping;

    @Override
    public void onInitializeClient() {
        passManager = new PassManager();
        uncachedElements = new UncachedElements();
        GnetumConfig.reload();

        configMapping = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "gnetum.config.keyMapping",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_END,
                "key.categories.misc"));

        ClientTickEvents.END_CLIENT_TICK.register(Gnetum::onClientTick);
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> onPlayerJoin());
    }

    public static CacheSetting getCacheSetting(String vanillaOverlay) {
        vanillaOverlay = PackedVanillaElements.consider(vanillaOverlay);
        if (!config.mapVanillaElements.containsKey(vanillaOverlay)) {
            config.mapVanillaElements.put(vanillaOverlay, new CacheSetting(SuggestedPass.get(vanillaOverlay)));
        }
        return Gnetum.config.mapVanillaElements.get(vanillaOverlay);
    }

    public static CacheSetting getCacheSetting(String moddedOverlay, ElementType type) {
        if (type == ElementType.VANILLA) return getCacheSetting(moddedOverlay);
        var map = type == ElementType.PRE ? config.mapModdedElementsPre : config.mapModdedElementsPost;
        if (!map.containsKey(moddedOverlay)) {
            map.put(moddedOverlay, new CacheSetting(type == ElementType.PRE ? 1 : config.numberOfPasses));
        }
        return map.get(moddedOverlay);
    }

    public static void disableCachingForCurrentElement(String reason) {
        if (currentElement == null || currentElementType == null) return;
        CacheSetting cacheSetting = getCacheSetting(currentElement, currentElementType);
        if (cacheSetting.enabled.get() && cacheSetting.enabled.value == me.decce.gnetum.util.AnyBooleanValue.AUTO) {
            LOGGER.info("Disabling caching for element {}. Reason: {}", currentElement, reason);
            cacheSetting.enabled.defaultValue = false;
            FramebufferManager.getInstance().dropCurrentFrame();
        }
    }

    private static void onClientTick(Minecraft mc) {
        while (configMapping.consumeClick()) {
            if (!(mc.screen instanceof ConfigScreen)) {
                mc.setScreen(new ConfigScreen(PerformanceAnalyzer.analyze()));
            }
        }
    }

    private static void onPlayerJoin() {
        FPS_COUNTER.reset();
        FramebufferManager.getInstance().reset();
        PackedVanillaElements.reset();
    }
}
