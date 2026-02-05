package com.github.bigenergy.glassential.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class IceGlassBlock extends IceBlock {
    private final String tooltip;

    public IceGlassBlock(@NotNull Properties properties, @NotNull String tooltip) {
        super(properties);
        this.tooltip = tooltip;
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level,
                              @NotNull BlockPos pos, @NotNull RandomSource random) {
        // Disable the ice melting mechanic to block permanent saving
    }
}
