package com.github.klee0kai.stone.test_feature.planning.project;

import com.github.klee0kai.stone.test_feature.planning.model.ProjectInfo;
import com.github.klee0kai.stone.test_feature.planning.model.TaskInfo;

import java.util.UUID;

public class ProjectImpl implements Project {

    public UUID uuid = UUID.randomUUID();

    @Override
    public String getId() {
        return uuid.toString();
    }

    @Override
    public ProjectInfo projectInfo() {
        return null;
    }

    @Override
    public void addTask(TaskInfo taskInfo) {

    }

    @Override
    public TaskInfo findTask(String taskId) {
        return null;
    }
}
