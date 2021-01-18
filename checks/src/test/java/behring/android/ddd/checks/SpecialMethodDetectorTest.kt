package behring.android.ddd.checks

import com.android.testutils.TestUtils
import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestLintTask
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Assert.*

@Suppress("UnstableApiUsage")
class SpecialMethodDetectorTest : LintDetectorTest() {
    fun testRuntimeExecMethodDetector() {
        lint().files(
            java(
                """
                    package $PACKAGE_NAME.android;
                    public class ViewModel {
                        public void callExec() {
                            Runtime.getRuntime().exec("ls -l");
                        }
                    }
                    """
            ).indented()
        )
            .run()
            .expect(
                """
                    src/behring/android/ddd/android/ViewModel.java:4: Warning: Special method is called, please check its safety: Review [SpecialMethodReview]
                            Runtime.getRuntime().exec("ls -l");
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    0 errors, 1 warnings
                    """
            )
    }


    fun testProcessBuilderStartMethodDetector() {
        lint().files(
            java(
                """
                    package $PACKAGE_NAME.android;
                    public class ViewModel {
                        public void callExec() {
                           ProcessBuilder pb = new ProcessBuilder("ls -l");
                           pb.start();
                        }
                    }
                    """
            ).indented()
        )
            .run()
            .expect(
                """
                    src/behring/android/ddd/android/ViewModel.java:5: Warning: Special method is called, please check its safety: Review [SpecialMethodReview]
                           pb.start();
                           ~~~~~~~~~~
                    0 errors, 1 warnings
                    """
            )
    }

    fun testUriGetPathSegmentsMethodDetector() {
        lint().files(
            java(
                """
                    package $PACKAGE_NAME.android;
                    import android.net.Uri;
                    public class ViewModel {
                        public void callExec() {
                           Uri uri = Uri.parse("content://com.behring.ddd/email/..%2F..%2F..%2FBehring.txt/2/test");
                           uri.getPathSegments();
                        }
                    }
                    """
            ).indented()
        )
            .run()
            .expect(
                """
                    src/behring/android/ddd/android/ViewModel.java:6: Warning: Special method is called, please check its safety: Review [SpecialMethodReview]
                           uri.getPathSegments();
                           ~~~~~~~~~~~~~~~~~~~~~
                    0 errors, 1 warnings
                    """
            )
    }

    override fun getDetector(): Detector {
        return SpecialMethodDetector()
    }

    override fun getIssues(): List<Issue> {
        return listOf(SpecialMethodDetector.ISSUE)
    }
}