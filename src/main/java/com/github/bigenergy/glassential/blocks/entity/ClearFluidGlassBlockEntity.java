package com.github.bigenergy.glassential.blocks.entity;

import com.github.bigenergy.glassential.init.GlassentialBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClearFluidGlassBlockEntity extends BlockEntity {
    protected static final VoxelShape SHAPE_DOWN = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.01D, 16.0D);
    protected static final VoxelShape SHAPE_UP = Block.box(0.0D, 15.99D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_NORTH = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 0.01D);
    protected static final VoxelShape SHAPE_EAST = Block.box(15.99D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_SOUTH = Block.box(0.0D, 0.0D, 15.99D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_WEST = Block.box(0.0D, 0.0D, 0.0D, 0.01D, 16.0D, 16.0D);
    private static final Map<Direction, VoxelShape> occlusionShapes = new HashMap<Direction, VoxelShape>() {{
        put(Direction.DOWN, SHAPE_DOWN);
        put(Direction.UP, SHAPE_UP);
        put(Direction.NORTH, SHAPE_NORTH);
        put(Direction.EAST, SHAPE_EAST);
        put(Direction.SOUTH, SHAPE_SOUTH);
        put(Direction.WEST, SHAPE_WEST);
    }};
    protected VoxelShape occlusionShape = Shapes.empty();
    private final List<Direction> occlusionDirs = new ArrayList<>();

    public ClearFluidGlassBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(GlassentialBlockEntities.CLEAR_FLUID_GLASS.get(), pPos, pBlockState);
    }

    public VoxelShape getOcclusionShape() {
        return occlusionShape;
    }

    public void setOcclusionShape(VoxelShape shape) {
        this.occlusionShape = shape;
    }

    public void addDirection(Direction direction) {
        occlusionDirs.add(direction);
        VoxelShape shape = Shapes.empty();
        for (Direction dir : occlusionDirs) {
            shape = Shapes.or(shape, occlusionShapes.get(dir));
        }
        setOcclusionShape(shape);
    }

    public List<Direction> getOcclusionDirs() {
        return occlusionDirs;
    }
}
