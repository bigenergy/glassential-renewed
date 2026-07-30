package com.github.bigenergy.glassential.mixin;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Fusion (SuperMartijn642's Connected Textures) bakes its connecting models with
 * {@code tintIndex = -1}, which means our Colorable Glass loses its dynamic RGB tint
 * whenever Fusion takes over the model. This forces {@code tintIndex = 0} on the quads
 * Fusion returns for our two colorable blocks so the registered
 * {@code BlockColor} handler is consulted again.
 *
 * <p>Target verified against the pinned Fusion build (curse file 6070260, MC 1.20.1):
 * {@code com.supermartijn642.fusion.model.types.connecting.ConnectingBakedModel} declares
 * {@code List<BakedQuad> getQuads(BlockState, Direction, RandomSource,
 * net.minecraftforge.client.model.data.ModelData, RenderType)}.</p>
 *
 * <p>Unlike the 1.21 build this does NOT reflect on {@code BakedQuad.tintIndex}: Forge
 * 1.20.1 uses SRG field names at runtime, so {@code getDeclaredField("tintIndex")} would
 * resolve in dev and silently fail in production. Instead the returned list is replaced
 * with copies carrying {@code tintIndex = 0}, which uses only public API and also avoids
 * mutating quads that Fusion may have cached and shared with other blocks.</p>
 */
@Pseudo
@Mixin(targets = "com.supermartijn642.fusion.model.types.connecting.ConnectingBakedModel")
public class ConnectingBakedModelMixin {

    private static final ResourceLocation GLASSENTIAL$COLORABLE_GLASS =
            new ResourceLocation("glassential", "colorable_glass");
    private static final ResourceLocation GLASSENTIAL$COLORABLE_STAINED_GLASS =
            new ResourceLocation("glassential", "colorable_stained_glass");

    // The explicit descriptor is required: ConnectingBakedModel declares two getQuads
    // overloads and in a deobfuscated dev environment both are literally named "getQuads",
    // which would make a bare name match ambiguous.
    @Inject(
            method = "getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)Ljava/util/List;",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private void glassential$injectTintIndex(BlockState state, Direction direction, RandomSource random,
                                             ModelData modelData, RenderType renderType,
                                             CallbackInfoReturnable<List<BakedQuad>> cir) {
        if (state == null) return;

        try {
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
            if (!GLASSENTIAL$COLORABLE_GLASS.equals(blockId)
                    && !GLASSENTIAL$COLORABLE_STAINED_GLASS.equals(blockId)) {
                return;
            }

            List<BakedQuad> original = cir.getReturnValue();
            if (original == null || original.isEmpty()) return;

            boolean needsRetint = false;
            for (BakedQuad quad : original) {
                if (quad.getTintIndex() != 0) {
                    needsRetint = true;
                    break;
                }
            }
            if (!needsRetint) return;

            List<BakedQuad> tinted = new ArrayList<>(original.size());
            for (BakedQuad quad : original) {
                if (quad.getTintIndex() == 0) {
                    tinted.add(quad);
                } else {
                    tinted.add(new BakedQuad(
                            quad.getVertices(),
                            0,
                            quad.getDirection(),
                            quad.getSprite(),
                            quad.isShade(),
                            quad.hasAmbientOcclusion()
                    ));
                }
            }
            cir.setReturnValue(tinted);
        } catch (Exception e) {
            // Never let a rendering hook take the game down.
        }
    }
}
