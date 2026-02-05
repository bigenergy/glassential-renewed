package com.github.bigenergy.glassential.client.gui;

import com.github.bigenergy.glassential.network.ColorUpdatePacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

// Simplified color picker - custom rendering removed due to API changes in 1.21.4+
public class ColorPickerScreen extends Screen {
    private final BlockPos blockPos;
    private int selectedColor = 0xFFFFFF;

    private static final int PICKER_SIZE = 256;
    private static final int PADDING = 10;

    private int pickerX;
    private int pickerY;

    private float hue = 0.0f;
    private float saturation = 1.0f;
    private float brightness = 1.0f;

    public ColorPickerScreen(BlockPos blockPos, int currentColor) {
        super(Component.literal("Color Picker"));
        this.blockPos = blockPos;
        this.selectedColor = currentColor;

        int r = (currentColor >> 16) & 0xFF;
        int g = (currentColor >> 8) & 0xFF;
        int b = currentColor & 0xFF;
        float[] hsb = rgbToHsb(r, g, b);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }

    @Override
    protected void init() {
        super.init();
        int totalWidth = PICKER_SIZE + PADDING;
        pickerX = (this.width - totalWidth) / 2;
        pickerY = (this.height - PICKER_SIZE) / 2;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Simple background
        guiGraphics.fill(0, 0, this.width, this.height, 0x80000000);

        // Render simplified color picker using fill rectangles
        int step = 8;
        for (int y = 0; y < PICKER_SIZE; y += step) {
            for (int x = 0; x < PICKER_SIZE; x += step) {
                float s = (float) x / PICKER_SIZE;
                float b = 1.0f - ((float) y / PICKER_SIZE);
                int color = hsbToRgb(hue, s, b);
                guiGraphics.fill(pickerX + x, pickerY + y, pickerX + x + step, pickerY + y + step, 0xFF000000 | color);
            }
        }

        // Draw selection indicator
        int indicatorX = pickerX + (int) (saturation * PICKER_SIZE);
        int indicatorY = pickerY + (int) ((1.0f - brightness) * PICKER_SIZE);
        guiGraphics.fill(indicatorX - 3, indicatorY - 3, indicatorX + 3, indicatorY + 3, 0xFFFFFFFF);
        guiGraphics.fill(indicatorX - 2, indicatorY - 2, indicatorX + 2, indicatorY + 2, 0xFF000000);

        // Color preview
        int previewX = pickerX;
        int previewY = pickerY + PICKER_SIZE + 10;
        guiGraphics.fill(previewX, previewY, previewX + 40, previewY + 40, 0xFF000000 | selectedColor);

        // Hex value
        String hexValue = String.format("#%06X", selectedColor);
        guiGraphics.drawString(this.font, hexValue, previewX + 50, previewY + 15, 0xFFFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (mouseX >= pickerX && mouseX < pickerX + PICKER_SIZE &&
                mouseY >= pickerY && mouseY < pickerY + PICKER_SIZE) {
                updateColorFromPicker((int) mouseX, (int) mouseY);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) {
            if (mouseX >= pickerX && mouseX < pickerX + PICKER_SIZE &&
                mouseY >= pickerY && mouseY < pickerY + PICKER_SIZE) {
                updateColorFromPicker((int) mouseX, (int) mouseY);
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private void updateColorFromPicker(int mouseX, int mouseY) {
        saturation = Math.max(0, Math.min(1, (float) (mouseX - pickerX) / PICKER_SIZE));
        brightness = Math.max(0, Math.min(1, 1.0f - (float) (mouseY - pickerY) / PICKER_SIZE));
        selectedColor = hsbToRgb(hue, saturation, brightness);
    }

    @Override
    public void onClose() {
        // Send packet to server with selected color
        ClientPacketDistributor.sendToServer(new ColorUpdatePacket(blockPos, selectedColor));
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static int hsbToRgb(float hue, float saturation, float brightness) {
        int rgb = java.awt.Color.HSBtoRGB(hue, saturation, brightness);
        return rgb & 0xFFFFFF;
    }

    private static float[] rgbToHsb(int r, int g, int b) {
        return java.awt.Color.RGBtoHSB(r, g, b, null);
    }
}
