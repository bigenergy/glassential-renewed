package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class GlassGhostDoor extends DoorBlock {
    public GlassGhostDoor(Properties properties, BlockSetType blockSetType) {
        super(blockSetType, properties.noCollision());
    }
}
