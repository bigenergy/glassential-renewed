package com.bigenergy.glassential;

import com.bigenergy.glassential.init.GlassentialBlocks;
import com.bigenergy.glassential.registration.RegistrationProvider;
import com.bigenergy.glassential.registration.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class GlassentialCommon {
    public static final RegistrationProvider<CreativeModeTab> CREATIVE_TABS = RegistrationProvider.get(BuiltInRegistries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    public static final RegistryObject<CreativeModeTab> GROUP = CREATIVE_TABS.register("tab", () -> new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(GlassentialBlocks.GLASS_DARK_ETHEREAL.get()))
            .title(Component.translatable("itemGroup." + Constants.MOD_ID))
            .displayItems((featureFlagSet, tabOutput) -> {
                GlassentialBlocks.ITEMS_FOR_TAB_LIST.forEach(registryObject -> tabOutput.accept(new ItemStack(registryObject.get())));
            }).build()
    );

    public static void init() {
        GlassentialBlocks.load();
    }
}