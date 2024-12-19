package com.christmas.letter.processor.model.pagination;

import com.christmas.letter.processor.model.ChristmasLetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginatedResponse {

    private List<ChristmasLetter> letters;

    private String lastReturnedEmail;
}
