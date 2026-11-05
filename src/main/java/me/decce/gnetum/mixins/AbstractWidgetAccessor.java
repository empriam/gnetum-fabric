package me.decce.gnetum.mixins;

import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// Minecraft 1.20.1 has no AbstractWidget#setHeight; this writes the height field directly.
@Mixin(AbstractWidget.class)
public interface AbstractWidgetAccessor {
    @Accessor("height")
    void gnetum$setHeight(int height);
}
