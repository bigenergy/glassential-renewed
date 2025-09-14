package com.github.bigenergy.glassential.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GravityGlassBlock extends TransparentBlock {
    private static final double MAX_DOWNWARD_SPEED = -0.10;
    private static final float FALL_DISTANCE_FACTOR = 0.2f;
    private static final boolean GIVE_SLOW_FALLING = true;
    private static final int SLOW_FALLING_TICKS = 6;

    private static final VoxelShape OUTLINE = box(0, 0, 0, 16, 16, 16);

    public GravityGlassBlock(Properties props) {
        super(props);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return !(context instanceof EntityCollisionContext && ((EntityCollisionContext) context).getEntity() instanceof Player) ? state.getShape(world, pos) : Shapes.empty();

    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return OUTLINE;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;

        var motion = entity.getDeltaMovement();
        if (motion.y < MAX_DOWNWARD_SPEED) {
            entity.setDeltaMovement(motion.x, MAX_DOWNWARD_SPEED, motion.z);
            entity.hurtMarked = true;
        }

        if (entity.fallDistance > 0f) {
            entity.fallDistance *= FALL_DISTANCE_FACTOR;
        }

        if (GIVE_SLOW_FALLING && entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, SLOW_FALLING_TICKS, 0, true, false, false));
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context,
                                @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.glassential.gravity").withStyle(ChatFormatting.GRAY));
    }
}
