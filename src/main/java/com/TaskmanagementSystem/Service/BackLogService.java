package com.TaskmanagementSystem.Service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TaskmanagementSystem.Entity.Issue;
import com.TaskmanagementSystem.Entity.Sprint;
import com.TaskmanagementSystem.Enum.IssueType;
import com.TaskmanagementSystem.Enum.SprintState;
import com.TaskmanagementSystem.Repository.IssueRepository;
import com.TaskmanagementSystem.Repository.SprintRepository;

import jakarta.transaction.Transactional;

@Service
public class BackLogService {

    @Autowired
    private IssueRepository issueRepo;

    @Autowired
    private SprintRepository sprintRepo;

    public List<Issue> getBackLog(Long projectId) {
        if (projectId == null) {
            return issueRepo.findByProjectIdAndSprintIdIsNullOrderByBackLogPosition(null);
        }
        return issueRepo.findByProjectIdAndSprintIdIsNullOrderByBackLogPosition(projectId);
    }

    @Transactional
    public void recordBackLog(Long projectId, List<Long> orderIssueId) {
        int pos = 0;
        for (Long issueId : orderIssueId) {
            Issue issue = issueRepo.findById(issueId)
                    .orElseThrow(() -> new RuntimeException("Issue not found"));
            issue.setBackLogPosition(pos++);
            issueRepo.save(issue);
        }
    }

    @Transactional
    public Issue addIssueToSprint(Long issueId, Long sprintId) {
        Issue issue = issueRepo.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));
        Sprint sprint = sprintRepo.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        SprintState sprintState = sprint.getState();
        if (sprintState != SprintState.PLANNED && sprintState != SprintState.ACTIVE) {
            throw new RuntimeException("Cannot add issue to sprint in state: " + sprintState);
        }

        issue.setSprintId(sprintId);
        issue.setBackLogPosition(null);
        return issueRepo.save(issue);
    }

    public Map<String, Object> getBackLogHierarchy(Long projectId) {

        List<Issue> backLog = getBackLog(projectId);

        // EpicId -> Epic Data
        Map<Long, Map<String, Object>> epicMap = new LinkedHashMap<>();

        /* =========================
           1. EPICS
           ========================= */
        for (Issue issue : backLog) {
            if (issue.getIssueType() == IssueType.EPICS) {

                Map<String, Object> data = new LinkedHashMap<>();
                data.put("epic", issue);
                data.put("stories", new ArrayList<Issue>());
                data.put("subtasks", new HashMap<Long, List<Issue>>());

                epicMap.put(issue.getId(), data);
            }
        }

        /* =========================
           2. STORIES
           ========================= */
        for (Issue issue : backLog) {
            if (issue.getIssueType() == IssueType.STORIES && issue.getEpicId() != null) {

                Map<String, Object> epicData = epicMap.get(issue.getEpicId());
                if (epicData != null) {
                    @SuppressWarnings("unchecked")
                    List<Issue> stories = (List<Issue>) epicData.get("stories");
                    stories.add(issue);
                }
            }
        }

        /* =========================
           3. SUBTASKS
           ========================= */
        for (Issue issue : backLog) {
            if (issue.getIssueType() == IssueType.SUBTASKS && issue.getSourceIssueId() != null) {

                Long sourceIssueId = issue.getSourceIssueId();

                for (Map<String, Object> epicData : epicMap.values()) {

                    @SuppressWarnings("unchecked")
                    List<Issue> stories = (List<Issue>) epicData.get("stories");

                    for (Issue story : stories) {
                        if (story.getId().equals(sourceIssueId)) {

                            @SuppressWarnings("unchecked")
                            Map<Long, List<Issue>> subTasks =
                                    (Map<Long, List<Issue>>) epicData.get("subtasks");

                            subTasks
                                .computeIfAbsent(sourceIssueId, k -> new ArrayList<>())
                                .add(issue);
                            break;
                        }
                    }
                }
            }
        }

        return Collections.singletonMap("epics", epicMap.values());
    }
}
