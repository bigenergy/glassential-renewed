package com.github.bigenergy.glassential.client;

import com.github.bigenergy.glassential.blocks.ClearFluidFakeGlassBlock;
import com.github.bigenergy.glassential.blocks.ClearFluidGlassBlock;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FogType;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles clearing water fog when the player is near Clear Fluid Glass blocks.
 * Registered manually via NeoForge.EVENT_BUS in ClientModEvents.
 */
public class ClearFluidFogHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger("Glassential");

    private static boolean hasClearFluidGlassNearby(BlockPos cameraPos) {
        var level = Minecraft.getInstance().level;
        if (level == null) return false;

        for (BlockPos pos : BlockPos.betweenClosed(
                cameraPos.offset(-3, -3, -3),
                cameraPos.offset(3, 3, 3))) {
            Block block = level.getBlockState(pos).getBlock();
            if (block instanceof ClearFluidGlassBlock || block instanceof ClearFluidFakeGlassBlock) {
                return true;
            }
        }
        return false;
    }

    public static void onRenderFog(ViewportEvent.RenderFog event) {
        if (event.getType() != FogType.WATER) return;

        Camera camera = event.getCamera();
        BlockPos cameraPos = camera.blockPosition();

        if (hasClearFluidGlassNearby(cameraPos)) {
            var fogData = event.getFogData();
            float farDistance = 1000.0f;
            fogData.environmentalStart = farDistance;
            fogData.environmentalEnd = farDistance;
            fogData.skyEnd = farDistance;
            fogData.cloudEnd = farDistance;
            fogData.renderDistanceStart = farDistance;
            fogData.renderDistanceEnd = farDistance;
        }
    }

    public static void onComputeFogColor(ViewportEvent.ComputeFogColor event) {
        Camera camera = event.getCamera();

        // Only modify color when in water
        if (camera.getFluidInCamera() != FogType.WATER) return;

        BlockPos cameraPos = camera.blockPosition();

        if (hasClearFluidGlassNearby(cameraPos)) {
            // Make fog color close to normal air color (remove blue tint)
            event.setRed(event.getRed() * 0.15f + 0.85f * 0.53f);
            event.setGreen(event.getGreen() * 0.15f + 0.85f * 0.65f);
            event.setBlue(event.getBlue() * 0.15f + 0.85f * 0.78f);
        }
    }
}
