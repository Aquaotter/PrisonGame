package prisongame.prisongame.config

import prisongame.prisongame.config.filter.Filter
import prisongame.prisongame.config.filter.FilterAction
import prisongame.prisongame.config.filter.FilterType
import prisongame.prisongame.config.filter.Filters

private val fallbackLocation = TOMLLocation(x = 0.5, y = -60.0, z = 0.5)

val fallbackConfig = Config(
    dev = true,
    general = Config.General(
        discordInvite = "",
        rules = emptyList()
    ),
    warden = Config.Warden(
        help = emptyMap(),
        prefix = Config.Warden.Prefix(
            bannedContainers = listOf(
                "SOLITARY",
                "BOOSTER",
                "ASCENDING"
            )
        )
    ),
    discord = Config.Discord(
        token = "",
        guild = "",
        chatChannel = "",
        commandsChannel = "",
        punshmentChannel = "",
        filterChannel = "",
        linkedRole = "",
        canSpeakRole = ""
    )
)

val fallbackPrisons = Prisons(
    defaultPrison = "jail",
    prisons = mapOf(
        "jail" to Prison(
            priority = 0,
            rawMaterial = "minecraft:cobblestone",
            showInSelector = false,
            rawDisplayName = "Jail",
            name = "Jail",
            firstTrack = fallbackLocation,
            secondTrack = fallbackLocation,
            nurse = fallbackLocation,
            warden = fallbackLocation,
            prisoner = fallbackLocation,
            blackMarketIn = fallbackLocation,
            blackMarketOut = fallbackLocation,
            solitary = fallbackLocation,
            bertrude = fallbackLocation
        )
    )
)

val fallbackFilters = Filters(filters = mapOf(
    "everything" to Filter(
        action = FilterAction.BLOCK_MESSAGE,
        type = FilterType.PLAIN_TEXT,
        value = ""
    )
))