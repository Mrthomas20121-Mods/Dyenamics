package cofh.dyenamics.common.entities;

import com.google.common.collect.Maps;

import cofh.dyenamics.core.init.BlockInit;
import cofh.dyenamics.core.init.EntityInit;
import cofh.dyenamics.core.util.DyenamicDyeColor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class DyenamicSheep extends Sheep {

    private static final EntityDataAccessor<Byte> DATA_WOOL_ID = SynchedEntityData.defineId(Sheep.class, EntityDataSerializers.BYTE);

   protected static final Map<DyenamicDyeColor, ItemLike> WOOL_BY_COLOR = Util.make(Maps.newEnumMap(DyenamicDyeColor.class), (p_203402_0_) -> {
	   for (DyenamicDyeColor color : DyenamicDyeColor.dyenamicValues()) {
		   p_203402_0_.put(color, BlockInit.DYED_BLOCKS.get(color.getSerializedName()).get("wool").get());
	   }
   });
   /** Map from EnumDyenamicDyeColor to RGB values for passage to GlStateManager.color() */
   private static final Map<DyenamicDyeColor, float[]> COLORARRAY_BY_COLOR = Maps.<DyenamicDyeColor, float[]>newEnumMap(Arrays.stream(DyenamicDyeColor.values()).collect(Collectors.toMap((p_29868_) -> p_29868_, DyenamicSheep::createSheepColor)));

   protected static float[] createSheepColor(DyenamicDyeColor dyeColorIn) {
	   if (dyeColorIn == DyenamicDyeColor.WHITE) {
	         return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
	      } else {
	         float[] afloat = dyeColorIn.getColorComponentValues();
	         float f = 0.75F;
	         return new float[]{afloat[0] * f, afloat[1] * f, afloat[2] * f};
	      }
   }

   @OnlyIn(Dist.CLIENT)
   public static float[] getDyeRgb(DyenamicDyeColor dyeColor) {
      return COLORARRAY_BY_COLOR.get(dyeColor);
   }

   public DyenamicSheep(EntityType<? extends DyenamicSheep> type, Level worldIn) {
      super(type, worldIn);
   }
   
   /*
   public DyenamicSheepEntity(EntityType<? extends DyenamicSheepEntity> type, World worldIn, DyenamicDyeColor color) {
	   super(type, worldIn);
	   this.setFleeceColor(color);
   }
	*/

   @Override
   protected void registerGoals() {
	  super.registerGoals();
      this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D, Sheep.class));
	}
   
   /*
    * Methods for converting between vanilla and dyenamics sheeps. Deletes the old sheep and spawns a new one.
    */
   public static Sheep convertToVanilla(DyenamicSheep oldSheep) {
	   return convertToVanilla(oldSheep, DyeColor.WHITE);
   }
   
   public static Sheep convertToVanilla(DyenamicSheep oldSheep, DyeColor color) {
	   Level world = oldSheep.level;
	   if (!world.isClientSide) {
		   Sheep sheep = new Sheep(EntityType.SHEEP, world);
		   sheep.setColor(color);
		   sheep.copyPosition(oldSheep);
		   sheep.setBaby(oldSheep.isBaby());
		   world.addFreshEntity(sheep);
		   oldSheep.remove(RemovalReason.KILLED);
		   return sheep;
	   }
	   return null;
   }
   
   public static DyenamicSheep convertToDyenamics(Sheep oldSheep) {
	   return convertToDyenamics(oldSheep, DyenamicDyeColor.PEACH);
   }
   
   public static DyenamicSheep convertToDyenamics(Sheep oldSheep, DyenamicDyeColor color) {
	   Level world = oldSheep.level;
	   if (!world.isClientSide) {
		   oldSheep.remove(RemovalReason.KILLED);
		   DyenamicSheep sheep = new DyenamicSheep(EntityInit.SHEEP.get(), world);
		   sheep.setColor(color);
		   sheep.copyPosition(oldSheep);
		   sheep.setBaby(oldSheep.isBaby());
		   world.addFreshEntity(sheep);
		   //oldSheep.remove(RemovalReason.KILLED);
		   return sheep;
	   }
	   return null;
   }

   // method is final and cannot be overriden
//   @Override
//   public ResourceLocation getLootTable() {
//      if (this.isSheared()) {
//         return this.getType().getDefaultLootTable();
//      } else {
//         return EntityInit.SHEEP_LOOT.get(this.getDyenamicFleeceColor().getTranslationKey());
//      }
//   }

   @Override
   public @NotNull InteractionResult mobInteract(Player p_29853_, @NotNull InteractionHand p_29854_) {
       ItemStack itemstack = p_29853_.getItemInHand(p_29854_);
       if (itemstack.getItem() == Items.SHEARS) { //Forge: Moved to onSheared
           if (!this.level.isClientSide && this.readyForShearing()) {
               this.shear(SoundSource.PLAYERS);
               this.gameEvent(GameEvent.SHEAR, p_29853_);
               itemstack.hurtAndBreak(1, p_29853_, (p_29822_) -> {
                   p_29822_.broadcastBreakEvent(p_29854_);
               });
               return InteractionResult.SUCCESS;
           } else {
               return InteractionResult.CONSUME;
           }
       }
       else if(itemstack.getItem() instanceof DyeItem) {
           if(!this.level.isClientSide) {
               DyenamicSheep.convertToVanilla(this, ((DyeItem) itemstack.getItem()).getDyeColor());
               itemstack.shrink(1);
               return InteractionResult.SUCCESS;
           }
       }
       else if (itemstack.getItem().equals(Items.SHEEP_SPAWN_EGG)) {
           DyenamicSheep sheep = new DyenamicSheep(EntityInit.SHEEP.get(), this.level);
           sheep.setColor(this.getDyenamicFleeceColor());
           sheep.copyPosition(this);
           sheep.setBaby(true);
           this.level.addFreshEntity(sheep);
       } else {
           return super.mobInteract(p_29853_, p_29854_);
       }
       return InteractionResult.PASS;
   }

    public void shear(SoundSource source) {
        this.level.playSound((Player)null, this, SoundEvents.SHEEP_SHEAR, source, 1.0F, 1.0F);
        this.setSheared(true);
        int i = 1 + this.random.nextInt(3);

        for(int j = 0; j < i; ++j) {
            ItemEntity itementity = this.spawnAtLocation(WOOL_BY_COLOR.get(this.getDyenamicFleeceColor()), 1);
            if (itementity != null) {
                itementity.setDeltaMovement(itementity.getDeltaMovement().add((double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double)(this.random.nextFloat() * 0.05F), (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
            }
        }

    }

    public void addAdditionalSaveData(CompoundTag p_29864_) {
        super.addAdditionalSaveData(p_29864_);
        p_29864_.putBoolean("Sheared", this.isSheared());
        p_29864_.putByte("Color", (byte)this.getDyenamicFleeceColor().getId());
    }

    public void readAdditionalSaveData(CompoundTag p_29845_) {
        super.readAdditionalSaveData(p_29845_);
        this.setSheared(p_29845_.getBoolean("Sheared"));
        this.setColor(DyeColor.byId(p_29845_.getByte("Color")));
    }
   
   public DyenamicDyeColor getDyenamicFleeceColor() {
      return DyenamicDyeColor.byId(this.entityData.get(DATA_WOOL_ID) & 127);
   }
   
   /**
    * Sets the wool color of this sheep
    */
   @Override
   public void setColor(DyeColor color) {
	      byte b0 = this.entityData.get(DATA_WOOL_ID);
	      this.entityData.set(DATA_WOOL_ID, (byte)(b0 & -128 | color.getId() & 15));
   }
   
   public void setColor(DyenamicDyeColor color) {
      byte b0 = this.entityData.get(DATA_WOOL_ID);
      this.entityData.set(DATA_WOOL_ID, (byte)(b0 & -128 | (color.getId() & 127)));
   }

   /**
    * returns true if a sheep's wool has been sheared
    */
    @Override
    public boolean isSheared() {
        return this.entityData.get(DATA_WOOL_ID) < 0;
    }

    /**
    * Make a sheep sheared if set to true (positive = unsheared, negative = sheared)
    */
   @Override
   public void setSheared(boolean sheared) {
      byte b0 = this.entityData.get(DATA_WOOL_ID);
      if (sheared) {
         this.entityData.set(DATA_WOOL_ID, (byte)(b0 | -128));
      } else {
         this.entityData.set(DATA_WOOL_ID, (byte)(b0 & 127));
      }
   }

    public Sheep getBreedOffspring(ServerLevel level, AgeableMob mate) {
        if(mate instanceof DyenamicSheep) {
            DyenamicSheep sheep = (DyenamicSheep) mate;
            DyenamicSheep sheep1 = EntityInit.SHEEP.get().create(level);
            sheep1.setColor(this.getOffspringColor(this, sheep));
            return sheep1;
        }
        else {
            Sheep sheep = (Sheep)mate;
            DyenamicDyeColor color = this.getDyeColorMixFromParents(this, sheep);
            if (color.getId() < 16) {
                Sheep child = EntityType.SHEEP.create(level);
                child.setColor(this.getOffspringColor(this, sheep));
                return child;
            }
            else {
                DyenamicSheep sheep1 = EntityInit.SHEEP.get().create(level);
                sheep1.setColor(this.getOffspringColor(this, sheep));
                return sheep1;
            }
        }
    }

    private DyeColor getOffspringColor(Animal p_29824_, Animal p_29825_) {
        DyeColor dyecolor = ((Sheep)p_29824_).getColor();
        DyeColor dyecolor1 = ((Sheep)p_29825_).getColor();
        CraftingContainer craftingcontainer = makeContainer(dyecolor, dyecolor1);
        return this.level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingcontainer, this.level).map((p_29828_) -> {
            return p_29828_.assemble(craftingcontainer);
        }).map(ItemStack::getItem).filter(DyeItem.class::isInstance).map(DyeItem.class::cast).map(DyeItem::getDyeColor).orElseGet(() -> {
            return this.level.random.nextBoolean() ? dyecolor : dyecolor1;
        });
    }

    private static CraftingContainer makeContainer(DyeColor p_29832_, DyeColor p_29833_) {
        CraftingContainer craftingcontainer = new CraftingContainer(new AbstractContainerMenu((MenuType)null, -1) {
            public boolean stillValid(Player p_29888_) {
                return false;
            }
        }, 2, 1);
        craftingcontainer.setItem(0, new ItemStack(DyeItem.byColor(p_29832_)));
        craftingcontainer.setItem(1, new ItemStack(DyeItem.byColor(p_29833_)));
        return craftingcontainer;
    }

    @Override
    public boolean canMate(Animal other) {
        if(other.equals(this)) {
            return false;
        }
        else if(other instanceof Sheep) {
            return this.isInLove() && other.isInLove();
        }
        else {
            return this.isInLove() && other.isInLove();
        }
    }
   
   /**
    * Attempts to mix both parent sheep to come up with a mixed dye color.
    */
   protected DyenamicDyeColor getDyeColorMixFromParents(Sheep father, DyenamicSheep mother) {
	   return this.level.random.nextBoolean() ? DyenamicDyeColor.byId(father.getColor().getId()) : ((DyenamicSheep)mother).getDyenamicFleeceColor();
   }
   
   protected DyenamicDyeColor getDyeColorMixFromParents(DyenamicSheep mother, Sheep father) {
	   return this.level.random.nextBoolean() ? DyenamicDyeColor.byId(father.getColor().getId()) : ((DyenamicSheep)mother).getDyenamicFleeceColor();
   }
   
   protected DyenamicDyeColor getDyeColorMixFromParents(DyenamicSheep father, DyenamicSheep mother) {
	   return this.level.random.nextBoolean() ? ((DyenamicSheep)father).getDyenamicFleeceColor() : ((DyenamicSheep)mother).getDyenamicFleeceColor();
   }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(Items.SHEEP_SPAWN_EGG);
    }
}

