package no.nav.helse.flex

import com.google.cloud.bigquery.BigQuery
import no.nav.helse.flex.controller.HotjarEmotionRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE, printOnlyOnFailure = false)

class ApplicationTest {

    @MockBean
    lateinit var bq: BigQuery

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun lagrerEmotion() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/emotion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(HotjarEmotionRequest(survey = "123", emotion = 3).serialisertTilString())
        ).andExpect(MockMvcResultMatchers.status().isCreated).andReturn()
    }

    @Test
    fun kreverRiktigRequest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/emotion")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest).andReturn()
    }

    @Test
    fun kreverRiktigPath() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/emotionasagdfg")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        ).andExpect(MockMvcResultMatchers.status().isNotFound).andReturn()
    }
}
