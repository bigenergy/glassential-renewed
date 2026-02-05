package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.function.Consumer;

public class ObsidianGlassDoor extends DoorBlock {

    public ObsidianGlassDoor(Properties properties, BlockSetType blockSetType) {
        super(blockSetType, properties.noOcclusion().explosionResistance(100000000));
    }
}
