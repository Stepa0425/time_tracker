package org.krainet.time.tracker.core.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "time_entries")
@NoArgsConstructor
@AllArgsConstructor
public class TimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "duration")
    private Integer duration;

    @OneToOne()
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne()
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @JsonProperty("start_time")
    @Column(name = "start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    @Column(name = "end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;
}
