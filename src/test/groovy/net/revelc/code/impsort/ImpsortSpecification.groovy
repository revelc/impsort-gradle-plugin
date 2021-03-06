/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.revelc.code.impsort

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class ImpsortSpecification extends Specification {
    static final String BUILD_SCRIPT_NAME = 'build.gradle'

    Path testProjectDir
    Path buildScript

    def setup() {
        testProjectDir = Files.createTempDirectory('impsort-')
        buildScript = getOrCreateFile(testProjectDir, BUILD_SCRIPT_NAME)
    }

    def cleanup() {
        testProjectDir.deleteDir()
    }

    def runner(String gradleVersion = null) {
        def runner = GradleRunner.create()
            .forwardOutput()
            .withProjectDir(testProjectDir.toFile())
            .withPluginClasspath()

        if (gradleVersion) {
            runner.withGradleVersion(gradleVersion)
        }

        return runner
    }

    def file(String filePath, String fileName) {
        def path = testProjectDir.resolve(filePath)
        def root = Files.createDirectories(path)

        return getOrCreateFile(root, fileName)
    }

    static def getOrCreateFile(Path root, String name) {
        def file = root.resolve(name)
        if (!Files.exists(file)) {
            Files.createFile(file)
        }

        return file
    }
}
