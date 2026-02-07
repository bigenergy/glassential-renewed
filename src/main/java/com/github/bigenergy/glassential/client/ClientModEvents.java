package com.github.bigenergy.glassential.client;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import com.github.bigenergy.glassential.client.model.OneWayBlockStateModel;
import com.github.bigenergy.glassential.init.GlassentialBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
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
            // Note: Render layers are now defined in block properties or JSON in 1.21.4+
            // ItemBlockRenderTypes.setRenderLayer is deprecated/removed

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
