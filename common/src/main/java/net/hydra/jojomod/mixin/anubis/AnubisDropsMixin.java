package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class AnubisDropsMixin {

    @Inject(method = "dropCustomDeathLoot",at = @At(value = "TAIL"))
    private void roundabout$dropAnubis(DamageSource $$0, int $$1, boolean $$2, CallbackInfo ci) {
        LivingEntity This = ((LivingEntity) (Object) this);

        if (!This.level().isClientSide()) {
            StandUser SU = (StandUser)  This;


            if (SU.roundabout$getStandPowers() instanceof PowersAnubis) {

                if (This instanceof Cow C) {
                    C.spawnAtLocation(ModItems.ANUBIS_ITEM.getDefaultInstance());
                }

                if (MainUtil.isTraitorDisc(SU.roundabout$getStandDisc())) {

                    if ($$0.getEntity() != null && $$0.getEntity() instanceof Player P
                            && ((StandUser) P).roundabout$getStandPowers() instanceof PowersAnubis PA
                            && !((IPlayerEntity) P).roundabout$getUnlockedBonusSkin()) {
                        PA.unlockSkin(SU.roundabout$getStandSkin());
                    } else {
                        ItemStack stack = new ItemStack(ModItems.ANUBIS_ITEM);
                        CompoundTag tag = stack.getOrCreateTagElement("SkinType");
                        tag.putByte("SkinType", SU.roundabout$getStandSkin());

                        This.spawnAtLocation(stack);


                    }
                }
            }
        }

    }

}
