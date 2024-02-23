package lykrast.glassential.init;

import lykrast.glassential.Glassential;
import lykrast.glassential.blocks.DarkEtherealGlassBlock;
import lykrast.glassential.blocks.EtherealGlassBlock;
import lykrast.glassential.blocks.RedstoneGlassBlock;
import lykrast.glassential.blocks.TooltipGlassBlock;
import lykrast.glassential.doors.GlassDoorBlock;
import lykrast.glassential.doors.GlassTrapDoorBlock;
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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class GBlocks {
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

    // end

    private static Block.Properties glassProp() {
        //Turns out "from" doesn't copy everything that glass sets
        return Block.Properties.copy(Blocks.GLASS)
                .isValidSpawn(GBlocks::neverAllowSpawn)
                .isRedstoneConductor(GBlocks::isntSolid)
                .isSuffocating(GBlocks::isntSolid)
                .isViewBlocking(GBlocks::isntSolid);
    }

    private static Boolean neverAllowSpawn(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
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
        return GItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
