package no.nav.helse.flex.controller

import java.time.OffsetDateTime

data class HotjarEmotionArkivRequest(
    val survey: String,
    val emotion: Int,
    val sendt: OffsetDateTime
)
