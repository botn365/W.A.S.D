package com.myname.mymodid.mixin.mixins.client.minecraft;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class GreglessMixin {
    @Inject(method = "run",
            at = @At(value = "HEAD"),
            require = 1)
    private void gregless(CallbackInfo ci) {
        System.out.println("Gregtech not detected");
        System.out.println("Gregless mixin loaded");
    }
}
