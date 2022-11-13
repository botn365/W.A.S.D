package com.github.botn365.wootingmovment.mixin.mixins.client.minecraft;

import com.github.botn365.wootingmovment.Settings;
import com.github.botn365.wootingmovment.WootingInit;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static com.github.botn365.main.WootingAnalogWrapper.wootingAnalogReadAnalogDevice;
import static com.github.botn365.wootingmovment.Settings.*;

@Mixin(value = EntityPlayerSP.class)
public abstract class EntitiyPlayerSPMixin extends AbstractClientPlayer {

    public EntitiyPlayerSPMixin(World p_i45074_1_, GameProfile p_i45074_2_) {
        super(p_i45074_1_, p_i45074_2_);
    }

    @ModifyExpressionValue(method = "onLivingUpdate", at = @At(value = "FIELD",target = "Lnet/minecraft/entity/player/PlayerCapabilities;isFlying:Z",ordinal = 1,opcode = Opcodes.GETFIELD),require = 1)
    public boolean cancelFleigtMovment(boolean original) {
        if (original && WootingInit.isInit() && this.mc.currentScreen == null && Settings.fleightEnabled) {
            val deviceID = WootingInit.getDeviceID();
            val f = getResponseCurve(UP).translate(wootingAnalogReadAnalogDevice(this.mc.gameSettings.keyBindJump.getKeyCode(),deviceID));
            this.motionY += f * 0.15;
            this.motionY -= getResponseCurve(DOWN).translate(wootingAnalogReadAnalogDevice(this.mc.gameSettings.keyBindSneak.getKeyCode(),deviceID)) * 0.15;
            return false;
        }
        return original;
    }

    @Shadow
    protected Minecraft mc;
}
