package org.krainet.time.tracker.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.krainet.time.tracker.core.services.EmployeeService;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonFileReader jsonFileReader;

    private static final String BASE_URL = "/time/tracker/api";
    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;


    @Test
    @DisplayName("Test 1: reading all employees")
    public void shouldReturnAllEmployees() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "/employees"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String jsonResponse = jsonFileReader.readJsonFromFile("rest/employee_controller/test_case_1/response.json");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedResponseNode = objectMapper.readTree(jsonResponse);
        JsonNode actualResponseNode = objectMapper.readTree(responseBodyContent);

        assertTrue(actualResponseNode.isArray());
        assertTrue(expectedResponseNode.isArray());
        assertEquals(expectedResponseNode.size(), actualResponseNode.size());

        for (int i = 0; i < expectedResponseNode.size(); i++) {
            JsonNode expectedEmployee = expectedResponseNode.get(i);
            JsonNode actualEmployee = actualResponseNode.get(i);

            assertEquals(expectedEmployee.get("username").asText(), actualEmployee.get("username").asText());
            assertEquals(expectedEmployee.get("email").asText(), actualEmployee.get("email").asText());

            assertNotNull(actualEmployee.get("password_hash"));
            assertTrue(!actualEmployee.get("password_hash").asText().isEmpty());
        }
    }


    @Test
    @Transactional
    @DisplayName("Test 2: creating new employee")
    public void shouldReturnSavedEmployee() throws Exception {
        String jsonRequest = jsonFileReader.readJsonFromFile("rest/employee_controller/test_case_2/request.json");

        MvcResult mvcResult = mockMvc.perform(post(BASE_URL + "/employees")
                        .content(jsonRequest)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String jsonResponse = jsonFileReader.readJsonFromFile("rest/employee_controller/test_case_2/response.json");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedResponseNode = objectMapper.readTree(jsonResponse);
        JsonNode actualResponseNode = objectMapper.readTree(responseBodyContent);

        assertEquals(expectedResponseNode.get("username").asText(), actualResponseNode.get("username").asText());
        assertEquals(expectedResponseNode.get("email").asText(), actualResponseNode.get("email").asText());

        assertNotNull(actualResponseNode.get("password_hash"));
        assertTrue(!actualResponseNode.get("password_hash").asText().isEmpty());
    }

    @Test
    @Transactional
    @DisplayName("Test 3: updating employee")
    public void shouldReturnUpdatedEmployee() throws Exception {
        String jsonRequest = jsonFileReader.readJsonFromFile("rest/employee_controller/test_case_3/request.json");

        MvcResult mvcResult = mockMvc.perform(put(BASE_URL + "/employees/3")
                        .content(jsonRequest)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String jsonResponse = jsonFileReader.readJsonFromFile("rest/employee_controller/test_case_3/response.json");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedResponseNode = objectMapper.readTree(jsonResponse);
        JsonNode actualResponseNode = objectMapper.readTree(responseBodyContent);

        assertEquals(expectedResponseNode.get("username").asText(), actualResponseNode.get("username").asText());
        assertEquals(expectedResponseNode.get("email").asText(), actualResponseNode.get("email").asText());

        assertNotNull(actualResponseNode.get("password_hash"));
        assertTrue(!actualResponseNode.get("password_hash").asText().isEmpty());
    }

    @Test
    @Transactional
    @DisplayName("Test 4: deleting employee")
    public void shouldReturnSuccessWhenDeleteEmployee() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(BASE_URL + "/employees/3"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBodyContent = mvcResult.getResponse().getContentAsString();
        String result = "Employee with id:3 deleted successfully!";
        assertEquals(responseBodyContent, result);
    }

}