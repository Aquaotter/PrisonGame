package prisongame.prisongame.discord.commands

import me.coralise.spigot.API.events.UnbanEvent
import me.coralise.spigot.AbstractAnnouncer
import me.coralise.spigot.CustomBansPlus
import me.coralise.spigot.commands.AbstractCommand
import me.coralise.spigot.enums.AnnouncementType
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import prisongame.prisongame.cbp.issueUnban
import prisongame.prisongame.discord.listeners.Messages

fun unban(event: SlashCommandInteractionEvent) {
    val playerName = event.getOption("player")!!.asString
    val reason = event.getOption("reason")!!.asString

    if (!Bukkit.getPluginManager().isPluginEnabled("CustomBansPlus")) {
        event.reply("CustomBansPlus is disabled.")
            .setEphemeral(true)
            .queue()
        return
    }

    event.deferReply().queue()
    val player = Bukkit.getOfflinePlayer(playerName)

    issueUnban(player)
    Messages.onUnpunishment(playerName, reason, "UNBAN", event.user.name)
    event.hook.sendMessage("Unbanned **${player.name}** for **$reason**.").queue()
}