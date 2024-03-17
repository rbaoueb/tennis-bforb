package com.bforb.tennis.model;

import lombok.Getter;

@Getter
public enum ScoreEnum {
    WIN("WIN", null),
    FORTY("40", null),
    THIRTY("30", FORTY),
    FIFTEEN("15",THIRTY),
    ZERO("0",FIFTEEN);

    private String scoreName;
    private ScoreEnum nextScore;

    ScoreEnum(String scoreName, ScoreEnum nextScore) {
        this.scoreName = scoreName;
        this.nextScore = nextScore;
    }

    @Override
    public String toString() {
        return scoreName;
    }
}
