package prisongame.prisongame.discord.listeners

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.Command
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import prisongame.prisongame.cbp.issueBan
import prisongame.prisongame.discord.commands.*
import java.util.*

object Commands : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "players" -> players(event)
            "ban" -> ban(event)
            "mute" -> mute(event)
            "unban" -> unban(event)
            "unmute" -> unmute(event)
            "link" -> link(event)
            "history" -> history(event)
            else -> {}
        }
    }

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        val id = event.button.id ?: ""

        if (!id.startsWith("ban_"))
            return

        val uuid = UUID.fromString(id.substring(4))
        val player = Bukkit.getOfflinePlayer(uuid) ?: return

        issueBan(player, "perm", "slurs")
        Messages.onPunishment(player.name.toString(), "slurs", "BAN", event.member?.nickname.toString(),"perm")
        event.reply("Banned **${player.name}** for **slurs**.").queue()
    }

    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        val players = Bukkit.getOnlinePlayers().map { Command.Choice(it.name, it.name) }

        when (event.name) {
            "ban", "mute", "kick", "warn", "unmute" -> {
                if (event.focusedOption.name != "player")
                    return

                event.replyChoices(players).queue()
            }
        }
    }
}