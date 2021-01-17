package behring.android.ddd.checks

import behring.android.ddd.checks.PACKAGE_NAME
import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import kotlin.Suppress

@Suppress("UnstableApiUsage")
class AndroidLayerDetectorTest : BaseLintDetectorTest() {

    fun testAndroidLayerDependencies() {
        lint().files(
            java(
                """
                    package $PACKAGE_NAME.android.viewmodel;
                    import $PACKAGE_NAME.data.RemoteDataSource;
                    public class UserViewModel {
                        private RemoteDataSource remoteDataSource;
                    }
                    """
            ).indented(),
            java(
                """
                    package $PACKAGE_NAME.data;
                    public class RemoteDataSource {
                    }
                    """
            ).indented()
        )
            .run()
            .expect(
                """
                        src/behring/android/ddd/android/viewmodel/UserViewModel.java:2: Error: This code reference data layer: Modification [DDD-DomainLayer]
                        import $PACKAGE_NAME.data.RemoteDataSource;
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
