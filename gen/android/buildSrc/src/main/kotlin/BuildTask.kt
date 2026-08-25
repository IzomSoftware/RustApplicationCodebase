import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class BuildTask : DefaultTask() {
    @Input
    var rootDirRel: String? = null
    @Input
    var target: String? = null
    @Input
    var release: Boolean? = null

    @TaskAction
    fun build() {
        val rootDirRel = rootDirRel ?: throw GradleException("rootDirRel cannot be null")
        val target = target ?: throw GradleException("target cannot be null")
        val release = release ?: throw GradleException("release cannot be null")

        val abiName = when (target) {
            "aarch64" -> "arm64-v8a"
            "armv7"   -> "armeabi-v7a"
            "i686"    -> "x86"
            "x86_64"  -> "x86_64"
            else      -> throw GradleException("Unknown target: $target")
        }
        val rustTarget =
            when (target) {
                "aarch64" -> "aarch64-linux-android"
                "armv7" -> "armv7-linux-androideabi"
                "i686" -> "i686-linux-android"
                "x86_64" -> "x86_64-linux-android"
                else -> throw GradleException("$target")
            }

        project.exec {
            workingDir = File("../../../")
            executable("cargo")
            args("ndk")
            args("--target")
            args(rustTarget)
            args("-o")
            args("gen/android/app/src/main/jniLibs/$abiName")
            args("build")
        }.assertNormalExitValue()
    }
}