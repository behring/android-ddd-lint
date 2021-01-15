package behring.android.ddd.checks

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import kotlin.Suppress

@Suppress("UnstableApiUsage")
class AndroidLayerDetectorTest : LintDetectorTest() {

    fun testAndroidLayerDependencies() {
        lint().files(
            java(
                """
                    package behring.android.ddd.android.viewmodel;
                    import behring.android.ddd.data.RemoteDataSource;
                    public class UserViewModel {
                        private RemoteDataSource remoteDataSource;
                    }
                    """
            ).indented(),
            java(
                """
                    package behring.android.ddd.data;
                    public class RemoteDataSource {
                    }
                    """
            ).indented()
        )
            .run()
            .expect(
                """
                        src/behring/android/ddd/android/viewmodel/UserViewModel.java:2: Error: This code reference data layer: Modification [DDD-DomainLayer]
                        import behring.android.ddd.data.RemoteDataSource;
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        1 errors, 0 warnings
                    """
            )
    }

    override fun getDetector(): Detector {
        return AndroidLayerDetector()
    }

    override fun getIssues(): List<Issue> {
        return listOf(AndroidLayerDetector.ISSUE)
    }
}
