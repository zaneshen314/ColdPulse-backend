package com.oocl.ita.web.domain.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
public class TicketRelease {
    @Id
    private Integer concertScheduleId;
    private String startTime;
    private String endTime;
    private Integer frequency;
}
