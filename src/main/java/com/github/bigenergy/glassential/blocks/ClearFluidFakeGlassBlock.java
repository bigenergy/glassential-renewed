package com.github.bigenergy.glassential.blocks;

import com.github.bigenergy.glassential.blocks.entity.BigGlassBlockEntity;
import com.github.bigenergy.glassential.blocks.entity.ClearFluidGlassBlockEntity;
import com.github.bigenergy.glassential.datagen.GlassentialTags;
import com.github.bigenergy.glassential.init.GlassentialBlockEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
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

    public ClearFluidFakeGlassBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            @Nullable BlockGetter level,
            @NotNull List<Component> tooltipComponents,
            @NotNull TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.glassential.clear_fluid").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.glassential.ethereal").withStyle(ChatFormatting.GRAY));
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
        if(!pLevel.isClientSide){
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
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
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
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        // Compute occlusion shape dynamically from the current world state.
        // This avoids client/server desync issues with the BE-cached shape that caused
        // certain planar floor configurations to not hide the water face on the DOWN side.
        VoxelShape shape = Shapes.empty();
        for (Direction direction : Direction.values()) {
            FluidState fs = pLevel.getFluidState(pPos.relative(direction));
            if (fs.is(GlassentialTags.Fluids.CLEAR_FLUID_GLASS_FLUIDS_TAG)) {
                shape = Shapes.or(shape, occlusionShapes.get(direction));
            }
        }
        return shape;
    }

    @Override
    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
        return pAdjacentBlockState.is(this) ? true : super.skipRendering(pState, pAdjacentBlockState, pSide);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return GlassentialBlockEntities.CLEAR_FLUID_GLASS.get().create(pPos, pState);
    }
}
