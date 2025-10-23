package com.joseph.poll_monolithic_app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private String optionText;
}
