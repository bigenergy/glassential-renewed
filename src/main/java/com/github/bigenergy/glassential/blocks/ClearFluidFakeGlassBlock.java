package com.github.bigenergy.glassential.blocks;

import com.github.bigenergy.glassential.blocks.entity.BigGlassBlockEntity;
import com.github.bigenergy.glassential.blocks.entity.ClearFluidGlassBlockEntity;
import com.github.bigenergy.glassential.datagen.GlassentialTags;
import com.github.bigenergy.glassential.init.GlassentialBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ClearFluidFakeGlassBlock extends BigGlassBlockEntity {
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

    public static final MapCodec<ClearFluidFakeGlassBlock> CODEC = simpleCodec(ClearFluidFakeGlassBlock::new);
    public ClearFluidFakeGlassBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        //Ty KingLemming for finding that new way for the old behavior
        return !(context instanceof EntityCollisionContext && ((EntityCollisionContext) context).getEntity() instanceof Player) ? state.getShape(world, pos) : Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if(!pLevel.isClientSide()){
            ClearFluidGlassBlockEntity blockEntity = (ClearFluidGlassBlockEntity) pLevel.getBlockEntity(pPos);
            blockEntity.getOcclusionDirs().clear();
            blockEntity.setOcclusionShape(Shapes.empty());
            VoxelShape shape = blockEntity.getOcclusionShape();
            for(Direction direction : Direction.values()){
                FluidState state = pLevel.getFluidState(pPos.relative(direction));
                if(state.is(GlassentialTags.Fluids.CLEAR_FLUID_GLASS_FLUIDS_TAG)){
                    shape = Shapes.or(shape, occlusionShapes.get(direction));
                    blockEntity.addDirection(direction);
                }
            }

            blockEntity.setOcclusionShape(shape);
        }
        pLevel.sendBlockUpdated(pPos, pState, pState, Block.UPDATE_IMMEDIATE);
    }

    @Override
    protected void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, @Nullable Orientation pOrientation, boolean pIsMoving) {
        if(!pLevel.isClientSide()){
            ClearFluidGlassBlockEntity blockEntity = (ClearFluidGlassBlockEntity) pLevel.getBlockEntity(pPos);
            blockEntity.getOcclusionDirs().clear();
            blockEntity.setOcclusionShape(Shapes.empty());
            VoxelShape shape = blockEntity.getOcclusionShape();

            for(Direction direction : Direction.values()){
                if(pLevel.getFluidState(pPos.relative(direction)).is(GlassentialTags.Fluids.CLEAR_FLUID_GLASS_FLUIDS_TAG)){
                    shape = Shapes.or(shape, occlusionShapes.get(direction));
                    blockEntity.addDirection(direction);
                }
            }

            blockEntity.setOcclusionShape(shape);
        }
        pLevel.sendBlockUpdated(pPos, pState, pState, Block.UPDATE_IMMEDIATE);
        super.neighborChanged(pState, pLevel, pPos, pBlock, pOrientation, pIsMoving);
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState pState) {
        // Note: In 1.21.5+, getOcclusionShape no longer has access to BlockGetter/BlockPos
        // The dynamic occlusion based on block entity is no longer directly possible here
        // Return empty shape as default - fluid blocking is handled elsewhere
        return Shapes.empty();
    }

    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
        return pAdjacentBlockState.is(this) ? true : super.skipRendering(pState, pAdjacentBlockState, pSide);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return GlassentialBlockEntities.CLEAR_FLUID_GLASS.get().create(pPos, pState);
    }
}
