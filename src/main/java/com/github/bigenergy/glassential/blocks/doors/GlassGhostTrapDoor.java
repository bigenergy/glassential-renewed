package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class GlassGhostTrapDoor extends TrapDoorBlock {
    public GlassGhostTrapDoor(Properties properties, BlockSetType blockSetType) {
        super(blockSetType, properties.noCollision());
    }
}
