package com.github.botn365.wootingmovment.mixin.plugin;

import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

import static com.falsepattern.lib.mixin.ITargetedMod.PredicateHelpers.*;

@RequiredArgsConstructor
public enum TargetedMod implements ITargetedMod {

    //
    // IMPORTANT: Do not make any references to any mod from this file. This file is loaded quite early on and if
    // you refer to other mods you load them as well. The consequence is: You can't inject any previously loaded classes!
    // Exception: Tags.java, as long as it is used for Strings only!
    //

    PLAYERAPI("PlayerApi",false,startsWith("PlayerApi")),
    DWS("dws",false,startsWith("dws-")),
    IC2("ic2",true,startsWith("industrialcraft-2-2.2"))

    // Replace with your injected mods here:
    ;

    @Getter
    public final String modName;
    @Getter
    public final boolean loadInDevelopment;
    @Getter
    public final Predicate<String> condition;
}
