package com.github.klee0kai.stone.test_feature.planning.project;

import com.github.klee0kai.stone.test_feature.planning.model.ProjectInfo;
import com.github.klee0kai.stone.test_feature.planning.model.TaskInfo;

public interface Project {

    ProjectInfo projectInfo();

    void addTask(TaskInfo taskInfo);

    TaskInfo findTask(String taskId);

}
