package com.github.botn365.wasd.mixin.mixins.client.minecraft;

import com.github.botn365.wasd.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.github.botn365.wasd.WASDInit.isInit;

@Deprecated
@Mixin(value = Minecraft.class)
public class MinecraftMixinNoDWS {
        @Redirect(method = "runTick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/settings/KeyBinding;isPressed()Z",
            ordinal = 2
    ),require = 1)
    public boolean doNothing(KeyBinding instance) {
        return (!isInit() || !Settings.hotBarEnabled) && instance.isPressed();
    }
}
