package cofh.dyenamics.common.blocks;

import cofh.dyenamics.core.util.DyenamicDyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DyenamicCarpetBlock extends Block {
   protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
   private final DyenamicDyeColor color;

   public DyenamicCarpetBlock(DyenamicDyeColor colorIn, Block.Properties properties) {
      super(properties);
      this.color = colorIn;
   }

   public DyeColor getColor() {
      return this.color.getAnalogue();
   }
   
   public DyenamicDyeColor getDyenamicColor() {
	      return this.color;
   }


   public @NotNull VoxelShape getShape(@NotNull BlockState p_152917_, @NotNull BlockGetter p_152918_, @NotNull BlockPos p_152919_, @NotNull CollisionContext p_152920_) {
      return SHAPE;
   }

   public @NotNull BlockState updateShape(BlockState p_152926_, @NotNull Direction p_152927_, @NotNull BlockState p_152928_, @NotNull LevelAccessor p_152929_, @NotNull BlockPos p_152930_, @NotNull BlockPos p_152931_) {
      return !p_152926_.canSurvive(p_152929_, p_152930_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_152926_, p_152927_, p_152928_, p_152929_, p_152930_, p_152931_);
   }

   public boolean canSurvive(@NotNull BlockState p_152922_, LevelReader p_152923_, BlockPos p_152924_) {
      return !p_152923_.isEmptyBlock(p_152924_.below());
   }
}