package com.example.conflictTracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ConflictTrackerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void getConflictsFilteredByStatus() throws Exception {
        mockMvc.perform(get("/api/v1/conflicts").param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    void getConflictsByCountryCode() throws Exception {
        mockMvc.perform(get("/api/v1/countries/UKR/conflicts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].countryCodes").isArray());
    }

    @Test
    void createFactionAndDeleteIt() throws Exception {
        String factionBody = objectMapper.writeValueAsString(new FactionBody(
                "Test Faction",
                1L,
                new String[]{"USA", "UKR"}
        ));

        String response = mockMvc.perform(post("/api/v1/factions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(factionBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Faction"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long createdId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/v1/factions/{id}", createdId))
                .andExpect(status().isNoContent());
    }

    @Test
    void createEvent() throws Exception {
        String body = """
                {
                  "eventDate": "2025-01-01",
                  "location": "Kharkiv",
                  "description": "Event created from test",
                  "conflictId": 1
                }
                """;

        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.location").value("Kharkiv"));
    }

    @Test
    void createConflictWithCountries() throws Exception {
        String body = """
                {
                  "name": "Test Conflict",
                  "startDate": "2023-01-01",
                  "status": "ACTIVE",
                  "description": "Created from integration test",
                  "countryCodes": ["UKR", "RUS"]
                }
                """;

        mockMvc.perform(post("/api/v1/conflicts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Conflict"))
                .andExpect(jsonPath("$.countryCodes").isArray());
    }

    private record FactionBody(String name, Long conflictId, String[] supportedCountryCodes) {
    }
}
