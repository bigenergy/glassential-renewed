package com.github.bigenergy.glassential.mixin;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(targets = "com.supermartijn642.fusion.model.types.connecting.ConnectingBakedModel")
public class ConnectingBakedModelMixin {

    private static java.lang.reflect.Field tintIndexField;

    static {
        try {
            tintIndexField = BakedQuad.class.getDeclaredField("tintIndex");
            tintIndexField.setAccessible(true);
        } catch (Exception e) {
            System.err.println("Failed to get tintIndex field: " + e);
        }
    }

    @Inject(method = "getQuads", at = @At("RETURN"), cancellable = false, remap = false)
    private void injectTintIndex(BlockState state, Direction direction, RandomSource random,
                                  ModelData modelData, RenderType renderType,
                                  CallbackInfoReturnable<List<BakedQuad>> cir) {
        // Check if this is colorable glass
        if (state != null) {
            try {
                String blockId = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
                if ((blockId.equals("glassential:colorable_glass") || blockId.equals("glassential:colorable_stained_glass"))
                        && tintIndexField != null) {
                    List<BakedQuad> originalQuads = cir.getReturnValue();

                    // Modify tintIndex in-place without creating new list
                    for (BakedQuad quad : originalQuads) {
                        try {
                            tintIndexField.setInt(quad, 0);
                        } catch (Exception e) {
                            // Silently fail
                        }
                    }
                }
            } catch (Exception e) {
                // Silently fail
            }
        }
    }
}
