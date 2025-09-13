package com.github.bigenergy.glassential.init;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.blocks.*;
import com.github.bigenergy.glassential.blocks.slabs.GlassSlabBlock;
import com.github.bigenergy.glassential.blocks.doors.*;
import com.github.bigenergy.glassential.blocks.panes.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class GlassentialBlocks {

    public static final Collection<DeferredItem<BlockItem>> ITEMS_FOR_TAB_LIST = new ArrayList<>();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Glassential.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Glassential.MODID);
    // Glasses
    public static final DeferredBlock<Block> GLASS_DARK_ETHEREAL = registerBlock("glass_dark_ethereal",
            () -> new DarkEtherealGlassBlock(glassProp().noCollission(), false));
    public static final DeferredBlock<Block> GLASS_DARK_ETHEREAL_REVERSE = registerBlock("glass_dark_ethereal_reverse",
            () -> new DarkEtherealGlassBlock(glassProp().noCollission(), true));
    public static final DeferredBlock<Block> GLASS_ETHEREAL = registerBlock("glass_ethereal",
            () -> new EtherealGlassBlock(glassProp().noCollission(), false));
    public static final DeferredBlock<Block> GLASS_ETHEREAL_REVERSE = registerBlock("glass_ethereal_reverse",
            () -> new EtherealGlassBlock(glassProp().noCollission(), true));
    public static final DeferredBlock<Block> GLASS_GHOSTLY = registerBlock("glass_ghostly",
            () -> new TooltipGlassBlock(glassProp().noCollission(), "tooltip.glassential.ghostly"));
    public static final DeferredBlock<Block> GLASS_LIGHT = registerBlock("glass_light",
            () -> new LightGlassBlock(glassProp()));
    public static final DeferredBlock<Block> GLASS_LIGHT_TINTED = registerBlock("glass_light_tinted",
            () -> new TintedLightGlassBlock(glassProp()));
    public static final DeferredBlock<Block> GLASS_REDSTONE = registerBlock("glass_redstone",
            () -> new RedstoneGlassBlock(glassProp()));
    public static final DeferredBlock<Block> GLASS_REDSTONE_TINTED = registerBlock("glass_redstone_tinted",
            () -> new TintedRedstoneGlassBlock(glassProp()));
    public static final DeferredBlock<Block> OBSIDIAN_GLASS = registerBlock("obsidian_glass",
            () -> new TooltipGlassBlock(glassProtectedProp(), "tooltip.glassential.protected"));
    public static final DeferredBlock<Block> STONE_GLASS = registerBlock("stone_glass",
            () -> new SimpleGlassBlock(glassProp()));
    public static final DeferredBlock<Block> SANDSTONE_GLASS = registerBlock("sandstone_glass",
            () -> new SimpleGlassBlock(glassProp()));
    public static final DeferredBlock<Block> ICE_GLASS = registerBlock("ice_glass",
            () -> new IceGlassBlock(glassProp(), "tooltip.glassential.ice"));
    public static final DeferredBlock<Block> IRON_GLASS = registerBlock("iron_glass",
            () -> new SimpleGlassBlock(glassProp()));
    public static final DeferredBlock<Block> GRAVITY_GLASS = registerBlock("gravity_glass",
            () -> new GravityGlassBlock(glassProp()));

    // Glass Lamps
    public static final DeferredBlock<Block> GLASS_LAVA_LAMP = registerBlock("glass_lava_lamp",
            () -> new CustomGlassBlock(glassProp().lightLevel((b) -> 15), "tooltip.glassential.lava_lamp"));
    public static final DeferredBlock<Block> GLASS_LAVA_LAMP_TINTED = registerBlock("glass_lava_lamp_tinted",
            () -> new CustomTintedGlassBlock(glassProp().lightLevel((b) -> 15), "tooltip.glassential.lava_lamp"));
    public static final DeferredBlock<Block> GLASS_GLOWSTONE_LAMP = registerBlock("glass_glowstone_lamp",
            () -> new TooltipGlassBlock(glassProp().lightLevel((b) -> 15), "tooltip.glassential.light"));
    public static final DeferredBlock<Block> GLASS_GLOWSTONE_LAMP_TINTED = registerBlock("glass_glowstone_lamp_tinted",
            () -> new TooltipTintedGlassBlock(glassProp().lightLevel((b) -> 15), "tooltip.glassential.light"));

    // Glass Doors
    public static final DeferredBlock<Block> GLASS_DOOR = registerGlassDoor("glass_door");
    public static final DeferredBlock<Block> DARK_ETHEREAL_DOOR = registerGlassDoor("dark_ethereal_door", false);
    public static final DeferredBlock<Block> DARK_ETHEREAL_REVERSE_DOOR = registerGlassDoor("dark_ethereal_reverse_door", true);
    public static final DeferredBlock<Block> ETHEREAL_DOOR = registerGlassDoor("ethereal_door", false);
    public static final DeferredBlock<Block> ETHEREAL_REVERSE_DOOR = registerGlassDoor("ethereal_reverse_door", true);
    public static final DeferredBlock<Block> LIGHT_DOOR = registerBlock("light_door",
            () -> new GlassLightDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel((b) -> 15), BlockSetType.OAK));
    public static final DeferredBlock<Block> REDSTONE_DOOR = registerBlock("redstone_door",
            () -> new GlassRedstoneDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));
    public static final DeferredBlock<Block> GHOSTLY_DOOR = registerBlock("ghostly_door",
            () -> new GlassGhostDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));
    public static final DeferredBlock<Block> OBSIDIAN_DOOR = registerBlock("obsidian_door",
            () -> new ObsidianGlassDoor(glassProtectedProp(), BlockSetType.IRON));

    // Glass Trapdoors
    public static final DeferredBlock<Block> GLASS_TRAPDOOR = registerGlassTrapdoor("glass_trapdoor");
    public static final DeferredBlock<Block> DARK_ETHEREAL_TRAPDOOR = registerGlassTrapdoor("dark_ethereal_trapdoor", false);
    public static final DeferredBlock<Block> DARK_ETHEREAL_REVERSE_TRAPDOOR = registerGlassTrapdoor("dark_ethereal_reverse_trapdoor", true);
    public static final DeferredBlock<Block> ETHEREAL_TRAPDOOR = registerGlassTrapdoor("ethereal_trapdoor", false);
    public static final DeferredBlock<Block> ETHEREAL_REVERSE_TRAPDOOR = registerGlassTrapdoor("ethereal_reverse_trapdoor", true);
    public static final DeferredBlock<Block> REDSTONE_TRAPDOOR = registerBlock("redstone_trapdoor",
            () -> new GlassRedstoneTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));
    public static final DeferredBlock<Block> GHOSTLY_TRAPDOOR = registerBlock("ghostly_trapdoor",
            () -> new GlassGhostTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));
    public static final DeferredBlock<Block> LIGHT_TRAPDOOR = registerBlock("light_trapdoor",
            () -> new GlassLightTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel((b) -> 15), BlockSetType.OAK));
    public static final DeferredBlock<Block> OBSIDIAN_TRAPDOOR = registerBlock("obsidian_trapdoor",
            () -> new ObsidianGlassTrapDoor(glassProtectedProp(), BlockSetType.IRON));

    // Glass Panes
    public static final DeferredBlock<Block> GLASS_DARK_ETHEREAL_PANE = registerBlock("glass_dark_ethereal_pane",
            () -> new DarkEtherealPaneBlock(glassPaneProp().noCollission(), false));
    public static final DeferredBlock<Block> GLASS_DARK_ETHEREAL_REVERSE_PANE = registerBlock("glass_dark_ethereal_reverse_pane",
            () -> new DarkEtherealPaneBlock(glassPaneProp().noCollission(), true));
    public static final DeferredBlock<Block> GLASS_ETHEREAL_PANE = registerBlock("glass_ethereal_pane",
            () -> new EtherealPaneBlock(glassPaneProp().noCollission(), false));
    public static final DeferredBlock<Block> GLASS_ETHEREAL_REVERSE_PANE = registerBlock("glass_ethereal_reverse_pane",
            () -> new EtherealPaneBlock(glassPaneProp().noCollission(), true));
    public static final DeferredBlock<Block> GLASS_REDSTONE_PANE = registerBlock("glass_redstone_pane",
            () -> new RedstonePaneBlock(glassPaneProp()));
    public static final DeferredBlock<Block> GLASS_REDSTONE_TINTED_PANE = registerBlock("glass_redstone_tinted_pane",
            () -> new TintedRedstonePaneBlock(glassPaneProp()));
    public static final DeferredBlock<Block> GLASS_GHOSTLY_PANE = registerBlock("glass_ghostly_pane",
            () -> new GhostPaneBlock(glassPaneProp().noCollission()));
    public static final DeferredBlock<Block> GLASS_LIGHT_PANE = registerBlock("glass_light_pane",
            () -> new BasicPaneBlock(glassPaneProp().lightLevel((b) -> 15), true));
    public static final DeferredBlock<Block> GLASS_LIGHT_TINTED_PANE = registerBlock("glass_light_tinted_pane",
            () -> new TintedBasicPaneBlock(glassPaneProp().lightLevel((b) -> 15), true));

    // Dyed Glass Doors
    public static final DeferredBlock<Block> BLACK_GLASS_DOOR = registerGlassDoor("black_glass_door");
    public static final DeferredBlock<Block> BLUE_GLASS_DOOR = registerGlassDoor("blue_glass_door");
    public static final DeferredBlock<Block> BROWN_GLASS_DOOR = registerGlassDoor("brown_glass_door");
    public static final DeferredBlock<Block> CYAN_GLASS_DOOR = registerGlassDoor("cyan_glass_door");
    public static final DeferredBlock<Block> GRAY_GLASS_DOOR = registerGlassDoor("gray_glass_door");
    public static final DeferredBlock<Block> GREEN_GLASS_DOOR = registerGlassDoor("green_glass_door");
    public static final DeferredBlock<Block> LIGHT_BLUE_GLASS_DOOR = registerGlassDoor("light_blue_glass_door");
    public static final DeferredBlock<Block> LIGHT_GRAY_GLASS_DOOR = registerGlassDoor("light_gray_glass_door");
    public static final DeferredBlock<Block> LIME_GLASS_DOOR = registerGlassDoor("lime_glass_door");
    public static final DeferredBlock<Block> MAGENTA_GLASS_DOOR = registerGlassDoor("magenta_glass_door");
    public static final DeferredBlock<Block> ORANGE_GLASS_DOOR = registerGlassDoor("orange_glass_door");
    public static final DeferredBlock<Block> PINK_GLASS_DOOR = registerGlassDoor("pink_glass_door");
    public static final DeferredBlock<Block> PURPLE_GLASS_DOOR = registerGlassDoor("purple_glass_door");
    public static final DeferredBlock<Block> RED_GLASS_DOOR = registerGlassDoor("red_glass_door");
    public static final DeferredBlock<Block> TINTED_GLASS_DOOR = registerTintedGlassDoor("tinted_glass_door");
    public static final DeferredBlock<Block> WHITE_GLASS_DOOR = registerGlassDoor("white_glass_door");
    public static final DeferredBlock<Block> YELLOW_GLASS_DOOR = registerGlassDoor("yellow_glass_door");

    // Dyed Glass Trapdoors
    public static final DeferredBlock<Block> BLACK_GLASS_TRAPDOOR = registerGlassTrapdoor("black_glass_trapdoor");
    public static final DeferredBlock<Block> BLUE_GLASS_TRAPDOOR = registerGlassTrapdoor("blue_glass_trapdoor");
    public static final DeferredBlock<Block> BROWN_GLASS_TRAPDOOR = registerGlassTrapdoor("brown_glass_trapdoor");
    public static final DeferredBlock<Block> CYAN_GLASS_TRAPDOOR = registerGlassTrapdoor("cyan_glass_trapdoor");
    public static final DeferredBlock<Block> GRAY_GLASS_TRAPDOOR = registerGlassTrapdoor("gray_glass_trapdoor");
    public static final DeferredBlock<Block> GREEN_GLASS_TRAPDOOR = registerGlassTrapdoor("green_glass_trapdoor");
    public static final DeferredBlock<Block> LIGHT_BLUE_GLASS_TRAPDOOR = registerGlassTrapdoor("light_blue_glass_trapdoor");
    public static final DeferredBlock<Block> LIGHT_GRAY_GLASS_TRAPDOOR = registerGlassTrapdoor("light_gray_glass_trapdoor");
    public static final DeferredBlock<Block> LIME_GLASS_TRAPDOOR = registerGlassTrapdoor("lime_glass_trapdoor");
    public static final DeferredBlock<Block> MAGENTA_GLASS_TRAPDOOR = registerGlassTrapdoor("magenta_glass_trapdoor");
    public static final DeferredBlock<Block> ORANGE_GLASS_TRAPDOOR = registerGlassTrapdoor("orange_glass_trapdoor");
    public static final DeferredBlock<Block> PINK_GLASS_TRAPDOOR = registerGlassTrapdoor("pink_glass_trapdoor");
    public static final DeferredBlock<Block> PURPLE_GLASS_TRAPDOOR = registerGlassTrapdoor("purple_glass_trapdoor");
    public static final DeferredBlock<Block> RED_GLASS_TRAPDOOR = registerGlassTrapdoor("red_glass_trapdoor");
    public static final DeferredBlock<Block> TINTED_GLASS_TRAPDOOR = registerTintedGlassTrapdoor("tinted_glass_trapdoor");
    public static final DeferredBlock<Block> WHITE_GLASS_TRAPDOOR = registerGlassTrapdoor("white_glass_trapdoor");
    public static final DeferredBlock<Block> YELLOW_GLASS_TRAPDOOR = registerGlassTrapdoor("yellow_glass_trapdoor");

    // Glass Slabs
    public static final DeferredBlock<Block> GLASS_SLAB = registerBlock("glass_slab",
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

    private static DeferredBlock<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        //DeferredBlock<Block> block = BLOCK_DEFERRED.register(name, blockSupplier);
        DeferredBlock<Block> block = BLOCKS.register(name, blockSupplier);
        DeferredItem<BlockItem> blockItem = ITEMS.registerSimpleBlockItem(name, block);

        ITEMS_FOR_TAB_LIST.add(blockItem);

        return block;
    }

    private static DeferredBlock<Block> registerGlassDoor(String name) {
        return registerBlock(name, () -> new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));
    }

    private static DeferredBlock<Block> registerTintedGlassDoor(String name) {
        return registerBlock(name, () -> new TintedGlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));
    }

    private static DeferredBlock<Block> registerTintedGlassTrapdoor(String name) {
        return registerBlock(name, () -> new TintedGlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));
    }

    private static DeferredBlock<Block> registerGlassDoor(String name, boolean collidePlayers) {
        return registerBlock(name, () -> new GlassEtherealDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK, collidePlayers));
    }

    private static DeferredBlock<Block> registerGlassTrapdoor(String name) {
        return registerBlock(name, () -> new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));
    }

    private static DeferredBlock<Block> registerGlassTrapdoor(String name, boolean collidePlayers) {
        return registerBlock(name, () -> new GlassEtherealTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK, collidePlayers));
    }

    public static void load() {
    }
}