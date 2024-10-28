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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonFileReader jsonFileReader;

    private static final String BASE_URL = "/time/tracker/api";
    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    @DisplayName("Test 1: reading all tasks")
    public void shouldReturnAllTasks() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String jsonResponse = jsonFileReader.readJsonFromFile("rest/task_controller/test_case_1/response.json");

        assertJson(responseBodyContent)
                .where()
                .keysInAnyOrder()
                .arrayInAnyOrder()
                .isEqualTo(jsonResponse);
    }


    @Test
    @Transactional
    @DisplayName("Test 2: creating new task")
    public void shouldReturnSavedTask() throws Exception {
        String jsonRequest = jsonFileReader.readJsonFromFile("rest/task_controller/test_case_2/request.json");

        MvcResult mvcResult = mockMvc.perform(post(BASE_URL + "/tasks")
                        .content(jsonRequest)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String jsonResponse = jsonFileReader.readJsonFromFile("rest/task_controller/test_case_2/response.json");

        assertJson(responseBodyContent)
                .where()
                .keysInAnyOrder()
                .arrayInAnyOrder()
                .isEqualTo(jsonResponse);
    }

    @Test
    @Transactional
    @DisplayName("Test 3: updating task")
    public void shouldReturnUpdatedTask() throws Exception {
        String jsonRequest = jsonFileReader.readJsonFromFile("rest/task_controller/test_case_3/request.json");

        MvcResult mvcResult = mockMvc.perform(put(BASE_URL + "/tasks/3")
                        .content(jsonRequest)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String jsonResponse = jsonFileReader.readJsonFromFile("rest/task_controller/test_case_3/response.json");

        assertJson(responseBodyContent)
                .where()
                .keysInAnyOrder()
                .arrayInAnyOrder()
                .isEqualTo(jsonResponse);
    }

    @Test
    @Transactional
    @DisplayName("Test 4: deleting task")
    public void shouldReturnSuccessWhenDeleteTask() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(BASE_URL + "/tasks/3"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String result = "Task with id:3 deleted successfully!";
        assertEquals(responseBodyContent, result);
    }

}