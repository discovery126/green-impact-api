package com.github.discovery126.greenimpact.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks_users_status")
public class TaskUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    private Task task;

    @Column(name="taken_at")
    @Builder.Default
    private LocalDateTime takenAt = LocalDateTime.now(ZoneId.of("Europe/Moscow"));

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskCompletionStatus status;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @ManyToOne
    @JoinColumn(name = "verified_by")
    private User admin;

    @OneToMany(mappedBy = "taskUser", cascade = CascadeType.ALL)
    private List<TaskProof> taskProofs;
}
