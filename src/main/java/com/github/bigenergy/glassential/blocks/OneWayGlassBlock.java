package com.github.bigenergy.glassential.blocks;

import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OneWayGlassBlock extends AbstractGlassBlock implements EntityBlock {
    public static final DirectionProperty OPAQUE_FACE = DirectionProperty.create("opaque_face", Direction.values());

    public OneWayGlassBlock(Properties props) {
        super(props);
        registerDefaultState(stateDefinition.any().setValue(OPAQUE_FACE, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(OPAQUE_FACE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(OPAQUE_FACE, ctx.getNearestLookingDirection().getOpposite());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(Component.translatable("tooltip.glassential.one_way").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("tooltip.glassential.one_way.hint").withStyle(ChatFormatting.GRAY));
    }

    /**
     * 1.20.1 has a single {@code use} hook, so the 1.21 {@code useItemOn} (disguise with a held
     * BlockItem) and {@code useWithoutItem} (empty hand rotates the opaque face) paths are merged
     * here and dispatched on what the player is holding.
     */
    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        Direction face = hit.getDirection();

        if (stack.isEmpty()) {
            // useWithoutItem: just re-aim the opaque face
            if (level.isClientSide) return InteractionResult.SUCCESS;
            level.setBlock(pos, state.setValue(OPAQUE_FACE, face), 3);
            return InteractionResult.SUCCESS;
        }

        if (!(stack.getItem() instanceof BlockItem bi)) {
            return InteractionResult.FAIL;
        }

        // Disallow disguising One Way Glass as another One Way Glass: the renderer
        // would recurse into itself and crash (StackOverflowError).
        if (bi.getBlock() instanceof OneWayGlassBlock) {
            return InteractionResult.FAIL;
        }

        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof OneWayGlassBlockEntity ow)) return InteractionResult.FAIL;

        level.setBlock(pos, state.setValue(OPAQUE_FACE, face), Block.UPDATE_CLIENTS);

        ow.setMimic(bi.getBlock().defaultBlockState());

        return InteractionResult.CONSUME;
    }

    // --- EntityBlock ---
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OneWayGlassBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level l, BlockState s, BlockEntityType<T> t) {
        return null;
    }
}
