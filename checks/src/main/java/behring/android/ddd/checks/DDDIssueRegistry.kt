package behring.android.ddd.checks

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API

@Suppress("UnstableApiUsage")
class DDDIssueRegistry : IssueRegistry() {
    override val issues = listOf(
        DomainLayerDetector.ISSUE,
        DataLayerDetector.ISSUE,
        AndroidLayerDetector.ISSUE
    )

    override val api: Int
        get() = CURRENT_API
}