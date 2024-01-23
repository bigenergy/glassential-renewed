package lykrast.glassential.doors;

import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class GlassTrapDoorBlock extends TrapDoorBlock {
    public GlassTrapDoorBlock(Properties properties, BlockSetType blockSetType) {
        super(properties.noOcclusion(), blockSetType);
    }
}
