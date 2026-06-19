package com.github.bigenergy.glassential.client.model;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.util.RandomSource;

import java.util.List;

/**
 * Pass-through wrapper around the baked glass model for One Way Glass.
 *
 * <p><b>Note (26.1.2 port):</b> In 26.1.x, {@link BlockStateModel#collectParts} lost
 * its {@code level / pos / state} parameters — models are now position-agnostic, so
 * we can no longer read the mimic block from the {@link
 * com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity} from inside
 * the model. The mimic feature therefore needs to be redesigned around a
 * {@code BlockEntityRenderer} (which still has full per-position context).
 * Until that BER is in place, this wrapper just delegates to the regular glass
 * model — i.e. all faces render as plain (Fusion-connected) glass.</p>
 */
public class OneWayBlockStateModel implements BlockStateModel {

    private final BlockStateModel delegate;

    public OneWayBlockStateModel(BlockStateModel delegate) {
        this.delegate = delegate;
    }

    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
        delegate.collectParts(random, parts);
    }

    @Override
    public Material.Baked particleMaterial() {
        return delegate.particleMaterial();
    }

    @Override
    public int materialFlags() {
        return delegate.materialFlags();
    }
}
