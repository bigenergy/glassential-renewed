package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public final class GlassentialTags {
    private GlassentialTags(){}

    /** Namespace used by the (Neo)Forge "conventional" tags on newer versions; kept for cross-version parity. */
    private static final String CONVENTION_NS = "c";

    public static final class Blocks {
        public static final TagKey<Block> DOORS_DYED = tag("doors/dyed"); // common mod-wide tag
        public static final Map<DyeColor, TagKey<Block>> DOORS_DYED_BY_COLOR = colored("doors/dyed/");

        public static final TagKey<Block> TRAPDOORS_DYED = tag("trapdoors/dyed");
        public static final Map<DyeColor, TagKey<Block>> TRAPDOORS_DYED_BY_COLOR = colored("trapdoors/dyed/");

        private static TagKey<Block> tag(String path) {
            return BlockTags.create(new ResourceLocation(Glassential.MODID, path));
        }
        private static Map<DyeColor, TagKey<Block>> colored(String base) {
            Map<DyeColor, TagKey<Block>> map = new EnumMap<>(DyeColor.class);
            Arrays.stream(DyeColor.values()).forEach(c ->
                    map.put(c, tag(base + c.getName()))
            );
            return map;
        }
    }

    public static final class Items {
        public static final TagKey<Item> DOORS_DYED = itag("doors/dyed");
        public static final Map<DyeColor, TagKey<Item>> DOORS_DYED_BY_COLOR = icolored("doors/dyed/");

        public static final TagKey<Item> TRAPDOORS_DYED = itag("trapdoors/dyed");
        public static final Map<DyeColor, TagKey<Item>> TRAPDOORS_DYED_BY_COLOR = icolored("trapdoors/dyed/");

        private static TagKey<Item> itag(String path) {
            return ItemTags.create(new ResourceLocation(Glassential.MODID, path));
        }
        private static Map<DyeColor, TagKey<Item>> icolored(String base) {
            Map<DyeColor, TagKey<Item>> map = new EnumMap<>(DyeColor.class);
            Arrays.stream(DyeColor.values()).forEach(c ->
                    map.put(c, itag(base + c.getName()))
            );
            return map;
        }
    }

    public static class Fluids {
        public static final TagKey<Fluid> CLEAR_FLUID_GLASS_FLUIDS_TAG = tag("clear_fluid_glass_fluids");

        private static TagKey<Fluid> tag(String name) {
            return FluidTags.create(new ResourceLocation(Glassential.MODID, name.toLowerCase(Locale.ROOT)));
        }
    }

    /**
     * Conventional tags.
     * <p>
     * On NeoForge 1.21+ these are exposed as {@code Tags.Blocks.DYED} / {@code Tags.Blocks.DYED_*}.
     * Forge 1.20.1 has no equivalent constants (its conventional tags live in the {@code forge}
     * namespace and do not include a "dyed" family), so they are declared explicitly here in
     * order to keep the emitted data identical across branches.
     */
    public static final class Convention {
        public static final TagKey<Block> DYED_BLOCK = BlockTags.create(new ResourceLocation(CONVENTION_NS, "dyed"));

        public static TagKey<Block> dyedBlock(DyeColor color) {
            return BlockTags.create(new ResourceLocation(CONVENTION_NS, "dyed/" + color.getName()));
        }

        public static TagKey<Item> dyedItem(DyeColor color) {
            return ItemTags.create(new ResourceLocation(CONVENTION_NS, "dyed/" + color.getName()));
        }

        private Convention() {}
    }
}
