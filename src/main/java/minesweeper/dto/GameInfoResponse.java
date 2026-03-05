package minesweeper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

public record GameInfoResponse (

    @JsonProperty("game_id")
    UUID gameId, // в Сваггере айдишник в GameInfoResponse лоу кейс, а в GameTurnRequest аппер кейс

    int width,
    int height,

    @JsonProperty("mines_count")
    int minesCount,
    boolean completed,
    String[][] field // заметка - хотелось бы единообразия, или кемелКейс или _
) {}
