package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.Vec3;
import widecat.meteorcrashaddon.CrashAddon;

import java.util.Random;

public class CreativeCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("Packets per tick")
        .defaultValue(15)
        .min(1)
        .sliderMax(100)
        .build());

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables module on kick.")
        .defaultValue(true)
        .build());

    public CreativeCrash() {
        super(CrashAddon.CATEGORY, "creative-crash", "");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!mc.player.getAbilities().instabuild) {
            error("its literally called creative crash why are you trying this in survival");
            toggle();
        }

        Vec3 pos = pickRandomPos();
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        ItemStack the = new ItemStack(Items.CAMPFIRE);
        list.add(DoubleTag.valueOf(pos.x));
        list.add(DoubleTag.valueOf(pos.y));
        list.add(DoubleTag.valueOf(pos.z));
        //idk
        tag.putString("id", "minecraft:small_fireball");
        tag.put("Pos", list);
        // Data component setting disabled for 1.21.11 compatibility
        for (int i = 0; i < amount.get(); i++) {
            mc.player.connection.send(new ServerboundSetCreativeModeSlotPacket(1, the));
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }

    private Vec3 pickRandomPos() {
        return new Vec3(new Random().nextInt(0xFFFFFF), 255, new Random().nextInt(0xFFFFFF));
    }
}
