package prisongame.prisongame.config.filter

import cc.ekblad.toml.decode
import cc.ekblad.toml.model.TomlException
import cc.ekblad.toml.tomlMapper
import prisongame.prisongame.config.fallbackFilters
import prisongame.prisongame.config.handleTOMLException
import prisongame.prisongame.instance

private val file = instance.dataFolder.resolve("filter.toml")

data class Filters(
    val filters: Map<String, Filter>
)

data class Filter(
    val contentMatch: ContentMatch = ContentMatch.SANITIZED,
    val action: FilterAction,
    val type: FilterType,
    val value: String
) {
    private val regex = if (type == FilterType.REGEX) Regex(value) else Regex("")

    fun test(input: String): Boolean {
        return when (type) {
            FilterType.PLAIN_TEXT -> input.contains(value)
            FilterType.REGEX -> regex.containsMatchIn(input)
        }
    }
}

fun reloadFilter(): Map<String, Filter> {
    val mapper = tomlMapper {
        mapping<Filter>("content-match" to "contentMatch")
    }

    if (!file.exists())
        instance.saveResource(file.name, false)

    return try {
        mapper.decode<Filters>(file.toPath()).filters
    } catch (exception: TomlException) {
        handleTOMLException(exception)
        fallbackFilters.filters
    }
}