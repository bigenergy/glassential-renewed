package com.github.bigenergy.glassential.init;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.blocks.entity.ClearFluidGlassBlockEntity;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GlassentialBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Glassential.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OneWayGlassBlockEntity>> ONE_WAY_GLASS =
            BLOCK_ENTITIES.register("one_way_glass",
                    () -> BlockEntityType.Builder.of(
                            OneWayGlassBlockEntity::new,
                            GlassentialBlocks.ONE_WAY_GLASS.get()
                    ).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ClearFluidGlassBlockEntity>> CLEAR_FLUID_GLASS =
            BLOCK_ENTITIES.register("clear_fluid_glass",
                    () -> BlockEntityType.Builder.of(
                            ClearFluidGlassBlockEntity::new,
                            GlassentialBlocks.CLEAR_FLUID_GLASS.get()
                    ).build(null)
            );
}
