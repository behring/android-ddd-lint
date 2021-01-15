package behring.android.ddd.checks

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import kotlin.Suppress

@Suppress("UnstableApiUsage")
class DomainLayerDetectorTest : LintDetectorTest() {

    fun testDomainLayerDependencies() {
        lint().files(
            java(
                """
                    package behring.android.ddd.data;
                    public class RemoteDataSource {
                    }
                    """
            ).indented(),
            java(
                """
                    package behring.android.ddd.domain;
                    import behring.android.ddd.data.RemoteDataSource;
                    public class User {
                        private RemoteDataSource remoteDataSource;
                    }
                    """
            ).indented()
        )
            .run()
            .expect(
                """
                    src/behring/android/ddd/domain/User.java:2: Error: This code reference data layer or android layer: Modification [DDD-DomainLayer]
                    import behring.android.ddd.data.RemoteDataSource;
                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    1 errors, 0 warnings
                    """
            )
    }

    override fun getDetector(): Detector {
        return DomainLayerDetector()
    }

    override fun getIssues(): List<Issue> {
        return listOf(DomainLayerDetector.ISSUE)
    }
}
