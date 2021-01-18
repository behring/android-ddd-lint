package behring.android.ddd.checks

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import org.jetbrains.uast.*
import org.jetbrains.uast.util.isConstructorCall

@Suppress("UnstableApiUsage")
class SpecialMethodDetector : Detector(), Detector.UastScanner {
    private val SPECIAL_METHODS = mapOf(
        "exec" to "java.lang.Runtime",
        "start" to "java.lang.ProcessBuilder",
        "getPathSegments" to "android.net.Uri"
    )

    override fun getApplicableMethodNames(): List<String>? {
        return SPECIAL_METHODS.keys.toList()
    }

    override fun getApplicableUastTypes(): List<Class<out UElement?>>? {
        return listOf(UCallExpression::class.java)
    }


    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) {
                val methodName = node.methodName
                val className = node.receiverType?.canonicalText

                if (SPECIAL_METHODS.keys.contains(methodName) && className.equals(SPECIAL_METHODS[methodName])) {
                    context.report(
                        ISSUE, node, context.getLocation(node),
                        "Special method is called, please check its safety: **Review**"
                    )
                }
            }
        }
    }

    companion object {
        @JvmField
        val ISSUE: Issue = Issue.create(
            id = "SpecialMethodReview",
            briefDescription = "Special methods may have safety risks.",
            explanation = """
                        Check whether there are safety risks in special methods.
                    """,
            category = Category.SECURITY,
            priority = 10,
            severity = Severity.WARNING,
            implementation = Implementation(
                SpecialMethodDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
