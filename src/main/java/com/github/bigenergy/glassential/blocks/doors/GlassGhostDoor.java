package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GlassGhostDoor extends DoorBlock {
    public GlassGhostDoor(Properties properties, BlockSetType blockSetType) {
        super(properties.noCollission(), blockSetType);
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack pStack,
            @Nullable BlockGetter pLevel,
            @NotNull List<Component> pTooltipComponents,
            @NotNull TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(Component.translatable("tooltip.glassential.ghostly").withStyle(ChatFormatting.GRAY));
    }
}
