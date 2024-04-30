package prisongame.prisongame.discord.commands

import me.coralise.spigot.objects.HistoryRecord
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.bukkit.Bukkit
import prisongame.prisongame.cbp.cbp
import java.awt.Color
import java.util.*

fun history(event: SlashCommandInteractionEvent) {
    val playerName = event.getOption("player")!!.asString
    val pageNumber = event.getOption("page")?.asString?.toIntOrNull() ?: 1
    val executor = event.user.name

    val player = Bukkit.getOfflinePlayer(playerName)

    val rawLogs = cbp.database.getHistories(player.uniqueId)
    val paginatedEmbeds = createPaginatedEmbeds(rawLogs)

    val validatedPageNumber = if (pageNumber < 1 || pageNumber > paginatedEmbeds.size) 1 else pageNumber

    val embed = paginatedEmbeds[validatedPageNumber - 1].build()

    event.reply("/history `$playerName` `$pageNumber` by `$executor`").addEmbeds(embed)
        .mentionRepliedUser(true)
        .queue()
}

fun createPaginatedEmbeds(rawLogs: List<HistoryRecord>): List<EmbedBuilder> {
    val pageSize = 3
    val pages = rawLogs.chunked(pageSize)

    val embeds = mutableListOf<EmbedBuilder>()
    for (page in pages) {
        val embedBuilder = EmbedBuilder()
            .setColor(Color.RED)
            .setTitle("PrisonButBad `Cbp` History")
            .setDescription("Page ${embeds.size + 1}/${pages.size}")

        for (record in page) {
            val date = formatDate(record.date)
            val reason = record.reason
            val executorName = if (record.staffUuid != null) {
                Bukkit.getOfflinePlayer(record.staffUuid).name ?: "THE GREAT CONSOLE"
            } else {
                "THE GREAT CONSOLE"
            }
            val liftedStatus = if (record.unpunishDate != null) formatDateAsTimestamp(record.unpunishDate) else "Not Lifted"
            val action = record.type
            val duration = record.duration ?: "Permanent"

            embedBuilder.addField(
                "$action by $executorName on $date",
                "Reason: $reason\nStatus: $liftedStatus\nDuration: $duration",
                false
            )
        }
        embeds.add(embedBuilder)
    }
    return embeds
}

fun formatDate(date: Date): String {
    val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return formatter.format(date)
}

fun formatDateAsTimestamp(date: Date): String {
    val timestamp = "<t:${date.toInstant().epochSecond}>"
    return timestamp
}

