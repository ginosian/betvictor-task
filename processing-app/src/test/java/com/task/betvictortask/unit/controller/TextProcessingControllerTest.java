package com.task.betvictortask.unit.controller;

import com.task.betvictortask.constants.ParagraphSize;
import com.task.betvictortask.controller.TextProcessingController;
import com.task.betvictortask.controller.TextProcessingFacade;
import com.task.betvictortask.controller.dto.TextProcessingStats;
import com.task.betvictortask.controller.error.TextProcessingControllerExceptionHandler;
import com.task.betvictortask.unit.TestConfig;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.task.betvictortask.constants.ParagraphSize.MEDIUM;
import static com.task.betvictortask.constants.ParagraphSize.SHORT;
import static com.task.betvictortask.controller.error.ErrorCodes.PV1;
import static com.task.betvictortask.controller.error.ErrorCodes.PV2;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TextProcessingController.class)
@ContextConfiguration(classes = {
        TextProcessingController.class,
        TextProcessingControllerExceptionHandler.class,
        TestConfig.class})
class TextProcessingControllerTest {

    public static final String PATH = "/betvictor/text";
    public static final String PARAM_P = "p";
    public static final String PARAM_L = "l";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TextProcessingFacade textProcessingFacade;

    @Test
    void whenValidParameters_thenReturnsHttpStatus200() throws Exception {
        TextProcessingStats mockStats = new TextProcessingStats(
                "example", 100, 0.008, 2.0);

        when(textProcessingFacade.process(2, MEDIUM)).thenReturn(mockStats);

        mockMvc.perform(get(PATH)
                        .param(PARAM_P, "2")
                        .param(PARAM_L, MEDIUM.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.freq_word").isNotEmpty())
                .andExpect(jsonPath("$.avg_paragraph_size").value(Matchers.greaterThan(0)))
                .andExpect(jsonPath("$.total_processing_time").value(Matchers.greaterThan(0.0)));
    }

    @Test
    void whenInvalidP_thenReturnsHttpStatus400() throws Exception {
        mockMvc.perform(get(PATH)
                        .param(PARAM_P, "11")
                        .param(PARAM_L, SHORT.name()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].code").value(PV2.name()))
                .andExpect(jsonPath("$.errors[0].message").value(PV2.getMessage()));
    }

    @Test
    void whenInvalidL_thenReturnsHttpStatus400() throws Exception {
        mockMvc.perform(get(PATH)
                        .param(PARAM_P, "2")
                        .param(PARAM_L, "INVALID_SIZE"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].code").value(PV1.name()))
                .andExpect(jsonPath("$.errors[0].message").value(
                        String.format(PV1.getMessage(),
                                "INVALID_SIZE",
                                "l",
                                Arrays.asList(ParagraphSize.values()))));
    }
}