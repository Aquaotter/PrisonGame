package prisongame.prisongame.discord.listeners

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.utils.MarkdownSanitizer
import net.dv8tion.jda.api.utils.Timestamp
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.advancement.Advancement
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType
import prisongame.prisongame.PrisonGame
import prisongame.prisongame.config.config
import prisongame.prisongame.discord.chatChannel
import prisongame.prisongame.discord.commands.formatDateAsTimestamp
import prisongame.prisongame.discord.jda
import prisongame.prisongame.discord.punshmentChannel
import java.awt.Color
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.Duration
import kotlin.time.toJavaDuration

object Messages : ListenerAdapter() {
    @Suppress("NAME_SHADOWING")
    fun onDeath(victim: Player, attacker: Entity?, message: String) {
        var message = message.replace(victim.name, "**${victim.name}**")

        if (attacker != null)
            message = message.replace(attacker.name, "**${attacker.name}**")
            message = message.replace("_", "\\_")
        chatChannel.sendMessage(message).queue();
    }

    fun onGrantAdvancement(player: Player, advancement: Advancement) {
        chatChannel.sendMessage(String.format(
            "**%s** has made the advancement **%s**",
            player.name.replace("_", "\\_"),
            PlainTextComponentSerializer.plainText().serialize(advancement.displayName())
        )).queue();
    }

    fun onJoin(player: Player) {
        val solitary = player.hasPotionEffect(PotionEffectType.WATER_BREATHING)

        chatChannel.sendMessage(String.format(
            "**%s** was caught and sent %s!",
            player.name.replace("_", "\\_"),
            if (solitary) "back to solitary"
            else "to prison"
        )).queue();
    }

    fun onLeave(player: Player) {
        chatChannel.sendMessage(String.format(
            "**%s** ran off somewhere else...",
            player.name.replace("_", "\\_")
        )).queue();
    }

    fun onChat(player: Player, message: String) {
        chatChannel.sendMessage(String.format(
            "**%s**: %s",
            PlainTextComponentSerializer.plainText()
                .serialize(player.displayName())
                .replace("_", "\\_"),
            message
                .replace("@", "`@`")
                .replace("_", "\\_")
        )).queue();
    }
    fun onIntercom(message: String) {
        chatChannel.sendMessage("\n"+String.format(
            "**%s**: %s",
            PlainTextComponentSerializer.plainText()
                .serialize(Component.text("INTERCOM >> ")),
            message
                .replace("@", "`@`")
                .replace("_", "\\_")
        )+"\n").queue()
    }

    fun onPunishment(player: String, reason: String, punishment: String, executor: String, time: String) {
        val timestamp = Instant.now()
        var truetime = time

        if (time.contains("hack-client")) truetime = "perm" //TODO make this config editable -Aquaotter
        if (time.contains("griefing")) truetime = "3d"
        if (time.contains("duping")) truetime = "3d"
        if (time.contains("xray")) truetime = "7d"
        if (time.contains("spawn_killing")) truetime = "3d"

        val embedBuilder = EmbedBuilder()
            .setTitle(punishment)
            .addField("Player: ", baseValue(player), true)
            .addField("Reason: ", baseValue(reason), true)
            .addField("Punishment: ", baseValue(punishment), true)
            .addField("Executor: ", baseValue(executor), true)
            .addField("Punishment Time of Execution: ", formatDateAsTimestamp(Date.from(timestamp)), false)
            .setColor(Color.RED)

        if (punishment !in listOf("KICK", "WARN")) {
            embedBuilder.addField("Provided Punishment Time: ", baseValue(time) + " (Formatted Time): `$truetime`", false)

            if (truetime != "perm") {
                val durationStr = truetime
                embedBuilder.addField("Time of un$punishment", durationStr, false)
            } else {
                embedBuilder.addField("Time of un$punishment", "NEVER", false)
            }
        } else {
            // For kick or warn, do not include the punishment time
            embedBuilder.addField("Reason: ", baseValue(reason), true)
        }

        val embed = embedBuilder.build()

        punshmentChannel.sendMessageEmbeds(embed).queue()
    }
    fun onUnpunishment(player: String, reason: String, punishment: String, executor: String) {
        val timestamp = Instant.now()

        val embedBuilder = EmbedBuilder()
            .setTitle("Punishment")
            .addField("Player", baseValue(player), true)
            .addField("Reason", baseValue(reason), true)
            .addField("Punishment", baseValue(punishment), true)
            .addField("Executor", baseValue(executor), true)
            .addField("Time of Executed", formatDiscordTimestamp(timestamp), false)
            .setColor(Color.GREEN)

        val embed = embedBuilder.build()

        punshmentChannel.sendMessageEmbeds(embed).queue()
    }

    fun baseValue(input: String): String {
        return "`$input`"
    }

    private fun formatDiscordTimestamp(timestamp: Instant): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneId.of("UTC"))
        val formattedTimestamp = formatter.format(timestamp)
        return "<t:${timestamp.epochSecond}>"
    }


    override fun onMessageReceived(event: MessageReceivedEvent) {
        val content = event.message.contentDisplay
        val channel = event.channel
        val user = event.author

        if (channel.id != config.discord.chatChannel || user.id == jda.selfUser.id || user.isBot)
            return

        val replacedContent = content
            .replace("\\b(https?://\\S+\\b)".toRegex(), "(Link)")
            .replace("\n", " ")
            .ifEmpty { "(Image)" }

        Bukkit.getServer().sendMessage(PrisonGame.mm.deserialize(
            "<name>: <message>",
            Placeholder.component("name", Component
                .text(user.name)
                .color(TextColor.fromHexString("#b3b9fc"))),
            Placeholder.component("message", Component
                .text(replacedContent)
                .color(TextColor.fromHexString("#d6daff")))
        ))
    }
}