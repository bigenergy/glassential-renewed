package com.bigenergy.glassential;

import com.bigenergy.glassential.init.GlassentialBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

// big thanks Up-Mods - https://github.com/Up-Mods/Glassential/blob/1.20.4/src/main/java/dev/upcraft/glassential/GlassentialClient.java
public class GlassentialClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(
                RenderType.cutout(),
                GlassentialBlocks.GLASS_ETHEREAL.get(),
                GlassentialBlocks.GLASS_ETHEREAL_REVERSE.get(),
                GlassentialBlocks.GLASS_GHOSTLY.get(),
                GlassentialBlocks.GLASS_LIGHT.get(),
                GlassentialBlocks.GLASS_REDSTONE.get(),
                GlassentialBlocks.OBSIDIAN_GLASS.get()
        );


        BlockRenderLayerMap.INSTANCE.putBlocks(
                RenderType.translucent(),
                GlassentialBlocks.GLASS_DARK_ETHEREAL.get(),
                GlassentialBlocks.GLASS_DARK_ETHEREAL_REVERSE.get(),
                GlassentialBlocks.GLASS_DOOR.get(),

                GlassentialBlocks.DARK_ETHEREAL_DOOR.get(),
                GlassentialBlocks.DARK_ETHEREAL_REVERSE_DOOR.get(),
                GlassentialBlocks.ETHEREAL_DOOR.get(),
                GlassentialBlocks.ETHEREAL_REVERSE_DOOR.get(),
                GlassentialBlocks.LIGHT_DOOR.get(),
                GlassentialBlocks.REDSTONE_DOOR.get(),
                GlassentialBlocks.GHOSTLY_DOOR.get(),
                GlassentialBlocks.OBSIDIAN_DOOR.get(),

                GlassentialBlocks.GLASS_TRAPDOOR.get(),
                GlassentialBlocks.DARK_ETHEREAL_TRAPDOOR.get(),
                GlassentialBlocks.DARK_ETHEREAL_REVERSE_TRAPDOOR.get(),
                GlassentialBlocks.ETHEREAL_TRAPDOOR.get(),
                GlassentialBlocks.ETHEREAL_REVERSE_TRAPDOOR.get(),
                GlassentialBlocks.REDSTONE_TRAPDOOR.get(),
                GlassentialBlocks.GHOSTLY_TRAPDOOR.get(),
                GlassentialBlocks.LIGHT_TRAPDOOR.get(),
                GlassentialBlocks.OBSIDIAN_TRAPDOOR.get(),

                GlassentialBlocks.GLASS_DARK_ETHEREAL_PANE.get(),
                GlassentialBlocks.GLASS_DARK_ETHEREAL_REVERSE_PANE.get(),
                GlassentialBlocks.GLASS_REDSTONE_PANE.get(),
                GlassentialBlocks.GLASS_GHOSTLY_PANE.get(),
                GlassentialBlocks.GLASS_LIGHT_PANE.get(),

                GlassentialBlocks.BLACK_GLASS_DOOR.get(),
                GlassentialBlocks.BLUE_GLASS_DOOR.get(),
                GlassentialBlocks.BROWN_GLASS_DOOR.get(),
                GlassentialBlocks.CYAN_GLASS_DOOR.get(),
                GlassentialBlocks.GRAY_GLASS_DOOR.get(),
                GlassentialBlocks.GREEN_GLASS_DOOR.get(),
                GlassentialBlocks.LIGHT_BLUE_GLASS_DOOR.get(),
                GlassentialBlocks.LIGHT_GRAY_GLASS_DOOR.get(),
                GlassentialBlocks.LIME_GLASS_DOOR.get(),
                GlassentialBlocks.MAGENTA_GLASS_DOOR.get(),
                GlassentialBlocks.ORANGE_GLASS_DOOR.get(),
                GlassentialBlocks.PINK_GLASS_DOOR.get(),
                GlassentialBlocks.PURPLE_GLASS_DOOR.get(),
                GlassentialBlocks.RED_GLASS_DOOR.get(),
                GlassentialBlocks.TINTED_GLASS_DOOR.get(),
                GlassentialBlocks.WHITE_GLASS_DOOR.get(),
                GlassentialBlocks.YELLOW_GLASS_DOOR.get(),

                GlassentialBlocks.BLACK_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.BLUE_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.BROWN_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.CYAN_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.GRAY_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.GREEN_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.LIGHT_BLUE_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.LIGHT_GRAY_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.LIME_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.MAGENTA_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.ORANGE_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.PINK_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.PURPLE_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.RED_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.TINTED_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.WHITE_GLASS_TRAPDOOR.get(),
                GlassentialBlocks.YELLOW_GLASS_TRAPDOOR.get()
        );
    }
}
