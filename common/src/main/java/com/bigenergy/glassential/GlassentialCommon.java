package com.bigenergy.glassential;

import com.bigenergy.glassential.init.GlassentialBlocks;
import com.bigenergy.glassential.init.GlassentialTab;
import net.minecraft.resources.ResourceLocation;

public class GlassentialCommon {

    public static void init() {
        GlassentialBlocks.load();
        GlassentialTab.load();
    }

    public static ResourceLocation id(String string) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, string);
    }
}