package com.bigenergy.glassential.init;

import com.bigenergy.glassential.Glassential;
import com.bigenergy.glassential.blocks.DarkEtherealGlassBlock;
import com.bigenergy.glassential.blocks.EtherealGlassBlock;
import com.bigenergy.glassential.blocks.RedstoneGlassBlock;
import com.bigenergy.glassential.blocks.TooltipGlassBlock;
import com.bigenergy.glassential.doors.GlassLightDoorBlock;
import com.bigenergy.glassential.doors.GlassLightTrapDoorBlock;
import com.bigenergy.glassential.doors.GlassTrapDoorBlock;
import com.bigenergy.glassential.doors.GlassDoorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class GlassentialBlocks {
    public static final SoundEvent METAL_OPEN = SoundEvents.IRON_DOOR_OPEN;
    public static final SoundEvent METAL_CLOSE = SoundEvents.IRON_DOOR_CLOSE;
    public static final SoundEvent WOOD_OPEN = SoundEvents.WOODEN_DOOR_OPEN;
    public static final SoundEvent WOOD_CLOSE = SoundEvents.WOODEN_DOOR_CLOSE;
    public static final SoundEvent METAL_TRAP_OPEN = SoundEvents.IRON_TRAPDOOR_OPEN;
    public static final SoundEvent METAL_TRAP_CLOSE = SoundEvents.IRON_TRAPDOOR_CLOSE;
    public static final SoundEvent WOOD_TRAP_OPEN = SoundEvents.WOODEN_TRAPDOOR_OPEN;
    public static final SoundEvent WOOD_TRAP_CLOSE = SoundEvents.WOODEN_TRAPDOOR_CLOSE;

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Glassential.MODID);

    public static BlockSetType registerSound(Block block, Boolean isMetal)  {
        SoundType soundGroup = block.getSoundType(null);


        if(isMetal){
            return BlockSetType.register(new BlockSetType("blockset",false, soundGroup, METAL_CLOSE, METAL_OPEN, METAL_TRAP_CLOSE, METAL_TRAP_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

        }else {
            return BlockSetType.register(new BlockSetType("blockset",true, soundGroup, WOOD_CLOSE, WOOD_OPEN, WOOD_TRAP_CLOSE, WOOD_TRAP_OPEN, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON));
        }


    }



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
            new GlassDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> DARK_ETHEREAL_DOOR = registerBlock("dark_ethereal_door", () ->
            new GlassDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> DARK_ETHEREAL_REVERSE_DOOR = registerBlock("dark_ethereal_reverse_door", () ->
            new GlassDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> ETHEREAL_DOOR = registerBlock("ethereal_door", () ->
            new GlassDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> ETHEREAL_REVERSE_DOOR = registerBlock("ethereal_reverse_door", () ->
            new GlassDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> LIGHT_DOOR = registerBlock("light_door", () ->
            new GlassLightDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> REDSTONE_DOOR = registerBlock("redstone_door", () ->
            new GlassDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> GHOSTLY_DOOR = registerBlock("ghostly_door", () ->
            new GlassDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    // trapdoors

    public static final RegistryObject<Block> GLASS_TRAPDOOR = registerBlock("glass_trapdoor", () ->
            new GlassTrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> DARK_ETHEREAL_TRAPDOOR = registerBlock("dark_ethereal_trapdoor", () ->
            new GlassTrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> DARK_ETHEREAL_REVERSE_TRAPDOOR = registerBlock("dark_ethereal_reverse_trapdoor", () ->
            new GlassTrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> ETHEREAL_TRAPDOOR = registerBlock("ethereal_trapdoor", () ->
            new GlassTrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> ETHEREAL_REVERSE_TRAPDOOR = registerBlock("ethereal_reverse_trapdoor", () ->
            new GlassTrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> REDSTONE_TRAPDOOR = registerBlock("redstone_trapdoor", () ->
            new GlassTrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> GHOSTLY_TRAPDOOR = registerBlock("ghostly_trapdoor", () ->
            new GlassTrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    public static final RegistryObject<Block> LIGHT_TRAPDOOR = registerBlock("light_trapdoor", () ->
            new GlassLightTrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.GLASS), registerSound(Blocks.GLASS, false)));

    // end

    private static Block.Properties glassProp() {
        //Turns out "from" doesn't copy everything that glass sets
        return Block.Properties.copy(Blocks.GLASS)
                .isValidSpawn(GlassentialBlocks::never)
                .isRedstoneConductor(GlassentialBlocks::isntSolid)
                .isSuffocating(GlassentialBlocks::isntSolid)
                .isViewBlocking(GlassentialBlocks::isntSolid);
    }

    private static Block.Properties glassStairsProp() {
        //Turns out "from" doesn't copy everything that glass sets
        return BlockBehaviour.Properties.of()
                .instrument(NoteBlockInstrument.HAT).strength(0.3F)
                .sound(SoundType.GLASS).noOcclusion()
                .isValidSpawn(GlassentialBlocks::never)
                .isRedstoneConductor(GlassentialBlocks::never)
                .isSuffocating(GlassentialBlocks::never)
                .isViewBlocking(GlassentialBlocks::never);
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

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return GlassentialItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
