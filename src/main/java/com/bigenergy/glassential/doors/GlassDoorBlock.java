package com.bigenergy.glassential.doors;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class GlassDoorBlock extends DoorBlock {
    public GlassDoorBlock(Block.Properties properties, BlockSetType type) {
        super(properties.noOcclusion(), type);
    }
}
