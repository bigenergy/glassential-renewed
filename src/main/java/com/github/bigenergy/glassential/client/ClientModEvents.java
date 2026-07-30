package com.github.bigenergy.glassential.client;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import com.github.bigenergy.glassential.client.model.OneWayModelGeometry;
import com.github.bigenergy.glassential.init.GlassentialBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Glassential.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        event.enqueueWork(() -> {
            // One Way and Tinted One Way use cutout so weather (snow/rain/clouds) renders
            // correctly through them. Their JSON models set render_type=cutout too.
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.ONE_WAY_GLASS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.TINTED_ONE_WAY_GLASS.get(), RenderType.cutout());
            // Clear Fluid and Colorable need real alpha blending (semi-transparent /
            // dynamic RGB tint), so they stay on translucent — weather won't render
            // through them but that's an acceptable trade for the visual.
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.CLEAR_FLUID_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.CLEAR_FLUID_FAKE_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.COLORABLE_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.COLORABLE_STAINED_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.COLORABLE_GLASS_PANE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.COLORABLE_STAINED_GLASS_PANE.get(), RenderType.translucent());

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

            // Colorable glass pane (same handler as the cube — same BE type)
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
    public static void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders e) {
        // Forge 1.20.1 takes a bare name and prefixes the active mod's namespace,
        // producing "glassential:one_way_loader".
        e.register("one_way_loader", new OneWayModelGeometry.Loader());
    }

    // Model wrapping is handled by ConnectingBakedModelMixin
}
