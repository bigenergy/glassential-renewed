package com.github.bigenergy.glassential.blocks;

import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
import com.github.bigenergy.glassential.init.GlassentialBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
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

public class ColorableStainedGlassBlock extends BaseEntityBlock {

    public static final MapCodec<ColorableStainedGlassBlock> CODEC = simpleCodec(ColorableStainedGlassBlock::new);
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public ColorableStainedGlassBlock(Properties properties) {
        super(properties.lightLevel(state -> state.getValue(LIT) ? 15 : 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pDirection) {
        return pAdjacentBlockState.is(this) || super.skipRendering(pState, pAdjacentBlockState, pDirection);
    }

    @Override
    public int getLightEmission(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
        var be = level.getBlockEntity(pos);
        if (be instanceof ColorableGlassBlockEntity colorable && colorable.getEmitLight()) {
            return 15;
        }
        return super.getLightEmission(state, level, pos);
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
    protected @NotNull BlockState updateShape(BlockState pState, LevelReader pLevel, ScheduledTickAccess pScheduledTickAccess,
                                              BlockPos pPos, Direction pDirection, BlockPos pNeighborPos,
                                              BlockState pNeighborState, RandomSource pRandom) {
        if (pLevel instanceof Level level) {
            level.updateNeighborsAt(pPos, this);
        }
        return super.updateShape(pState, pLevel, pScheduledTickAccess, pPos, pDirection, pNeighborPos, pNeighborState, pRandom);
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
        return GlassentialBlockEntities.COLORABLE_STAINED_GLASS.get().create(pPos, pState);
    }

    @Override
    public void addToTooltip(net.minecraft.world.item.Item.TooltipContext tooltipContext, java.util.function.Consumer<net.minecraft.network.chat.Component> consumer, net.minecraft.world.item.TooltipFlag tooltipFlag, net.minecraft.world.item.component.DataComponentGetter dataComponentGetter) {
        consumer.accept(net.minecraft.network.chat.Component.translatable("tooltip.glassential.colorable").withStyle(net.minecraft.ChatFormatting.GRAY));
    }
}
