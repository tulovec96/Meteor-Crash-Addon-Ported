package widecat.meteorcrashaddon.modules;

import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screens.inventory.LecternScreen;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import widecat.meteorcrashaddon.CrashAddon;

public class LecternCrash extends Module {

    public LecternCrash() {
        super(CrashAddon.CATEGORY, "lectern-crash", "Sends a funny packet when you open a lectern");
    }

    @EventHandler
    private void onOpenScreenEvent(OpenScreenEvent event) {
        if (!(event.screen instanceof LecternScreen)) return;
        // Lectern crash disabled for 1.21.11 compatibility
        toggle();
    }
}
