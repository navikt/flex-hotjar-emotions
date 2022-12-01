package no.nav.helse.flex.bigquery

import com.google.api.client.util.DateTime
import com.google.cloud.bigquery.BigQuery
import com.google.cloud.bigquery.InsertAllRequest
import com.google.cloud.bigquery.TableId
import no.nav.helse.flex.logger
import org.springframework.stereotype.Component
import java.time.Instant
import kotlin.collections.HashMap

@Component
class HotjarEmotionTable(val bq: BigQuery) {

    val log = logger()

    fun lagreHotjarEmotion(he: HotjarEmotion) {

        val insertAll = bq.insertAll(
            InsertAllRequest.newBuilder(TableId.of(dataset, hotjarEmotionTable))
                .also { builder ->
                    builder.addRow(he.tilMap())
                }
                .build()
        )

        if (insertAll != null && insertAll.hasErrors()) {
            insertAll.insertErrors.forEach { (t, u) -> log.error("$t - $u") }
            throw RuntimeException("Bigquery insert har errors")
        }
    }
}

private fun HotjarEmotion.tilMap(): Map<String, Any> {
    val data: MutableMap<String, Any> = HashMap()
    data["survey"] = survey
    data["sendt"] = DateTime(sendt.toEpochMilli())
    data["emotion"] = emotion
    return data.toMap()
}

data class HotjarEmotion(
    val survey: String,
    val emotion: Int,
    val sendt: Instant,
)
