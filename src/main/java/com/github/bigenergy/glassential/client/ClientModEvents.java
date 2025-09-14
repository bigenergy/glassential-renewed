package com.github.bigenergy.glassential.client;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import com.github.bigenergy.glassential.client.model.OneWayModelGeometry;
import com.github.bigenergy.glassential.init.GlassentialBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = Glassential.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.ONE_WAY_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.CLEAR_FLUID_GLASS.get(), RenderType.translucent());

            BlockColors bc = Minecraft.getInstance().getBlockColors();
            bc.register((state, level, pos, tintIndex) -> {
                if (level == null || pos == null) return -1;
                var be = level.getBlockEntity(pos);
                if (be instanceof OneWayGlassBlockEntity ow) {
                    BlockState mimic = ow.getMimic();
                    return Minecraft.getInstance().getBlockColors().getColor(mimic, level, pos, tintIndex);
                }
                return -1; // нет тинта
            }, GlassentialBlocks.ONE_WAY_GLASS.get());
        });
    }


    @SubscribeEvent
    public static void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders e) {
        e.register(ResourceLocation.fromNamespaceAndPath(Glassential.MODID, "one_way_loader"), new OneWayModelGeometry.Loader());
    }



}
