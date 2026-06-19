package com.github.bigenergy.glassential.client;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import com.github.bigenergy.glassential.client.gui.GlassPainterScreen;
import com.github.bigenergy.glassential.client.model.OneWayBlockStateModel;
import com.github.bigenergy.glassential.init.GlassentialBlocks;
import com.github.bigenergy.glassential.items.GlassPainterItem;
import com.github.bigenergy.glassential.network.GlassPainterPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@EventBusSubscriber(modid = Glassential.MODID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register render types for translucent glass blocks
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.ONE_WAY_GLASS.get(), ChunkSectionLayer.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.TINTED_ONE_WAY_GLASS.get(), ChunkSectionLayer.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.CLEAR_FLUID_GLASS.get(), ChunkSectionLayer.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.CLEAR_FLUID_FAKE_GLASS.get(), ChunkSectionLayer.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.COLORABLE_GLASS.get(), ChunkSectionLayer.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.COLORABLE_STAINED_GLASS.get(), ChunkSectionLayer.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.COLORABLE_GLASS_PANE.get(), ChunkSectionLayer.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.COLORABLE_STAINED_GLASS_PANE.get(), ChunkSectionLayer.TRANSLUCENT);

            // Register client-side eyedropper for the Glass Painter item.
            // All client-only class references (Minecraft, BlockColors, ClientPacketDistributor)
            // live here so they never end up in GlassPainterItem's bytecode.
            GlassPainterItem.EYEDROPPER = ctx -> {
                Level level = ctx.getLevel();
                var pos = ctx.getClickedPos();
                var player = ctx.getPlayer();
                ItemStack stack = ctx.getItemInHand();
                if (player == null) return;

                BlockState state = level.getBlockState(pos);
                BlockEntity be = level.getBlockEntity(pos);

                int color = -1;

                if (be instanceof ColorableGlassBlockEntity colorable) {
                    color = colorable.getColor() & 0xFFFFFF;
                }

                if (color == -1) {
                    int tint = Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0);
                    if (tint != -1) {
                        color = tint & 0xFFFFFF;
                    }
                }

                if (color == -1) {
                    MapColor mc = state.getMapColor(level, pos);
                    if (mc != null && mc != MapColor.NONE) {
                        color = mc.col & 0xFFFFFF;
                    }
                }

                if (color == -1) {
                    player.displayClientMessage(
                        Component.translatable("message.glassential.brush.copy_failed")
                            .withStyle(ChatFormatting.RED),
                        true
                    );
                    return;
                }

                CustomData cd = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                CompoundTag t = cd.copyTag();
                ClientPacketDistributor.sendToServer(new GlassPainterPacket(
                    color,
                    t.getBooleanOr("EmitLight", false),
                    t.getBooleanOr("EmitRedstone", false),
                    t.getBooleanOr("PassPlayer", false),
                    t.getBooleanOr("PassEntity", false)
                ));

                String hex = String.format("#%06X", color & 0xFFFFFF);
                player.displayClientMessage(
                    Component.translatable("message.glassential.brush.copy_success", hex)
                        .withStyle(ChatFormatting.GREEN),
                    true
                );
            };

            // Register client-side screen opener for the Glass Painter item
            GlassPainterItem.OPEN_SCREEN = stack -> {
                CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                CompoundTag tag = customData.copyTag();
                int currentColor = tag.getIntOr("Color", 0xFFFFFF);
                if (currentColor == 0) currentColor = 0xFFFFFF;
                boolean emitLight = tag.getBooleanOr("EmitLight", false);
                boolean emitRedstone = tag.getBooleanOr("EmitRedstone", false);
                boolean passPlayer = tag.getBooleanOr("PassPlayer", false);
                boolean passEntity = tag.getBooleanOr("PassEntity", false);
                Minecraft.getInstance().setScreen(
                    new GlassPainterScreen(stack, currentColor, emitLight, emitRedstone, passPlayer, passEntity)
                );
            };

            BlockColors bc = Minecraft.getInstance().getBlockColors();

            // reentrancy guard
            final ThreadLocal<Boolean> REENTRANT = ThreadLocal.withInitial(() -> false);

            bc.register((state, level, pos, tintIndex) -> {
                if (level == null || pos == null) return -1;

                if (Boolean.TRUE.equals(REENTRANT.get())) return -1;

                var be = level.getBlockEntity(pos);
                if (be instanceof OneWayGlassBlockEntity ow) {
                    BlockState mimic = ow.getMimic();

                    if (mimic == null || mimic.isAir()) return -1;
                    if (mimic.getBlock() == state.getBlock()) return -1;

                    try {
                        REENTRANT.set(true);
                        return bc.getColor(mimic, level, pos, tintIndex);
                    } finally {
                        REENTRANT.set(false);
                    }
                }
                return -1;
            }, GlassentialBlocks.ONE_WAY_GLASS.get(), GlassentialBlocks.TINTED_ONE_WAY_GLASS.get());

            // Colorable glass
            bc.register((state, level, pos, tintIndex) -> {
                if (level == null || pos == null) return 0xFFFFFF;

                var be = level.getBlockEntity(pos);
                if (be instanceof ColorableGlassBlockEntity colorable) {
                    return colorable.getColor();
                }
                return 0xFFFFFF;
            }, GlassentialBlocks.COLORABLE_GLASS.get());

            // Colorable stained glass
            bc.register((state, level, pos, tintIndex) -> {
                if (level == null || pos == null) return 0xFFFFFF;

                var be = level.getBlockEntity(pos);
                if (be instanceof ColorableGlassBlockEntity colorable) {
                    return colorable.getColor();
                }
                return 0xFFFFFF;
            }, GlassentialBlocks.COLORABLE_STAINED_GLASS.get());

            // Colorable glass panes (same handler — same BE type)
            bc.register((state, level, pos, tintIndex) -> {
                if (level == null || pos == null) return 0xFFFFFF;
                var be = level.getBlockEntity(pos);
                if (be instanceof ColorableGlassBlockEntity colorable) {
                    return colorable.getColor();
                }
                return 0xFFFFFF;
            }, GlassentialBlocks.COLORABLE_GLASS_PANE.get(), GlassentialBlocks.COLORABLE_STAINED_GLASS_PANE.get());
        });
    }

    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        // Wrap one-way glass models with our custom model for each block state
        for (var block : new net.minecraft.world.level.block.Block[]{
                GlassentialBlocks.ONE_WAY_GLASS.get(),
                GlassentialBlocks.TINTED_ONE_WAY_GLASS.get()
        }) {
            block.getStateDefinition().getPossibleStates().forEach(state -> {
                event.getBakingResult().blockStateModels().computeIfPresent(
                        state,
                        (bs, model) -> {
                            if (model instanceof OneWayBlockStateModel) {
                                return model;
                            }
                            return new OneWayBlockStateModel(model);
                        }
                );
            });
        }
    }
}
