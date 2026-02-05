package com.github.bigenergy.glassential.client.gui;

import com.github.bigenergy.glassential.network.GlassPainterPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

// Simplified glass painter - using buttons instead of custom mouse tracking for 1.21.11 compatibility
public class GlassPainterScreen extends Screen {

    private static final int GUI_WIDTH = 400;
    private static final int GUI_HEIGHT = 260;
    private static final int COLOR_GRID_SIZE = 12;
    private static final int COLOR_CELL_SIZE = 14;

    private final ItemStack painterStack;
    private int selectedColor;
    private boolean emitLight;
    private boolean emitRedstone;
    private boolean passPlayer;
    private boolean passEntity;

    private int leftPos;
    private int topPos;

    public GlassPainterScreen(ItemStack stack, int currentColor, boolean emitLight, boolean emitRedstone, boolean passPlayer, boolean passEntity) {
        super(Component.translatable("gui.glassential.glass_painter"));
        this.painterStack = stack;
        this.selectedColor = currentColor;
        this.emitLight = emitLight;
        this.emitRedstone = emitRedstone;
        this.passPlayer = passPlayer;
        this.passEntity = passEntity;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - GUI_WIDTH) / 2;
        this.topPos = (this.height - GUI_HEIGHT) / 2;

        int checkboxX = leftPos + 220;
        int checkboxStartY = topPos + 25;
        int checkboxSpacing = 22;

        this.addRenderableWidget(Checkbox.builder(Component.translatable("gui.glassential.emit_light"), this.font)
                .pos(checkboxX, checkboxStartY)
                .selected(this.emitLight)
                .onValueChange((checkbox, selected) -> this.emitLight = selected)
                .build());

        this.addRenderableWidget(Checkbox.builder(Component.translatable("gui.glassential.emit_redstone"), this.font)
                .pos(checkboxX, checkboxStartY + checkboxSpacing)
                .selected(this.emitRedstone)
                .onValueChange((checkbox, selected) -> this.emitRedstone = selected)
                .build());

        this.addRenderableWidget(Checkbox.builder(Component.translatable("gui.glassential.pass_player"), this.font)
                .pos(checkboxX, checkboxStartY + checkboxSpacing * 2)
                .selected(this.passPlayer)
                .onValueChange((checkbox, selected) -> this.passPlayer = selected)
                .build());

        this.addRenderableWidget(Checkbox.builder(Component.translatable("gui.glassential.pass_entity"), this.font)
                .pos(checkboxX, checkboxStartY + checkboxSpacing * 3)
                .selected(this.passEntity)
                .onValueChange((checkbox, selected) -> this.passEntity = selected)
                .build());

        // Create color palette buttons instead of custom mouse tracking
        int gridX = leftPos + 10;
        int gridY = topPos + 25;

        // Predefined color palette
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
            int buttonX = gridX + col * (COLOR_CELL_SIZE + 2);
            int buttonY = gridY + row * (COLOR_CELL_SIZE + 2);

            this.addRenderableWidget(new ColorButton(buttonX, buttonY, COLOR_CELL_SIZE, COLOR_CELL_SIZE, color, btn -> {
                this.selectedColor = color;
            }));
        }

        // Apply button
        this.addRenderableWidget(Button.builder(Component.translatable("gui.glassential.apply"), button -> {
            this.saveAndClose();
        }).bounds(leftPos + GUI_WIDTH / 2 - 50, topPos + GUI_HEIGHT - 30, 100, 20).build());
    }

    private void saveAndClose() {
        PacketDistributor.sendToServer(new GlassPainterPacket(selectedColor, emitLight, emitRedstone, passPlayer, passEntity));
        this.onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Draw GUI background
        graphics.fill(leftPos, topPos, leftPos + GUI_WIDTH, topPos + GUI_HEIGHT, 0xE0101010);
        graphics.renderOutline(leftPos, topPos, GUI_WIDTH, GUI_HEIGHT, 0xFF8B8B8B);

        // Draw title
        graphics.drawString(this.font, this.title, leftPos + 8, topPos + 6, 0xFFFFFF, false);

        // Draw current color preview
        int previewX = leftPos + 220;
        int previewY = topPos + 130;
        int previewSize = 60;
        graphics.fill(previewX, previewY, previewX + previewSize, previewY + previewSize, 0xFF000000 | selectedColor);
        graphics.renderOutline(previewX, previewY, previewSize, previewSize, 0xFFFFFFFF);

        // Draw hex color
        String hexColor = String.format("#%06X", selectedColor);
        int hexX = previewX + previewSize / 2 - this.font.width(hexColor) / 2;
        graphics.drawString(this.font, hexColor, hexX, previewY + previewSize + 5, 0xFFFFFF, false);

        super.render(graphics, mouseX, mouseY, partialTick);
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
