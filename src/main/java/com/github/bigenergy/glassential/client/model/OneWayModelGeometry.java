package com.github.bigenergy.glassential.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class OneWayModelGeometry implements IUnbakedGeometry<OneWayModelGeometry> {
    private final ResourceLocation glassModel;

    public OneWayModelGeometry(ResourceLocation glassModel) {
        this.glassModel = glassModel;
    }

    /**
     * Note the trailing {@code ResourceLocation modelLocation} parameter — Forge 1.20.1's
     * {@code IUnbakedGeometry#bake} carries it; NeoForge 1.21 dropped it.
     */
    @Override
    public @NotNull BakedModel bake(@NotNull IGeometryBakingContext context, @NotNull ModelBaker baker,
                                    @NotNull Function<Material, TextureAtlasSprite> spriteGetter,
                                    @NotNull ModelState modelState, @NotNull ItemOverrides overrides,
                                    @NotNull ResourceLocation modelLocation) {
        BakedModel glass = baker.bake(glassModel, modelState, spriteGetter);
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
