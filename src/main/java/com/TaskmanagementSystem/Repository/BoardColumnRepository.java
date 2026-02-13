package com.TaskmanagementSystem.Repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TaskmanagementSystem.Entity.Board;
import com.TaskmanagementSystem.Entity.BoardColumn;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn,Long> {
	
	List<Board> findByboardIdOrderByPosition(Long boardId);

}