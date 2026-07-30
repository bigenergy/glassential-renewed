package com.github.bigenergy.glassential.init;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.blocks.*;
import com.github.bigenergy.glassential.blocks.doors.*;
import com.github.bigenergy.glassential.blocks.panes.*;
import com.github.bigenergy.glassential.blocks.slabs.GlassSlabBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Supplier;

public class GlassentialBlocks {

    public static final Collection<RegistryObject<Item>> ITEMS_FOR_TAB_LIST = new ArrayList<>();
    public static final Collection<RegistryObject<Item>> ITEMS_FOR_TAB_LIST_FUNC = new ArrayList<>();

    public static final Collection<RegistryObject<Block>> GLASSES         = new ArrayList<>();
    public static final Collection<RegistryObject<Block>> GLASSES_TINTED  = new ArrayList<>();
    public static final Collection<RegistryObject<Block>> PANES           = new ArrayList<>();
    public static final Collection<RegistryObject<Block>> DOORS           = new ArrayList<>();
    public static final Collection<RegistryObject<Block>> DOORS_DYED      = new ArrayList<>();
    public static final Collection<RegistryObject<Block>> TRAPDOORS       = new ArrayList<>();
    public static final Collection<RegistryObject<Block>> TRAPDOORS_DYED  = new ArrayList<>();
    public static final Collection<RegistryObject<Block>> SLABS           = new ArrayList<>();

    public static final Map<RegistryObject<? extends Block>, DyeColor> DOOR_COLOR = new LinkedHashMap<>();
    public static final Map<RegistryObject<? extends Block>, DyeColor> TRAPDOOR_COLOR = new LinkedHashMap<>();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Glassential.MODID);
    public static final DeferredRegister<Item>  ITEMS  = DeferredRegister.create(ForgeRegistries.ITEMS, Glassential.MODID);

    private enum Kind { GLASS, TINTED_GLASS, PANE, DOOR, DOOR_DYED, TRAPDOOR, TRAPDOOR_DYED, SLAB }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, boolean isSimpleBlock, Kind... kinds) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
        RegistryObject<Item> item = ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));

        if (isSimpleBlock) {
            ITEMS_FOR_TAB_LIST.add(item);
        } else {
            ITEMS_FOR_TAB_LIST_FUNC.add(item);
        }

        for (Kind k : kinds) {
            switch (k) {
                case GLASS        -> addTo(GLASSES, block);
                case TINTED_GLASS -> addTo(GLASSES_TINTED, block);
                case PANE         -> addTo(PANES, block);
                case DOOR         -> addTo(DOORS, block);
                case DOOR_DYED    -> addTo(DOORS_DYED, block);
                case TRAPDOOR     -> addTo(TRAPDOORS, block);
                case TRAPDOOR_DYED-> addTo(TRAPDOORS_DYED, block);
                case SLAB         -> addTo(SLABS, block);
            }
        }
        return block;
    }

    private static <T extends Block> void addTo(Collection<RegistryObject<Block>> coll, RegistryObject<T> value) {
        @SuppressWarnings("unchecked") RegistryObject<Block> cast = (RegistryObject<Block>)(RegistryObject<?>) value;
        coll.add(cast);
    }

    private static <T extends Block> RegistryObject<T> registerGlass(String name, Supplier<T> sup, boolean isSimpleBlock) {
        return register(name, sup, isSimpleBlock, Kind.GLASS);
    }
    private static <T extends Block> RegistryObject<T> registerTintedGlass(String name, Supplier<T> sup, boolean isSimpleBlock) {
        return register(name, sup, isSimpleBlock, Kind.TINTED_GLASS, Kind.GLASS);
    }
    private static <T extends Block> RegistryObject<T> registerPane(String name, Supplier<T> sup, boolean isSimpleBlock) {
        return register(name, sup, isSimpleBlock, Kind.PANE);
    }
    private static <T extends Block> RegistryObject<T> registerDoor(String name, Supplier<T> sup, boolean isSimpleBlock) {
        return register(name, sup, isSimpleBlock, Kind.DOOR);
    }
    private static <T extends Block> RegistryObject<T> registerTrapDoor(String name, Supplier<T> sup, boolean isSimpleBlock) {
        return register(name, sup, isSimpleBlock, Kind.TRAPDOOR);
    }
    private static <T extends Block> RegistryObject<T> registerSlab(String name, Supplier<T> sup, boolean isSimpleBlock) {
        return register(name, sup, isSimpleBlock, Kind.SLAB);
    }

    private static <T extends Block> RegistryObject<T> registerDyedDoor(String name, DyeColor color, Supplier<T> sup) {
        RegistryObject<T> def = register(name, sup, true, Kind.DOOR, Kind.DOOR_DYED);
        DOOR_COLOR.put(def, color);
        return def;
    }
    private static RegistryObject<Block> registerDyedDoorColoredProps(String name, DyeColor color, BlockSetType setType) {
        return registerDyedDoor(name, color,
                () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS).mapColor(color.getMapColor()), setType));
    }

    private static <T extends Block> RegistryObject<T> registerDyedTrapdoor(String name, DyeColor color, Supplier<T> sup, boolean isSimpleBlock) {
        RegistryObject<T> def = register(name, sup, isSimpleBlock, Kind.TRAPDOOR, Kind.TRAPDOOR_DYED);
        TRAPDOOR_COLOR.put(def, color);
        return def;
    }

    private static RegistryObject<Block> registerDyedTrapdoorColoredProps(String name, DyeColor color, BlockSetType setType) {
        return registerDyedTrapdoor(name, color,
                () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS).mapColor(color.getMapColor()), setType), true);
    }

    private static Block.Properties glassProp() {
        return Block.Properties.copy(Blocks.GLASS)
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
        return Block.Properties.copy(Blocks.GLASS)
                .strength(1, 1200)
                .requiresCorrectToolForDrops()
                .isValidSpawn(GlassentialBlocks::never)
                .isRedstoneConductor(GlassentialBlocks::isntSolid)
                .isSuffocating(GlassentialBlocks::isntSolid)
                .isViewBlocking(GlassentialBlocks::isntSolid);
    }

    private static boolean never(BlockState s, BlockGetter g, BlockPos p) { return false; }
    private static Boolean never(BlockState s, BlockGetter g, BlockPos p, EntityType<?> e) { return false; }
    private static boolean isntSolid(BlockState s, BlockGetter g, BlockPos p) { return false; }

    // Glasses
    public static final RegistryObject<Block> GLASS_DARK_ETHEREAL = registerGlass("glass_dark_ethereal",
            () -> new DarkEtherealGlassBlock(glassProp().noCollission().isValidSpawn(GlassentialBlocks::never), false), false);
    public static final RegistryObject<Block> GLASS_DARK_ETHEREAL_REVERSE = registerGlass("glass_dark_ethereal_reverse",
            () -> new DarkEtherealGlassBlock(glassProp().noCollission().isValidSpawn(GlassentialBlocks::never), true), false);
    public static final RegistryObject<Block> GLASS_ETHEREAL = registerGlass("glass_ethereal",
            () -> new EtherealGlassBlock(glassProp().noCollission().isValidSpawn(GlassentialBlocks::never), false), false);
    public static final RegistryObject<Block> GLASS_ETHEREAL_REVERSE = registerGlass("glass_ethereal_reverse",
            () -> new EtherealGlassBlock(glassProp().noCollission().isValidSpawn(GlassentialBlocks::never), true), false);
    public static final RegistryObject<Block> GLASS_GHOSTLY = registerGlass("glass_ghostly",
            () -> new GhostGlassBlock(glassProp()), false);
    public static final RegistryObject<Block> COLORABLE_GLASS = registerGlass("colorable_glass",
            () -> new ColorableGlassBlock(glassProp()), false);
    public static final RegistryObject<Block> COLORABLE_STAINED_GLASS = registerGlass("colorable_stained_glass",
            () -> new ColorableStainedGlassBlock(glassProp()), false);
    public static final RegistryObject<Block> GLASS_LIGHT = registerGlass("glass_light",
            () -> new LightGlassBlock(glassProp()), false);
    public static final RegistryObject<Block> GLASS_LIGHT_TINTED = registerTintedGlass("glass_light_tinted",
            () -> new TintedLightGlassBlock(glassProp()), false);
    public static final RegistryObject<Block> GLASS_REDSTONE = registerGlass("glass_redstone",
            () -> new RedstoneGlassBlock(glassProp()), false);
    public static final RegistryObject<Block> GLASS_REDSTONE_TINTED = registerTintedGlass("glass_redstone_tinted",
            () -> new TintedRedstoneGlassBlock(glassProp()), false);
    public static final RegistryObject<Block> OBSIDIAN_GLASS = registerGlass("obsidian_glass",
            () -> new TooltipGlassBlock(glassProtectedProp(), "tooltip.glassential.protected"), false);
    public static final RegistryObject<Block> STONE_GLASS = registerGlass("stone_glass",
            () -> new SimpleGlassBlock(glassProp()), true);
    public static final RegistryObject<Block> SANDSTONE_GLASS = registerGlass("sandstone_glass",
            () -> new SimpleGlassBlock(glassProp()), true);
    public static final RegistryObject<Block> ICE_GLASS = registerGlass("ice_glass",
            () -> new IceGlassBlock(glassProp(), "tooltip.glassential.ice"), true);
    public static final RegistryObject<Block> IRON_GLASS = registerGlass("iron_glass",
            () -> new SimpleGlassBlock(glassProp()), true);
    public static final RegistryObject<Block> GRAVITY_GLASS = registerGlass("gravity_glass",
            () -> new GravityGlassBlock(glassProp()), false);
    public static final RegistryObject<Block> ONE_WAY_GLASS = registerGlass("one_way_glass",
            () -> new OneWayGlassBlock(glassProp().strength(0.3F).noOcclusion()
                    .isViewBlocking((s,w,p)->false)
                    .isSuffocating((s,w,p)->false)
                    .isRedstoneConductor((s,w,p)->false)), false);
    public static final RegistryObject<Block> TINTED_ONE_WAY_GLASS = registerTintedGlass("tinted_one_way_glass",
            () -> new TintedOneWayGlassBlock(glassProp().strength(0.3F).noOcclusion()
                    .isViewBlocking((s,w,p)->false)
                    .isSuffocating((s,w,p)->false)
                    .isRedstoneConductor((s,w,p)->false)), false);
    public static final RegistryObject<Block> CLEAR_FLUID_GLASS = registerGlass("clear_fluid_glass",
            () -> new ClearFluidGlassBlock(BlockBehaviour.Properties.of().strength(0.6F).sound(SoundType.GLASS)), false);
    public static final RegistryObject<Block> CLEAR_FLUID_FAKE_GLASS = registerGlass("clear_fluid_fake_glass",
            () -> new ClearFluidFakeGlassBlock(BlockBehaviour.Properties.of()
                    .strength(0.6F)
                    .sound(SoundType.GLASS)
                    .isSuffocating((s, l, p) -> false) ), false);

    // Lamps
    public static final RegistryObject<Block> GLASS_LAVA_LAMP = registerGlass("glass_lava_lamp",
            () -> new CustomGlassBlock(glassProp().lightLevel(b -> 15), "tooltip.glassential.lava_lamp"), true);
    public static final RegistryObject<Block> GLASS_LAVA_LAMP_TINTED = registerTintedGlass("glass_lava_lamp_tinted",
            () -> new CustomTintedGlassBlock(glassProp().lightLevel(b -> 15), "tooltip.glassential.lava_lamp"), true);
    public static final RegistryObject<Block> GLASS_GLOWSTONE_LAMP = registerGlass("glass_glowstone_lamp",
            () -> new TooltipGlassBlock(glassProp().lightLevel(b -> 15), "tooltip.glassential.light"), true);
    public static final RegistryObject<Block> GLASS_GLOWSTONE_LAMP_TINTED = registerTintedGlass("glass_glowstone_lamp_tinted",
            () -> new TooltipTintedGlassBlock(glassProp().lightLevel(b -> 15), "tooltip.glassential.light"), true);

    // Doors
    public static final RegistryObject<Block> GLASS_DOOR = registerDoor("glass_door",
            () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.OAK), true);
    public static final RegistryObject<Block> DARK_ETHEREAL_DOOR = registerDoor("dark_ethereal_door",
            () -> new GlassEtherealDoor(BlockBehaviour.Properties.copy(Blocks.GLASS).isValidSpawn(GlassentialBlocks::never), BlockSetType.OAK, false), false);
    public static final RegistryObject<Block> DARK_ETHEREAL_REVERSE_DOOR = registerDoor("dark_ethereal_reverse_door",
            () -> new GlassEtherealDoor(BlockBehaviour.Properties.copy(Blocks.GLASS).isValidSpawn(GlassentialBlocks::never), BlockSetType.OAK, true), false);
    public static final RegistryObject<Block> ETHEREAL_DOOR = registerDoor("ethereal_door",
            () -> new GlassEtherealDoor(BlockBehaviour.Properties.copy(Blocks.GLASS).isValidSpawn(GlassentialBlocks::never), BlockSetType.OAK, false), false);
    public static final RegistryObject<Block> ETHEREAL_REVERSE_DOOR = registerDoor("ethereal_reverse_door",
            () -> new GlassEtherealDoor(BlockBehaviour.Properties.copy(Blocks.GLASS).isValidSpawn(GlassentialBlocks::never), BlockSetType.OAK, true), false);
    public static final RegistryObject<Block> LIGHT_DOOR = registerDoor("light_door",
            () -> new GlassLightDoor(BlockBehaviour.Properties.copy(Blocks.GLASS).lightLevel(b -> 15), BlockSetType.OAK), false);
    public static final RegistryObject<Block> REDSTONE_DOOR = registerDoor("redstone_door",
            () -> new GlassRedstoneDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.OAK), false);
    public static final RegistryObject<Block> GHOSTLY_DOOR = registerDoor("ghostly_door",
            () -> new GlassGhostDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.OAK), false);
    public static final RegistryObject<Block> OBSIDIAN_DOOR = registerDoor("obsidian_door",
            () -> new ObsidianGlassDoor(glassProtectedProp(), BlockSetType.IRON), false);

    // Other glass doors
    public static final RegistryObject<Block> OAK_GLASS_DOOR = registerDoor("oak_glass_door",
            () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.OAK), true);
    public static final RegistryObject<Block> SPRUCE_GLASS_DOOR = registerDoor("spruce_glass_door",
            () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.SPRUCE), true);
    public static final RegistryObject<Block> BIRCH_GLASS_DOOR = registerDoor("birch_glass_door",
            () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.BIRCH), true);
    public static final RegistryObject<Block> ACACIA_GLASS_DOOR = registerDoor("acacia_glass_door",
            () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.ACACIA), true);
    public static final RegistryObject<Block> JUNGLE_GLASS_DOOR = registerDoor("jungle_glass_door",
            () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.JUNGLE), true);
    public static final RegistryObject<Block> DARK_OAK_GLASS_DOOR = registerDoor("dark_oak_glass_door",
            () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.DARK_OAK), true);
    public static final RegistryObject<Block> CRIMSON_GLASS_DOOR = registerDoor("crimson_glass_door",
            () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.CRIMSON), true);
    public static final RegistryObject<Block> WARPED_GLASS_DOOR = registerDoor("warped_glass_door",
            () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.WARPED), true);
    public static final RegistryObject<Block> IRON_GLASS_DOOR = registerDoor("iron_glass_door",
            () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.IRON), true);
    public static final RegistryObject<Block> MANGROVE_GLASS_DOOR = registerDoor("mangrove_glass_door",
            () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.MANGROVE), true);
    public static final RegistryObject<Block> BAMBOO_GLASS_DOOR = registerDoor("bamboo_glass_door",
            () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.BAMBOO), true);
    public static final RegistryObject<Block> CHERRY_GLASS_DOOR = registerDoor("cherry_glass_door",
            () -> new GlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.CHERRY), true);

    // Trapdoors
    public static final RegistryObject<Block> GLASS_TRAPDOOR = registerTrapDoor("glass_trapdoor",
            () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.OAK), true);
    public static final RegistryObject<Block> DARK_ETHEREAL_TRAPDOOR = registerTrapDoor("dark_ethereal_trapdoor",
            () -> new GlassEtherealTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS).isValidSpawn(GlassentialBlocks::never), BlockSetType.OAK, false), false);
    public static final RegistryObject<Block> DARK_ETHEREAL_REVERSE_TRAPDOOR = registerTrapDoor("dark_ethereal_reverse_trapdoor",
            () -> new GlassEtherealTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS).isValidSpawn(GlassentialBlocks::never), BlockSetType.OAK, true), false);
    public static final RegistryObject<Block> ETHEREAL_TRAPDOOR = registerTrapDoor("ethereal_trapdoor",
            () -> new GlassEtherealTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS).isValidSpawn(GlassentialBlocks::never), BlockSetType.OAK, false), false);
    public static final RegistryObject<Block> ETHEREAL_REVERSE_TRAPDOOR = registerTrapDoor("ethereal_reverse_trapdoor",
            () -> new GlassEtherealTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS).isValidSpawn(GlassentialBlocks::never), BlockSetType.OAK, true), false);
    public static final RegistryObject<Block> REDSTONE_TRAPDOOR = registerTrapDoor("redstone_trapdoor",
            () -> new GlassRedstoneTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.OAK), false);
    public static final RegistryObject<Block> GHOSTLY_TRAPDOOR = registerTrapDoor("ghostly_trapdoor",
            () -> new GlassGhostTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.OAK), false);
    public static final RegistryObject<Block> LIGHT_TRAPDOOR = registerTrapDoor("light_trapdoor",
            () -> new GlassLightTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS).lightLevel(b -> 15), BlockSetType.OAK), false);
    public static final RegistryObject<Block> OBSIDIAN_TRAPDOOR = registerTrapDoor("obsidian_trapdoor",
            () -> new ObsidianGlassTrapDoor(glassProtectedProp(), BlockSetType.IRON), false);

    // Other trapdoors
    public static final RegistryObject<Block> OAK_GLASS_TRAPDOOR = registerTrapDoor("oak_glass_trapdoor",
            () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.OAK), true);
    public static final RegistryObject<Block> SPRUCE_GLASS_TRAPDOOR = registerTrapDoor("spruce_glass_trapdoor",
            () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.SPRUCE), true);
    public static final RegistryObject<Block> BIRCH_GLASS_TRAPDOOR = registerTrapDoor("birch_glass_trapdoor",
            () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.BIRCH), true);
    public static final RegistryObject<Block> ACACIA_GLASS_TRAPDOOR = registerTrapDoor("acacia_glass_trapdoor",
            () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.ACACIA), true);
    public static final RegistryObject<Block> JUNGLE_GLASS_TRAPDOOR = registerTrapDoor("jungle_glass_trapdoor",
            () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.JUNGLE), true);
    public static final RegistryObject<Block> DARK_OAK_GLASS_TRAPDOOR = registerTrapDoor("dark_oak_glass_trapdoor",
            () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.DARK_OAK), true);
    public static final RegistryObject<Block> CRIMSON_GLASS_TRAPDOOR = registerTrapDoor("crimson_glass_trapdoor",
            () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.CRIMSON), true);
    public static final RegistryObject<Block> WARPED_GLASS_TRAPDOOR = registerTrapDoor("warped_glass_trapdoor",
            () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.WARPED), true);
    public static final RegistryObject<Block> IRON_GLASS_TRAPDOOR = registerTrapDoor("iron_glass_trapdoor",
            () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.IRON), true);
    public static final RegistryObject<Block> MANGROVE_GLASS_TRAPDOOR = registerTrapDoor("mangrove_glass_trapdoor",
            () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.MANGROVE), true);
    public static final RegistryObject<Block> BAMBOO_GLASS_TRAPDOOR = registerTrapDoor("bamboo_glass_trapdoor",
            () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.BAMBOO), true);
    public static final RegistryObject<Block> CHERRY_GLASS_TRAPDOOR = registerTrapDoor("cherry_glass_trapdoor",
            () -> new GlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.CHERRY), true);

    // Panes
    public static final RegistryObject<Block> GLASS_DARK_ETHEREAL_PANE = registerPane("glass_dark_ethereal_pane",
            () -> new DarkEtherealPaneBlock(glassPaneProp().noCollission().isValidSpawn(GlassentialBlocks::never), false), false);
    public static final RegistryObject<Block> GLASS_DARK_ETHEREAL_REVERSE_PANE = registerPane("glass_dark_ethereal_reverse_pane",
            () -> new DarkEtherealPaneBlock(glassPaneProp().noCollission().isValidSpawn(GlassentialBlocks::never), true), false);
    public static final RegistryObject<Block> GLASS_ETHEREAL_PANE = registerPane("glass_ethereal_pane",
            () -> new EtherealPaneBlock(glassPaneProp().noCollission().isValidSpawn(GlassentialBlocks::never), false), false);
    public static final RegistryObject<Block> GLASS_ETHEREAL_REVERSE_PANE = registerPane("glass_ethereal_reverse_pane",
            () -> new EtherealPaneBlock(glassPaneProp().noCollission().isValidSpawn(GlassentialBlocks::never), true), false);
    public static final RegistryObject<Block> GLASS_REDSTONE_PANE = registerPane("glass_redstone_pane",
            () -> new RedstonePaneBlock(glassPaneProp()), false);
    public static final RegistryObject<Block> GLASS_REDSTONE_TINTED_PANE = registerPane("glass_redstone_tinted_pane",
            () -> new TintedRedstonePaneBlock(glassPaneProp()), false);
    public static final RegistryObject<Block> GLASS_GHOSTLY_PANE = registerPane("glass_ghostly_pane",
            () -> new GhostPaneBlock(glassPaneProp().noCollission()), false);
    public static final RegistryObject<Block> GLASS_LIGHT_PANE = registerPane("glass_light_pane",
            () -> new BasicPaneBlock(glassPaneProp().lightLevel(b -> 15), true), false);
    public static final RegistryObject<Block> GLASS_LIGHT_TINTED_PANE = registerPane("glass_light_tinted_pane",
            () -> new TintedBasicPaneBlock(glassPaneProp().lightLevel(b -> 15), true), false);
    public static final RegistryObject<Block> COLORABLE_GLASS_PANE = registerPane("colorable_glass_pane",
            () -> new ColorableGlassPaneBlock(glassPaneProp()), false);
    public static final RegistryObject<Block> COLORABLE_STAINED_GLASS_PANE = registerPane("colorable_stained_glass_pane",
            () -> new ColorableGlassPaneBlock(glassPaneProp()), false);

    // Slab
    public static final RegistryObject<Block> GLASS_SLAB = registerSlab("glass_slab",
            () -> new GlassSlabBlock(BlockBehaviour.Properties.of()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(GlassentialBlocks::never)
                    .isRedstoneConductor(GlassentialBlocks::never)
                    .isSuffocating(GlassentialBlocks::never)
                    .isViewBlocking(GlassentialBlocks::never)), true);

    // Dyed Doors
    public static final RegistryObject<Block> BLACK_GLASS_DOOR      = registerDyedDoorColoredProps("black_glass_door", DyeColor.BLACK,      BlockSetType.OAK);
    public static final RegistryObject<Block> BLUE_GLASS_DOOR       = registerDyedDoorColoredProps("blue_glass_door", DyeColor.BLUE,        BlockSetType.OAK);
    public static final RegistryObject<Block> BROWN_GLASS_DOOR      = registerDyedDoorColoredProps("brown_glass_door", DyeColor.BROWN,      BlockSetType.OAK);
    public static final RegistryObject<Block> CYAN_GLASS_DOOR       = registerDyedDoorColoredProps("cyan_glass_door", DyeColor.CYAN,        BlockSetType.OAK);
    public static final RegistryObject<Block> GRAY_GLASS_DOOR       = registerDyedDoorColoredProps("gray_glass_door", DyeColor.GRAY,        BlockSetType.OAK);
    public static final RegistryObject<Block> GREEN_GLASS_DOOR      = registerDyedDoorColoredProps("green_glass_door", DyeColor.GREEN,       BlockSetType.OAK);
    public static final RegistryObject<Block> LIGHT_BLUE_GLASS_DOOR = registerDyedDoorColoredProps("light_blue_glass_door", DyeColor.LIGHT_BLUE, BlockSetType.OAK);
    public static final RegistryObject<Block> LIGHT_GRAY_GLASS_DOOR = registerDyedDoorColoredProps("light_gray_glass_door", DyeColor.LIGHT_GRAY, BlockSetType.OAK);
    public static final RegistryObject<Block> LIME_GLASS_DOOR       = registerDyedDoorColoredProps("lime_glass_door", DyeColor.LIME,        BlockSetType.OAK);
    public static final RegistryObject<Block> MAGENTA_GLASS_DOOR    = registerDyedDoorColoredProps("magenta_glass_door", DyeColor.MAGENTA,  BlockSetType.OAK);
    public static final RegistryObject<Block> ORANGE_GLASS_DOOR     = registerDyedDoorColoredProps("orange_glass_door", DyeColor.ORANGE,    BlockSetType.OAK);
    public static final RegistryObject<Block> PINK_GLASS_DOOR       = registerDyedDoorColoredProps("pink_glass_door", DyeColor.PINK,        BlockSetType.OAK);
    public static final RegistryObject<Block> PURPLE_GLASS_DOOR     = registerDyedDoorColoredProps("purple_glass_door", DyeColor.PURPLE,    BlockSetType.OAK);
    public static final RegistryObject<Block> RED_GLASS_DOOR        = registerDyedDoorColoredProps("red_glass_door", DyeColor.RED,          BlockSetType.OAK);
    public static final RegistryObject<Block> WHITE_GLASS_DOOR      = registerDyedDoorColoredProps("white_glass_door", DyeColor.WHITE,      BlockSetType.OAK);
    public static final RegistryObject<Block> YELLOW_GLASS_DOOR     = registerDyedDoorColoredProps("yellow_glass_door", DyeColor.YELLOW,    BlockSetType.OAK);

    public static final RegistryObject<Block> TINTED_GLASS_DOOR = registerDoor("tinted_glass_door",
            () -> new TintedGlassDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.OAK), true);

    // Dyed Trapdoors
    public static final RegistryObject<Block> BLACK_GLASS_TRAPDOOR      = registerDyedTrapdoorColoredProps("black_glass_trapdoor", DyeColor.BLACK, BlockSetType.OAK);
    public static final RegistryObject<Block> BLUE_GLASS_TRAPDOOR       = registerDyedTrapdoorColoredProps("blue_glass_trapdoor", DyeColor.BLUE, BlockSetType.OAK);
    public static final RegistryObject<Block> BROWN_GLASS_TRAPDOOR      = registerDyedTrapdoorColoredProps("brown_glass_trapdoor", DyeColor.BROWN, BlockSetType.OAK);
    public static final RegistryObject<Block> CYAN_GLASS_TRAPDOOR       = registerDyedTrapdoorColoredProps("cyan_glass_trapdoor", DyeColor.CYAN, BlockSetType.OAK);
    public static final RegistryObject<Block> GRAY_GLASS_TRAPDOOR       = registerDyedTrapdoorColoredProps("gray_glass_trapdoor", DyeColor.GRAY, BlockSetType.OAK);
    public static final RegistryObject<Block> GREEN_GLASS_TRAPDOOR      = registerDyedTrapdoorColoredProps("green_glass_trapdoor", DyeColor.GREEN, BlockSetType.OAK);
    public static final RegistryObject<Block> LIGHT_BLUE_GLASS_TRAPDOOR = registerDyedTrapdoorColoredProps("light_blue_glass_trapdoor", DyeColor.LIGHT_BLUE, BlockSetType.OAK);
    public static final RegistryObject<Block> LIGHT_GRAY_GLASS_TRAPDOOR = registerDyedTrapdoorColoredProps("light_gray_glass_trapdoor", DyeColor.LIGHT_GRAY, BlockSetType.OAK);
    public static final RegistryObject<Block> LIME_GLASS_TRAPDOOR       = registerDyedTrapdoorColoredProps("lime_glass_trapdoor", DyeColor.LIME, BlockSetType.OAK);
    public static final RegistryObject<Block> MAGENTA_GLASS_TRAPDOOR    = registerDyedTrapdoorColoredProps("magenta_glass_trapdoor", DyeColor.MAGENTA, BlockSetType.OAK);
    public static final RegistryObject<Block> ORANGE_GLASS_TRAPDOOR     = registerDyedTrapdoorColoredProps("orange_glass_trapdoor", DyeColor.ORANGE, BlockSetType.OAK);
    public static final RegistryObject<Block> PINK_GLASS_TRAPDOOR       = registerDyedTrapdoorColoredProps("pink_glass_trapdoor", DyeColor.PINK, BlockSetType.OAK);
    public static final RegistryObject<Block> PURPLE_GLASS_TRAPDOOR     = registerDyedTrapdoorColoredProps("purple_glass_trapdoor", DyeColor.PURPLE, BlockSetType.OAK);
    public static final RegistryObject<Block> RED_GLASS_TRAPDOOR        = registerDyedTrapdoorColoredProps("red_glass_trapdoor", DyeColor.RED, BlockSetType.OAK);
    public static final RegistryObject<Block> WHITE_GLASS_TRAPDOOR      = registerDyedTrapdoorColoredProps("white_glass_trapdoor", DyeColor.WHITE, BlockSetType.OAK);
    public static final RegistryObject<Block> YELLOW_GLASS_TRAPDOOR     = registerDyedTrapdoorColoredProps("yellow_glass_trapdoor", DyeColor.YELLOW, BlockSetType.OAK);

    public static final RegistryObject<Block> TINTED_GLASS_TRAPDOOR = registerTrapDoor("tinted_glass_trapdoor",
            () -> new TintedGlassTrapDoor(BlockBehaviour.Properties.copy(Blocks.GLASS), BlockSetType.OAK), true);

    // Tools
    public static final RegistryObject<com.github.bigenergy.glassential.items.GlassPainterItem> GLASS_PAINTER =
            ITEMS.register("glass_painter", () -> new com.github.bigenergy.glassential.items.GlassPainterItem(
                    new net.minecraft.world.item.Item.Properties().stacksTo(1)
            ));
}
