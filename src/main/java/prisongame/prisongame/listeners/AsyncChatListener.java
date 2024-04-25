package prisongame.prisongame.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import prisongame.prisongame.FilteredWords;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.cbp.PunishmentKt;
import prisongame.prisongame.config.filter.FilterAction;
import prisongame.prisongame.discord.listeners.Messages;
import prisongame.prisongame.lib.ChatFormat;
import prisongame.prisongame.lib.Role;

import static prisongame.prisongame.config.ConfigKt.getConfig;

public class AsyncChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        var message = event.message();
        var player = event.getPlayer();
        var role = PrisonGame.roles.get(player);
        var lastMessage = PrisonGame.word.get(player);
        var plainMessage = PlainTextComponentSerializer.plainText().serialize(message);
        var legacyMessage = LegacyComponentSerializer.legacyAmpersand().serialize(message).toLowerCase();

        if (plainMessage.contains("piggopet reference"))
            PrisonGame.givepig = true;

        if (lastMessage != null && lastMessage.equals(plainMessage)) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>Do not spam."));
            event.setCancelled(true);
            return;
        }

        if (plainMessage.equals("1775182")) {
            event.setCancelled(true);
            return;
        }

        var display = LegacyComponentSerializer.legacyAmpersand().serialize(player.displayName());
        if (display.contains("SOLITARY")) {
            event.setCancelled(true);
            return;
        }

        if (PrisonGame.chatmuted && role == Role.PRISONER) {
            event.setCancelled(true);
            player.performCommand(String.format("tc %s", plainMessage));
            return;
        }

        if (legacyMessage.contains("&k") && role == Role.WARDEN) {
            player.sendMessage(PrisonGame.mm.deserialize("<red>You cannot do that."));
            event.setCancelled(true);
            return;
        }

        var result = FilteredWords.isClean(legacyMessage);
        if (result != null) {
            var name = result.getFirst();
            var filter = result.getSecond();

            FilteredWords.alert(player, legacyMessage, name, "chat");

            switch (filter.getAction()) {
                case BAN -> {
                    PunishmentKt.issueBan(player, "perm", "(Auto ban) Filter");
                    event.setCancelled(true);
                    return;
                }
                case BLOCK_MESSAGE -> {
                    player.sendMessage(PrisonGame.mm.deserialize("<red>Chat is currently disabled."));
                    event.setCancelled(true);
                    return;
                }
            }
        } else if (!getConfig().getDev())
            Messages.INSTANCE.onChat(player, LegacyComponentSerializer.legacyAmpersand().serialize(message));

        PrisonGame.word.put(player, plainMessage);
        event.renderer(new ChatFormat());
    }
}
