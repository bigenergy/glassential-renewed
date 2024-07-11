package com.bigenergy.glassential.doors;

import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class GlassTrapDoor extends TrapDoorBlock {
    public GlassTrapDoor(Properties properties, BlockSetType blockSetType) {
        super(blockSetType, properties.noOcclusion());
    }
}
