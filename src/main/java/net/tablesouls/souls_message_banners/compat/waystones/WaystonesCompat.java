package net.tablesouls.souls_message_banners.compat.waystones;

import net.blay09.mods.waystones.api.WaystoneActivatedEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.data.TriggerEntry;
import net.tablesouls.souls_message_banners.data.TriggerManager;
import net.tablesouls.souls_message_banners.data.TriggerType;

public class WaystonesCompat {
    public static final String MODID = "waystones";
    public static final boolean LOADED = ModList.get().isLoaded(MODID);

    @SubscribeEvent
    public static void onWaystoneActivated(WaystoneActivatedEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer serverPlayer)) return;
        if (serverPlayer.getServer() == null) return;

        ResourceKey<Level> dimension = event.getWaystone().getDimension();
        ServerLevel level = serverPlayer.getServer().getLevel(dimension);
        if (level == null) return;

        BlockState state = level.getBlockState(event.getWaystone().getPos());

        TriggerEntry entry = TriggerManager.getBlockTrigger(TriggerType.WAYSTONE_ACTIVATION, state);
        if (entry == null) return;

        Component message = entry.message() != null
                ? Component.Serializer.fromJson(entry.message())
                : Component.translatable("souls_message_banners.message.waystone_activated");

        String blockId = ForgeRegistries.BLOCKS.getKey(state.getBlock()).toString();
        MessageBannerAPI.send(serverPlayer, entry, message, blockId);
    }
}