package com.github.klee0kai.stone.test_feature.planning.project

import com.github.klee0kai.stone.test_feature.planning.model.ProjectInfo
import com.github.klee0kai.stone.test_feature.planning.model.TaskInfo
import java.util.*

open class ProjectImpl : Project {
    var uuid: UUID = UUID.randomUUID()

    override val id: String?
        get() = uuid.toString()

    override fun projectInfo(): ProjectInfo? {
        return null
    }

    override fun addTask(taskInfo: TaskInfo?) {
    }

    override fun findTask(taskId: String?): TaskInfo? {
        return null
    }
}
