package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.SBA;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class AnubisDropsMixin {

    @Inject(method = "dropCustomDeathLoot",at = @At(value = "TAIL"))
    private void sba$dropAnubis(DamageSource $$0, int $$1, boolean $$2, CallbackInfo ci) {
        LivingEntity This = ((LivingEntity) (Object) this);

        if (!This.level().isClientSide()) {
            StandUser SU = (StandUser)  This;


            if (SU.sba$getStandPowers() instanceof PowersAnubis) {
                if (MainUtil.isTraitorDisc(SU.sba$getStandDisc())) {
                    ItemStack stack = new ItemStack(ModItems.ANUBIS_ITEM);

                    if ($$0.getEntity() != null && $$0.getEntity() instanceof Player P
                            && ((StandUser) P).sba$getStandPowers() instanceof PowersAnubis PA
                            && !((IPlayerEntity) P).sba$getUnlockedBonusSkin()) {
                        PA.unlockSkin(SU.sba$getStandSkin());
                    } else {
                        CompoundTag tag = stack.getOrCreateTagElement("SkinType");
                        tag.putByte("SkinType", SU.sba$getStandSkin());

                        float skin = 0;
                        if (This instanceof Evoker || This instanceof Illusioner) {
                            skin = 3F;
                        } else {
                            skin = 2F;
                        }
                        stack.getTag().putFloat("CustomModelData",skin);
                    }
                    This.spawnAtLocation(stack);

                }
            }
        }

    }

}
