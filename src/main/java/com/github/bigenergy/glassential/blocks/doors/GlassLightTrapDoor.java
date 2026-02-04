package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class GlassLightTrapDoor extends TrapDoorBlock {
    public GlassLightTrapDoor(Properties properties, BlockSetType blockSetType) {
        super(blockSetType, properties.noOcclusion().lightLevel((b) -> 15));
    }
}
