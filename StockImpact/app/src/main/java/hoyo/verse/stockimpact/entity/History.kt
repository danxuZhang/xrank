package hoyo.verse.stockimpact.entity

import java.sql.Date

data class History(
    val ID: Int,
    val Topic: Int,
    val Time: Date,
    val Summary: String
)
