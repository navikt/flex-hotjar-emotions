package no.nav.helse.flex.bigquery

import com.google.cloud.bigquery.BigQuery
import com.google.cloud.bigquery.BigQueryException
import com.google.cloud.bigquery.Field
import com.google.cloud.bigquery.Schema
import com.google.cloud.bigquery.StandardSQLTypeName
import com.google.cloud.bigquery.StandardTableDefinition
import com.google.cloud.bigquery.TableDefinition
import com.google.cloud.bigquery.TableId
import com.google.cloud.bigquery.TableInfo
import no.nav.helse.flex.logger
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
@Profile("createtable")
class TableCreator(
    val bigQuery: BigQuery
) {

    val log = logger()

    @PostConstruct
    fun initTestTabell() {
        log.info("Kjører postconstruct i table creator")

        createTable(
            tableName = hotjarEmotionTable,
            schema = Schema.of(
                Field.newBuilder("survey", StandardSQLTypeName.STRING)
                    .setDescription("Hvilken survey id surveyen har i hotjar").build(),
                Field.newBuilder("emotion", StandardSQLTypeName.INT64)
                    .setDescription("Emotion som ble gitt i feedback. 1 til 5").build(),
                Field.newBuilder("sendt", StandardSQLTypeName.TIMESTAMP)
                    .setDescription("Tidspunktet feedbacken ble sendt").build(),
            )
        )
    }

    fun createTable(tableName: String, schema: Schema) {

        val table = bigQuery.getTable(TableId.of(dataset, tableName))
        if (table != null && table.exists()) {
            log.info("Table $tableName eksisterer allerede")
            return
        }

        try {
            // Initialize client that will be used to send requests. This client only needs to be created
            // once, and can be reused for multiple requests.
            val tableId = TableId.of(dataset, tableName)
            val tableDefinition: TableDefinition = StandardTableDefinition.of(schema)
            val tableInfo = TableInfo.newBuilder(tableId, tableDefinition).build()
            val create = bigQuery.create(tableInfo)
            log.info("Table $tableName created successfully: ${create.tableId.iamResourceName}")
        } catch (e: BigQueryException) {
            log.error("Table was not created.,", e)
            throw e
        }
    }
}

const val hotjarEmotionTable = "hotjar_emotion"
