package cofh.dyenamics.client.render.entity;

import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.entity.animal.Sheep;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DyenamicSheepRenderer extends MobRenderer<Sheep, SheepModel<Sheep>> {
   private static final ResourceLocation SHEARED_SHEEP_TEXTURES = new ResourceLocation("textures/entity/sheep/sheep.png");

   public DyenamicSheepRenderer(EntityRendererProvider.Context p_174366_) {
      super(p_174366_, new SheepModel<>(p_174366_.bakeLayer(ModelLayers.SHEEP)), 0.7F);
      this.addLayer(new SheepFurLayer(this, p_174366_.getModelSet()));
   }

   /**
    * Returns the location of an entity's texture.
    */
   public @NotNull ResourceLocation getTextureLocation(@NotNull Sheep entity) {
      return SHEARED_SHEEP_TEXTURES;
   }
}