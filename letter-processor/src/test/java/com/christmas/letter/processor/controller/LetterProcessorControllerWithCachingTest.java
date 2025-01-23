package com.christmas.letter.processor.controller;

import com.christmas.letter.processor.model.ChristmasLetter;
import com.christmas.letter.processor.repository.ChristmasLetterRepository;
import com.christmas.letter.processor.service.ChristmasLetterProcessorService;
import com.christmas.letter.processor.utils.LetterTestHelper;
import com.christmas.letter.processor.utils.RedisTestContainer;
import io.awspring.cloud.autoconfigure.dynamodb.DynamoDbAutoConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {DynamoDbAutoConfiguration.class})
@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:config-test.properties")
public class LetterProcessorControllerWithCachingTest extends RedisTestContainer {

    @MockBean
    private ChristmasLetterRepository christmasLetterRepository;

    @InjectMocks
    private ChristmasLetterProcessorService christmasLetterProcessorService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    private static final String PATH = "/letters";

    @Test
    void whenGivenPage_thenReturnCachedPageLetters() throws Exception {
        Set<String> keys = redisTemplate.keys("60m-letters:*");
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        ChristmasLetter christmasLetter = LetterTestHelper.createLetter();
        Pageable pageRequest =  PageRequest.of(0, 10, Sort.by("email"));
        List<ChristmasLetter> content = new ArrayList<>();
        content.add(christmasLetter);
        Page<ChristmasLetter> result = new PageImpl<>(content, pageRequest, 1);
        when(christmasLetterRepository.findAll(any(Pageable.class))).thenReturn(result);

        assertThat(Objects.requireNonNull(redisTemplate.keys("60m-letters:*"))).isEmpty();

        mockMvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));


        verify(christmasLetterRepository, times(1)).findAll(any(Pageable.class));

        mockMvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(christmasLetterRepository, times(1)).findAll(any(Pageable.class));
        assertThat(Objects.requireNonNull(redisTemplate.keys("60m-letters:*")).size()).isOne();
    }

    @AfterEach
    void cleanUp(){
        Set<String> keys = redisTemplate.keys("60m-letters:*");
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

}