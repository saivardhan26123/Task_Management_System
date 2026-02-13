package com.TaskmanagementSystem.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One Board has many Columns
    @OneToMany(
        mappedBy = "board",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<BoardColumn> columns = new ArrayList<>();

    // =====================
    // Constructors
    // =====================
    public Board() {
    }

    public Board(Long id) {
        this.id = id;
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

    public List<BoardColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<BoardColumn> columns) {
        this.columns = columns;
    }

    // =====================
    // Helper methods
    // =====================
    public void addColumn(BoardColumn column) {
        columns.add(column);
        column.setBoard(this);
    }

    public void removeColumn(BoardColumn column) {
        columns.remove(column);
        column.setBoard(null);
    }
}
