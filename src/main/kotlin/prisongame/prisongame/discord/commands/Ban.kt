package prisongame.prisongame.discord.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.Bukkit
import prisongame.prisongame.cbp.cbp
import prisongame.prisongame.cbp.issueBan

fun ban(event: SlashCommandInteractionEvent) {
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
    }

    issueBan(player, duration, reason)
    event.hook.sendMessage("Banned **${player.name}** for **$reason**.").queue()
}