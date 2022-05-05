package cofh.dyenamics;

import cofh.dyenamics.client.render.block.DyenamicBedBlockEntityRenderer;
import cofh.dyenamics.client.render.block.DyenamicShulkerBoxBlockEntityRenderer;
import cofh.dyenamics.client.render.entity.DyenamicSheepRenderer;
import cofh.dyenamics.core.init.*;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(Dyenamics.MOD_ID)
public class Dyenamics {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "dyenamics";
    public static final Map<String, RenderMaterial> BED_MATERIAL_MAP = new HashMap<>();
    public static final Map<String, RenderMaterial> SHULKER_MATERIAL_MAP = new HashMap<>();

    public Dyenamics() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);

        BlockInit.register();
        ItemInit.register();
        EntityInit.register();

        BlockInit.BLOCKS.register(bus);
        ItemInit.ITEMS.register(bus);
        EntityInit.ENTITIES.register(bus);
        BlockEntityInit.BLOCK_ENTITY_TYPES.register(bus);
        RecipeSerializerInit.RECIPE_SERIALIZERS.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(EntityInit::setup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.SHEEP.get(), DyenamicSheepRenderer::new);
        for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
	        RenderTypeLookup.setRenderLayer(BlockInit.DYED_BLOCKS.get(color.getString()).get("stained_glass").get(), RenderType.getTranslucent());
	        RenderTypeLookup.setRenderLayer(BlockInit.DYED_BLOCKS.get(color.getString()).get("stained_glass_pane").get(), RenderType.getTranslucent());
        }

        ClientRegistry.bindTileEntityRenderer(BlockEntityInit.BED.get(), DyenamicBedBlockEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(BlockEntityInit.SHULKER_BOX.get(), DyenamicShulkerBoxBlockEntityRenderer::new);
    }

}