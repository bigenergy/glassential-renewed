package com.github.bigenergy.glassential.blocks;

import net.minecraft.world.level.block.TintedGlassBlock;
import org.jetbrains.annotations.NotNull;

public class TooltipTintedGlassBlock extends TintedGlassBlock {

    private final String tooltip;

    public TooltipTintedGlassBlock(@NotNull Properties properties, @NotNull String tooltip) {
        super(properties);
        this.tooltip = tooltip;
    }
}
