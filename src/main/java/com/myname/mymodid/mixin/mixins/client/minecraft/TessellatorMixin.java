package com.myname.mymodid.mixin.mixins.client.minecraft;

import net.minecraft.client.renderer.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//Example client-side mixin
@Mixin(Tessellator.class)
public abstract class TessellatorMixin {
    @Inject(method = "<init>()V",
            at = @At(value = "RETURN"),
            require = 1)
    private void exampleMixinClient(CallbackInfo ci) {
        System.out.println("Tessellator instance created!");
    }
}
