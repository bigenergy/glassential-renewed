package lykrast.glassential.init;

import lykrast.glassential.Glassential;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class GItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Glassential.MODID);

    public static void register (IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
