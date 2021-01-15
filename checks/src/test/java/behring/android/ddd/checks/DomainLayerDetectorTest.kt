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
                    package $PACKAGE_NAME.data;
                    public class RemoteDataSource {
                    }
                    """
            ).indented(),
            java(
                """
                    package $PACKAGE_NAME.domain;
                    import $PACKAGE_NAME.data.RemoteDataSource;
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
                    import $PACKAGE_NAME.data.RemoteDataSource;
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
