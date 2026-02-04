package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class GlassLightDoor extends DoorBlock {
    public GlassLightDoor(Properties properties, BlockSetType blockSetType) {
        super(blockSetType, properties.noOcclusion().lightLevel((b) -> 15));
    }
}
