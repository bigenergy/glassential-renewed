package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ObsidianGlassDoor extends DoorBlock {

    public ObsidianGlassDoor(Properties properties, BlockSetType blockSetType) {
        super(properties.noOcclusion().explosionResistance(100000000), blockSetType);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pTooltipFlag);

        pTooltipComponents.add(Component.translatable("tooltip.glassential.protected_door").withStyle(ChatFormatting.GRAY));
    }
}
