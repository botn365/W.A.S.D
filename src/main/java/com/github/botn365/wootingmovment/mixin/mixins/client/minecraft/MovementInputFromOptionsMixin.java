package com.github.botn365.wootingmovment.mixin.mixins.client.minecraft;

import com.github.botn365.wootingmovment.Settings;
import com.github.botn365.wootingmovment.WootingInit;
import com.github.botn365.wootingmovment.WootingMovment;
import lombok.val;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

import static com.github.botn365.main.WootingAnalogWrapper.*;
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

    private static int TICK_VALUE = 0;

    private void wootingMovment() {
//        float[] floats = new float[16];
//        short[] shorts = new short[16];
//
//        if (++TICK_VALUE%10 == 0) {
//            int ret = wootingAnalogReadFullBuffer(shorts,floats);
//            WootingMovment.info("Wooting debug print "+ret);
//            WootingMovment.info(Arrays.toString(floats));
//            WootingMovment.info(Arrays.toString(shorts));
//            WootingMovment.info("W:"+gameSettings.keyBindForward.getKeyCode()+"  S:"+gameSettings.keyBindBack.getKeyCode()+"   A:"+gameSettings.keyBindLeft.getKeyCode()+"   D:"+gameSettings.keyBindRight.getKeyCode());
//        }


        val deviceID = WootingInit.getDeviceID();
        this.moveForward = 0;
        this.moveStrafe = 0;
        float w = getResponseCurve(FORWARD).translate(wootingAnalogReadAnalog(gameSettings.keyBindForward.getKeyCode()));
        float s = getResponseCurve(BACKWARD).translate(wootingAnalogReadAnalog(gameSettings.keyBindBack.getKeyCode()));
        float a = getResponseCurve(LEFT).translate(wootingAnalogReadAnalog(gameSettings.keyBindLeft.getKeyCode()));
        float d = getResponseCurve(RIGHT).translate(wootingAnalogReadAnalog(gameSettings.keyBindRight.getKeyCode()));
//        if (TICK_VALUE%10 == 0) {
//            WootingMovment.info(""+w+" "+s+" "+a+" "+d);
//        }

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
