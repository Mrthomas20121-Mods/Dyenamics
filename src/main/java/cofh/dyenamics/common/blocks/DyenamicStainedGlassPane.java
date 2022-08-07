package cofh.dyenamics.common.blocks;

import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;


public class DyenamicStainedGlassPane extends IronBarsBlock {
    private final DyenamicDyeColor color;

    public DyenamicStainedGlassPane(DyenamicDyeColor colorIn, BlockBehaviour.Properties properties) {
        super(properties);
        this.color = colorIn;
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    public @NotNull DyenamicDyeColor getColor() {
        return this.color;
    }
}
