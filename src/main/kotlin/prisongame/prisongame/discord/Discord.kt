package prisongame.prisongame.discord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands as Command
import net.dv8tion.jda.api.requests.GatewayIntent
import prisongame.prisongame.config.config
import prisongame.prisongame.discord.listeners.Commands
import prisongame.prisongame.discord.listeners.Messages

lateinit var jda: JDA
lateinit var guild: Guild
lateinit var chatChannel: TextChannel
lateinit var punshmentChannel: TextChannel
lateinit var commandsChannel: TextChannel
lateinit var filterChannel: TextChannel
lateinit var linkedRole: Role
lateinit var canSpeakRole: Role

fun setup() {
    if (config.dev)
        return

    jda = JDABuilder
        .createDefault(config.discord.token)
        .enableIntents(GatewayIntent.GUILD_MESSAGES)
        .enableIntents(GatewayIntent.MESSAGE_CONTENT)
        .addEventListeners(Messages, Commands)
        .setActivity(Activity.playing("Playing `PrisonButBad.Minehut.GG` 1.20.4"))
        .build();

    jda.awaitReady()
    guild = jda.getGuildById(config.discord.guild)!!
    chatChannel = jda.getTextChannelById(config.discord.chatChannel)!!
    commandsChannel = jda.getTextChannelById(config.discord.commandsChannel)!!
    punshmentChannel = jda.getTextChannelById(config.discord.punshmentChannel)!!
    filterChannel = jda.getTextChannelById(config.discord.filterChannel)!!
    linkedRole = jda.getRoleById(config.discord.linkedRole)!!
    canSpeakRole = jda.getRoleById(config.discord.canSpeakRole)!!

    chatChannel.guild.updateCommands().addCommands(
        Command.slash("players", "List online players."),
        Command.slash("link", "Link your Minecraft and Discord accounts.")
            .addOption(OptionType.INTEGER, "code", "The link code.", true),
        Command.slash("ban", "Ban players from PrisonButBad.")
            .addOption(OptionType.STRING, "player", "The player to ban.", true, true)
            .addOption(OptionType.STRING, "duration", "The ban duration.", true)
            .addOption(OptionType.STRING, "reason", "The ban reason.", true)
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)),
        Command.slash("mute", "Mute players on PrisonButBad.")
            .addOption(OptionType.STRING, "player", "The player to mute.", true, true)
            .addOption(OptionType.STRING, "duration", "The mute duration.", true)
            .addOption(OptionType.STRING, "reason", "The mute reason.", true)
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)),
        Command.slash("unban", "Unban players from PrisonButBad.")
            .addOption(OptionType.STRING, "player", "The player to unban.", true, true)
            .addOption(OptionType.STRING, "reason", "The unban reason.", false, true)
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)),
        Command.slash("unmute", "Unmute players from PrisonButBad.")
            .addOption(OptionType.STRING, "player", "The player to unmute.", true, true)
            .addOption(OptionType.STRING, "reason", "The unmute reason.", false, true)
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)),
        Command.slash("history", "Gets player History from PrisonButBad [TEST SERVER].")
            .addOption(OptionType.STRING, "player", "The player to get History", true, false)
            .addOption(OptionType.STRING, "page", "Page of Players History", true, false)
            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)),
    ).queue()
}

fun close() {
    if (config.dev)
        return

    jda.shutdown()
}

fun addMuted(member: Member) {
    guild.removeRoleFromMember(member, canSpeakRole).queue()
}

fun removeMuted(member: Member) {
    guild.addRoleToMember(member, canSpeakRole).queue()
}