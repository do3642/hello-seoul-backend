package com.helloseoul.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TouristDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 시퀀스

    private String contentid; // 콘텐츠 ID
    private String contenttypeid; // 콘텐츠 타입 ID
    private String opendate; // 오픈 날짜
    private String useseason; // 사용 시즌
    private String eventstartdate; // 이벤트 시작 날짜
    private String eventenddate; // 이벤트 종료 날짜
}
