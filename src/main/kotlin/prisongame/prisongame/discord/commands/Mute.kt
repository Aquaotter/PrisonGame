package prisongame.prisongame.discord.commands

import me.coralise.spigot.API.events.PostMuteEvent
import me.coralise.spigot.API.events.PreMuteEvent
import me.coralise.spigot.AbstractAnnouncer
import me.coralise.spigot.CustomBansPlus
import me.coralise.spigot.commands.AbstractCommand
import me.coralise.spigot.enums.AnnouncementType
import me.coralise.spigot.enums.MuteType
import me.coralise.spigot.enums.Punishment
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.Bukkit
import prisongame.prisongame.cbp.cbp
import prisongame.prisongame.cbp.issueMute
import java.util.*

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
    }

    issueMute(player, duration, reason)
    event.hook.sendMessage("Muted **${player.name}** for **$reason**.").queue()
}