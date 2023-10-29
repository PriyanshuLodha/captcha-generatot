package com.example.captchabackend2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "user_notes")
public class UserNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Integer id;
    private String data;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;
}
