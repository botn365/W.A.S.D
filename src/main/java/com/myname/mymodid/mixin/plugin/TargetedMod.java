package com.myname.mymodid.mixin.plugin;

import com.google.common.io.Files;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public enum TargetedMod {

    //
    // IMPORTANT: Do not make any references to any mod from this file. This file is loaded quite early on and if
    // you refer to other mods you load them as well. The consequence is: You can't inject any previously loaded classes!
    // Exception: Tags.java, as long as it is used for Strings only!
    //

    // Replace with your injected mods here:
    GREGTECH("GregTech", false, startsWith("gregtech").or(startsWith("gt5u")));

    public final String modName;
    public final Predicate<String> condition;
    public final boolean loadInDevelopment;

    TargetedMod(String modName, boolean loadInDevelopment, Predicate<String> condition) {
        this.modName = modName;
        this.condition = condition;
        this.loadInDevelopment = loadInDevelopment;
    }

    private static Predicate<String> startsWith(String subString) {
        return (name) -> name.startsWith(subString);
    }

    private static Predicate<String> contains(String subString) {
        return (name) -> name.contains(subString);
    }

    private static Predicate<String> matches(String regex) {
        return (name) -> name.matches(regex);
    }

    @SuppressWarnings("UnstableApiUsage")
    public boolean isMatchingJar(Path path) {
        String pathString = path.toString();
        String nameLowerCase = Files.getNameWithoutExtension(pathString).toLowerCase();
        String fileExtension = Files.getFileExtension(pathString);

        return "jar".equals(fileExtension) && condition.test(nameLowerCase);
    }

    @Override
    public String toString() {
        return "TargetedMod{" +
               "modName='" + modName + '\'' +
               '}';
    }
}
