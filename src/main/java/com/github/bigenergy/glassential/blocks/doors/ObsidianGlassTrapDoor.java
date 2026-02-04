package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class ObsidianGlassTrapDoor extends TrapDoorBlock {
    public ObsidianGlassTrapDoor(Properties properties, BlockSetType blockSetType) {
        super(blockSetType, properties.noOcclusion());
    }
}
