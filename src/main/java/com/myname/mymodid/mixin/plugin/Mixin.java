package com.myname.mymodid.mixin.plugin;

import com.falsepattern.lib.mixin.IMixin;
import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Predicate;

import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.*;
import static com.myname.mymodid.mixin.plugin.TargetedMod.*;

@RequiredArgsConstructor
public enum Mixin implements IMixin {

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

    @Getter
    public final Side side;
    @Getter
    public final Predicate<List<ITargetedMod>> filter;
    @Getter
    public final String mixin;
}
