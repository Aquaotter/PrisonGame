package prisongame.prisongame.discord.commands

import me.coralise.spigot.API.events.UnmuteEvent
import me.coralise.spigot.AbstractAnnouncer
import me.coralise.spigot.CustomBansPlus
import me.coralise.spigot.commands.AbstractCommand
import me.coralise.spigot.enums.AnnouncementType
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import prisongame.prisongame.cbp.issueUnmute
import prisongame.prisongame.discord.listeners.Messages

fun unmute(event: SlashCommandInteractionEvent) {
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

    issueUnmute(player)
    Messages.onUnpunishment(playerName, reason, "UNMUTE", event.user.name)
    event.hook.sendMessage("Unmuted **${player.name}** for **$reason**.").queue()
}