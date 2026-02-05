package com.github.bigenergy.glassential.client.gui;

import com.github.bigenergy.glassential.network.ColorUpdatePacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

// Simplified color picker - using buttons instead of custom mouse tracking for 1.21.11 compatibility
public class ColorPickerScreen extends Screen {
    private final BlockPos blockPos;
    private int selectedColor;

    private static final int COLOR_CELL_SIZE = 20;

    private int leftPos;
    private int topPos;

    public ColorPickerScreen(BlockPos blockPos, int currentColor) {
        super(Component.literal("Color Picker"));
        this.blockPos = blockPos;
        this.selectedColor = currentColor;
    }

    @Override
    protected void init() {
        super.init();
        int gridWidth = 6 * (COLOR_CELL_SIZE + 2);
        int gridHeight = 6 * (COLOR_CELL_SIZE + 2);
        leftPos = (this.width - gridWidth) / 2;
        topPos = (this.height - gridHeight - 80) / 2;

        // Create color palette buttons
        int[] colors = {
            0xFF0000, 0xFF7F00, 0xFFFF00, 0x7FFF00, 0x00FF00, 0x00FF7F,
            0x00FFFF, 0x007FFF, 0x0000FF, 0x7F00FF, 0xFF00FF, 0xFF007F,
            0x800000, 0x804000, 0x808000, 0x408000, 0x008000, 0x008040,
            0x008080, 0x004080, 0x000080, 0x400080, 0x800080, 0x800040,
            0xFFFFFF, 0xC0C0C0, 0x808080, 0x404040, 0x202020, 0x000000,
            0xFFC0C0, 0xC0FFC0, 0xC0C0FF, 0xFFFFC0, 0xFFC0FF, 0xC0FFFF
        };

        for (int i = 0; i < colors.length; i++) {
            int row = i / 6;
            int col = i % 6;
            final int color = colors[i];
            int buttonX = leftPos + col * (COLOR_CELL_SIZE + 2);
            int buttonY = topPos + row * (COLOR_CELL_SIZE + 2);

            this.addRenderableWidget(new ColorButton(buttonX, buttonY, COLOR_CELL_SIZE, COLOR_CELL_SIZE, color, btn -> {
                this.selectedColor = color;
            }));
        }

        // Apply button
        int applyY = topPos + 6 * (COLOR_CELL_SIZE + 2) + 50;
        this.addRenderableWidget(Button.builder(Component.literal("Apply"), button -> {
            PacketDistributor.sendToServer(new ColorUpdatePacket(blockPos, selectedColor));
            this.onClose();
        }).bounds(this.width / 2 - 50, applyY, 100, 20).build());
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Simple background
        guiGraphics.fill(0, 0, this.width, this.height, 0x80000000);

        // Color preview
        int previewY = topPos + 6 * (COLOR_CELL_SIZE + 2) + 10;
        int previewX = this.width / 2 - 20;
        guiGraphics.fill(previewX, previewY, previewX + 40, previewY + 30, 0xFF000000 | selectedColor);
        guiGraphics.renderOutline(previewX - 1, previewY - 1, 42, 32, 0xFFFFFFFF);

        // Hex value
        String hexValue = String.format("#%06X", selectedColor);
        guiGraphics.drawString(this.font, hexValue, this.width / 2 - this.font.width(hexValue) / 2, previewY - 15, 0xFFFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // Simple color button widget
    private static class ColorButton extends Button {
        private final int color;

        public ColorButton(int x, int y, int width, int height, int color, OnPress onPress) {
            super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
            this.color = color;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.fill(getX(), getY(), getX() + width, getY() + height, 0xFF000000 | color);
            if (isHovered) {
                graphics.renderOutline(getX() - 1, getY() - 1, width + 2, height + 2, 0xFFFFFFFF);
            }
        }
    }
}
