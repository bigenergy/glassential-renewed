package com.bigenergy.glassential.init;

import com.bigenergy.glassential.Constants;
import com.bigenergy.glassential.registration.RegistrationProvider;
import com.bigenergy.glassential.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class GlassentialTab {
    /**
     * The provider for creative tabs
     */
    public static final RegistrationProvider<CreativeModeTab> TABS = RegistrationProvider.get(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static final RegistryObject<CreativeModeTab> GLASSENTIAL_TAB = TABS.register(Constants.MOD_ID, () -> CreativeModeTab
            .builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup." + Constants.MOD_ID))
            .icon(() -> new ItemStack(GlassentialBlocks.GLASS_DARK_ETHEREAL.get()))
            .displayItems((itemDisplayParameters, tabOutput) -> {
                GlassentialBlocks.ITEMS_FOR_TAB_LIST.forEach(registryObject -> tabOutput.accept(new ItemStack(registryObject.get())));
            })
            .build());

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
