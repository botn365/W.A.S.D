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

    @Inject(method = "runTick", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/settings/GameSettings;chatVisibility:Lnet/minecraft/entity/player/EntityPlayer$EnumChatVisibility;",
            opcode = Opcodes.GETFIELD,
            shift = At.Shift.BEFORE,
            ordinal = 0),
            require = 1)
    public void analogItemSelect(CallbackInfo ci) {
        if (isInit() && Settings.hotBarEnabled) {
            for (int slot = 0; slot < 5; ++slot)
            {
                int keyCode = this.gameSettings.keyBindsHotbar[slot].getKeyCode();
                float value = wootingAnalogReadAnalog(keyCode);

                if (value == 0) {
                    isUp = false;
                    oldValues[slot] = 0;
                    continue;
                }
                if (value < oldValues[slot]) {
                    isUp = true;
                    continue;
                }

                if (value < 0.34) {
                    this.thePlayer.inventory.currentItem = Math.min(slot + 15,17);
                } else if (value < 0.66) {
                    this.thePlayer.inventory.currentItem = slot + 10;
                } else if (value < 0.98) {
                    this.thePlayer.inventory.currentItem = slot + 5;
                } else {
                    this.thePlayer.inventory.currentItem = slot;
                }

                oldValues[slot] = value;
            }
        }
    }

    public boolean isUp = false;

    @Shadow
    public GameSettings gameSettings;

    @Shadow
    public EntityClientPlayerMP thePlayer;
}
