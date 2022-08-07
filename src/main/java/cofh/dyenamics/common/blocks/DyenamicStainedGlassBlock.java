package cofh.dyenamics.common.blocks;

import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;


public class DyenamicStainedGlassBlock extends AbstractGlassBlock {
    private final DyenamicDyeColor color;

    public DyenamicStainedGlassBlock(DyenamicDyeColor colorIn, Block.Properties properties) {
        super(properties);
        this.color = colorIn;
    }

    public DyenamicDyeColor getColor() {
        return this.color;
    }
}
