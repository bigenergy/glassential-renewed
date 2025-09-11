package com.bigenergy.glassential.datagen;

import com.bigenergy.glassential.init.GlassentialBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.CompletableFuture;

/**
 * Generates block loot tables.
 */
public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    public ModBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        // Add loot table generation here

       // dropWhenSilkTouch(GlassentialBlocks.GLASS_ETHEREAL.get());

       // GlassentialBlocks.ITEMS_FOR_TAB_LIST.forEach(registryObject ->  dropWhenSilkTouch(registryObject.get()));


        GlassentialBlocks.BLOCK_DEFERRED.getEntries().forEach(registryObject -> dropWhenSilkTouch(registryObject.get()));
    }
}
