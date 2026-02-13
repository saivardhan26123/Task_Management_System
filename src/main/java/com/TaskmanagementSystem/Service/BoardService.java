package com.TaskmanagementSystem.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TaskmanagementSystem.Entity.Board;
import com.TaskmanagementSystem.Entity.BoardCard;
import com.TaskmanagementSystem.Entity.BoardColumn;
import com.TaskmanagementSystem.Entity.Issue;
import com.TaskmanagementSystem.Repository.BoardCardRepository;
import com.TaskmanagementSystem.Repository.BoardColumnRepository;
import com.TaskmanagementSystem.Repository.BoardRepository;
import com.TaskmanagementSystem.Repository.IssueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	@Autowired
	private BoardRepository boardRepo;
	
	@Autowired
	private BoardColumnRepository boardColumnRepo;
	
	@Autowired
	private BoardCardRepository boardCardRepo;
	
	@Autowired
	private IssueRepository issueRepo;
	
	
	
	public Board createBoard(Board board) {
		return boardRepo.save(board);
	}
	
	public Optional<Board>findByBoardId(Long id){
		return boardRepo.findById(id);
	}
	public List<Board>getByColumns(Long boardId){
		return boardColumnRepo.findByboardIdOrderByPosition(boardId);
	}
	public List<BoardCard>getCardsForColumn(Long boardId,Long columnId){
		return boardCardRepo.findByboardIdAndColumnIdOrderByPosition(boardId, columnId);
	}
	
	@Transactional
	public BoardCard addIssueToBoard(Long boardId,Long columnId,Long issueId) {
		
		Issue issue = issueRepo.findById(issueId).orElseThrow(()-> new RuntimeException("Issue not found"));
		boardCardRepo.findByIssueId(issueId).ifPresent(boardCardRepo::delete);
		
		BoardColumn column= boardColumnRepo.findById(columnId).orElseThrow(()-> new RuntimeException("column not found"));
		if(column.getWiplimit() != null && column.getWiplimit()>0) {
			long count = boardCardRepo.countByBoardIdAndColumnId(boardId, columnId);
			if(count >= column.getWiplimit() ) {
				throw new RuntimeException("WIP limits reached for column:"+column.getName() );
			}
		}
		
		List<BoardCard>existing = boardCardRepo.findByboardIdAndColumnIdOrderByPosition(boardId, columnId);
		int post = existing.size();
		
		
		BoardCard card = new BoardCard();
		card.setBoardId(boardId);
		card.setColumn(column);
		card.setIssueId(issueId);
		card.setPosition(post);
		
		card = boardCardRepo.save(card);
		
		if(column.getStatusKey() != null) {
			issue.setStatus(Enum.valueOf(com.TaskmanagementSystem.Enum.IssueStatus.class,column.getStatusKey() ));
		}
		
		return card;
	}
	
	@Transactional
	public void moveCard(Long boardId,Long tocolumnId,Long cardId,int toPosition,String performBy) {
		
		BoardCard card = boardCardRepo.getById(cardId);
		if(card == null) {
			throw new RuntimeException("Card not found");
		}
		BoardColumn from = card.getColumn();
		BoardColumn to = boardColumnRepo.findById(tocolumnId).orElseThrow(()-> new RuntimeException("toStatus not found"));
		if(to == null) {
			throw new RuntimeException("Target not fount");
		}
		
		if(to.getWiplimit() != null && to.getWiplimit()>0) {
			long count = boardCardRepo.countByBoardIdAndColumnId(boardId, tocolumnId);
			
			if(!Objects.equals(from.getId(), to.getId()) && count>= to.getWiplimit()) {
				throw new RuntimeException("Wip limit Exceeded for column:" + to.getWiplimit());
			}
		}
		
		
		List<BoardCard>fromList= boardCardRepo.findByboardIdAndColumnIdOrderByPosition(boardId, from.getId());
		for(BoardCard bc:fromList)	{
			if(bc.getPosition()> card.getPosition()) {
				bc.setPosition(bc.getPosition()-1);
				boardCardRepo.save(bc);
			}
		}
		
		List<BoardCard>toList= boardCardRepo.findByboardIdAndColumnIdOrderByPosition(boardId, to.getId());
		for(BoardCard bc:toList ) {
			if(bc.getPosition() >= toPosition) {
				bc.setPosition(bc.getPosition()+1);
				boardCardRepo.save(bc);
				
			}
		}
		card.setColumn(to);
		card.setPosition(toPosition);
		boardCardRepo.save(card);
		
		
		Issue issue = issueRepo.findById(card.getIssueId()).orElseThrow(()-> new RuntimeException("Issue not found"));
		
		if(to.getStatusKey() !=null) {
			issue.setStatus(Enum.valueOf(com.TaskmanagementSystem.Enum.IssueStatus.class, to.getStatusKey()));
			
			issueRepo.save(issue);
		}
		
	}
	
	
	@Transactional
	public void recordColumn(Long boardId,Long columnId,List<Long>orderCardIds) {
		int post = 0;
		for(Long cid:orderCardIds) {
			BoardCard card = boardCardRepo.findById(cid).orElseThrow(()-> new RuntimeException("card nor found"));
			card.setPosition(post++);
			boardCardRepo.save(card);
		}
	}
	
	@Transactional
	public void startSprint(Long sprintId) {
		
	}
	
	@Transactional
	public void completeSprint(Long sprintId) {
		
	}
	
	
}