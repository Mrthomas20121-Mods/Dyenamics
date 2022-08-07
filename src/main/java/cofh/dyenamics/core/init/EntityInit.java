package cofh.dyenamics.core.init;

import java.util.HashMap;
import java.util.Map;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.common.entities.DyenamicSheep;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Dyenamics.MOD_ID)
public class EntityInit {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Dyenamics.MOD_ID);
	public static final RegistryObject<EntityType<DyenamicSheep>> SHEEP = ENTITIES.register("sheep", () -> EntityType.Builder.<DyenamicSheep>of(DyenamicSheep::new, MobCategory.CREATURE).sized(0.9F, 1.3F).clientTrackingRange(10).build("sheep"));
	public static final Map<String, ResourceLocation> SHEEP_LOOT = new HashMap<>();

	public static void register() {
		for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
			SHEEP_LOOT.put(color.getTranslationKey(), new ResourceLocation(Dyenamics.MOD_ID, "entities/sheep/" + color.getTranslationKey()));
		}
	}

	@SubscribeEvent
	public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
		event.put(SHEEP.get(), DyenamicSheep.createAttributes().build());
	}

	public static void setup() {
		SpawnPlacements.register(SHEEP.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (animal, worldIn, reason, pos, random) -> false);
	}
}
