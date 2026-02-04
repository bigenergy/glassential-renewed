package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class ObsidianGlassDoor extends DoorBlock {

    public ObsidianGlassDoor(Properties properties, BlockSetType blockSetType) {
        super(blockSetType, properties.noOcclusion().explosionResistance(100000000));
    }
}
