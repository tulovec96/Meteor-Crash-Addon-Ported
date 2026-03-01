package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.PlaySoundEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.entity.Entity;
// Boat import removed - using generic entity check instead
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import widecat.meteorcrashaddon.CrashAddon;

public class EntityCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Modes> mode = sgGeneral.add(new EnumSetting.Builder<Modes>()
        .name("mode")
        .description("the")
        .defaultValue(Modes.Position)
        .build());

    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("speed")
        .description("Speed in blocks per second.")
        .defaultValue(1337)
        .sliderRange(50, 10000)
        .visible(() -> mode.get() == Modes.Movement)
        .build());

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("Packets per tick")
        .defaultValue(2000)
        .sliderRange(100, 10000)
        .build());

    private final Setting<Boolean> noSound = sgGeneral.add(new BoolSetting.Builder()
        .name("no-sound")
        .description("Blocks the paddle sounds.")
        .defaultValue(false)
        .visible(() -> mode.get() == Modes.Boat)
        .build());

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables module on kick.")
        .defaultValue(true)
        .build());

    public EntityCrash() {
        super(CrashAddon.CATEGORY, "entity-crash", "Does some funny stuff when you ride an entity");
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        Entity vehicle = mc.player.getVehicle();
        if (vehicle == null) {
            error("You must be riding an entity, toggling");
            toggle();
            return;
        }

        switch (mode.get()) {
            case Boat -> {
                // Boat check disabled for 1.21.11 compatibility
                if (false) {
                    error("You must be in a boat, toggling");
                    toggle();
                }
                for (int i = 0; i < amount.get(); i++) {
                    mc.player.connection.send(new ServerboundPaddleBoatPacket(true, true));
                }
            }
            case Movement -> {
                for (int i = 0; i < amount.get(); i++) {
                    Vec3 v = vehicle.position();
                    vehicle.setPos(v.x, v.y + speed.get(), v.z);
                    mc.player.connection.send(new ServerboundMoveVehiclePacket(vehicle.position(), vehicle.getYRot(), vehicle.getXRot(), vehicle.onGround()));
                }
            }
            case Position -> {
                BlockPos start = mc.player.blockPosition();
                Vec3 end = new Vec3(start.getX() + .5, start.getY() + 1, start.getZ() + .5);
                vehicle.setPos(end.x, end.y - 1, end.z);
                for (int i = 0; i < amount.get(); i++) {
                    mc.player.connection.send(new ServerboundMoveVehiclePacket(vehicle.position(), vehicle.getYRot(), vehicle.getXRot(), vehicle.onGround()));
                }
            }
        }
    }

    @EventHandler
    private void onPlaySound(PlaySoundEvent event) {
        // Sound cancellation disabled for 1.21.11 compatibility
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }

    public enum Modes {
        Boat, Position, Movement
    }
}
