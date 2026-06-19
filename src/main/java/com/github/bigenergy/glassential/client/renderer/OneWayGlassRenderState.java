package com.github.bigenergy.glassential.client.renderer;

import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * Per-frame render state for the One Way Glass BER.
 *
 * <p>Stores the mimic's full-block render state along with the opaque-face direction,
 * which the BER uses to squash the mimic into a thin slice on that side of the cube.</p>
 */
public class OneWayGlassRenderState extends BlockEntityRenderState {

    /** The mimic block prepared for {@code SubmitNodeCollector#submitMovingBlock}. */
    @Nullable
    public MovingBlockRenderState mimicMovingBlock;

    /** Which face of the One Way Glass should show the mimic. */
    @Nullable
    public Direction opaqueFace;
}
