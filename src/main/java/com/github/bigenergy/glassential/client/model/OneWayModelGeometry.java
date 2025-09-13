package com.github.bigenergy.glassential.client.model;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class OneWayModelGeometry implements IUnbakedGeometry<OneWayModelGeometry> {
    private final ResourceLocation glassModel;

    public OneWayModelGeometry(ResourceLocation glassModel) {
        this.glassModel = glassModel;
    }

    @Override
    public @NotNull BakedModel bake(@NotNull IGeometryBakingContext iGeometryBakingContext, ModelBaker modelBaker, @NotNull Function<Material, TextureAtlasSprite> function, @NotNull ModelState modelState, @NotNull ItemOverrides itemOverrides) {
        BakedModel glass = modelBaker.bake(glassModel, modelState);
        return new OneWayBakedModel(glass);
    }

    public static class Loader implements IGeometryLoader<OneWayModelGeometry> {
        @Override
        public @NotNull OneWayModelGeometry read(@NotNull JsonObject json, @NotNull JsonDeserializationContext ctx) throws JsonParseException {
            String glassStr = GsonHelper.getAsString(json, "glass_model", "minecraft:block/glass");
            ResourceLocation glass = ResourceLocation.tryParse(glassStr);
            if (glass == null) throw new JsonParseException("Invalid glass_model: " + glassStr);
            return new OneWayModelGeometry(glass);
        }
    }

}
