package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class TintedGlassTrapDoor extends TrapDoorBlock {
    public TintedGlassTrapDoor(Properties properties, BlockSetType blockSetType) {
        super(blockSetType, properties.noOcclusion());
    }
}
