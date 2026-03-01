package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.InteractionHand;
import widecat.meteorcrashaddon.CrashAddon;

public class PacketSpammer extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("How many packets to send to the server per tick.")
        .defaultValue(100)
        .min(1)
        .sliderMax(1000)
        .build());

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables module on kick.")
        .defaultValue(true)
        .build());

    public PacketSpammer() {
        super(CrashAddon.CATEGORY, "packet-spammer", "Spams random packets (fucking useless)");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        for (int i = 0; i < amount.get(); i++) {
            mc.player.connection.send(new ServerboundMovePlayerPacket.StatusOnly(Math.random() >= 0.5, false));
            mc.player.connection.send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }
}
