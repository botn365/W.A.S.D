package com.myname.mymodid.mixin.mixins.client.minecraft;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//Example conditional mixin
@Mixin(Minecraft.class)
public abstract class ConditionalMixin {
    @Inject(method = "run",
            at = @At(value = "HEAD"),
            require = 1)
    private void sayHello(CallbackInfo ci) {
        System.out.println("Minecraft starting!");
    }
}
