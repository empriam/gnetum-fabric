package me.decce.gnetum.mixins;

import me.decce.gnetum.HudEngine;
import me.decce.gnetum.PoseStackHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks the vanilla HUD. Frame hooks wrap Gui.render; element hooks wrap each vanilla HUD element.
 * Element hooks use require = 0: a name that does not exist in this Minecraft version is ignored and the
 * element is drawn every frame, as in vanilla. The engine logs which elements were hooked (see HudEngine).
 */
@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;F)V", at = @At("HEAD"))
    private void gnetum$frameHead(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        PoseStackHelper.beginHudRendering(guiGraphics.pose());
        HudEngine.beginFrame(guiGraphics);
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;F)V", at = @At("RETURN"))
    private void gnetum$frameReturn(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        HudEngine.endFrame();
        PoseStackHelper.endHudRendering(guiGraphics.pose());
    }

    @Inject(method = "renderVignette", at = @At("HEAD"), cancellable = true, require = 0)
    private void gnetum$head_vignette_0(CallbackInfo ci) {
        if (HudEngine.onElementHead("minecraft:vignette")) ci.cancel();
    }

    @Inject(method = "renderVignette", at = @At("RETURN"), require = 0)
    private void gnetum$return_vignette_0(CallbackInfo ci) {
        HudEngine.onElementReturn();
    }

    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true, require = 0)
    private void gnetum$head_spyglass_0(CallbackInfo ci) {
        if (HudEngine.onElementHead("minecraft:spyglass")) ci.cancel();
    }

    @Inject(method = "renderSpyglassOverlay", at = @At("RETURN"), require = 0)
    private void gnetum$return_spyglass_0(CallbackInfo ci) {
        HudEngine.onElementReturn();
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true, require = 0)
    private void gnetum$head_portal_0(CallbackInfo ci) {
        if (HudEngine.onElementHead("minecraft:portal")) ci.cancel();
    }

    @Inject(method = "renderPortalOverlay", at = @At("RETURN"), require = 0)
    private void gnetum$return_portal_0(CallbackInfo ci) {
        HudEngine.onElementReturn();
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true, require = 0)
    private void gnetum$head_crosshair_0(CallbackInfo ci) {
        if (HudEngine.onElementHead("minecraft:crosshair")) ci.cancel();
    }

    @Inject(method = "renderCrosshair", at = @At("RETURN"), require = 0)
    private void gnetum$return_crosshair_0(CallbackInfo ci) {
        HudEngine.onElementReturn();
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true, require = 0)
    private void gnetum$head_hotbar_1(CallbackInfo ci) {
        if (HudEngine.onElementHead("minecraft:hotbar")) ci.cancel();
    }

    @Inject(method = "renderHotbar", at = @At("RETURN"), require = 0)
    private void gnetum$return_hotbar_1(CallbackInfo ci) {
        HudEngine.onElementReturn();
    }

    @Inject(method = "renderPlayerHealth", at = @At("HEAD"), cancellable = true, require = 0)
    private void gnetum$head_player_health_0(CallbackInfo ci) {
        if (HudEngine.onElementHead("minecraft:player_health")) ci.cancel();
    }

    @Inject(method = "renderPlayerHealth", at = @At("RETURN"), require = 0)
    private void gnetum$return_player_health_0(CallbackInfo ci) {
        HudEngine.onElementReturn();
    }

    @Inject(method = "renderVehicleHealth", at = @At("HEAD"), cancellable = true, require = 0)
    private void gnetum$head_mount_health_0(CallbackInfo ci) {
        if (HudEngine.onElementHead("minecraft:mount_health")) ci.cancel();
    }

    @Inject(method = "renderVehicleHealth", at = @At("RETURN"), require = 0)
    private void gnetum$return_mount_health_0(CallbackInfo ci) {
        HudEngine.onElementReturn();
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true, require = 0)
    private void gnetum$head_experience_bar_0(CallbackInfo ci) {
        if (HudEngine.onElementHead("minecraft:experience_bar")) ci.cancel();
    }

    @Inject(method = "renderExperienceBar", at = @At("RETURN"), require = 0)
    private void gnetum$return_experience_bar_0(CallbackInfo ci) {
        HudEngine.onElementReturn();
    }

    @Inject(method = "renderSelectedItemName", at = @At("HEAD"), cancellable = true, require = 0)
    private void gnetum$head_item_name_0(CallbackInfo ci) {
        if (HudEngine.onElementHead("minecraft:item_name")) ci.cancel();
    }

    @Inject(method = "renderSelectedItemName", at = @At("RETURN"), require = 0)
    private void gnetum$return_item_name_0(CallbackInfo ci) {
        HudEngine.onElementReturn();
    }

    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true, require = 0)
    private void gnetum$head_potion_icons_0(CallbackInfo ci) {
        if (HudEngine.onElementHead("minecraft:potion_icons")) ci.cancel();
    }

    @Inject(method = "renderEffects", at = @At("RETURN"), require = 0)
    private void gnetum$return_potion_icons_0(CallbackInfo ci) {
        HudEngine.onElementReturn();
    }

}
