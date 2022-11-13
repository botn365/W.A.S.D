package com.github.botn365.wootingmovment.mixin.mixins.client.minecraft;

import com.github.botn365.wootingmovment.Settings;
import com.github.botn365.wootingmovment.WootingInit;
import lombok.val;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.botn365.main.WootingAnalogWrapper.wootingAnalogReadAnalogDevice;
import static com.github.botn365.wootingmovment.Settings.*;
import static net.minecraft.client.Minecraft.getMinecraft;

//Example vanilla mixin
@Mixin(value = MovementInputFromOptions.class)
abstract class MovementInputFromOptionsMixin extends MovementInput {
    /**
     * @author
     */
    @Inject(method = "updatePlayerMoveState",at = @At(value = "HEAD"),cancellable = true,require = 1)
    public void updatePlayerMove(CallbackInfo ci) {
        val gui = getMinecraft().currentScreen;
        if (WootingInit.isInit() && gui == null && Settings.movementEnabled){
            wootingMovment();
            ci.cancel();
        }
    }

    private void wootingMovment() {
        val deviceID = WootingInit.getDeviceID();
        this.moveForward = 0;
        this.moveStrafe = 0;
        float w = getResponseCurve(FORWARD).translate(wootingAnalogReadAnalogDevice(gameSettings.keyBindForward.getKeyCode(),deviceID));
        float s = getResponseCurve(BACKWARD).translate(wootingAnalogReadAnalogDevice(gameSettings.keyBindBack.getKeyCode(),deviceID));
        float a = getResponseCurve(LEFT).translate(wootingAnalogReadAnalogDevice(gameSettings.keyBindLeft.getKeyCode(),deviceID));
        float d = getResponseCurve(RIGHT).translate(wootingAnalogReadAnalogDevice(gameSettings.keyBindRight.getKeyCode(),deviceID));
        if (w > 0.05) {
            this.moveForward += w;
        }
        if (s > 0.05) {
            this.moveForward -= s;
        }
        if (a > 0.05) {
            this.moveStrafe += a;
        }
        if (d > 0.05) {
            this.moveStrafe -= d;
        }

        this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
        this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();

        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
        }
    }

    @Shadow
    private GameSettings gameSettings;
}
