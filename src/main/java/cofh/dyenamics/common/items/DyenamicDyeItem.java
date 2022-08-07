package cofh.dyenamics.common.items;

import com.google.common.collect.Maps;

import cofh.dyenamics.common.entities.DyenamicSheep;
import cofh.dyenamics.core.util.DyenamicDyeColor;

import java.util.Map;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class DyenamicDyeItem extends Item {
   private static final Map<DyenamicDyeColor, DyenamicDyeItem> COLOR_DYE_ITEM_MAP = Maps.newEnumMap(DyenamicDyeColor.class);
   private final DyenamicDyeColor dyeColor;

   public DyenamicDyeItem(DyenamicDyeColor dyeColorIn, Item.Properties builder) {
      super(builder);
      this.dyeColor = dyeColorIn;
      COLOR_DYE_ITEM_MAP.put(dyeColorIn, this);
   }

   /**
    * Returns true if the item can be used on the given entity, e.g. shears on sheep.
    */
   @Nonnull
   @Override
   public InteractionResult interactLivingEntity(@Nonnull ItemStack stack, @Nonnull Player player, @Nonnull LivingEntity target, @Nonnull InteractionHand hand) {
      if(target instanceof DyenamicSheep dyenamicSheep) {
         if(dyenamicSheep.isAlive() && !dyenamicSheep.isSheared() && dyenamicSheep.getDyenamicFleeceColor() != this.dyeColor) {
            dyenamicSheep.level.playSound(player, dyenamicSheep, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.level.isClientSide) {
               dyenamicSheep.setColor(this.dyeColor);
               stack.shrink(1);
            }

            return InteractionResult.sidedSuccess(player.level.isClientSide);
         }
      }
      else if (target instanceof Sheep sheep) {
         if (sheep.isAlive() && !sheep.isSheared() && sheep.getColor().getId() != this.dyeColor.getId()) {
            sheep.level.playSound(player, sheep, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.level.isClientSide) {
               DyenamicSheep.convertToDyenamics(sheep, dyeColor);
               stack.shrink(1);
            }

            return InteractionResult.sidedSuccess(player.level.isClientSide);
         }
      }
      return InteractionResult.PASS;
   }

   public DyenamicDyeColor getDyeColor() {
      return this.dyeColor;
   }

   public static DyenamicDyeItem getItem(DyenamicDyeColor color) {
      return COLOR_DYE_ITEM_MAP.get(color);
   }
}
