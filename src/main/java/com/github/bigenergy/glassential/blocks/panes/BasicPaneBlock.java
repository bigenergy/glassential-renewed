package com.github.bigenergy.glassential.blocks.panes;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.IronBarsBlock;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BasicPaneBlock extends IronBarsBlock {

    private final boolean lightBlock;

    public BasicPaneBlock(Properties properties, boolean lightBlock) {
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
}
