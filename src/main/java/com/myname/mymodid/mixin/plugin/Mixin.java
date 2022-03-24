package com.myname.mymodid.mixin.plugin;

import cpw.mods.fml.relauncher.FMLLaunchHandler;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.myname.mymodid.mixin.plugin.TargetedMod.*;

public enum Mixin {

    //
    // IMPORTANT: Do not make any references to any mod from this file. This file is loaded quite early on and if
    // you refer to other mods you load them as well. The consequence is: You can't inject any previously loaded classes!
    // Exception: Tags.java, as long as it is used for Strings only!
    //

    // Replace with your own mixins:

    //Vanilla minecraft is implicitly defined as a dependency, no need to explicitly declare it
    ItemEditableBookMixin(Side.COMMON, always(), "minecraft.ItemEditableBookMixin"),
    // You may also require multiple mods to be loaded if your mixin requires both
    GT_Block_Ores_AbstractMixin(Side.COMMON, require(GREGTECH), "gregtech.GT_Block_Ores_AbstractMixin"),
    // Client only mixins
    TessellatorMixin(Side.CLIENT, always(), "minecraft.TessellatorMixin"),
    // Conditional mixins
    ConditionalMixin(Side.CLIENT, condition(() -> 1 + 1 == 2), "minecraft.ConditionalMixin"),
    // "Mod avoiding" mixins
    GreglessMixin(Side.CLIENT, avoid(GREGTECH), "minecraft.GreglessMixin"),

    // The modFilter argument is a predicate, so you can also use the .and(), .or(), and .negate() methods to mix and match multiple predicates.
    ;

    public final Side side;
    public final String mixin;
    public final Predicate<List<TargetedMod>> filter;

    Mixin(Side side, Predicate<List<TargetedMod>> modFilter, String mixin) {
        this.side = side;
        this.mixin = mixin;
        this.filter = modFilter;
    }

    public boolean shouldLoad(List<TargetedMod> loadedMods) {
        return (side == Side.COMMON
                || side == Side.SERVER && FMLLaunchHandler.side().isServer()
                || side == Side.CLIENT && FMLLaunchHandler.side().isClient())
               && filter.test(loadedMods);
    }

    private static Predicate<List<TargetedMod>> never() {
        return (list) -> false;
    }

    private static Predicate<List<TargetedMod>> condition(Supplier<Boolean> condition) {
        return (list) -> condition.get();
    }

    private static Predicate<List<TargetedMod>> always() {
        return (list) -> true;
    }

    private static Predicate<List<TargetedMod>> require(TargetedMod mod) {
        return (list) -> list.contains(mod);
    }

    private static Predicate<List<TargetedMod>> avoid(TargetedMod mod) {
        return (list) -> !list.contains(mod);
    }

    private enum Side {
        COMMON,
        CLIENT,
        SERVER
    }
}
