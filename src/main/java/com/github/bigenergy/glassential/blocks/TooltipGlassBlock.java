package com.github.bigenergy.glassential.blocks;

import net.minecraft.world.level.block.TransparentBlock;
import org.jetbrains.annotations.NotNull;

public class TooltipGlassBlock extends TransparentBlock {

    private final String tooltip;

    public TooltipGlassBlock(@NotNull Properties properties, @NotNull String tooltip) {
        super(properties);
        this.tooltip = tooltip;
    }
}
