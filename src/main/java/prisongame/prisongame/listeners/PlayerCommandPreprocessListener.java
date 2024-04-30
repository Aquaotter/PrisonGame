package prisongame.prisongame.listeners;

import me.coralise.bungee.CustomBansPlus;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.discord.listeners.Messages;

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

        if (command.startsWith("/ban ") || command.startsWith("/mute ") || command.startsWith("/kick ") || command.startsWith("/warn ")) {
            String[] args = command.split(" ");
            String punishment = args[0].toUpperCase().replaceAll("/", "").replaceAll("\\\\", "");
            String punishmentperm = args[0].toLowerCase().replaceAll("/", "").replaceAll("\\\\", "");
            if (!(player.hasPermission("custombansplus." + punishmentperm))) return;
            String playerName = args[1];
            if (Bukkit.getPlayer(playerName).hasPermission("custombanplus.bypass")) return;
            String reason;
            String executorName = event.getPlayer().getName();
            String duration = args[2];
            StringBuilder reasonBuilder = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                reasonBuilder.append(args[i]).append(" ");
            }
            reason = reasonBuilder.toString().trim();
            if (punishment.equals("WARN") || punishment.equals("KICK")) {
                duration = reason;
                reason = args[2];
            }
            Messages.INSTANCE.onPunishment(playerName, reason, punishment, executorName, duration);
        }



        if (!command.startsWith("/report ") || !command.contains("&k"))
            return;

        player.sendMessage(PrisonGame.mm.deserialize("<red>You cannot use obfuscation."));
        event.setCancelled(true);
    }
}
