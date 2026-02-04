package com.github.bigenergy.glassential.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class GhostGlassBlock extends TransparentBlock {


    public GhostGlassBlock(@NotNull Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        //Ty KingLemming for finding that new way for the old behavior
        return !(context instanceof EntityCollisionContext && ((EntityCollisionContext) context).getEntity() instanceof Entity) ? state.getShape(world, pos) : Shapes.empty();
    }
}
