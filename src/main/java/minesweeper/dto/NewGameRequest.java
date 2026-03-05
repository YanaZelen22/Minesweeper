package minesweeper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record NewGameRequest(
        @Min(1)
        @Max(30)
        int width,

        @Min(1)
        @Max(30)
        int height,

        @JsonProperty("mines_count")
        @Min(1)
        int minesCount
) {}