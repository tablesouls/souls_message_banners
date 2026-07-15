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
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;

@Mod.EventBusSubscriber(modid = SoulsMessageBanners.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CampfireEvent {
    @SubscribeEvent
    public static void onCampfireInteraction(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Player player = event.getEntity();

        if(level.isClientSide()) return;

        if (state.is(BlockTags.CAMPFIRES)) {
            if (!SoulsMessageBannersConfig.CAMPFIRE_LIT.get()) return;
            
            boolean wasLit = state.getValue(BlockStateProperties.LIT);
            if (wasLit) return;

            level.getServer().tell(new TickTask(level.getServer().getTickCount(), () -> {
                BlockState stateAfter = level.getBlockState(pos);

                if (stateAfter.is(BlockTags.CAMPFIRES)) {
                    boolean isLitAfter = stateAfter.getValue(BlockStateProperties.LIT);

                    if (isLitAfter) {
                        MessageBannerAPI.send(player,
                                Component.translatable("souls_message_banners.message.campfire_lit"),
                                ResourceLocation.fromNamespaceAndPath(SoulsMessageBanners.MODID, "campfire_lit"));
                    }
                }
            }));
        }
    }
}
