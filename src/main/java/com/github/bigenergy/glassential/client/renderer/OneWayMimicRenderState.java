package com.github.bigenergy.glassential.client.renderer;

import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Moving-block render state that asks the One Way Glass model to draw only the
 * mimic's opaque face.
 *
 * <p>The BER submits it with {@code blockState} set to the One Way Glass itself, so the
 * renderer resolves {@code OneWayBlockStateModel}. NeoForge's block renderer then passes
 * this object back into that model's context-aware {@code collectParts} as the level,
 * which is how the model tells a mimic request apart from normal chunk rendering.</p>
 */
public class OneWayMimicRenderState extends MovingBlockRenderState {

    /** The block shown on the opaque face. */
    public BlockState mimic;

    /** The face of the glass the mimic covers. */
    public Direction opaqueFace;
}
