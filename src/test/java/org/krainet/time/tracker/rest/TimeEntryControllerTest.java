package org.krainet.time.tracker.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.krainet.time.tracker.core.services.TaskService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TimeEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonFileReader jsonFileReader;

    private static final String BASE_URL = "/time/tracker/api";
    @Mock
    private TaskService timeEntryService;

    @InjectMocks
    private TaskController timeEntryController;

    @Test
    @DisplayName("Test 1: reading all timeEntries")
    public void shouldReturnAllTimeEntries() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "/timeEntries"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String jsonResponse = jsonFileReader.readJsonFromFile("rest/timeEntry_controller/test_case_1/response.json");

        assertJson(responseBodyContent)
                .where()
                .keysInAnyOrder()
                .arrayInAnyOrder()
                .isEqualTo(jsonResponse);
    }


    @Test
    @Transactional
    @DisplayName("Test 2: creating new timeEntry")
    public void shouldReturnSavedTimeEntry() throws Exception {
        String jsonRequest = jsonFileReader.readJsonFromFile("rest/timeEntry_controller/test_case_2/request.json");

        MvcResult mvcResult = mockMvc.perform(post(BASE_URL + "/timeEntries")
                        .content(jsonRequest)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String jsonResponse = jsonFileReader.readJsonFromFile("rest/timeEntry_controller/test_case_2/response.json");

        assertJson(responseBodyContent)
                .where()
                .keysInAnyOrder()
                .arrayInAnyOrder()
                .isEqualTo(jsonResponse);
    }

    @Test
    @Transactional
    @DisplayName("Test 3: updating timeEntry")
    public void shouldReturnUpdatedTimeEntry() throws Exception {
        String jsonRequest = jsonFileReader.readJsonFromFile("rest/timeEntry_controller/test_case_3/request.json");

        MvcResult mvcResult = mockMvc.perform(put(BASE_URL + "/timeEntries/2")
                        .content(jsonRequest)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String jsonResponse = jsonFileReader.readJsonFromFile("rest/timeEntry_controller/test_case_3/response.json");

        assertJson(responseBodyContent)
                .where()
                .keysInAnyOrder()
                .arrayInAnyOrder()
                .isEqualTo(jsonResponse);
    }

    @Test
    @Transactional
    @DisplayName("Test 4: deleting timeEntry")
    public void shouldReturnSuccessWhenDeleteTimeEntry() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(BASE_URL + "/timeEntries/2"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String result = "TimeEntry with id:2 deleted successfully!";
        assertEquals(responseBodyContent, result);
    }

    @Test
    @Transactional
    @DisplayName("Test 5: start timeEntry")
    public void shouldReturnSuccessWhenStartTimeEntry() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "/timeEntries/2/start"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String result = "TimeEntry with id:2 started successfully!";
        assertEquals(responseBodyContent, result);
    }

    @Test
    @Transactional
    @DisplayName("Test 6: finish timeEntry")
    public void shouldReturnSuccessWhenFinishedTimeEntry() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "/timeEntries/3/finish"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String result = "TimeEntry with id:3 finished successfully!";
        assertEquals(responseBodyContent, result);
    }

}