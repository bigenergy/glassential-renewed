package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GlassGhostTrapDoor extends TrapDoorBlock {
    public GlassGhostTrapDoor(Properties properties, BlockSetType blockSetType) {
        super(blockSetType, properties.noCollission());
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack pStack,
            Item.@NotNull TooltipContext pContext,
            @NotNull List<Component> pTooltipComponents,
            @NotNull TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(Component.translatable("tooltip.glassential.ghostly").withStyle(ChatFormatting.GRAY));
    }
}
