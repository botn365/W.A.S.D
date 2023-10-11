package com.github.botn365.wootingmovment.mixin.mixins.client.minecraft;

import com.github.botn365.main.WootingAnalogWrapper;
import com.github.botn365.wootingmovment.Settings;
import com.github.botn365.wootingmovment.WootingInit;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static com.github.botn365.main.WootingAnalogWrapper.wootingAnalogReadAnalog;
import static com.github.botn365.main.WootingAnalogWrapper.wootingAnalogReadAnalogDevice;
import static com.github.botn365.wootingmovment.WootingInit.*;

@Mixin(value = Minecraft.class)
public class MinecraftMixin {


    @Redirect(method = "runTick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/settings/KeyBinding;isPressed()Z",
            ordinal = 2
    ),require = 1)
    public boolean doNothing(KeyBinding instance) {
        return (!isInit() || !Settings.hotBarEnabled) && instance.isPressed();
    }

    @Inject(method = "runTick", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/settings/GameSettings;chatVisibility:Lnet/minecraft/entity/player/EntityPlayer$EnumChatVisibility;",
            opcode = Opcodes.GETFIELD,
            shift = At.Shift.BEFORE,
            ordinal = 0),
            require = 1)
    public void analogItemSelect(CallbackInfo ci) {
        if (isInit() && Settings.hotBarEnabled) {
            for (int slot = 0; slot < 9; ++slot)
            {
                int keyCode = this.gameSettings.keyBindsHotbar[slot].getKeyCode();
                float value = wootingAnalogReadAnalogDevice(keyCode,getDeviceID());
                if (value > oldValues[slot]) {
                    isUp = true;
                    oldValues[slot] = value;
                } else if (isUp && oldValues[slot] - Settings.lowerValueThreshold > value) {
                    if (oldValues[slot] > Settings.upperValueThreshold) {
                        this.thePlayer.inventory.currentItem = slot;
                    } else {
                        this.thePlayer.inventory.currentItem = Math.min(slot + 9,17);
                    }
                    oldValues[slot] = value;
                    isUp = false;
                } else if (value == 0) {
                    oldValues[slot] = value;
                }
            }
        }
    }

    public boolean isUp = false;

    @Shadow
    public GameSettings gameSettings;

    @Shadow
    public EntityClientPlayerMP thePlayer;
}
