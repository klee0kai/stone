package com.github.klee0kai.stone.test_feature.planning.project

import com.github.klee0kai.stone.test_feature.planning.model.ProjectInfo
import com.github.klee0kai.stone.test_feature.planning.model.TaskInfo

interface Project {

    val id: String?

    fun projectInfo(): ProjectInfo?

    fun addTask(taskInfo: TaskInfo?)

    fun findTask(taskId: String?): TaskInfo?
}
