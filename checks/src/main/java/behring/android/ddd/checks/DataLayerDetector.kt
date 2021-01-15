package behring.android.ddd.checks

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UImportStatement

@Suppress("UnstableApiUsage")
class DataLayerDetector : Detector(), Detector.UastScanner {
    override fun getApplicableUastTypes(): List<Class<out UElement?>>? {
        return listOf(UImportStatement::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitImportStatement(node: UImportStatement) {
                val currentClassName:String = context.uastFile?.packageName ?: return

                if (!currentClassName.contains(PACKAGE_NAME_DATA_LAYER)) return

                val importStr = node.importReference.toString()

                if (importStr.contains(PACKAGE_NAME_ANDROID_LAYER)) {
                    context.report(
                        ISSUE, node, context.getLocation(node),
                        "This code reference android layer: **Modification**"
                    )
                }
            }
        }
    }

    companion object {
        @JvmField
        val ISSUE: Issue = Issue.create(
            id = "DDD-DomainLayer",
            briefDescription = "Class Dependencies Checks in Data Layer.",
            explanation = """
                        Check whether the data layer has classes that reference android layer.
                    """,
            category = Category.CORRECTNESS,
            priority = 10,
            severity = Severity.ERROR,
            implementation = Implementation(
                DataLayerDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
