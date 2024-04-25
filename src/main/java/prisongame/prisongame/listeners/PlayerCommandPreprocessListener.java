package prisongame.prisongame.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import prisongame.prisongame.PrisonGame;

import java.awt.*;

import static prisongame.prisongame.config.ConfigKt.getConfig;
import static prisongame.prisongame.discord.DiscordKt.commandsChannel;

public class PlayerCommandPreprocessListener implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        var player = event.getPlayer();
        var command = event.getMessage().toLowerCase();

        if (!getConfig().getDev()) {
            var embed = new EmbedBuilder()
                    .addField("Player", player.getName(), true)
                    .addField("Command", command, true);

            if (command.startsWith("/pay "))
                embed.setColor(Color.ORANGE);

            commandsChannel.sendMessageEmbeds(embed.build()).queue();
        }

        if (!command.startsWith("/report ") || !command.contains("&k"))
            return;

        player.sendMessage(PrisonGame.mm.deserialize("<red>You cannot use obfuscation."));
        event.setCancelled(true);
    }
}
