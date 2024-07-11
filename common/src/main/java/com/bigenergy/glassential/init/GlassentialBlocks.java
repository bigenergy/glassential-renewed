package com.bigenergy.glassential.init;

import com.bigenergy.glassential.Constants;
import com.bigenergy.glassential.blocks.DarkEtherealGlassBlock;
import com.bigenergy.glassential.blocks.EtherealGlassBlock;
import com.bigenergy.glassential.blocks.RedstoneGlassBlock;
import com.bigenergy.glassential.blocks.TooltipGlassBlock;
import com.bigenergy.glassential.doors.GlassLightDoor;
import com.bigenergy.glassential.doors.GlassLightTrapDoor;
import com.bigenergy.glassential.doors.GlassTrapDoor;
import com.bigenergy.glassential.doors.GlassDoor;
import com.bigenergy.glassential.panes.DarkEtherealPaneBlock;
import com.bigenergy.glassential.panes.EtherealPaneBlock;
import com.bigenergy.glassential.panes.BasicPaneBlock;
import com.bigenergy.glassential.panes.RedstonePaneBlock;
import com.bigenergy.glassential.registration.RegistrationProvider;
import com.bigenergy.glassential.registration.RegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;


import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class GlassentialBlocks {

    // new

    public static final RegistrationProvider<Block> BLOCK_DEFERRED = RegistrationProvider.get(BuiltInRegistries.BLOCK, Constants.MOD_ID);
    public static final RegistrationProvider<Item> ITEM_DEFERRED = RegistrationProvider.get(BuiltInRegistries.ITEM, Constants.MOD_ID);
    public static final Collection<RegistryObject<Item>> ITEMS_FOR_TAB_LIST = new ArrayList<>();


    // register blocks

    // glasses
    public static final RegistryObject<Block> GLASS_DARK_ETHEREAL = registerBlock("glass_dark_ethereal", () ->
            new DarkEtherealGlassBlock(glassProp().noCollission(), false));
    public static final RegistryObject<Block> GLASS_DARK_ETHEREAL_REVERSE = registerBlock("glass_dark_ethereal_reverse", () ->
            new DarkEtherealGlassBlock(glassProp().noCollission(), true));
    public static final RegistryObject<Block> GLASS_ETHEREAL = registerBlock("glass_ethereal", () ->
            new EtherealGlassBlock(glassProp().noCollission(), false));
    public static final RegistryObject<Block> GLASS_ETHEREAL_REVERSE = registerBlock("glass_ethereal_reverse", () ->
            new EtherealGlassBlock(glassProp().noCollission(), true));
    public static final RegistryObject<Block> GLASS_GHOSTLY = registerBlock("glass_ghostly", () ->
            new TooltipGlassBlock(glassProp().noCollission(), "tooltip.glassential.ghostly"));
    public static final RegistryObject<Block> GLASS_LIGHT = registerBlock("glass_light", () ->
            new TooltipGlassBlock(glassProp().lightLevel((b) -> 15), "tooltip.glassential.light"));
    public static final RegistryObject<Block> GLASS_REDSTONE = registerBlock("glass_redstone", () ->
            new RedstoneGlassBlock(glassProp()));

    // doors
    public static final RegistryObject<Block> GLASS_DOOR = registerBlock("glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> DARK_ETHEREAL_DOOR = registerBlock("dark_ethereal_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> DARK_ETHEREAL_REVERSE_DOOR = registerBlock("dark_ethereal_reverse_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> ETHEREAL_DOOR = registerBlock("ethereal_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> ETHEREAL_REVERSE_DOOR = registerBlock("ethereal_reverse_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> LIGHT_DOOR = registerBlock("light_door", () ->
            new GlassLightDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> REDSTONE_DOOR = registerBlock("redstone_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> GHOSTLY_DOOR = registerBlock("ghostly_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    // trapdoors

    public static final RegistryObject<Block> GLASS_TRAPDOOR = registerBlock("glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> DARK_ETHEREAL_TRAPDOOR = registerBlock("dark_ethereal_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> DARK_ETHEREAL_REVERSE_TRAPDOOR = registerBlock("dark_ethereal_reverse_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> ETHEREAL_TRAPDOOR = registerBlock("ethereal_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> ETHEREAL_REVERSE_TRAPDOOR = registerBlock("ethereal_reverse_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> REDSTONE_TRAPDOOR = registerBlock("redstone_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> GHOSTLY_TRAPDOOR = registerBlock("ghostly_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> LIGHT_TRAPDOOR = registerBlock("light_trapdoor", () ->
            new GlassLightTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    // end

    // panes
    public static final RegistryObject<Block> GLASS_DARK_ETHEREAL_PANE = registerBlock("glass_dark_ethereal_pane", () ->
            new DarkEtherealPaneBlock(glassPaneProp().noCollission(), false));

    public static final RegistryObject<Block> GLASS_DARK_ETHEREAL_REVERSE_PANE = registerBlock("glass_dark_ethereal_reverse_pane", () ->
            new DarkEtherealPaneBlock(glassPaneProp().noCollission(), true));

    public static final RegistryObject<Block> GLASS_ETHEREAL_PANE = registerBlock("glass_ethereal_pane", () ->
            new EtherealPaneBlock(glassPaneProp().noCollission(), false));

    public static final RegistryObject<Block> GLASS_ETHEREAL_REVERSE_PANE = registerBlock("glass_ethereal_reverse_pane", () ->
            new EtherealPaneBlock(glassPaneProp().noCollission(), true));

    public static final RegistryObject<Block> GLASS_REDSTONE_PANE = registerBlock("glass_redstone_pane", () ->
            new RedstonePaneBlock(glassPaneProp()));

    public static final RegistryObject<Block> GLASS_GHOSTLY_PANE = registerBlock("glass_ghostly_pane", () ->
            new BasicPaneBlock(glassPaneProp().noCollission(), false));

    public static final RegistryObject<Block> GLASS_LIGHT_PANE = registerBlock("glass_light_pane", () ->
            new BasicPaneBlock(glassPaneProp().lightLevel((b) -> 15), true));

    // end

    // dyed glass doors & trapdoors

    public static final RegistryObject<Block> BLACK_GLASS_DOOR = registerBlock("black_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> BLUE_GLASS_DOOR = registerBlock("blue_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> BROWN_GLASS_DOOR = registerBlock("brown_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> CYAN_GLASS_DOOR = registerBlock("cyan_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> GRAY_GLASS_DOOR = registerBlock("gray_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> GREEN_GLASS_DOOR = registerBlock("green_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> LIGHT_BLUE_GLASS_DOOR = registerBlock("light_blue_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> LIGHT_GRAY_GLASS_DOOR = registerBlock("light_gray_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> LIME_GLASS_DOOR = registerBlock("lime_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> MAGENTA_GLASS_DOOR = registerBlock("magenta_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> ORANGE_GLASS_DOOR = registerBlock("orange_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> PINK_GLASS_DOOR = registerBlock("pink_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> PURPLE_GLASS_DOOR = registerBlock("purple_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> RED_GLASS_DOOR = registerBlock("red_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> TINTED_GLASS_DOOR = registerBlock("tinted_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> WHITE_GLASS_DOOR = registerBlock("white_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> YELLOW_GLASS_DOOR = registerBlock("yellow_glass_door", () ->
            new GlassDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    ////////////////////////////////////

    public static final RegistryObject<Block> BLACK_GLASS_TRAPDOOR = registerBlock("black_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> BLUE_GLASS_TRAPDOOR = registerBlock("blue_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> BROWN_GLASS_TRAPDOOR = registerBlock("brown_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> CYAN_GLASS_TRAPDOOR = registerBlock("cyan_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> GRAY_GLASS_TRAPDOOR = registerBlock("gray_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> GREEN_GLASS_TRAPDOOR = registerBlock("green_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> LIGHT_BLUE_GLASS_TRAPDOOR = registerBlock("light_blue_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> LIGHT_GRAY_GLASS_TRAPDOOR = registerBlock("light_gray_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> LIME_GLASS_TRAPDOOR = registerBlock("lime_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> MAGENTA_GLASS_TRAPDOOR = registerBlock("magenta_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> ORANGE_GLASS_TRAPDOOR = registerBlock("orange_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> PINK_GLASS_TRAPDOOR = registerBlock("pink_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> PURPLE_GLASS_TRAPDOOR = registerBlock("purple_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> RED_GLASS_TRAPDOOR = registerBlock("red_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> TINTED_GLASS_TRAPDOOR = registerBlock("tinted_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> WHITE_GLASS_TRAPDOOR = registerBlock("white_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    public static final RegistryObject<Block> YELLOW_GLASS_TRAPDOOR = registerBlock("yellow_glass_trapdoor", () ->
            new GlassTrapDoor(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), BlockSetType.OAK));

    // end

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


    private static boolean never(BlockState blockState, BlockGetter blockView,
                                 BlockPos blockPos)
    {
        return false;
    }

    private static Boolean never(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
        return false;
    }
    private static boolean isntSolid(BlockState state, BlockGetter reader, BlockPos pos) {
        return false;
    }


    // new
    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        RegistryObject<Block> block = BLOCK_DEFERRED.register(name, blockSupplier);

        RegistryObject<Item> blockItem = ITEM_DEFERRED.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        ITEMS_FOR_TAB_LIST.add(blockItem);

        return block;
    }

    //Needed to statically initialize fields
    public static void load() {}
}
