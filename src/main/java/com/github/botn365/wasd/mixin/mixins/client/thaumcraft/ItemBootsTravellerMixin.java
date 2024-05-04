package com.github.botn365.wasd.mixin.mixins.client.thaumcraft;

import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thaumcraft.common.items.armor.ItemBootsTraveller;

import static com.github.botn365.main.WootingAnalogWrapper.wootingAnalogReadAnalog;
import static com.github.botn365.wasd.WASDInit.isInit;

@Mixin(value = ItemBootsTraveller.class)
public class ItemBootsTravellerMixin {


    @Redirect(method = "onArmorTick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/EntityPlayer;moveFlying(FFF)V"),
            require = 1)
    public void analogBoost(EntityPlayer instance, float a, float b, float bonus) {
        if (isInit()) {
            val mc = Minecraft.getMinecraft();
            int keyCode = mc.gameSettings.keyBindForward.getKeyCode();
            float value = wootingAnalogReadAnalog(keyCode);
            bonus *= value;
        }
        instance.moveFlying(a,b,bonus);
    }
}
