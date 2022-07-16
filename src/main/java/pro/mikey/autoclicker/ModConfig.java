package pro.mikey.autoclicker;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.*;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = AutoClicker.MOD_ID)
public class ModConfig implements ConfigData {

    @ConfigEntry.Category("attack")
    @ConfigEntry.Gui.TransitiveObject
    LeftMouseConfig leftClick = new LeftMouseConfig();

    @ConfigEntry.Category("use")
    @ConfigEntry.Gui.TransitiveObject
    RightMouseConfig rightClick = new RightMouseConfig();

    static class LeftMouseConfig {
        @ConfigEntry.Gui.TransitiveObject
        SharedConfig shared = new SharedConfig();
        @ConfigEntry.Gui.Tooltip
        boolean respectCooldown = true;
        @ConfigEntry.Gui.Tooltip
        boolean mobMode = true;
        @ConfigEntry.Gui.Tooltip
        boolean cropMode= false;
        @ConfigEntry.Gui.Tooltip
        String blacklist = "armor_stand, player, villager";
        @ConfigEntry.Gui.Tooltip
        String cropsList = "bamboo, beetroot, brown_mushroom, cactus, carrots, carved_pumpkin, cocoa, gravel, melon, nether_wart, potatoes, red_mushroom, sand, sugar_cane, sweet_berry_bush, wheat";
        @ConfigEntry.Gui.Tooltip
        int inertia = 30;
        boolean showInertia = false;
    }

    static class RightMouseConfig {
        @ConfigEntry.Gui.TransitiveObject
        SharedConfig shared = new SharedConfig();
    }

    // ClothConfig does not support inheritance? :(
    static class SharedConfig {
        @ConfigEntry.Gui.Tooltip
        boolean active = false;
        @ConfigEntry.Gui.Tooltip
        boolean spamming = false;
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min=0, max=50)
        int speed = 0;
    }
}
