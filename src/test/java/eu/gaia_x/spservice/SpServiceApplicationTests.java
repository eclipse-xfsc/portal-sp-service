package eu.gaia_x.spservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import eu.gaia_x.spservice.model.SaveRequest;
import eu.gaia_x.spservice.model.SelectedItems;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("unused")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
class SpServiceApplicationTests {
  @Autowired
  private MockMvc mockMvc;
  public static MockWebServer mockBackEnd;

  final ObjectMapper mapper = new ObjectMapper();
  final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

  @BeforeAll
  static void setUp() throws IOException {
    mockBackEnd = new MockWebServer();
    mockBackEnd.start(8081);
  }

  @AfterAll
  static void tearDown() throws IOException {
    mockBackEnd.shutdown();
  }

  @Test
  void testSuccessSave() throws Exception {
    mockBackEnd.enqueue(new MockResponse());

    final SaveRequest saveRequest = new SaveRequest(
            "testSP", "testServiceId", "save",
            Arrays.asList(
                    new SelectedItems("0", "childService")
            )
    );
    final String requestJson = ow.writeValueAsString(saveRequest);
    mockMvc.perform(
            post("/api/sp-service/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson)
    ).andExpect(status().isOk());

    final RecordedRequest recordedRequest = mockBackEnd.takeRequest();
    assertEquals("POST", recordedRequest.getMethod());
    assertEquals("/demo/api/sp-service/save", recordedRequest.getPath());
  }

  @Test
  void testSuccessBuild() throws Exception {
    mockBackEnd.enqueue(new MockResponse());

    final SaveRequest saveRequest = new SaveRequest(
            "testSP", "testServiceId", "build",
            Arrays.asList(
                    new SelectedItems("0", "childService")
            )
    );
    final String requestJson = ow.writeValueAsString(saveRequest);
    mockMvc.perform(
            post("/api/sp-service/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson)
    ).andExpect(status().isOk());

    final RecordedRequest recordedRequest = mockBackEnd.takeRequest();
    assertEquals("POST", recordedRequest.getMethod());
    assertEquals("/demo/api/sp-service/save", recordedRequest.getPath());
  }

  @Test
  void testWrongAction() throws Exception {
    final SaveRequest saveRequest = new SaveRequest(
            "testSP", "testServiceId", "wrong",
            Arrays.asList(
                    new SelectedItems("0", "childService")
            )
    );
    final String requestJson = ow.writeValueAsString(saveRequest);
    mockMvc.perform(
                   post("/api/sp-service/save")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(requestJson)
           ).andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.message").value("Incorrect action type"));
  }

  @Test
  void testIncorrectSelectedItems() throws Exception {
    mockMvc.perform(
                   post("/api/sp-service/save")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(ow.writeValueAsString(new SaveRequest(
                                   "testSP", "testServiceId", "save",
                                   null
                           )))
           ).andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.message").value("Incorrect selected items specification"))
           .andExpect(jsonPath("$.path").value("/api/sp-service/save"));

    mockMvc.perform(
                   post("/api/sp-service/save")
                           .contentType(MediaType.APPLICATION_JSON)
                           .content(ow.writeValueAsString(
                                   new SaveRequest(
                                           "testSP", "testServiceId", "save",
                                           Collections.emptyList()
                                   )
                           ))
           ).andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.message").value("Incorrect selected items specification"))
           .andExpect(jsonPath("$.path").value("/api/sp-service/save"));
  }
}
