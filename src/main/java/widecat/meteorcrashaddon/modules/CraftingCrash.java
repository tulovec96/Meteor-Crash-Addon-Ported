package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
// Crafting crash disabled for 1.21.11 compatibility due to recipe API changes
import widecat.meteorcrashaddon.CrashAddon;

import java.util.List;

public class CraftingCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> packets = sgGeneral.add(new IntSetting.Builder()
        .name("packets")
        .description("How many packets to send per tick. Warning: this is multiplied by the amount of unlocked recipes")
        .defaultValue(24)
        .min(1)
        .sliderMax(50)
        .build());

    public CraftingCrash() {
        super(CrashAddon.CATEGORY, "Crafting-Crash", "Spam craft request packets. Use with planks in inventory for best results.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        // Crafting crash disabled for 1.21.11 compatibility
    }
}
