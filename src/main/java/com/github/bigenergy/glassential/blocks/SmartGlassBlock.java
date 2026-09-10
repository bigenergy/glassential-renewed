package com.github.bigenergy.glassential.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Electrochromic ("smart") glass. Clear on its own, frosted while it receives a
 * redstone signal.
 *
 * <p>A signal anywhere on a connected run of Smart Glass frosts the whole run, so
 * one lever tucked out of sight switches an entire window rather than one block.
 *
 * <p>Only the view is blocked — light still passes through in both states, since
 * the block keeps vanilla glass' light properties. That is the whole point: a
 * window that turns opaque without turning the room dark.
 */
public class SmartGlassBlock extends TransparentBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    /**
     * Upper bound on how far a single signal spreads. Without it, one lever on a
     * large glass structure would walk the whole thing on every block update.
     */
    private static final int MAX_PANEL_SIZE = 1024;

    public SmartGlassBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                        @NotNull BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        // LevelChunk calls onPlace for every server-side state change, not just for a
        // genuinely new block, so refreshPanel's own setBlock calls land back here.
        // Bailing out when the block was already Smart Glass keeps that from recursing
        // once per block in the panel.
        if (level.isClientSide || oldState.is(this)) {
            return;
        }

        // A block added to an already-frosted panel has to catch up with it.
        refreshPanel(level, pos);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                @NotNull Block neighborBlock, @NotNull BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        if (!level.isClientSide) {
            refreshPanel(level, pos);
        }
    }

    /**
     * Re-evaluates the whole connected panel containing {@code origin}: if any block
     * in it sees a signal, all of them frost, otherwise all of them clear.
     */
    private static void refreshPanel(Level level, BlockPos origin) {
        List<BlockPos> panel = collectPanel(level, origin);

        boolean powered = false;
        for (BlockPos pos : panel) {
            if (level.hasNeighborSignal(pos)) {
                powered = true;
                break;
            }
        }

        for (BlockPos pos : panel) {
            BlockState state = level.getBlockState(pos);
            if (state.getValue(POWERED) != powered) {
                // UPDATE_CLIENTS only: re-rendering the block is all that is needed, and
                // skipping the neighbour update stops this from cascading back into
                // neighborChanged on the rest of the panel.
                level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_CLIENTS);
            }
        }
    }

    /** Breadth-first walk over Smart Glass blocks reachable from {@code origin} by their faces. */
    private static List<BlockPos> collectPanel(Level level, BlockPos origin) {
        List<BlockPos> panel = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();

        queue.add(origin);
        visited.add(origin);

        while (!queue.isEmpty() && panel.size() < MAX_PANEL_SIZE) {
            BlockPos pos = queue.poll();
            if (!(level.getBlockState(pos).getBlock() instanceof SmartGlassBlock)) {
                continue;
            }
            panel.add(pos);

            for (Direction direction : Direction.values()) {
                BlockPos next = pos.relative(direction);
                if (visited.add(next)) {
                    queue.add(next);
                }
            }
        }
        return panel;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context,
                                @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.glassential.smart").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.glassential.smart.panel").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.glassential.smart.hint").withStyle(ChatFormatting.DARK_GRAY));
    }
}
