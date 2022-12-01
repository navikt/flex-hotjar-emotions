package no.nav.helse.flex.controller

import no.nav.helse.flex.bigquery.HotjarEmotion
import no.nav.helse.flex.bigquery.HotjarEmotionTable
import no.nav.helse.flex.logger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class HotjarEmotionController(
    val hotjarEmotionTable: HotjarEmotionTable
) {

    private val log = logger()

    private fun HotjarEmotionRequest.tilBqRequest(): HotjarEmotion = HotjarEmotion(
        emotion = this.emotion,
        survey = this.survey,
        sendt = Instant.now()
    )

    private fun HotjarEmotionArkivRequest.tilBqRequest(): HotjarEmotion = HotjarEmotion(
        emotion = this.emotion,
        survey = this.survey,
        sendt = this.sendt.toInstant()
    )

    @PostMapping(value = ["/api/v1/emotion"], consumes = [APPLICATION_JSON_VALUE])
    @ResponseStatus(code = HttpStatus.CREATED)
    fun lagreEmotion(@RequestBody hotjarEmotionRequest: HotjarEmotionRequest) {
        log.info("Lagrer emotion $hotjarEmotionRequest")
        hotjarEmotionTable.lagreHotjarEmotion(hotjarEmotionRequest.tilBqRequest())
    }

    @PostMapping(value = ["/api/v1/emotion/arkiv"], consumes = [APPLICATION_JSON_VALUE])
    @ResponseStatus(code = HttpStatus.CREATED)
    fun lagreEmotion(@RequestBody hotjarEmotionRequest: HotjarEmotionArkivRequest) {
        log.info("Lagrer emotion $hotjarEmotionRequest")
        hotjarEmotionTable.lagreHotjarEmotion(hotjarEmotionRequest.tilBqRequest())
    }
}
