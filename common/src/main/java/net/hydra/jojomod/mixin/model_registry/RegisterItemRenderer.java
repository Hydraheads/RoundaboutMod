package net.hydra.jojomod.mixin.model_registry;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IItemRenderer;
import net.hydra.jojomod.client.ModItemModels;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@Mixin(value = ItemRenderer.class, priority = 100)
public abstract class RegisterItemRenderer implements IItemRenderer {



    /**Modded items are attributed the custom models if their models change with context*/
    @ModifyVariable(method = "getModel", at = @At(value = "STORE"), ordinal = 0)
    public BakedModel roundabout$render(
            BakedModel value,ItemStack $$0, @Nullable Level $$1, @Nullable LivingEntity $$2, int $$3) {
        if ($$0.is(Items.BOW) && $$2 !=null){
            if ($$2.getProjectile($$0).getItem() == ModItems.STAND_ARROW){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STAND_BOW);
            } else if ($$2.getProjectile($$0).getItem() == ModItems.STAND_BEETLE_ARROW){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STAND_BEETLE_BOW);
            } else if ($$2.getProjectile($$0).getItem() == ModItems.WORTHY_ARROW){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.WORTHY_BOW);
            }
        } if ($$0.is(Items.CROSSBOW) && $$2 !=null){
            if (CrossbowItem.containsChargedProjectile($$0,ModItems.STAND_ARROW)){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STAND_ARROW_CROSSBOW);
            } else if (CrossbowItem.containsChargedProjectile($$0,ModItems.STAND_BEETLE_ARROW)){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STAND_BEETLE_CROSSBOW);
            } else if (CrossbowItem.containsChargedProjectile($$0,ModItems.WORTHY_ARROW)){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STAND_WORTHY_CROSSBOW);
            }
        }if ($$0.is(Items.BOOK)){
            if (MainUtil.isDreadBook($$0)){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.DREAD_BOOK);
            }
        } if ($$0.is(ModItems.STREET_SIGN_DIO_BLOCK_ITEM)){
            CompoundTag ct = $$0.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_DIO_D);
            }
            if (ctd == 2) {
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_DIO_D2);
            }
        } if ($$0.is(ModItems.STREET_SIGN_RIGHT_BLOCK_ITEM)){
            CompoundTag ct = $$0.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_RIGHT_D);
            }
            if (ctd == 2) {
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_RIGHT_D2);
            }
        } if ($$0.is(ModItems.STREET_SIGN_STOP_BLOCK_ITEM)){
            CompoundTag ct = $$0.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_STOP_D);
            }
            if (ctd == 2) {
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_STOP_D2);
            }
        } if ($$0.is(ModItems.STREET_SIGN_YIELD_BLOCK_ITEM)){
            CompoundTag ct = $$0.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_YIELD_D);
            }
            if (ctd == 2) {
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_YIELD_D2);
            }
        } if ($$0.is(ModItems.STREET_SIGN_DANGER_BLOCK_ITEM)){
            CompoundTag ct = $$0.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_DANGER_D);
            }
            if (ctd == 2) {
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_DANGER_D2);
            }
        }

        return value;
    }

    @ModifyVariable(method = "render", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel roundabout$render(
            BakedModel value, ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack matrices,
            MultiBufferSource vertexConsumers, int light, int overlay, BakedModel $$7) {
        if (stack.is(ModItems.HARPOON) && renderMode != ItemDisplayContext.GUI && renderMode != ItemDisplayContext.GROUND) {
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.HARPOON_IN_HAND);
        } if (stack.is(ModItems.SNUBNOSE_REVOLVER) && renderMode != ItemDisplayContext.GUI && renderMode != ItemDisplayContext.GROUND) {
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.SNUBNOSE_REVOLVER_IN_HAND);
        } if (stack.is(ModItems.TOMMY_GUN) && renderMode != ItemDisplayContext.GUI && renderMode != ItemDisplayContext.GROUND) {
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.TOMMY_GUN_IN_HAND);
        } if (stack.is(ModItems.COLT_REVOLVER) && renderMode != ItemDisplayContext.GUI && renderMode != ItemDisplayContext.GROUND) {
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.COLT_REVOLVER_IN_HAND);
        } if (stack.is(ModItems.STREET_SIGN_DIO_BLOCK_ITEM) && renderMode != ItemDisplayContext.GUI) {

            CompoundTag ct = stack.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_DIO_HELD_D);
            } if (ctd == 2){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_DIO_HELD_D2);
            }
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.STREET_SIGN_DIO_HELD);
        } if (stack.is(ModItems.STREET_SIGN_RIGHT_BLOCK_ITEM) && renderMode != ItemDisplayContext.GUI) {

            CompoundTag ct = stack.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_RIGHT_HELD_D);
            } if (ctd == 2){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_RIGHT_HELD_D2);
            }
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.STREET_SIGN_RIGHT_HELD);
        } if (stack.is(ModItems.STREET_SIGN_STOP_BLOCK_ITEM) && renderMode != ItemDisplayContext.GUI) {

            CompoundTag ct = stack.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_STOP_HELD_D);
            } if (ctd == 2){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_STOP_HELD_D2);
            }
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.STREET_SIGN_STOP_HELD);
        } if (stack.is(ModItems.STREET_SIGN_YIELD_BLOCK_ITEM) && renderMode != ItemDisplayContext.GUI) {

            CompoundTag ct = stack.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_YIELD_HELD_D);
            } if (ctd == 2){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_YIELD_HELD_D2);
            }
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.STREET_SIGN_YIELD_HELD);
        } if (stack.is(ModItems.STREET_SIGN_DANGER_BLOCK_ITEM) && renderMode != ItemDisplayContext.GUI) {

            CompoundTag ct = stack.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_DANGER_HELD_D);
            } if (ctd == 2){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_DANGER_HELD_D2);
            }
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.STREET_SIGN_DANGER_HELD);
        }

        return value;
    }


    /**Just Accessor code*/
    @Unique
    @Override
    public BlockEntityWithoutLevelRenderer roundabout$getBlockEntityRenderer(){
        return this.blockEntityRenderer;
    }


    @Override
    @Unique
    public void roundabout$renderModelLists(BakedModel $$0, ItemStack $$1, int $$2, int $$3, PoseStack $$4, VertexConsumer $$5) {
        renderModelLists($$0,$$1,$$2,$$3,$$4,$$5);
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    @Shadow
    private void renderModelLists(BakedModel $$0, ItemStack $$1, int $$2, int $$3, PoseStack $$4, VertexConsumer $$5) {
    }
    @Shadow
    @Final
    private BlockEntityWithoutLevelRenderer blockEntityRenderer;
    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @Shadow
    public void render(ItemStack $$0, ItemDisplayContext $$1, boolean $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, int $$6, BakedModel $$7) {
    }


}
