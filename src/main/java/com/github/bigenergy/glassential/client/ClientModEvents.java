package com.github.bigenergy.glassential.client;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import com.github.bigenergy.glassential.client.gui.GlassPainterScreen;
import com.github.bigenergy.glassential.client.model.OneWayBlockStateModel;
import com.github.bigenergy.glassential.client.renderer.OneWayGlassBlockEntityRenderer;
import com.github.bigenergy.glassential.init.GlassentialBlockEntities;
import com.github.bigenergy.glassential.init.GlassentialBlocks;
import com.github.bigenergy.glassential.items.GlassPainterItem;
import com.github.bigenergy.glassential.network.GlassPainterPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
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
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.List;

@EventBusSubscriber(modid = Glassential.MODID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Render types are now defined exclusively via the "render_type" field in each
            // block model JSON (e.g. "render_type": "translucent"). ItemBlockRenderTypes
            // was removed in 26.x — there's no Java-side per-block render-layer registration.

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
                    // 26.x: BlockColors.getColor is gone; iterate registered tint sources instead.
                    // colorInWorld wants a BlockAndTintGetter — on the client, the level
                    // is always a ClientLevel which implements it.
                    BlockAndTintGetter tintLevel = Minecraft.getInstance().level;
                    if (tintLevel != null) {
                        List<BlockTintSource> tintSources = Minecraft.getInstance().getBlockColors().getTintSources(state);
                        for (BlockTintSource src : tintSources) {
                            int tint = src.colorInWorld(state, tintLevel, pos);
                            if (tint != -1 && (tint & 0xFFFFFF) != 0xFFFFFF) {
                                color = tint & 0xFFFFFF;
                                break;
                            }
                        }
                    }
                }

                if (color == -1) {
                    MapColor mc = state.getMapColor(level, pos);
                    if (mc != null && mc != MapColor.NONE) {
                        color = mc.col & 0xFFFFFF;
                    }
                }

                if (color == -1) {
                    player.sendOverlayMessage(
                        Component.translatable("message.glassential.brush.copy_failed")
                            .withStyle(ChatFormatting.RED)
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
                player.sendOverlayMessage(
                    Component.translatable("message.glassential.brush.copy_success", hex)
                        .withStyle(ChatFormatting.GREEN)
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
                Minecraft.getInstance().setScreenAndShow(
                    new GlassPainterScreen(stack, currentColor, emitLight, emitRedstone, passPlayer, passEntity)
                );
            };

            BlockColors bc = Minecraft.getInstance().getBlockColors();

            // 26.x: BlockColors.register now takes a List<BlockTintSource> + Block varargs.
            // The lambda-based (state, level, pos, tintIdx) -> int form is gone.
            // We wrap each color logic in a BlockTintSource and override colorInWorld
            // (the no-context color(state) fallback returns -1 / white).

            // One Way Glass — mirror the mimic block's color.
            final ThreadLocal<Boolean> REENTRANT = ThreadLocal.withInitial(() -> false);
            BlockTintSource oneWayTint = new BlockTintSource() {
                @Override
                public int color(BlockState state) {
                    return 0xFFFFFFFF;
                }
                @Override
                public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                    if (Boolean.TRUE.equals(REENTRANT.get())) return 0xFFFFFFFF;
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof OneWayGlassBlockEntity ow) {
                        BlockState mimic = ow.getMimic();
                        if (mimic == null || mimic.isAir()) return 0xFFFFFFFF;
                        if (mimic.getBlock() == state.getBlock()) return 0xFFFFFFFF;
                        try {
                            REENTRANT.set(true);
                            List<BlockTintSource> srcs = bc.getTintSources(mimic);
                            if (srcs.isEmpty()) return 0xFFFFFFFF;
                            int c = srcs.get(0).colorInWorld(mimic, level, pos);
                            // ensure full alpha for translucent rendering
                            return 0xFF000000 | (c & 0xFFFFFF);
                        } finally {
                            REENTRANT.set(false);
                        }
                    }
                    return 0xFFFFFFFF;
                }
            };
            bc.register(List.of(oneWayTint),
                    GlassentialBlocks.ONE_WAY_GLASS.get(),
                    GlassentialBlocks.TINTED_ONE_WAY_GLASS.get());

            // Colorable glass / stained / panes — all read from ColorableGlassBlockEntity.
            BlockTintSource colorableTint = new BlockTintSource() {
                @Override
                public int color(BlockState state) {
                    // 26.x BlockTintSource expects ARGB; alpha 0 makes translucent rendering invisible.
                    return 0xFFFFFFFF;
                }
                @Override
                public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof ColorableGlassBlockEntity colorable) {
                        return 0xFF000000 | (colorable.getColor() & 0xFFFFFF);
                    }
                    return 0xFFFFFFFF;
                }
            };
            bc.register(List.of(colorableTint),
                    GlassentialBlocks.COLORABLE_GLASS.get(),
                    GlassentialBlocks.COLORABLE_STAINED_GLASS.get(),
                    GlassentialBlocks.COLORABLE_GLASS_PANE.get(),
                    GlassentialBlocks.COLORABLE_STAINED_GLASS_PANE.get());
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

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                GlassentialBlockEntities.ONE_WAY_GLASS.get(),
                OneWayGlassBlockEntityRenderer::new
        );
    }
}
