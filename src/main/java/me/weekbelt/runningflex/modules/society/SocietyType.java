package me.weekbelt.runningflex.modules.society;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SocietyType {
    SPORT("운동/스포츠"),
    STUDY("스터디"),
    SHOW("공연"),
    TRAVEL("여행"),
    MUSIC("음악/악기"),
    FREE("자유주제"),
    DANCE("댄스/무용");

    private final String value;
}
