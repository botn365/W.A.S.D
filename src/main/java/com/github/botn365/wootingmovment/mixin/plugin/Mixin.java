package com.github.botn365.wootingmovment.mixin.plugin;

import com.falsepattern.lib.mixin.IMixin;
import com.falsepattern.lib.mixin.ITargetedMod;
import com.github.botn365.main.WootingAnalogWrapper;
import com.github.botn365.wootingmovment.WootingMovment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.*;
import java.util.function.Predicate;

import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.*;
import static com.github.botn365.main.WootingAnalogWrapper.WootingAnalogResult.*;
import static com.github.botn365.wootingmovment.mixin.plugin.TargetedMod.PLAYERAPI;

@RequiredArgsConstructor
public enum Mixin implements IMixin {

    //
    // IMPORTANT: Do not make any references to any mod from this file. This file is loaded quite early on and if
    // you refer to other mods you load them as well. The consequence is: You can't inject any previously loaded classes!
    // Exception: Tags.java, as long as it is used for Strings only!
    //

    // Replace with your own mixins:

    //Vanilla minecraft is implicitly defined as a dependency, no need to explicitly declare it
    MovementMixin(Side.CLIENT, condition(Mixin::hasSDK), "minecraft.MovementInputFromOptionsMixin"),
    FleightMixin(Side.CLIENT,avoid(PLAYERAPI).and(condition(Mixin::hasSDK)),"minecraft.EntitiyPlayerSPMixin"),
    FleightMixinPlayerApi(Side.CLIENT,require(PLAYERAPI).and(condition(Mixin::hasSDK)),"minecraft.EntitiyPlayerSPPlayerApiMixin"),
    // The modFilter argument is a predicate, so you can also use the .and(), .or(), and .negate() methods to mix and match multiple predicates.
    ;
    //

    private static boolean hasSDK() {
        val value = WootingAnalogWrapper.wootingAnalogInitialise();
        if (value <= -1990 && value >= -1997) {
            WootingMovment.warn("Error initialising SDK "+WootingAnalogResult_Failure.fromInt(value).name());
            return false;
        }
        return true;
    }

    @Getter
    public final Side side;
    @Getter
    public final Predicate<List<ITargetedMod>> filter;
    @Getter
    public final String mixin;
}
