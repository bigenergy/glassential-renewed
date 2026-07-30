package com.github.bigenergy.glassential.blocks;

import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ColorableGlassBlock extends BaseEntityBlock {

    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public ColorableGlassBlock(Properties properties) {
        super(properties.lightLevel(state -> state.getValue(LIT) ? 15 : 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.glassential.colorable").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        var be = level.getBlockEntity(pos);
        if (be instanceof ColorableGlassBlockEntity colorable && colorable.getEmitLight()) {
            return 15;
        }
        return super.getLightEmission(state, level, pos);
    }

    @Override
    public int getLightBlock(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        // If emitting light, block incoming light to emit properly
        if (pState.getValue(LIT)) {
            return 15;
        }
        // Otherwise glass doesn't block light
        return 0;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return false;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        var be = pLevel.getBlockEntity(pPos);
        if (be instanceof ColorableGlassBlockEntity colorable && colorable.getEmitRedstone()) {
            return 15;
        }
        return 0;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel,
                                           BlockPos pPos, BlockPos pNeighborPos) {
        if (pLevel instanceof Level level) {
            level.updateNeighborsAt(pPos, this);
        }
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        // If emitting light, don't propagate skylight so the block can emit properly
        if (pState.getValue(LIT)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pDirection) {
        return pAdjacentBlockState.is(this) || super.skipRendering(pState, pAdjacentBlockState, pDirection);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        var be = pLevel.getBlockEntity(pPos);
        if (be instanceof ColorableGlassBlockEntity colorable) {
            // Check if player should pass through
            if (colorable.getPassPlayer() && pContext instanceof EntityCollisionContext entityContext) {
                var entity = entityContext.getEntity();
                if (entity instanceof Player) {
                    return Shapes.empty();
                }
            }
            // Check if all entities should pass through
            if (colorable.getPassEntity()) {
                return Shapes.empty();
            }
        }
        return super.getCollisionShape(pState, pLevel, pPos, pContext);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return GlassentialBlockEntities.COLORABLE_GLASS.get().create(pPos, pState);
    }
}
