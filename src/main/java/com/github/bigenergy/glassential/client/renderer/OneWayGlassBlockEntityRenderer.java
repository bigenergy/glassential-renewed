package com.github.bigenergy.glassential.client.renderer;

import com.github.bigenergy.glassential.blocks.OneWayGlassBlock;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Renders the mimic block ON THE OPAQUE FACE of a One Way Glass.
 *
 * <p>The implementation uses {@code SubmitNodeCollector#submitMovingBlock}, which is
 * the pipeline that goes through the full block-rendering path (atlas binding, tint,
 * AO — everything that {@code submitBlockModel} skips when called from a BER context).
 * To avoid rendering the mimic on all six faces of the cube, the pose is translated
 * and scaled before submitting so the mimic is squashed into a paper-thin slab
 * sitting flush against the opaque face. From any viewpoint where the opaque face is
 * visible, the player sees a full-size mimic texture; the other faces of the mimic
 * collapse into invisible slivers around the perimeter.</p>
 *
 * <p>The 5-side glass-only effect comes for free: the base glass model still renders
 * all six glass faces normally. The mimic slab simply sits outside the opaque face,
 * so only that face is visually replaced.</p>
 */
public class OneWayGlassBlockEntityRenderer
        implements BlockEntityRenderer<OneWayGlassBlockEntity, OneWayGlassRenderState> {

    /** Thickness of the mimic slab. Small enough to feel like a "face only". */
    private static final float SLAB_DEPTH = 0.001F;
    /** How far outside the opaque face the slab sits (to avoid Z-fighting with the glass). */
    private static final float SLAB_OFFSET = 0.001F;

    @SuppressWarnings("unused")
    public OneWayGlassBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        // No per-renderer resources needed.
    }

    @Override
    public @NotNull OneWayGlassRenderState createRenderState() {
        return new OneWayGlassRenderState();
    }

    @Override
    public void extractRenderState(@NotNull OneWayGlassBlockEntity be,
                                   @NotNull OneWayGlassRenderState state,
                                   float partialTick,
                                   @NotNull Vec3 cameraPos,
                                   @NotNull ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(be, state, partialTick, cameraPos, crumblingOverlay);

        state.mimicMovingBlock = null;
        state.opaqueFace = null;

        BlockState ownState = be.getBlockState();
        if (!(ownState.getBlock() instanceof OneWayGlassBlock)) return;

        BlockState mimic = be.getMimic();
        if (mimic == null || mimic.isAir()) return;
        if (mimic.getBlock() instanceof OneWayGlassBlock) return;

        if (!(be.getLevel() instanceof ClientLevel level)) return;

        MovingBlockRenderState mbs = new MovingBlockRenderState();
        mbs.blockPos = be.getBlockPos();
        mbs.randomSeedPos = be.getBlockPos();
        mbs.blockState = mimic;
        mbs.biome = level.getBiome(be.getBlockPos());
        mbs.cardinalLighting = level.cardinalLighting();
        mbs.lightEngine = level.getLightEngine();

        state.mimicMovingBlock = mbs;
        state.opaqueFace = ownState.getValue(OneWayGlassBlock.OPAQUE_FACE);
    }

    @Override
    public void submit(@NotNull OneWayGlassRenderState state,
                       @NotNull PoseStack pose,
                       @NotNull SubmitNodeCollector collector,
                       @NotNull CameraRenderState camera) {
        if (state.mimicMovingBlock == null || state.opaqueFace == null) return;

        pose.pushPose();
        applyFaceSlabTransform(pose, state.opaqueFace);
        // 26.2 added a third int parameter (outline color, 0 = no outline).
        collector.submitMovingBlock(pose, state.mimicMovingBlock, 0);
        pose.popPose();
    }

    /**
     * Compress the mimic block to a paper-thin slab sitting just outside the
     * given face of the unit cube. The slab's outward face aligns with the
     * one-way glass's opaque face (offset slightly to avoid Z-fighting); the
     * remaining five faces of the mimic collapse to invisible slivers.
     *
     * <p>Block-local coordinates: cube occupies (0,0,0)–(1,1,1). The face
     * pointing in the {@link Direction#getNormal()} direction is the visible one.</p>
     */
    private static void applyFaceSlabTransform(PoseStack pose, Direction face) {
        // PoseStack matrix multiplication: scale is applied first to the vertex,
        // then translate. So a vertex v transforms as: v' = T * S * v.
        //
        // For each direction, we want the mimic's "front" face (the one aligned
        // with the cube face for that direction) to land just outside the
        // corresponding face of (0,0,0)-(1,1,1), and the slab to extend inward
        // by SLAB_DEPTH.
        switch (face) {
            case DOWN -> {
                pose.translate(0.0F, -SLAB_OFFSET, 0.0F);
                pose.scale(1.0F, SLAB_DEPTH, 1.0F);
            }
            case UP -> {
                pose.translate(0.0F, 1.0F + SLAB_OFFSET - SLAB_DEPTH, 0.0F);
                pose.scale(1.0F, SLAB_DEPTH, 1.0F);
            }
            case NORTH -> {
                pose.translate(0.0F, 0.0F, -SLAB_OFFSET);
                pose.scale(1.0F, 1.0F, SLAB_DEPTH);
            }
            case SOUTH -> {
                pose.translate(0.0F, 0.0F, 1.0F + SLAB_OFFSET - SLAB_DEPTH);
                pose.scale(1.0F, 1.0F, SLAB_DEPTH);
            }
            case WEST -> {
                pose.translate(-SLAB_OFFSET, 0.0F, 0.0F);
                pose.scale(SLAB_DEPTH, 1.0F, 1.0F);
            }
            case EAST -> {
                pose.translate(1.0F + SLAB_OFFSET - SLAB_DEPTH, 0.0F, 0.0F);
                pose.scale(SLAB_DEPTH, 1.0F, 1.0F);
            }
        }
    }
}
