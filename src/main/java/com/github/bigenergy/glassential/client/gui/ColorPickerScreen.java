package com.github.bigenergy.glassential.client.gui;

import com.github.bigenergy.glassential.network.ColorUpdatePacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

// temp isn't used
public class ColorPickerScreen extends Screen {
    private final BlockPos blockPos;
    private int selectedColor = 0xFFFFFF;

    // Color picker dimensions
    private static final int PICKER_SIZE = 256;
    private static final int HUE_BAR_WIDTH = 20;
    private static final int PADDING = 10;

    private int pickerX;
    private int pickerY;
    private int hueBarX;

    private float hue = 0.0f;
    private float saturation = 1.0f;
    private float brightness = 1.0f;

    public ColorPickerScreen(BlockPos blockPos, int currentColor) {
        super(Component.literal("Color Picker"));
        this.blockPos = blockPos;
        this.selectedColor = currentColor;

        // Convert RGB to HSB
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

        int totalWidth = PICKER_SIZE + PADDING + HUE_BAR_WIDTH;
        pickerX = (this.width - totalWidth) / 2;
        pickerY = (this.height - PICKER_SIZE) / 2;
        hueBarX = pickerX + PICKER_SIZE + PADDING;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        // Render color picker square
        renderColorPicker(guiGraphics);

        // Render hue bar
        renderHueBar(guiGraphics);

        // Render color preview and hex value
        renderColorInfo(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderColorPicker(GuiGraphics guiGraphics) {
        Matrix4f matrix = guiGraphics.pose().last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        // Render in strips for better performance
        int step = 4; // Render every 4th pixel for performance
        for (int y = 0; y < PICKER_SIZE; y += step) {
            for (int x = 0; x < PICKER_SIZE; x += step) {
                float s = (float) x / PICKER_SIZE;
                float b = 1.0f - ((float) y / PICKER_SIZE);
                int color = hsbToRgb(hue, s, b);

                int r = (color >> 16) & 0xFF;
                int g = (color >> 8) & 0xFF;
                int bl = color & 0xFF;

                float x1 = pickerX + x;
                float y1 = pickerY + y;
                float x2 = x1 + step;
                float y2 = y1 + step;

                bufferBuilder.addVertex(matrix, x1, y2, 0).setColor(r, g, bl, 255);
                bufferBuilder.addVertex(matrix, x2, y2, 0).setColor(r, g, bl, 255);
                bufferBuilder.addVertex(matrix, x2, y1, 0).setColor(r, g, bl, 255);
                bufferBuilder.addVertex(matrix, x1, y1, 0).setColor(r, g, bl, 255);
            }
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
        RenderSystem.disableBlend();

        // Draw selection indicator
        int indicatorX = pickerX + (int) (saturation * PICKER_SIZE);
        int indicatorY = pickerY + (int) ((1.0f - brightness) * PICKER_SIZE);
        guiGraphics.fill(indicatorX - 3, indicatorY - 3, indicatorX + 3, indicatorY + 3, 0xFFFFFFFF);
        guiGraphics.fill(indicatorX - 2, indicatorY - 2, indicatorX + 2, indicatorY + 2, 0xFF000000);
    }

    private void renderHueBar(GuiGraphics guiGraphics) {
        Matrix4f matrix = guiGraphics.pose().last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        int step = 2;
        for (int y = 0; y < PICKER_SIZE; y += step) {
            float h = (float) y / PICKER_SIZE;
            int color = hsbToRgb(h, 1.0f, 1.0f);

            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int bl = color & 0xFF;

            float y1 = pickerY + y;
            float y2 = y1 + step;

            bufferBuilder.addVertex(matrix, hueBarX, y2, 0).setColor(r, g, bl, 255);
            bufferBuilder.addVertex(matrix, hueBarX + HUE_BAR_WIDTH, y2, 0).setColor(r, g, bl, 255);
            bufferBuilder.addVertex(matrix, hueBarX + HUE_BAR_WIDTH, y1, 0).setColor(r, g, bl, 255);
            bufferBuilder.addVertex(matrix, hueBarX, y1, 0).setColor(r, g, bl, 255);
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
        RenderSystem.disableBlend();

        // Draw selection indicator
        int indicatorY = pickerY + (int) (hue * PICKER_SIZE);
        guiGraphics.fill(hueBarX - 2, indicatorY - 2, hueBarX + HUE_BAR_WIDTH + 2, indicatorY + 2, 0xFFFFFFFF);
    }

    private void renderColorInfo(GuiGraphics guiGraphics) {
        int previewSize = 40;
        int previewX = pickerX;
        int previewY = pickerY + PICKER_SIZE + 10;

        // Color preview
        guiGraphics.fill(previewX, previewY, previewX + previewSize, previewY + previewSize, 0xFF000000 | selectedColor);

        // Hex value
        String hexValue = String.format("#%06X", selectedColor);
        guiGraphics.drawString(this.font, hexValue, previewX + previewSize + 10, previewY + 15, 0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            // Check if clicked on picker
            if (mouseX >= pickerX && mouseX < pickerX + PICKER_SIZE &&
                mouseY >= pickerY && mouseY < pickerY + PICKER_SIZE) {
                updateColorFromPicker((int) mouseX, (int) mouseY);
                return true;
            }

            // Check if clicked on hue bar
            if (mouseX >= hueBarX && mouseX < hueBarX + HUE_BAR_WIDTH &&
                mouseY >= pickerY && mouseY < pickerY + PICKER_SIZE) {
                updateHue((int) mouseY);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) {
            // Dragging on picker
            if (mouseX >= pickerX && mouseX < pickerX + PICKER_SIZE &&
                mouseY >= pickerY && mouseY < pickerY + PICKER_SIZE) {
                updateColorFromPicker((int) mouseX, (int) mouseY);
                return true;
            }

            // Dragging on hue bar
            if (mouseX >= hueBarX && mouseX < hueBarX + HUE_BAR_WIDTH &&
                mouseY >= pickerY && mouseY < pickerY + PICKER_SIZE) {
                updateHue((int) mouseY);
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

    private void updateHue(int mouseY) {
        hue = Math.max(0, Math.min(1, (float) (mouseY - pickerY) / PICKER_SIZE));
        selectedColor = hsbToRgb(hue, saturation, brightness);
    }

    @Override
    public void onClose() {
        // Send packet to server with selected color
        PacketDistributor.sendToServer(new ColorUpdatePacket(blockPos, selectedColor));
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Don't render default blurred background
        // Just render a simple transparent overlay
        guiGraphics.fill(0, 0, this.width, this.height, 0x80000000);
    }

    // Helper methods for color conversion
    private static int hsbToRgb(float hue, float saturation, float brightness) {
        int rgb = java.awt.Color.HSBtoRGB(hue, saturation, brightness);
        return rgb & 0xFFFFFF;
    }

    private static float[] rgbToHsb(int r, int g, int b) {
        return java.awt.Color.RGBtoHSB(r, g, b, null);
    }
}
