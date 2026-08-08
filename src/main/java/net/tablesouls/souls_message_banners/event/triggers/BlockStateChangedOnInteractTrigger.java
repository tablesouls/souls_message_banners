package net.tablesouls.souls_message_banners.event.triggers;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.data.TriggerEntry;
import net.tablesouls.souls_message_banners.data.TriggerManager;
import net.tablesouls.souls_message_banners.util.BlockStateHelper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = SoulsMessageBanners.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockStateChangedOnInteractTrigger extends AbstractMessageTrigger {

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;

        BlockPos pos = event.getPos();
        BlockState stateBefore = level.getBlockState(pos);
        Player player = event.getEntity();

        List<TriggerEntry> candidates = TriggerManager.getBlockStateCandidates(stateBefore);
        if (candidates.isEmpty()) return;

        BlockStateHelper.onInteractBlockStateChanged((ServerLevel) level, pos, stateBefore, () -> {
            BlockState stateAfter = level.getBlockState(pos);

            String blockId = ForgeRegistries.BLOCKS.getKey(stateBefore.getBlock()).toString();
            for (TriggerEntry entry : candidates) {
                if (firedBy(entry, stateBefore, stateAfter)) {
                    Component message = resolveMessage(entry, "souls_message_banners.message.block_activated");
                    MessageBannerAPI.send(player, entry, message, blockId);
                }
            }
        });
    }

    private static boolean firedBy(TriggerEntry entry, BlockState before, BlockState after) {
        for (Map.Entry<String, Boolean> target : entry.newBlockState().entrySet()) {
            Optional<BooleanProperty> property = resolveBooleanProperty(before, target.getKey());
            if (property.isEmpty() || !after.hasProperty(property.get())) return false;

            boolean wasTriggered = before.getValue(property.get()) == target.getValue();
            boolean isTriggered = after.getValue(property.get()) == target.getValue();
            if (wasTriggered || !isTriggered) return false;
        }

        return matchesOldBlockState(entry, before);
    }

    private static boolean matchesOldBlockState(TriggerEntry entry, BlockState before) {
        for (Map.Entry<String, Boolean> target : entry.oldBlockState().entrySet()) {
            Optional<BooleanProperty> property = resolveBooleanProperty(before, target.getKey());
            if (property.isEmpty() || before.getValue(property.get()) != target.getValue()) return false;
        }
        return true;
    }

    private static Optional<BooleanProperty> resolveBooleanProperty(BlockState state, String name) {
        for (Property<?> property : state.getProperties()) {
            if (property.getName().equals(name) && property instanceof BooleanProperty booleanProperty) {
                return Optional.of(booleanProperty);
            }
        }
        return Optional.empty();
    }
}