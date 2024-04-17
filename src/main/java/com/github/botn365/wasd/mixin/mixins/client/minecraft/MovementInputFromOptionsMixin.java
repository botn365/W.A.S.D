package com.github.botn365.wasd.mixin.mixins.client.minecraft;

import com.github.botn365.wasd.Settings;
import com.github.botn365.wasd.WASDInit;
import lombok.val;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.botn365.main.WootingAnalogWrapper.*;
import static com.github.botn365.wasd.Settings.*;
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
        if (WASDInit.isInit() && gui == null && Settings.movementEnabled){
            wootingMovment();
            ci.cancel();
        }
    }

    private static int TICK_VALUE = 0;

    private void wootingMovment() {


        val deviceID = WASDInit.getDeviceID();
        this.moveForward = 0;
        this.moveStrafe = 0;
        float w = getResponseCurve(FORWARD).translate(wootingAnalogReadAnalog(gameSettings.keyBindForward.getKeyCode()));
        float s = getResponseCurve(BACKWARD).translate(wootingAnalogReadAnalog(gameSettings.keyBindBack.getKeyCode()));
        float a = getResponseCurve(LEFT).translate(wootingAnalogReadAnalog(gameSettings.keyBindLeft.getKeyCode()));
        float d = getResponseCurve(RIGHT).translate(wootingAnalogReadAnalog(gameSettings.keyBindRight.getKeyCode()));

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
