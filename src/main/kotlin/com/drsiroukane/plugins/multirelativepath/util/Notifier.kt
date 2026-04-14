package com.drsiroukane.plugins.multirelativepath.util

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object Notifier {

    fun info(project: Project?, title: String) {
        if (project == null) return

        NotificationGroupManager.getInstance()
            .getNotificationGroup("MultiRelativePath")
            .createNotification(title, NotificationType.INFORMATION)
            .notify(project)
    }

    fun error(project: Project?, title: String) {
        if (project == null) return

        NotificationGroupManager.getInstance()
            .getNotificationGroup("MultiRelativePath")
            .createNotification(title, NotificationType.ERROR)
            .notify(project)
    }
}