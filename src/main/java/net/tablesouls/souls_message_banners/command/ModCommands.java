package net.tablesouls.souls_message_banners.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.assets.BannerStyleManager;

@EventBusSubscriber(modid = SoulsMessageBanners.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("souls_message_banner")
                        .requires(source -> source.hasPermission(2))
                        .then(
                                Commands.literal("send")
                                        .then(
                                                Commands.argument("target", EntityArgument.player())
                                                        .then(
                                                                Commands.argument("message", StringArgumentType.string())
                                                                        .executes(ctx -> sendBanner(ctx, BannerStyleManager.DEFAULT))
                                                                        .then(
                                                                                Commands.argument("style_name", ResourceLocationArgument.id())
                                                                                        .executes(ctx -> sendBanner(ctx, ResourceLocationArgument.getId(ctx, "style_name")))
                                                                        )
                                                        )
                                        )
                        )
        );
    }

    private static int sendBanner(CommandContext<CommandSourceStack> ctx, ResourceLocation styleId) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
        String message = StringArgumentType.getString(ctx, "message");

        MessageBannerAPI.send(target, Component.literal(message), styleId);
        return 1;
    }
}