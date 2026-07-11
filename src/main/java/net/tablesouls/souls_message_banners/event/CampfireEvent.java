package net.tablesouls.souls_message_banners.event;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;

@EventBusSubscriber(modid = SoulsMessageBanners.MODID)
public class CampfireEvent {

    @SubscribeEvent
    public static void onCampfireInteraction(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Player player = event.getEntity();

        if (level.isClientSide()) {
            return;
        }

        if (!state.is(BlockTags.CAMPFIRES)) {
            return;
        }

        if (!SoulsMessageBannersConfig.CAMPFIRE_LIT.get()) {
            return;
        }

        if (state.getValue(BlockStateProperties.LIT)) {
            return;
        }

        level.getServer().tell(new TickTask(level.getServer().getTickCount(), () -> {
            BlockState stateAfter = level.getBlockState(pos);

            if (stateAfter.is(BlockTags.CAMPFIRES)
                    && stateAfter.getValue(BlockStateProperties.LIT)) {

                MessageBannerAPI.send(
                        player,
                        Component.translatable("souls_message_banners.message.campfire_lit"),
                        ResourceLocation.fromNamespaceAndPath(
                                SoulsMessageBanners.MODID,
                                "campfire_lit"
                        )
                );
            }
        }));
    }
}