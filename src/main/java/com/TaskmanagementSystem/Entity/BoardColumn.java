package com.TaskmanagementSystem.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "board_columns")
public class BoardColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many columns belong to one board
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // Column display name (To Do, In Progress, Done)
    @Column(nullable = false)
    private String name;

    // REQUIRED by BoardService
    @Column(name = "wip_limit")
    private Integer wipLimit;

    //  REQUIRED by BoardService
    @Column(name = "status_key", nullable = false)
    private String statusKey;

    // =====================
    // Constructors
    // =====================
    public BoardColumn() {
    }

    public BoardColumn(String name, String statusKey, Integer wipLimit) {
        this.name = name;
        this.statusKey = statusKey;
        this.wipLimit = wipLimit;
    }

    // =====================
    // Getters & Setters
    // =====================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //  USED BY BoardService
    public Integer getWiplimit() {
        return wipLimit;
    }

    public void setWiplimit(Integer wipLimit) {
        this.wipLimit = wipLimit;
    }

    //  USED BY BoardService
    public String getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(String statusKey) {
        this.statusKey = statusKey;
    }
}
