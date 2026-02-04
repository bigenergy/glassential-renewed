package com.github.bigenergy.glassential.client;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import com.github.bigenergy.glassential.init.GlassentialBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

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

    // TODO: One-way glass model wrapping disabled - model system changed in 1.21.4+
    // Need to implement using new BlockStateModel system
}
