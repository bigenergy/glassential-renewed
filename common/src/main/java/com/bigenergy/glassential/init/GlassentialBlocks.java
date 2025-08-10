package com.bigenergy.glassential.init;

import com.bigenergy.glassential.Constants;
import com.bigenergy.glassential.blocks.*;
import com.bigenergy.glassential.blocks.slabs.GlassSlabBlock;
import com.bigenergy.glassential.doors.*;
import com.bigenergy.glassential.panes.*;
import com.bigenergy.glassential.registration.RegistrationProvider;
import com.bigenergy.glassential.registration.RegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class GlassentialBlocks {

    private static final RegistrationProvider<Block> BLOCK_DEFERRED = RegistrationProvider.get(BuiltInRegistries.BLOCK, Constants.MOD_ID);
    private static final RegistrationProvider<Item> ITEM_DEFERRED = RegistrationProvider.get(BuiltInRegistries.ITEM, Constants.MOD_ID);
    public static final Collection<RegistryObject<Item>> ITEMS_FOR_TAB_LIST = new ArrayList<>();

    // Glasses
    public static final RegistryObject<Block> GLASS_DARK_ETHEREAL = registerBlock("glass_dark_ethereal",
            () -> new DarkEtherealGlassBlock(glassProp().noCollission(), false));
    public static final RegistryObject<Block> GLASS_DARK_ETHEREAL_REVERSE = registerBlock("glass_dark_ethereal_reverse",
            () -> new DarkEtherealGlassBlock(glassProp().noCollission(), true));
    public static final RegistryObject<Block> GLASS_ETHEREAL = registerBlock("glass_ethereal",
            () -> new EtherealGlassBlock(glassProp().noCollission(), false));
    public static final RegistryObject<Block> GLASS_ETHEREAL_REVERSE = registerBlock("glass_ethereal_reverse",
            () -> new EtherealGlassBlock(glassProp().noCollission(), true));
    public static final RegistryObject<Block> GLASS_GHOSTLY = registerBlock("glass_ghostly",
            () -> new TooltipGlassBlock(glassProp().noCollission(), "tooltip.glassential.ghostly"));
    public static final RegistryObject<Block> GLASS_LIGHT = registerBlock("glass_light",
            () -> new TooltipGlassBlock(glassProp().lightLevel((b) -> 15), "tooltip.glassential.light"));
    public static final RegistryObject<Block> GLASS_LIGHT_TINTED = registerBlock("glass_light_tinted",
            () -> new TooltipGlassBlock(glassProp().lightLevel((b) -> 15), "tooltip.glassential.light"));
    public static final RegistryObject<Block> GLASS_REDSTONE = registerBlock("glass_redstone",
            () -> new RedstoneGlassBlock(glassProp()));
    public static final RegistryObject<Block> GLASS_REDSTONE_TINTED = registerBlock("glass_redstone_tinted",
            () -> new RedstoneGlassBlock(glassProp()));
    public static final RegistryObject<Block> OBSIDIAN_GLASS = registerBlock("obsidian_glass",
            () -> new TooltipGlassBlock(glassProtectedProp(), "tooltip.glassential.protected"));
    public static final RegistryObject<Block> STONE_GLASS = registerBlock("stone_glass",
            () -> new SimpleGlassBlock(glassProp()));
    public static final RegistryObject<Block> SANDSTONE_GLASS = registerBlock("sandstone_glass",
            () -> new SimpleGlassBlock(glassProp()));
    public static final RegistryObject<Block> ICE_GLASS = registerBlock("ice_glass",
            () -> new IceGlassBlock(glassProp(), "tooltip.glassential.ice"));
    public static final RegistryObject<Block> IRON_GLASS = registerBlock("iron_glass",
            () -> new SimpleGlassBlock(glassProp()));

    // Glass Lamps
    public static final RegistryObject<Block> GLASS_LAVA_LAMP = registerBlock("glass_lava_lamp",
            () -> new CustomGlassBlock(glassProp().lightLevel((b) -> 15), "tooltip.glassential.lava_lamp"));
    public static final RegistryObject<Block> GLASS_LAVA_LAMP_TINTED = registerBlock("glass_lava_lamp_tinted",
            () -> new CustomGlassBlock(glassProp().lightLevel((b) -> 15), "tooltip.glassential.lava_lamp"));
    public static final RegistryObject<Block> GLASS_GLOWSTONE_LAMP = registerBlock("glass_glowstone_lamp",
            () -> new TooltipGlassBlock(glassProp().lightLevel((b) -> 15), "tooltip.glassential.light"));
    public static final RegistryObject<Block> GLASS_GLOWSTONE_LAMP_TINTED = registerBlock("glass_glowstone_lamp_tinted",
            () -> new TooltipGlassBlock(glassProp().lightLevel((b) -> 15), "tooltip.glassential.light"));

    // Glass Doors
    public static final RegistryObject<Block> GLASS_DOOR = registerGlassDoor("glass_door");
    public static final RegistryObject<Block> DARK_ETHEREAL_DOOR = registerGlassDoor("dark_ethereal_door", false);
    public static final RegistryObject<Block> DARK_ETHEREAL_REVERSE_DOOR = registerGlassDoor("dark_ethereal_reverse_door", true);
    public static final RegistryObject<Block> ETHEREAL_DOOR = registerGlassDoor("ethereal_door", false);
    public static final RegistryObject<Block> ETHEREAL_REVERSE_DOOR = registerGlassDoor("ethereal_reverse_door", true);
    public static final RegistryObject<Block> LIGHT_DOOR = registerBlock("light_door",
            () -> new GlassLightDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel((b) -> 15), BlockSetType.OAK));
    public static final RegistryObject<Block> REDSTONE_DOOR = registerBlock("redstone_door",
            () -> new GlassRedstoneDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));
    public static final RegistryObject<Block> GHOSTLY_DOOR = registerGlassDoor("ghostly_door");
    public static final RegistryObject<Block> OBSIDIAN_DOOR = registerBlock("obsidian_door",
            () -> new ObsidianGlassDoor(glassProtectedProp(), BlockSetType.IRON));

    // Glass Trapdoors
    public static final RegistryObject<Block> GLASS_TRAPDOOR = registerGlassTrapdoor("glass_trapdoor");
    public static final RegistryObject<Block> DARK_ETHEREAL_TRAPDOOR = registerGlassTrapdoor("dark_ethereal_trapdoor", false);
    public static final RegistryObject<Block> DARK_ETHEREAL_REVERSE_TRAPDOOR = registerGlassTrapdoor("dark_ethereal_reverse_trapdoor", true);
    public static final RegistryObject<Block> ETHEREAL_TRAPDOOR = registerGlassTrapdoor("ethereal_trapdoor", false);
    public static final RegistryObject<Block> ETHEREAL_REVERSE_TRAPDOOR = registerGlassTrapdoor("ethereal_reverse_trapdoor", true);
    public static final RegistryObject<Block> REDSTONE_TRAPDOOR = registerBlock("redstone_trapdoor",
            () -> new GlassRedstoneTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));
    public static final RegistryObject<Block> GHOSTLY_TRAPDOOR = registerGlassTrapdoor("ghostly_trapdoor");
    public static final RegistryObject<Block> LIGHT_TRAPDOOR = registerBlock("light_trapdoor",
            () -> new GlassLightTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel((b) -> 15), BlockSetType.OAK));
    public static final RegistryObject<Block> OBSIDIAN_TRAPDOOR = registerBlock("obsidian_trapdoor",
            () -> new ObsidianGlassTrapDoor(glassProtectedProp(), BlockSetType.IRON));

    // Glass Panes
    public static final RegistryObject<Block> GLASS_DARK_ETHEREAL_PANE = registerBlock("glass_dark_ethereal_pane",
            () -> new DarkEtherealPaneBlock(glassPaneProp().noCollission(), false));
    public static final RegistryObject<Block> GLASS_DARK_ETHEREAL_REVERSE_PANE = registerBlock("glass_dark_ethereal_reverse_pane",
            () -> new DarkEtherealPaneBlock(glassPaneProp().noCollission(), true));
    public static final RegistryObject<Block> GLASS_ETHEREAL_PANE = registerBlock("glass_ethereal_pane",
            () -> new EtherealPaneBlock(glassPaneProp().noCollission(), false));
    public static final RegistryObject<Block> GLASS_ETHEREAL_REVERSE_PANE = registerBlock("glass_ethereal_reverse_pane",
            () -> new EtherealPaneBlock(glassPaneProp().noCollission(), true));
    public static final RegistryObject<Block> GLASS_REDSTONE_PANE = registerBlock("glass_redstone_pane",
            () -> new RedstonePaneBlock(glassPaneProp()));
    public static final RegistryObject<Block> GLASS_REDSTONE_TINTED_PANE = registerBlock("glass_redstone_tinted_pane",
            () -> new RedstonePaneBlock(glassPaneProp()));
    public static final RegistryObject<Block> GLASS_GHOSTLY_PANE = registerBlock("glass_ghostly_pane",
            () -> new BasicPaneBlock(glassPaneProp().noCollission(), false));
    public static final RegistryObject<Block> GLASS_LIGHT_PANE = registerBlock("glass_light_pane",
            () -> new BasicPaneBlock(glassPaneProp().lightLevel((b) -> 15), true));
    public static final RegistryObject<Block> GLASS_LIGHT_TINTED_PANE = registerBlock("glass_light_tinted_pane",
            () -> new BasicPaneBlock(glassPaneProp().lightLevel((b) -> 15), true));

    // Dyed Glass Doors
    public static final RegistryObject<Block> BLACK_GLASS_DOOR = registerGlassDoor("black_glass_door");
    public static final RegistryObject<Block> BLUE_GLASS_DOOR = registerGlassDoor("blue_glass_door");
    public static final RegistryObject<Block> BROWN_GLASS_DOOR = registerGlassDoor("brown_glass_door");
    public static final RegistryObject<Block> CYAN_GLASS_DOOR = registerGlassDoor("cyan_glass_door");
    public static final RegistryObject<Block> GRAY_GLASS_DOOR = registerGlassDoor("gray_glass_door");
    public static final RegistryObject<Block> GREEN_GLASS_DOOR = registerGlassDoor("green_glass_door");
    public static final RegistryObject<Block> LIGHT_BLUE_GLASS_DOOR = registerGlassDoor("light_blue_glass_door");
    public static final RegistryObject<Block> LIGHT_GRAY_GLASS_DOOR = registerGlassDoor("light_gray_glass_door");
    public static final RegistryObject<Block> LIME_GLASS_DOOR = registerGlassDoor("lime_glass_door");
    public static final RegistryObject<Block> MAGENTA_GLASS_DOOR = registerGlassDoor("magenta_glass_door");
    public static final RegistryObject<Block> ORANGE_GLASS_DOOR = registerGlassDoor("orange_glass_door");
    public static final RegistryObject<Block> PINK_GLASS_DOOR = registerGlassDoor("pink_glass_door");
    public static final RegistryObject<Block> PURPLE_GLASS_DOOR = registerGlassDoor("purple_glass_door");
    public static final RegistryObject<Block> RED_GLASS_DOOR = registerGlassDoor("red_glass_door");
    public static final RegistryObject<Block> TINTED_GLASS_DOOR = registerGlassDoor("tinted_glass_door");
    public static final RegistryObject<Block> WHITE_GLASS_DOOR = registerGlassDoor("white_glass_door");
    public static final RegistryObject<Block> YELLOW_GLASS_DOOR = registerGlassDoor("yellow_glass_door");

    // Dyed Glass Trapdoors
    public static final RegistryObject<Block> BLACK_GLASS_TRAPDOOR = registerGlassTrapdoor("black_glass_trapdoor");
    public static final RegistryObject<Block> BLUE_GLASS_TRAPDOOR = registerGlassTrapdoor("blue_glass_trapdoor");
    public static final RegistryObject<Block> BROWN_GLASS_TRAPDOOR = registerGlassTrapdoor("brown_glass_trapdoor");
    public static final RegistryObject<Block> CYAN_GLASS_TRAPDOOR = registerGlassTrapdoor("cyan_glass_trapdoor");
    public static final RegistryObject<Block> GRAY_GLASS_TRAPDOOR = registerGlassTrapdoor("gray_glass_trapdoor");
    public static final RegistryObject<Block> GREEN_GLASS_TRAPDOOR = registerGlassTrapdoor("green_glass_trapdoor");
    public static final RegistryObject<Block> LIGHT_BLUE_GLASS_TRAPDOOR = registerGlassTrapdoor("light_blue_glass_trapdoor");
    public static final RegistryObject<Block> LIGHT_GRAY_GLASS_TRAPDOOR = registerGlassTrapdoor("light_gray_glass_trapdoor");
    public static final RegistryObject<Block> LIME_GLASS_TRAPDOOR = registerGlassTrapdoor("lime_glass_trapdoor");
    public static final RegistryObject<Block> MAGENTA_GLASS_TRAPDOOR = registerGlassTrapdoor("magenta_glass_trapdoor");
    public static final RegistryObject<Block> ORANGE_GLASS_TRAPDOOR = registerGlassTrapdoor("orange_glass_trapdoor");
    public static final RegistryObject<Block> PINK_GLASS_TRAPDOOR = registerGlassTrapdoor("pink_glass_trapdoor");
    public static final RegistryObject<Block> PURPLE_GLASS_TRAPDOOR = registerGlassTrapdoor("purple_glass_trapdoor");
    public static final RegistryObject<Block> RED_GLASS_TRAPDOOR = registerGlassTrapdoor("red_glass_trapdoor");
    public static final RegistryObject<Block> TINTED_GLASS_TRAPDOOR = registerGlassTrapdoor("tinted_glass_trapdoor");
    public static final RegistryObject<Block> WHITE_GLASS_TRAPDOOR = registerGlassTrapdoor("white_glass_trapdoor");
    public static final RegistryObject<Block> YELLOW_GLASS_TRAPDOOR = registerGlassTrapdoor("yellow_glass_trapdoor");

    // Glass Slabs
    public static final RegistryObject<Block> GLASS_SLAB = registerBlock("glass_slab",
            () -> new GlassSlabBlock(BlockBehaviour.Properties.of()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(GlassentialBlocks::never)
                    .isRedstoneConductor(GlassentialBlocks::never)
                    .isSuffocating(GlassentialBlocks::never)
                    .isViewBlocking(GlassentialBlocks::never)));

    private static Block.Properties glassProp() {
        return Block.Properties.ofFullCopy(Blocks.GLASS)
                .isValidSpawn(GlassentialBlocks::never)
                .isRedstoneConductor(GlassentialBlocks::isntSolid)
                .isSuffocating(GlassentialBlocks::isntSolid)
                .isViewBlocking(GlassentialBlocks::isntSolid);
    }

    private static Block.Properties glassPaneProp() {
        return BlockBehaviour.Properties.of()
                .instrument(NoteBlockInstrument.HAT)
                .strength(0.3F)
                .sound(SoundType.GLASS)
                .noOcclusion();
    }

    private static Block.Properties glassProtectedProp() {
        return Block.Properties.ofFullCopy(Blocks.GLASS)
                .strength(1, 1200)
                .requiresCorrectToolForDrops()
                .isValidSpawn(GlassentialBlocks::never)
                .isRedstoneConductor(GlassentialBlocks::isntSolid)
                .isSuffocating(GlassentialBlocks::isntSolid)
                .isViewBlocking(GlassentialBlocks::isntSolid);
    }

    private static boolean never(BlockState blockState, BlockGetter blockView, BlockPos blockPos) {
        return false;
    }

    private static Boolean never(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
        return false;
    }

    private static boolean isntSolid(BlockState state, BlockGetter reader, BlockPos pos) {
        return false;
    }

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        RegistryObject<Block> block = BLOCK_DEFERRED.register(name, blockSupplier);
        RegistryObject<Item> blockItem = ITEM_DEFERRED.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        ITEMS_FOR_TAB_LIST.add(blockItem);
        return block;
    }

    private static RegistryObject<Block> registerGlassDoor(String name) {
        return registerBlock(name, () -> new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));
    }

    private static RegistryObject<Block> registerGlassDoor(String name, boolean collidePlayers) {
        return registerBlock(name, () -> new GlassEtherealDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK, collidePlayers));
    }

    private static RegistryObject<Block> registerGlassTrapdoor(String name) {
        return registerBlock(name, () -> new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));
    }

    private static RegistryObject<Block> registerGlassTrapdoor(String name, boolean collidePlayers) {
        return registerBlock(name, () -> new GlassEtherealTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK, collidePlayers));
    }

    public static void load() {
    }
}