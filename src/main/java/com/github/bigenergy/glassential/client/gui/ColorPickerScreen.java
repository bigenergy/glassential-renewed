package com.github.bigenergy.glassential.client.gui;

import com.github.bigenergy.glassential.network.ColorUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

// Simplified color picker for 1.21.11 compatibility
public class ColorPickerScreen extends Screen {
    private final BlockPos blockPos;
    private int selectedColor;

    private static final int COLOR_CELL_SIZE = 20;
    private static final int[] COLORS = {
        0xFF0000, 0xFF7F00, 0xFFFF00, 0x7FFF00, 0x00FF00, 0x00FF7F,
        0x00FFFF, 0x007FFF, 0x0000FF, 0x7F00FF, 0xFF00FF, 0xFF007F,
        0x800000, 0x804000, 0x808000, 0x408000, 0x008000, 0x008040,
        0x008080, 0x004080, 0x000080, 0x400080, 0x800080, 0x800040,
        0xFFFFFF, 0xC0C0C0, 0x808080, 0x404040, 0x202020, 0x000000,
        0xFFC0C0, 0xC0FFC0, 0xC0C0FF, 0xFFFFC0, 0xFFC0FF, 0xC0FFFF
    };

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
        for (int i = 0; i < COLORS.length; i++) {
            int row = i / 6;
            int col = i % 6;
            final int color = COLORS[i];
            int buttonX = leftPos + col * (COLOR_CELL_SIZE + 2);
            int buttonY = topPos + row * (COLOR_CELL_SIZE + 2);

            this.addRenderableWidget(Button.builder(Component.empty(), btn -> {
                this.selectedColor = color;
            }).bounds(buttonX, buttonY, COLOR_CELL_SIZE, COLOR_CELL_SIZE).build());
        }

        // Apply button
        int applyY = topPos + 6 * (COLOR_CELL_SIZE + 2) + 50;
        this.addRenderableWidget(Button.builder(Component.literal("Apply"), button -> {
            sendPacketToServer();
            this.onClose();
        }).bounds(this.width / 2 - 50, applyY, 100, 20).build());
    }

    private void sendPacketToServer() {
        var connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            connection.send(new ColorUpdatePacket(blockPos, selectedColor));
        }
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Simple background
        guiGraphics.fill(0, 0, this.width, this.height, 0x80000000);

        // Draw color swatches over buttons
        for (int i = 0; i < COLORS.length; i++) {
            int row = i / 6;
            int col = i % 6;
            int color = COLORS[i];
            int buttonX = leftPos + col * (COLOR_CELL_SIZE + 2);
            int buttonY = topPos + row * (COLOR_CELL_SIZE + 2);
            guiGraphics.fill(buttonX + 2, buttonY + 2, buttonX + COLOR_CELL_SIZE - 2, buttonY + COLOR_CELL_SIZE - 2, 0xFF000000 | color);
        }

        // Color preview
        int previewY = topPos + 6 * (COLOR_CELL_SIZE + 2) + 10;
        int previewX = this.width / 2 - 20;
        guiGraphics.fill(previewX, previewY, previewX + 40, previewY + 30, 0xFF000000 | selectedColor);
        guiGraphics.outline(previewX - 1, previewY - 1, 42, 32, 0xFFFFFFFF);

        // Hex value
        String hexValue = String.format("#%06X", selectedColor);
        guiGraphics.text(this.font, hexValue, this.width / 2 - this.font.width(hexValue) / 2, previewY - 15, 0xFFFFFFFF);

        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
