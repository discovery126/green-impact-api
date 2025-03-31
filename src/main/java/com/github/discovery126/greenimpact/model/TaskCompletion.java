package com.github.discovery126.greenimpact.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks_completion")
public class TaskCompletion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private TaskCompletionStatus status;

    @Column(name = "completed_at")
    @Builder.Default
    private LocalDateTime completedAt = LocalDateTime.now();

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @ManyToOne
    @JoinColumn(name = "taken_task_id")
    private TakenTask takenTask;

    @ManyToOne
    @JoinColumn(name = "verified_by")
    private User user;

    @OneToMany(mappedBy = "taskCompletion", cascade = CascadeType.ALL)
    private List<TaskProof> taskProof;

}
