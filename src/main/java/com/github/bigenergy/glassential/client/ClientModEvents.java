package com.github.bigenergy.glassential.client;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import com.github.bigenergy.glassential.client.gui.GlassPainterScreen;
import com.github.bigenergy.glassential.client.model.OneWayBlockStateModel;
import com.github.bigenergy.glassential.init.GlassentialBlocks;
import com.github.bigenergy.glassential.items.GlassPainterItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = Glassential.MODID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register render types for translucent glass blocks
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.ONE_WAY_GLASS.get(), ChunkSectionLayer.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.CLEAR_FLUID_GLASS.get(), ChunkSectionLayer.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.CLEAR_FLUID_FAKE_GLASS.get(), ChunkSectionLayer.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.COLORABLE_GLASS.get(), ChunkSectionLayer.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.COLORABLE_STAINED_GLASS.get(), ChunkSectionLayer.TRANSLUCENT);

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
            }, GlassentialBlocks.ONE_WAY_GLASS.get());

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
        });
    }

    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        // Wrap one-way glass models with our custom model for each block state
        GlassentialBlocks.ONE_WAY_GLASS.get().getStateDefinition().getPossibleStates().forEach(state -> {
            event.getBakingResult().blockStateModels().computeIfPresent(
                    state,
                    (bs, model) -> {
                        if (model instanceof OneWayBlockStateModel) {
                            return model; // Already wrapped
                        }
                        return new OneWayBlockStateModel(model);
                    }
            );
        });
    }
}
