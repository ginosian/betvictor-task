package com.task.betvictortask.it.controller;

import com.task.betvictortask.repository.ProcessedWordsEntity;
import com.task.betvictortask.repository.ProcessedWordsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class TextHistoryControllerIntegrationTest {

    public static final String PATH = "/betvictor/history";

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProcessedWordsRepository processedWordsRepository;

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        processedWordsRepository.deleteAll();
    }

    @Test
    void whenDataExists_shouldReturnLast10() throws Exception {
        IntStream.rangeClosed(1, 15).forEach(i -> {
            ProcessedWordsEntity entity = new ProcessedWordsEntity();
            entity.setFreqWord("Word" + i);
            entity.setAvgParagraphSize(100 + i);
            entity.setAvgParagraphProcessingTime(1.5 + i);
            entity.setTotalProcessingTime(10.0 + i);
            entity.setCreated(LocalDateTime.now().minusMinutes(i));
            processedWordsRepository.save(entity);
        });

        mockMvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].freq_word").value("Word15"))
                .andExpect(jsonPath("$[0].avg_paragraph_size").value(115))
                .andExpect(jsonPath("$[0].avg_paragraph_processing_time").value(16.5))
                .andExpect(jsonPath("$[0].total_processing_time").value(25.0))
                .andExpect(jsonPath("$[9].freq_word").value("Word6"))
                .andExpect(jsonPath("$[9].avg_paragraph_size").value(106))
                .andExpect(jsonPath("$[9].avg_paragraph_processing_time").value(7.5))
                .andExpect(jsonPath("$[9].total_processing_time").value(16.0));
    }

    @Test
    void whenNoData_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
