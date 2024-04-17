package hoyo.verse.stockimpact.entity

data class Summaries(val topic: String, val items: List<History>, var expanded: Boolean = false)
