package me.decce.gnetum.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.decce.gnetum.Gnetum;
import me.decce.gnetum.GnetumConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

// Adds the "HUD: x fps" line to the F3 overlay (replaces Forge's CustomizeGuiOverlayEvent.DebugText).
@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {
    @ModifyReturnValue(method = "getGameInformation", at = @At("RETURN"), require = 0)
    private List<String> gnetum$addHudFps(List<String> original) {
        if (!Gnetum.config.isEnabled() || !Gnetum.config.showHudFps.get() || !Minecraft.getInstance().options.renderDebug || original.size() <= 2) {
            return original;
        }
        List<String> lines = new ArrayList<>(original);
        lines.add(2, String.format("HUD: %d fps (%d passes, max %s)",
                Gnetum.FPS_COUNTER.getFps(),
                Gnetum.config.numberOfPasses,
                Gnetum.config.getMaxFps() == GnetumConfig.UNLIMITED_FPS ? "unlimited" : Gnetum.config.getMaxFps()));
        return lines;
    }
}
