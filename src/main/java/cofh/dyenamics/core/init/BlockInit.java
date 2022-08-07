package cofh.dyenamics.core.init;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import cofh.dyenamics.Dyenamics;
import cofh.dyenamics.common.blocks.DyenamicCarpetBlock;
import cofh.dyenamics.common.blocks.DyenamicFlammableBlock;
import cofh.dyenamics.common.blocks.DyenamicStainedGlassBlock;
import cofh.dyenamics.common.blocks.DyenamicStainedGlassPane;
import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.GlazedTerracottaBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Dyenamics.MOD_ID);
	public static final Map<String, Map<String, RegistryObject<Block>>> DYED_BLOCKS = new HashMap<>();

	public static void register() {
		for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
			registerDyeBlocks(color);
		}
	}

	public synchronized static void registerDyeBlocks(DyenamicDyeColor color) {
		String colorName = color.getSerializedName();
		int light = color.getLightValue();
		MaterialColor mapColor = color.getMapColor();
		DyeColor analogue = color.getAnalogue();
		final Map<String, RegistryObject<Block>> blocks = new HashMap<>();
		DYED_BLOCKS.put(colorName, blocks);

		registerBlockAndItem(colorName, "terracotta", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, () -> new Block(Block.Properties.of(Material.STONE, mapColor).strength(1.25F, 4.2F).lightLevel(state -> light)));
		registerBlockAndItem(colorName, "glazed_terracotta", blocks, CreativeModeTab.TAB_DECORATIONS, () -> new GlazedTerracottaBlock(Block.Properties.of(Material.STONE, analogue).strength(1.4F).lightLevel(state -> light)));
		final RegistryObject<Block> concrete = registerBlockAndItem(colorName, "concrete", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, () -> new Block(Block.Properties.of(Material.STONE, analogue).strength(1.8F).lightLevel(state -> light)));
		registerBlockAndItem(colorName, "concrete_powder", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, () -> new ConcretePowderBlock(concrete.get(), Block.Properties.of(Material.SAND, analogue).strength(0.5F).sound(SoundType.SAND).lightLevel(state -> light)));
		registerBlockAndItem(colorName, "wool", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, () -> new DyenamicFlammableBlock(Block.Properties.of(Material.WOOL, mapColor).strength(0.8F).sound(SoundType.WOOL).lightLevel(state -> light)));
		registerBlockAndItem(colorName, "carpet", blocks, CreativeModeTab.TAB_DECORATIONS, () -> new DyenamicCarpetBlock(color, Block.Properties.of(Material.CLOTH_DECORATION, mapColor).strength(0.1F).sound(SoundType.WOOL).lightLevel(state -> light)));
		registerBlockAndItem(colorName, "stained_glass", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, () -> new DyenamicStainedGlassBlock(color, Block.Properties.of(Material.GLASS, mapColor).strength(0.3F).sound(SoundType.GLASS).lightLevel(state -> light)));
		registerBlockAndItem(colorName, "stained_glass_pane", blocks, CreativeModeTab.TAB_BUILDING_BLOCKS, () -> new DyenamicStainedGlassPane(color, BlockBehaviour.Properties.of(Material.GLASS, mapColor).strength(0.3F).sound(SoundType.GLASS).lightLevel(state -> light)));
	}

	public synchronized static RegistryObject<Block> registerBlockAndItem(String color, String block, Map<String, RegistryObject<Block>> blockMap, CreativeModeTab group, Supplier<Block> supplier) {
		String name = color + "_" + block;
		RegistryObject<Block> blockRegistryObject = BLOCKS.register(name, supplier);
		ItemInit.ITEMS.register(name, () -> new BlockItem(blockRegistryObject.get(), new Item.Properties().tab(group)));
		blockMap.put(block, blockRegistryObject);
		return blockRegistryObject;
	}

}