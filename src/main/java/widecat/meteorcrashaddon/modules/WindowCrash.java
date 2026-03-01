package widecat.meteorcrashaddon.modules;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import widecat.meteorcrashaddon.CrashAddon;

public class WindowCrash extends Module {
    private final SettingGroup sgGeneral = settings.createGroup("Crash");

    private final Setting<Integer> crashPower = sgGeneral.add(new IntSetting.Builder()
        .name("Packets per tick")
        .description("Amount of packets sent each tick")
        .defaultValue(6)
        .min(2)
        .sliderMax(12)
        .build()
    );

    private final Setting<Boolean> disableOnLeave = sgGeneral.add(new BoolSetting.Builder()
        .name("Disable on Leave")
        .description("Automatically disables when you change leave.")
        .defaultValue(true)
        .build()
    );

    public WindowCrash() {
        super(CrashAddon.CATEGORY, "Window Crasher", "Sends silly packets, paper 1.20.1");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        // Window crash disabled for 1.21.11 compatibility
    }

    @EventHandler
    private void onWorldChange(GameLeftEvent event) {
        if (disableOnLeave.get() && this.isActive()) {
            this.toggle();
        }
    }

}
