package com.github.bigenergy.glassential.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DataComponentGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CustomGlassBlock extends TransparentBlock {

    private final String tooltip;

    public CustomGlassBlock(Properties p_309186_, String tooltip) {
        super(p_309186_);
        this.tooltip = tooltip;
    }

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        consumer.accept(Component.translatable(tooltip).withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockState(pos.above()).is(Blocks.WATER)) {
            level.setBlock(pos.above(),
                    Blocks.BUBBLE_COLUMN.defaultBlockState().setValue(BubbleColumnBlock.DRAG_DOWN, true),
                    3
            );
        }
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState pState, LevelReader pLevel, ScheduledTickAccess pScheduledTickAccess,
                                              BlockPos pCurrentPos, Direction pFacing, BlockPos pFacingPos,
                                              BlockState pFacingState, RandomSource pRandom) {
        if (pFacing == Direction.UP && pFacingState.is(Blocks.WATER)) {
            pScheduledTickAccess.scheduleTick(pCurrentPos, this, 20);
        }

        return super.updateShape(pState, pLevel, pScheduledTickAccess, pCurrentPos, pFacing, pFacingPos, pFacingState, pRandom);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!pEntity.isSteppingCarefully() && pEntity instanceof LivingEntity) {
            pEntity.hurt(pLevel.damageSources().hotFloor(), 1.0F);
        }

        super.stepOn(pLevel, pPos, pState, pEntity);
    }
}
