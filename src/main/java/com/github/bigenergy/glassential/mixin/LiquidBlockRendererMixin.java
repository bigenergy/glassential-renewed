package com.github.bigenergy.glassential.mixin;

import com.github.bigenergy.glassential.blocks.ClearFluidFakeGlassBlock;
import com.github.bigenergy.glassential.blocks.ClearFluidGlassBlock;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Forces water faces adjacent to Clear Fluid Glass blocks to be culled.
 *
 * <p>Vanilla {@code LiquidBlockRenderer#isFaceOccludedByState} relies on
 * {@code Shapes.blockOccudes}, which requires the fluid's projected face to reach 1.0
 * on the relevant axis. Source water without water above only reaches 8/9 (≈0.889),
 * so face culling via the occlusion-shape mechanism never triggers. This intercepts
 * the check and unconditionally reports "occluded" whenever the checked neighbor is
 * one of our Clear Fluid Glass blocks.</p>
 */
@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererMixin {

    @Inject(method = "isFaceOccludedByState", at = @At("HEAD"), cancellable = true)
    private static void glassential$hideClearFluidGlassFace(
            BlockGetter level,
            Direction direction,
            float height,
            BlockPos pos,
            BlockState state,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (state.getBlock() instanceof ClearFluidGlassBlock
                || state.getBlock() instanceof ClearFluidFakeGlassBlock) {
            cir.setReturnValue(true);
        }
    }
}
