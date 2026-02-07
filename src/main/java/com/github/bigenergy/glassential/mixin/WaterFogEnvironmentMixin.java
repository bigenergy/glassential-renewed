package com.github.bigenergy.glassential.mixin;

import com.github.bigenergy.glassential.blocks.ClearFluidGlassBlock;
import com.github.bigenergy.glassential.blocks.ClearFluidFakeGlassBlock;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterFogEnvironment.class)
public class WaterFogEnvironmentMixin {

    @Inject(
            at = @At("TAIL"),
            method = "setupFog(Lnet/minecraft/client/renderer/fog/FogData;Lnet/minecraft/client/Camera;Lnet/minecraft/client/multiplayer/ClientLevel;FLnet/minecraft/client/DeltaTracker;)V"
    )
    private void glassential$clearWaterFog(FogData fogData, Camera camera, ClientLevel level,
                                            float viewDistance, DeltaTracker deltaTracker,
                                            CallbackInfo ci) {
        BlockPos cameraPos = camera.blockPosition();

        // Check if any ClearFluidGlass block is within 2 blocks of the camera
        for (BlockPos pos : BlockPos.betweenClosed(
                cameraPos.offset(-2, -2, -2),
                cameraPos.offset(2, 2, 2))) {
            Block block = level.getBlockState(pos).getBlock();
            if (block instanceof ClearFluidGlassBlock || block instanceof ClearFluidFakeGlassBlock) {
                // Push fog far away to give clear underwater vision
                fogData.environmentalStart = viewDistance;
                fogData.environmentalEnd = viewDistance;
                fogData.skyEnd = viewDistance;
                fogData.cloudEnd = viewDistance;
                return;
            }
        }
    }
}
