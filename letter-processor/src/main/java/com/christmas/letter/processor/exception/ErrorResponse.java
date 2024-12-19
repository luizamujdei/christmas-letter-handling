package com.christmas.letter.processor.exception;

import java.util.List;

public record ErrorResponse(String message, List<String> details) {
}
