package com.github.bigenergy.glassential.client.gui;

import com.github.bigenergy.glassential.network.GlassPainterPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class GlassPainterScreen extends Screen {

    private static final int GUI_WIDTH = 400;
    private static final int GUI_HEIGHT = 240;
    private static final int PICKER_SIZE = 180;
    private static final int STEP = 3; // pixel step for fill-based rendering

    private final ItemStack painterStack;
    private int selectedColor;
    private boolean emitLight;
    private boolean emitRedstone;
    private boolean passPlayer;
    private boolean passEntity;

    private int leftPos;
    private int topPos;

    // HSV color picker
    private float hue = 0;
    private float saturation = 1;
    private float brightness = 1;

    private boolean draggingPicker = false;
    private boolean draggingHue = false;

    public GlassPainterScreen(ItemStack stack, int currentColor, boolean emitLight, boolean emitRedstone, boolean passPlayer, boolean passEntity) {
        super(Component.translatable("gui.glassential.glass_painter"));
        this.painterStack = stack;
        this.selectedColor = currentColor;
        this.emitLight = emitLight;
        this.emitRedstone = emitRedstone;
        this.passPlayer = passPlayer;
        this.passEntity = passEntity;

        // Convert RGB to HSV
        float[] hsv = rgbToHsv(
            ((currentColor >> 16) & 0xFF) / 255f,
            ((currentColor >> 8) & 0xFF) / 255f,
            (currentColor & 0xFF) / 255f
        );
        this.hue = hsv[0];
        this.saturation = hsv[1];
        this.brightness = hsv[2];
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - GUI_WIDTH) / 2;
        this.topPos = (this.height - GUI_HEIGHT) / 2;

        int checkboxX = leftPos + PICKER_SIZE + 70;
        int checkboxStartY = topPos + 20;
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

        // Apply button
        this.addRenderableWidget(Button.builder(Component.translatable("gui.glassential.apply"), button -> {
            this.saveAndClose();
        }).bounds(leftPos + GUI_WIDTH / 2 - 50, topPos + GUI_HEIGHT - 30, 100, 20).build());
    }

    private void saveAndClose() {
        float[] rgb = hsvToRgb(hue, saturation, brightness);
        this.selectedColor = (((int)(rgb[0] * 255) & 0xFF) << 16) |
                            (((int)(rgb[1] * 255) & 0xFF) << 8) |
                            ((int)(rgb[2] * 255) & 0xFF);

        var connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            connection.send(new GlassPainterPacket(selectedColor, emitLight, emitRedstone, passPlayer, passEntity));
        }
        this.onClose();
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Disable background blur
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Draw GUI background
        graphics.fill(leftPos, topPos, leftPos + GUI_WIDTH, topPos + GUI_HEIGHT, 0xE0101010);
        graphics.renderOutline(leftPos, topPos, GUI_WIDTH, GUI_HEIGHT, 0xFF8B8B8B);

        // Draw title
        graphics.drawString(this.font, this.title, leftPos + 8, topPos + 6, 0xFFFFFF, false);

        // Draw color picker
        int pickerX = leftPos + 10;
        int pickerY = topPos + 25;
        drawColorPicker(graphics, pickerX, pickerY, PICKER_SIZE, PICKER_SIZE);

        // Draw hue slider
        int hueSliderX = pickerX + PICKER_SIZE + 10;
        int hueSliderY = pickerY;
        int hueSliderWidth = 20;
        drawHueSlider(graphics, hueSliderX, hueSliderY, hueSliderWidth, PICKER_SIZE);

        // Draw color preview
        int previewX = leftPos + PICKER_SIZE + 70;
        int previewY = topPos + 115;
        int previewSize = 80;
        float[] rgb = hsvToRgb(hue, saturation, brightness);
        int currentColor = (((int)(rgb[0] * 255) & 0xFF) << 16) |
                          (((int)(rgb[1] * 255) & 0xFF) << 8) |
                          ((int)(rgb[2] * 255) & 0xFF);
        graphics.fill(previewX, previewY, previewX + previewSize, previewY + previewSize, 0xFF000000 | currentColor);
        graphics.renderOutline(previewX, previewY, previewSize, previewSize, 0xFFFFFFFF);

        // Draw hex color
        String hexColor = String.format("#%06X", currentColor);
        int hexX = previewX + previewSize / 2 - this.font.width(hexColor) / 2;
        graphics.drawString(this.font, hexColor, hexX, previewY + previewSize + 5, 0xFFFFFF, false);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawColorPicker(GuiGraphics graphics, int x, int y, int width, int height) {
        // Render saturation/brightness gradient using fill calls
        for (int py = 0; py < height; py += STEP) {
            for (int px = 0; px < width; px += STEP) {
                float s = (float) px / width;
                float v = 1.0f - (float) py / height;
                float[] rgb = hsvToRgb(hue, s, v);
                int color = 0xFF000000 |
                           (((int)(rgb[0] * 255) & 0xFF) << 16) |
                           (((int)(rgb[1] * 255) & 0xFF) << 8) |
                           ((int)(rgb[2] * 255) & 0xFF);
                int x2 = Math.min(x + px + STEP, x + width);
                int y2 = Math.min(y + py + STEP, y + height);
                graphics.fill(x + px, y + py, x2, y2, color);
            }
        }

        // Draw selection indicator
        int selX = x + (int)(saturation * width);
        int selY = y + (int)((1.0f - brightness) * height);
        graphics.renderOutline(selX - 4, selY - 4, 8, 8, 0xFFFFFFFF);
        graphics.renderOutline(selX - 5, selY - 5, 10, 10, 0xFF000000);
    }

    private void drawHueSlider(GuiGraphics graphics, int x, int y, int width, int height) {
        // Render hue gradient using fill calls
        for (int py = 0; py < height; py += STEP) {
            float h = (float) py / height;
            float[] rgb = hsvToRgb(h, 1.0f, 1.0f);
            int color = 0xFF000000 |
                       (((int)(rgb[0] * 255) & 0xFF) << 16) |
                       (((int)(rgb[1] * 255) & 0xFF) << 8) |
                       ((int)(rgb[2] * 255) & 0xFF);
            int y2 = Math.min(y + py + STEP, y + height);
            graphics.fill(x, y + py, x + width, y2, color);
        }

        graphics.renderOutline(x, y, width, height, 0xFFFFFFFF);

        // Draw selection indicator
        int selY = y + (int)(hue * height);
        graphics.fill(x - 2, selY - 2, x + width + 2, selY + 2, 0xFFFFFFFF);
        graphics.fill(x - 1, selY - 1, x + width + 1, selY + 1, 0xFF000000);
    }

    // Handle mouse input using Screen's built-in tracking
    private void handleMouseInput(double mouseX, double mouseY, int button, boolean isClick) {
        if (button != 0) return;

        int pickerX = leftPos + 10;
        int pickerY = topPos + 25;
        int hueSliderX = pickerX + PICKER_SIZE + 10;
        int hueSliderY = pickerY;
        int hueSliderWidth = 20;

        if (isClick) {
            // Check color picker area
            if (mouseX >= pickerX && mouseX <= pickerX + PICKER_SIZE &&
                mouseY >= pickerY && mouseY <= pickerY + PICKER_SIZE) {
                draggingPicker = true;
                updatePickerColor(mouseX, mouseY, pickerX, pickerY);
                return;
            }

            // Check hue slider area
            if (mouseX >= hueSliderX && mouseX <= hueSliderX + hueSliderWidth &&
                mouseY >= hueSliderY && mouseY <= hueSliderY + PICKER_SIZE) {
                draggingHue = true;
                updateHue(mouseY, hueSliderY);
                return;
            }
        } else {
            // Dragging
            if (draggingPicker) {
                updatePickerColor(mouseX, mouseY, pickerX, pickerY);
            } else if (draggingHue) {
                updateHue(mouseY, hueSliderY);
            }
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        handleMouseInput(event.x(), event.y(), event.button(), true);
        if (draggingPicker || draggingHue) return true;
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        draggingPicker = false;
        draggingHue = false;
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        handleMouseInput(event.x(), event.y(), event.button(), false);
        if (draggingPicker || draggingHue) return true;
        return super.mouseDragged(event, dragX, dragY);
    }

    private void updatePickerColor(double mouseX, double mouseY, int pickerX, int pickerY) {
        saturation = Math.max(0, Math.min(1, (float)(mouseX - pickerX) / PICKER_SIZE));
        brightness = Math.max(0, Math.min(1, 1.0f - (float)(mouseY - pickerY) / PICKER_SIZE));
    }

    private void updateHue(double mouseY, int hueSliderY) {
        hue = Math.max(0, Math.min(1, (float)(mouseY - hueSliderY) / PICKER_SIZE));
    }

    private float[] hsvToRgb(float h, float s, float v) {
        int i = (int)(h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);

        return switch (i % 6) {
            case 0 -> new float[]{v, t, p};
            case 1 -> new float[]{q, v, p};
            case 2 -> new float[]{p, v, t};
            case 3 -> new float[]{p, q, v};
            case 4 -> new float[]{t, p, v};
            case 5 -> new float[]{v, p, q};
            default -> new float[]{0, 0, 0};
        };
    }

    private float[] rgbToHsv(float r, float g, float b) {
        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float delta = max - min;

        float h = 0;
        if (delta != 0) {
            if (max == r) {
                h = ((g - b) / delta) % 6;
            } else if (max == g) {
                h = (b - r) / delta + 2;
            } else {
                h = (r - g) / delta + 4;
            }
            h /= 6;
            if (h < 0) h += 1;
        }

        float s = max == 0 ? 0 : delta / max;
        float v = max;

        return new float[]{h, s, v};
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
