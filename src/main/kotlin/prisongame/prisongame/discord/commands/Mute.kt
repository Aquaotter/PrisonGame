package prisongame.prisongame.discord.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.Bukkit
import prisongame.prisongame.cbp.cbp
import prisongame.prisongame.cbp.issueMute
import prisongame.prisongame.discord.listeners.Messages

fun mute(event: SlashCommandInteractionEvent) {
    val playerName = event.getOption("player")!!.asString
    val duration = event.getOption("duration")!!.asString
    val reason = event.getOption("reason")?.asString ?: "No reason specified"

    if (!Bukkit.getPluginManager().isPluginEnabled("CustomBansPlus")) {
        event.reply("CustomBansPlus is disabled.")
            .setEphemeral(true)
            .queue()
        return
    }

    event.deferReply().queue()
    val player = Bukkit.getOfflinePlayer(playerName)

    if (cbp.u.getValueType(duration) == null) {
        event.reply("Please provide a valid duration.")
            .setEphemeral(true)
            .queue()
        return
    }

    val executorName = event.user.name ?: "Unknown"

    issueMute(player, duration, reason)
    Messages.onPunishment(playerName, reason, "MUTE", executorName, duration)
    event.hook.sendMessage("Muted **${player.name}** for **$reason**.").queue()
}
