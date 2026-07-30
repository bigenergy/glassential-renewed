package com.github.bigenergy.glassential.init;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.blocks.entity.ClearFluidGlassBlockEntity;
import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GlassentialBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Glassential.MODID);

    public static final RegistryObject<BlockEntityType<OneWayGlassBlockEntity>> ONE_WAY_GLASS =
            BLOCK_ENTITIES.register("one_way_glass",
                    () -> BlockEntityType.Builder.of(
                            OneWayGlassBlockEntity::new,
                            GlassentialBlocks.ONE_WAY_GLASS.get(),
                            GlassentialBlocks.TINTED_ONE_WAY_GLASS.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<ClearFluidGlassBlockEntity>> CLEAR_FLUID_GLASS =
            BLOCK_ENTITIES.register("clear_fluid_glass",
                    () -> BlockEntityType.Builder.of(
                            ClearFluidGlassBlockEntity::new,
                            GlassentialBlocks.CLEAR_FLUID_GLASS.get(),
                            GlassentialBlocks.CLEAR_FLUID_FAKE_GLASS.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<ColorableGlassBlockEntity>> COLORABLE_GLASS =
            BLOCK_ENTITIES.register("colorable_glass",
                    () -> BlockEntityType.Builder.of(
                            ColorableGlassBlockEntity::new,
                            GlassentialBlocks.COLORABLE_GLASS.get(),
                            GlassentialBlocks.COLORABLE_STAINED_GLASS.get(),
                            GlassentialBlocks.COLORABLE_GLASS_PANE.get(),
                            GlassentialBlocks.COLORABLE_STAINED_GLASS_PANE.get()
                    ).build(null)
            );

    /**
     * Alias kept for parity with the 1.21 source: the stained variant shares one
     * BlockEntityType with the plain colorable glass.
     */
    public static final RegistryObject<BlockEntityType<ColorableGlassBlockEntity>> COLORABLE_STAINED_GLASS =
            COLORABLE_GLASS;
}
