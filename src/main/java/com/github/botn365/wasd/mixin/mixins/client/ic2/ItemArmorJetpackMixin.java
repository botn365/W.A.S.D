package com.github.botn365.wasd.mixin.mixins.client.ic2;


import com.github.botn365.main.WootingAnalogWrapper;
import com.github.botn365.wasd.WASDInit;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import ic2.core.item.armor.ItemArmorFluidTank;
import ic2.core.item.armor.ItemArmorJetpack;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.github.botn365.wasd.Settings.*;

@Mixin(value = ItemArmorJetpack.class)
public abstract class ItemArmorJetpackMixin extends ItemArmorFluidTank {
    public ItemArmorJetpackMixin(InternalName internalName, InternalName armorName, Fluid allowfluid, int capacity) {
        super(internalName, armorName, allowfluid, capacity);
    }

    @ModifyExpressionValue(method = "onArmorTick", at = @At(value = "INVOKE",target = "Lic2/core/util/Keyboard;isJumpKeyDown(Lnet/minecraft/entity/player/EntityPlayer;)Z"),require = 1,remap = false)
    public boolean setPowerAnalog(boolean original) {
        if (!WASDInit.isInit()) return original;
        return true;
    }


    @Redirect(method = "onArmorTick", at = @At(value = "INVOKE",target = "Lic2/core/item/armor/ItemArmorJetpack;useJetpack(Lnet/minecraft/entity/player/EntityPlayer;Z)Z"),require = 1,remap = false)
    private boolean useWootingJetpack(ItemArmorJetpack instance, EntityPlayer player, boolean hoverMode) {
        val settings = Minecraft.getMinecraft().gameSettings;
        float spaceBarValue = getResponseCurve(UP).translate(WootingAnalogWrapper.wootingAnalogReadAnalog(settings.keyBindJump.getKeyCode()));
        if (spaceBarValue <= 0 && !hoverMode) {
            return false;
        } else if (!hoverMode && Minecraft.getMinecraft().currentScreen != null) {
            return false;
        }
        ItemStack jetpack = player.inventory.armorInventory[2];
        if (this.getCharge(jetpack) <= 0.0) {
            return false;
        } else {
            boolean electric = jetpack.getItem() != Ic2Items.jetpack.getItem();
            float power = spaceBarValue;
            float dropPercentage = 0.2F;
            if (electric) {
                power = 0.7F;
                dropPercentage = 0.05F;
            }

            if (this.getCharge(jetpack) / this.getMaxCharge(jetpack) <= (double)dropPercentage) {
                power = (float)((double)power * (this.getCharge(jetpack) / (this.getMaxCharge(jetpack) * (double)dropPercentage)));
            }

            if (IC2.keyboard.isForwardKeyDown(player)) {
                float retruster = 0.15F;
                if (hoverMode) {
                    retruster = 1.0F;
                }

                if (electric) {
                    retruster += 0.15F;
                }

                float forwardpower = power * retruster * 2.0F;
                if (forwardpower > 0.0F) {
                    player.moveFlying(0.0F, 0.4F * forwardpower, 0.02F);
                }
            }

            int worldHeight = IC2.getWorldHeight(player.worldObj);
            int maxFlightHeight = electric ? (int)((float)worldHeight / 1.28F) : worldHeight;
            double y = player.posY;
            if (y > (double)(maxFlightHeight - 25)) {
                if (y > (double)maxFlightHeight) {
                    y = (double)maxFlightHeight;
                }

                power = (float)((double)power * (((double)maxFlightHeight - y) / 25.0));
            }

            double prevmotion = player.motionY;
            if (hoverMode) {
                player.motionY = 0;
            }
            player.motionY = Math.min(player.motionY + (double)(power * 0.2F), 0.6000000238418579);
            player.fallDistance = 0;
            if (hoverMode) {

                float maxHoverY = 0.0F;
                if (spaceBarValue > 0) {
                    if (electric) {
                        maxHoverY = 0.1F;
                    } else {
                        maxHoverY = 0.2F;
                    }
                }

                val sneakFloat = getResponseCurve(DOWN).translate(WootingAnalogWrapper.wootingAnalogReadAnalog(settings.keyBindSneak.getKeyCode()));

                if (sneakFloat > 0) {
                    if (electric) {
                        maxHoverY = -0.1F * sneakFloat;
                    } else {
                        maxHoverY = -0.2F * sneakFloat;
                    }
                }

                if (player.motionY > (double)maxHoverY) {
                    player.motionY = (double)maxHoverY;
                    if (prevmotion > player.motionY) {
                        player.motionY = prevmotion;
                    }
                }
            }

            int consume = 2;
            if (hoverMode) {
                consume = 1;
            }

            if (electric) {
                consume += 6;
            }

            if (!player.onGround) {
                this.use(jetpack, (double)consume);
            }

            player.fallDistance = 0.0F;
            player.distanceWalkedModified = 0.0F;
            IC2.platform.resetPlayerInAirTime(player);
            return true;
        }
    }

    @Shadow
    public abstract void use(ItemStack itemStack, double amount);
}
