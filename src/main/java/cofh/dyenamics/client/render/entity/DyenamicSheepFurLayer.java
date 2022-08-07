package cofh.dyenamics.client.render.entity;

import cofh.dyenamics.common.entities.DyenamicSheep;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;

public class DyenamicSheepFurLayer extends SheepFurLayer {

	private static final ResourceLocation SHEEP_FUR_LOCATION = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
	private final SheepFurModel<Sheep> model;

	public DyenamicSheepFurLayer(RenderLayerParent<Sheep, SheepModel<Sheep>> p_174533_, EntityModelSet p_174534_) {
		super(p_174533_, p_174534_);
		this.model = new SheepFurModel<>(p_174534_.bakeLayer(ModelLayers.SHEEP_FUR));
	}

	public void render(@NotNull PoseStack p_117421_, @NotNull MultiBufferSource p_117422_, int p_117423_, Sheep sheep, float p_117425_, float p_117426_, float p_117427_, float p_117428_, float p_117429_, float p_117430_) {
		if (!sheep.isSheared()) {

			if (sheep.isInvisible()) {

				Minecraft minecraft = Minecraft.getInstance();
				boolean flag = minecraft.shouldEntityAppearGlowing(sheep);
				if (flag) {
					this.getParentModel().copyPropertiesTo(this.model);
					this.model.prepareMobModel(sheep, p_117425_, p_117426_, p_117427_);
					this.model.setupAnim(sheep, p_117425_, p_117426_, p_117428_, p_117429_, p_117430_);
					VertexConsumer vertexconsumer = p_117422_.getBuffer(RenderType.outline(SHEEP_FUR_LOCATION));
					this.model.renderToBuffer(p_117421_, vertexconsumer, p_117423_, LivingEntityRenderer.getOverlayCoords(sheep, 0.0F), 0.0F, 0.0F, 0.0F, 1.0F);
				}

			} else {

				float f;
				float f1;
				float f2;
				if (sheep.hasCustomName() && "jeb_".equals(sheep.getName().getContents())) {
					int i1 = 25;
					int i = sheep.tickCount / 25 + sheep.getId();
					int j = DyeColor.values().length;
					int k = i % j;
					int l = (i + 1) % j;
					float f3 = ((float)(sheep.tickCount % 25) + p_117427_) / 25.0F;
					float[] afloat1 = getColorArray(sheep, DyeColor.byId(k), DyenamicDyeColor.byId(k));
					float[] afloat2 = getColorArray(sheep, DyeColor.byId(l), DyenamicDyeColor.byId(l));
					f = afloat1[0] * (1.0F - f3) + afloat2[0] * f3;
					f1 = afloat1[1] * (1.0F - f3) + afloat2[1] * f3;
					f2 = afloat1[2] * (1.0F - f3) + afloat2[2] * f3;
				} else {
					float[] afloat = getColorArray(sheep);
					f = afloat[0];
					f1 = afloat[1];
					f2 = afloat[2];
				}

				coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, SHEEP_FUR_LOCATION, p_117421_, p_117422_, p_117423_, sheep, p_117425_, p_117426_, p_117428_, p_117429_, p_117430_, p_117427_, f, f1, f2);
			}
		}
	}

	private static float[] getColorArray(Sheep sheep, DyeColor dyeColor, DyenamicDyeColor dyenamicDyeColor) {
		if(sheep instanceof DyenamicSheep) {
			return DyenamicSheep.getDyeRgb(dyenamicDyeColor);
		}
		else {
			return Sheep.getColorArray(dyeColor);
		}
	}

	private static float[] getColorArray(Sheep sheep) {
		if(sheep instanceof DyenamicSheep) {
			return DyenamicSheep.getDyeRgb(((DyenamicSheep) sheep).getDyenamicFleeceColor());
		}
		else {
			return Sheep.getColorArray(sheep.getColor());
		}
	}
}
