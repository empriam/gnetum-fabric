package me.decce.gnetum;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Frame engine for the vanilla HUD on Fabric.
 *
 * Each HUD frame: the previously completed HUD (front buffer) is shown first. Then every element is either drawn
 * directly to the screen (uncached), drawn into the back buffer when its pass is due, or skipped because its cached
 * image is still valid. After the last pass the buffers are swapped.
 *
 * Elements are identified by the hooks in GuiMixin. Any element whose hook does not match is drawn uncached, so a
 * mismatch degrades to vanilla behavior. If the engine throws, it disables itself until the game restarts.
 */
public final class HudEngine {
    private static final int HEALTH_CHECK_FRAMES = 300;
    private static final String[] KNOWN_ELEMENTS = {
            "minecraft:vignette", "minecraft:spyglass", "minecraft:portal", "minecraft:crosshair",
            "minecraft:hotbar", "minecraft:player_health", "minecraft:food_level", "minecraft:air_level",
            "minecraft:mount_health", "minecraft:experience_bar", "minecraft:item_name",
            "minecraft:potion_icons", "minecraft:subtitles"
    };

    private static final Set<String> hooked = new HashSet<>();
    private static GuiGraphics graphics;
    private static boolean frameActive;
    private static boolean elementBound;
    private static boolean healthy = true;
    private static int frames;

    private HudEngine() {}

    public static boolean isHealthy() {
        return healthy;
    }

    public static void beginFrame(GuiGraphics guiGraphics) {
        frameActive = false;
        graphics = guiGraphics;
        if (!healthy || !Gnetum.config.isEnabled()) return;
        try {
            frameActive = true;
            FramebufferManager fbm = FramebufferManager.getInstance();
            fbm.ensureSize();
            HudDeltaTracker.update(Minecraft.getInstance());
            fbm.unbind(); // the blit must target the screen, not the back buffer
            fbm.blit();
        } catch (Throwable t) {
            fail("inicio de frame", t);
        }
    }

    public static void endFrame() {
        if (!frameActive) return;
        frameActive = false;
        try {
            Gnetum.passManager.nextPass();
        } catch (Throwable t) {
            fail("fin de frame", t);
        }
        if (++frames == HEALTH_CHECK_FRAMES) checkHealth();
    }

    /** @return true if the element must be skipped this frame because its cached image is still valid. */
    public static boolean onElementHead(String id) {
        if (!frameActive) return false;
        hooked.add(id);
        try {
            PassManager passes = Gnetum.passManager;
            if (passes.cachingDisabled(id)) return false; // drawn directly to the screen every frame
            if (!passes.shouldRender(id)) return true;     // cached, and this element is not due in this pass
            if (graphics != null) graphics.flush();
            passes.begin();
            elementBound = true;
            Gnetum.currentElement = id;
            Gnetum.currentElementType = ElementType.VANILLA;
            Gnetum.rendering = true;
            FramebufferManager.getInstance().bind();
            return false;
        } catch (Throwable t) {
            fail("elemento " + id, t);
            return false;
        }
    }

    public static void onElementReturn() {
        if (!elementBound) return;
        try {
            endElement();
        } catch (Throwable t) {
            fail("fin de elemento", t);
        }
    }

    private static void endElement() {
        elementBound = false;
        if (graphics != null) graphics.flush(); // submit pending vertices while the back buffer is still bound
        Gnetum.rendering = false;
        Gnetum.currentElement = null;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        FramebufferManager.getInstance().unbind();
        Gnetum.passManager.end();
    }

    private static void fail(String where, Throwable t) {
        Gnetum.LOGGER.error("Gnetum: error en {}. La caché del HUD queda desactivada hasta reiniciar el juego.", where, t);
        healthy = false;
        frameActive = false;
        if (elementBound) {
            try {
                endElement();
            } catch (Throwable ignored) {
                elementBound = false;
            }
        }
        Gnetum.rendering = false;
        Gnetum.currentElement = null;
    }

    private static void checkHealth() {
        if (hooked.isEmpty()) {
            healthy = false;
            Gnetum.LOGGER.warn("Gnetum: ningún elemento del HUD fue interceptado. El HUD se dibuja sin caché.");
            return;
        }
        Set<String> missing = new TreeSet<>(Arrays.asList(KNOWN_ELEMENTS));
        missing.removeAll(hooked);
        Gnetum.LOGGER.info("Gnetum: elementos interceptados {}. Sin interceptar {}.", hooked, missing);
    }
}
