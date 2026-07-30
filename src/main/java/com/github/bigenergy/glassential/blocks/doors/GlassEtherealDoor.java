package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GlassEtherealDoor extends DoorBlock {

    private final boolean collidePlayers;

    public GlassEtherealDoor(Properties properties, BlockSetType blockSetType, boolean collidePlayers) {
        super(properties.noOcclusion(), blockSetType);
        this.collidePlayers = collidePlayers;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        //Ty KingLemming for finding that new way for the old behavior
        return (context instanceof EntityCollisionContext && ((EntityCollisionContext)context).getEntity() instanceof Player) == collidePlayers ? state.getShape(world, pos) : Shapes.empty();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(Component.translatable(collidePlayers ? "tooltip.glassential.ethereal_reverse" : "tooltip.glassential.ethereal").withStyle(ChatFormatting.GRAY));
    }
}
