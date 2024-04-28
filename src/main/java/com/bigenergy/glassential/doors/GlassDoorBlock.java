package com.bigenergy.glassential.doors;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class GlassDoorBlock extends DoorBlock {
    public GlassDoorBlock(Properties properties, BlockSetType blockSetType) {
        super(properties.noOcclusion(), blockSetType);
    }
}
