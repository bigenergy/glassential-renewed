package com.bigenergy.glassential.blocks;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class EtherealGlassBlock extends TransparentBlock {

    private final boolean collidePlayers;

    public EtherealGlassBlock(Block.Properties properties, boolean collidePlayers) {
        super(properties);
        this.collidePlayers = collidePlayers;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    	//Ty KingLemming for finding that new way for the old behavior
        return (context instanceof EntityCollisionContext && ((EntityCollisionContext)context).getEntity() instanceof Player) == collidePlayers ? state.getShape(world, pos) : Shapes.empty();
    }

	@Override
	public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
		super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
		pTooltipComponents.add(Component.translatable(collidePlayers ? "tooltip.glassential.ethereal_reverse" : "tooltip.glassential.ethereal").withStyle(ChatFormatting.GRAY));
	}
}

