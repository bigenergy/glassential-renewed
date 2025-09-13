package com.github.bigenergy.glassential.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.TintedGlassBlock;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TintedLightGlassBlock extends TintedGlassBlock {

	public TintedLightGlassBlock(Properties settings) {
		super(settings.lightLevel((b) -> 15));
	}

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            @NotNull Item.TooltipContext context,
            @NotNull List<Component> tooltipComponents,
            @NotNull TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.glassential.light").withStyle(ChatFormatting.GRAY));
    }
}
