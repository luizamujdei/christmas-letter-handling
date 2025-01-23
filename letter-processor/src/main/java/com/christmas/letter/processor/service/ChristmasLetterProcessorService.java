package com.christmas.letter.processor.service;

import com.christmas.letter.processor.exception.NotFoundException;
import com.christmas.letter.processor.model.CachedPage;
import com.christmas.letter.processor.model.ChristmasLetter;
import com.christmas.letter.processor.repository.ChristmasLetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChristmasLetterProcessorService {

    private final ChristmasLetterRepository christmasLetterRepository;

    @Cacheable(value = "60m-letters", key = "#email", cacheResolver = "configurableRedisCacheResolver")
    public ChristmasLetter getLetterByEmail(String email){
        return christmasLetterRepository.findById(email)
                .orElseThrow(() -> new NotFoundException(String.format("No letter found for email: %s", email)));
    }

    @Cacheable(value = "60m-letters", keyGenerator = "pageableKeyGenerator", cacheResolver = "configurableRedisCacheResolver")
    public CachedPage<ChristmasLetter> getLetters(Pageable pageable){
        return new CachedPage<>(christmasLetterRepository.findAll(pageable), pageable);
    }

}
