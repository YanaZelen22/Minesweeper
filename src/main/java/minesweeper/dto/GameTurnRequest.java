package minesweeper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record GameTurnRequest(
        @JsonProperty("game_id")
        UUID gameId,
        int col,
        int row
) {}
