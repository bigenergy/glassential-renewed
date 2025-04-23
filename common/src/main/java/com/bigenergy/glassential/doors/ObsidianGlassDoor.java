package com.bigenergy.glassential.doors;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.function.Consumer;

public class ObsidianGlassDoor extends DoorBlock implements TooltipProvider {

    public ObsidianGlassDoor(Properties properties, BlockSetType blockSetType) {
        super(blockSetType, properties.noOcclusion().explosionResistance(100000000));
    }

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        consumer.accept(Component.translatable("tooltip.glassential.protected_door").withStyle(ChatFormatting.GRAY));
    }
}
