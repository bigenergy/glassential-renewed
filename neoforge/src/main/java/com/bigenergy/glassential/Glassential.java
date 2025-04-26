package com.bigenergy.glassential;


import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@Mod(Constants.MOD_ID)
public class Glassential {

    public Glassential(IEventBus eventBus) {

        Constants.LOG.info("Loading Glassential Renewed - NeoForge");
        GlassentialCommon.init();
        eventBus.addListener(this::packSetup);

    }

    public void packSetup(AddPackFindersEvent event) {
        event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "resourcepacks/glassential_fusion"),
                PackType.CLIENT_RESOURCES,
                Component.translatable("pack.glassential_fusion.name"),
                PackSource.BUILT_IN,
                false,
                Pack.Position.TOP);
        event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "resourcepacks/glassential_vanilla_fusion"),
                PackType.CLIENT_RESOURCES,
                Component.translatable("pack.glassential_vanilla_fusion.name"),
                PackSource.BUILT_IN,
                false,
                Pack.Position.TOP);
    }
}