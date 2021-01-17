package behring.android.ddd.checks

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestLintTask

@Suppress("UnstableApiUsage")
abstract class BaseLintDetectorTest : LintDetectorTest() {
    override fun lint(): TestLintTask {
        val task = TestLintTask.lint()
        task.allowMissingSdk(true)
            .detector(detector)
            .issues(*issues.toTypedArray())
        return task
    }
}