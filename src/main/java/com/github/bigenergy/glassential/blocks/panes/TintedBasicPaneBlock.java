package com.github.bigenergy.glassential.blocks.panes;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TintedBasicPaneBlock extends IronBarsBlock {

    private final boolean lightBlock;

    public TintedBasicPaneBlock(Properties properties, boolean lightBlock) {
        super(properties);
        this.lightBlock = lightBlock;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pTooltipFlag);

        pTooltipComponents.add(Component.translatable("tooltip.glassential.pane_frameless").withStyle(ChatFormatting.GRAY));

        if (this.lightBlock) {
            pTooltipComponents.add(Component.translatable("tooltip.glassential.pane_light").withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_154824_, BlockGetter p_154825_, BlockPos p_154826_) {
        return false;
    }

    @Override
    public int getLightBlock(BlockState p_154828_, BlockGetter p_154829_, BlockPos p_154830_) {
        return p_154829_.getMaxLightLevel();
    }
}
