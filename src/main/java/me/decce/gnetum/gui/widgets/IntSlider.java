package me.decce.gnetum.gui.widgets;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

// Replacement for Forge's ForgeSlider: an integer slider built on the vanilla AbstractSliderButton.
public class IntSlider extends AbstractSliderButton {
    private final Supplier<String> text;
    private final Supplier<String> specialText;
    private final Consumer<Integer> setter;
    private final Predicate<Integer> special;
    private final int minValue;
    private final int maxValue;
    private final int step;

    public IntSlider(int x, int y, int width, int height, Supplier<String> text, int minValue, int maxValue, int currentValue, boolean drawString, Consumer<Integer> setter) {
        this(x, y, width, height, text, minValue, maxValue, currentValue, 1, drawString, setter);
    }

    public IntSlider(int x, int y, int width, int height, Supplier<String> text, int minValue, int maxValue, int currentValue, int step, boolean drawString, Consumer<Integer> setter) {
        this(x, y, width, height, text, minValue, maxValue, currentValue, step, drawString, setter, null, null);
    }

    public IntSlider(int x, int y, int width, int height, Supplier<String> text, int minValue, int maxValue, int currentValue, int step, boolean drawString, Consumer<Integer> setter, Predicate<Integer> special, Supplier<String> specialText) {
        super(x, y, width, height, Component.empty(), toSliderValue(currentValue, minValue, maxValue));
        this.text = text;
        this.setter = setter;
        this.special = special;
        this.specialText = specialText;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = Math.max(1, step);
        this.updateMessage();
    }

    private static double toSliderValue(int value, int min, int max) {
        return max == min ? 0.0 : (double) (value - min) / (max - min);
    }

    public int getValueInt() {
        double raw = minValue + this.value * (maxValue - minValue);
        int stepped = minValue + (int) Math.round((raw - minValue) / step) * step;
        return Math.max(minValue, Math.min(maxValue, stepped));
    }

    @Override
    protected void applyValue() {
        int v = getValueInt();
        this.value = toSliderValue(v, minValue, maxValue); // snap the knob to the step
        this.setter.accept(v);
    }

    @Override
    protected void updateMessage() {
        if (text == null) return; // called from the superclass constructor path before fields are set
        int v = getValueInt();
        if (special != null && special.test(v) && specialText != null) {
            this.setMessage(Component.literal(String.format(specialText.get(), v)));
        }
        else {
            this.setMessage(Component.literal(String.format(text.get(), v)));
        }
    }
}
