package com.bigenergy.glassential.doors;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import javax.annotation.Nullable;
import java.util.List;

public class GlassLightDoor extends DoorBlock {
    public GlassLightDoor(Properties properties, BlockSetType blockSetType) {
        super(blockSetType, properties.noOcclusion().lightLevel((b) -> 15));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("tooltip.glassential.light").withStyle(ChatFormatting.GRAY));
    }
}
