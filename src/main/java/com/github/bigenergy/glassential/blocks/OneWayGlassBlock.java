package com.github.bigenergy.glassential.blocks;

import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OneWayGlassBlock extends TransparentBlock implements EntityBlock {
    public static final EnumProperty<Direction> OPAQUE_FACE = EnumProperty.create("opaque_face", Direction.class);

    public OneWayGlassBlock(Properties props) {
        super(props);
        registerDefaultState(stateDefinition.any().setValue(OPAQUE_FACE, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> b) {
        b.add(OPAQUE_FACE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(OPAQUE_FACE, ctx.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected @NotNull InteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                                   BlockPos pos, Player player, InteractionHand hand,
                                                   BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof OneWayGlassBlockEntity ow)) return InteractionResult.FAIL;

        Direction face = hit.getDirection();

        if (stack.getItem() instanceof BlockItem bi) {
            level.setBlock(pos, state.setValue(OPAQUE_FACE, face), Block.UPDATE_CLIENTS);

            ow.setMimic(bi.getBlock().defaultBlockState());

            return InteractionResult.CONSUME;
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level,
                                                        BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        level.setBlock(pos, state.setValue(OPAQUE_FACE, hit.getDirection()), 3);
        return InteractionResult.SUCCESS;
    }
    // --- EntityBlock ---
    @Override public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OneWayGlassBlockEntity(pos, state);
    }
    @Override public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level l, BlockState s, BlockEntityType<T> t) { return null; }
}
