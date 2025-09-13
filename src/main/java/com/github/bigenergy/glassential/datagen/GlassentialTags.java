package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public final class GlassentialTags {
    private GlassentialTags(){}

    public static final class Blocks {
        public static final TagKey<Block> DOORS_DYED = tag("doors/dyed"); // общий модовый тег
        public static final Map<DyeColor, TagKey<Block>> DOORS_DYED_BY_COLOR = colored("doors/dyed/");

        public static final TagKey<Block> TRAPDOORS_DYED = tag("trapdoors/dyed");
        public static final Map<DyeColor, TagKey<Block>> TRAPDOORS_DYED_BY_COLOR = colored("trapdoors/dyed/");

        private static TagKey<Block> tag(String path) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Glassential.MODID, path));
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
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Glassential.MODID, path));
        }
        private static Map<DyeColor, TagKey<Item>> icolored(String base) {
            Map<DyeColor, TagKey<Item>> map = new EnumMap<>(DyeColor.class);
            Arrays.stream(DyeColor.values()).forEach(c ->
                    map.put(c, itag(base + c.getName()))
            );
            return map;
        }
    }
}
