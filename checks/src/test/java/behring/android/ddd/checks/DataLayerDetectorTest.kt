package behring.android.ddd.checks

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import kotlin.Suppress

@Suppress("UnstableApiUsage")
class DataLayerDetectorTest : LintDetectorTest() {

    fun testDataLayerDependencies() {
        lint().files(
            java(
                """
                    package $PACKAGE_NAME.android.viewmodel;
                    public class UserViewModel {
                    }
                    """
            ).indented(),
            java(
                """
                    package $PACKAGE_NAME.data;
                    import $PACKAGE_NAME.android.viewmodel.UserViewModel;
                    public class RemoteDataSource {
                        private UserViewModel userViewModel;
                    }
                    """
            ).indented()
        )
            .run()
            .expect(
                """
                        src/behring/android/ddd/data/RemoteDataSource.java:2: Error: This code reference android layer: Modification [DDD-DomainLayer]
                        import $PACKAGE_NAME.android.viewmodel.UserViewModel;
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        1 errors, 0 warnings
                    """
            )
    }

    override fun getDetector(): Detector {
        return DataLayerDetector()
    }

    override fun getIssues(): List<Issue> {
        return listOf(DataLayerDetector.ISSUE)
    }
}
