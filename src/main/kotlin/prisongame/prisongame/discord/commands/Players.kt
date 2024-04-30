
package prisongame.prisongame.discord.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import prisongame.prisongame.PrisonGame
import prisongame.prisongame.commands.staff.VanishCommand
import prisongame.prisongame.lib.Role

fun players(event: SlashCommandInteractionEvent) {
    val warden = PrisonGame.warden
    val embed = EmbedBuilder()

    val allplayers = Bukkit.getOnlinePlayers()
    val players = allplayers.filter { !it.persistentDataContainer.has(VanishCommand.VANISHED) }
    val vanishedPlayers = players.filter { it.persistentDataContainer.has(VanishCommand.VANISHED) }
    val prisoners = players.filter { PrisonGame.roles[it] == Role.PRISONER && !it.persistentDataContainer.has(VanishCommand.VANISHED) }
    val guards = players.filter { PrisonGame.roles[it] != Role.WARDEN && it !in prisoners && !it.persistentDataContainer.has(VanishCommand.VANISHED) }

    val verb = if (players.size == 1) "is" else "are"
    val noun = if (players.size == 1) "player" else "players"
    embed.setTitle("There $verb ${players.size} $noun online.")

    if (warden != null)
        embed.setDescription("""
            **[WARDEN] ${warden.name}** [${warden.ping}ms]
            ${PrisonGame.wardentime[warden]!! / 20 / 60} minutes
        """.trimIndent())
    else embed.setDescription("No warden!")

    embed.addField(
        "Guards (${guards.size})",
        guards.joinToString("\n") {
            val display = PlainTextComponentSerializer.plainText().serialize(it.displayName())
            if (it.isDead) ":skull_crossbones: **${it.name}**"
            else "**$display** [${it.ping}ms]"
        },
        false)

    embed.addField(
        "Prisoners (${prisoners.size})",
        prisoners.joinToString("\n") {
            val display = PlainTextComponentSerializer.plainText().serialize(it.displayName())
            if (it.isDead) ":skull_crossbones: **${it.name}**"
            else "**$display** [${it.ping}ms]"
        },
        false)
    if(event.member?.hasPermission(Permission.BAN_MEMBERS) == true){ // @Goose pls rewrite this if needed -Aquaotter (I am not good at kotlin)
        embed.addField(
            "Vanished (${vanishedPlayers.size}) [ONLY STAFF CAN SEE THIS]",
            vanishedPlayers.joinToString("\n") {
                val display = PlainTextComponentSerializer.plainText().serialize(it.displayName())
                "**$display** [${it.ping}ms]"
            },
            false)
    }
    event.replyEmbeds(embed.build()).setEphemeral(true).queue();
}