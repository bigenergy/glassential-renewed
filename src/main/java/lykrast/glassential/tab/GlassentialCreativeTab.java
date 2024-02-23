package lykrast.glassential.tab;

import lykrast.glassential.Glassential;
import lykrast.glassential.init.GBlocks;
import lykrast.glassential.init.GItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegisterEvent;

public class GlassentialCreativeTab {
    public static final ResourceKey<CreativeModeTab> GLASSENTIAL = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation("glassential", Glassential.MODID));

    public static void registerTab(RegisterEvent event) {
        event.register(Registries.CREATIVE_MODE_TAB, helper -> {
            helper.register(GLASSENTIAL, CreativeModeTab.builder().displayItems((flags, output) -> GItems.ITEMS.getEntries().forEach(o -> output.accept(o.get()))).title(Component.translatable("itemGroup.glassential")).icon(() -> { return new ItemStack(GBlocks.GLASS_DARK_ETHEREAL.get()); }).build());

        });
    }
}
