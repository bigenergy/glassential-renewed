package com.github.bigenergy.glassential.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class DarkEtherealGlassBlock extends EtherealGlassBlock implements TooltipProvider {

    public DarkEtherealGlassBlock(Properties properties, boolean collidePlayers) {
        super(properties, collidePlayers);
    }

    @Override
    public void addToTooltip(Item.@NonNull TooltipContext tooltipContext, Consumer<net.minecraft.network.chat.Component> consumer, @NonNull TooltipFlag tooltipFlag, @NonNull DataComponentGetter dataComponentGetter) {
        consumer.accept(Component.translatable("tooltip.glassential.dark").withStyle(ChatFormatting.GRAY));
    }
}
