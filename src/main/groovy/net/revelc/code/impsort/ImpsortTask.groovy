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

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException
import org.gradle.internal.time.TimeFormatting

import java.nio.file.Path
import java.time.Duration
import java.util.concurrent.atomic.AtomicLong

@Slf4j
class ImpsortTask extends DefaultTask {
    ImpsortPluginExtension extension

    ImpsortTask() {
        extension = project.extensions.findByName(ImpsortPlugin.EXTENSION_ID) as ImpsortPluginExtension
    }

    @TaskAction
    void run() {
        def impsort = new ImpSort(
            extension.sourceEncoding,
            new Grouper(
                    extension.groups,
                    extension.staticGroups,
                    extension.staticAfter,
                    extension.joinStaticWithNonStatic,
                    extension.breadthFirstComparator),
            extension.removeUnused,
            extension.treatSamePackageAsUnused,
            extension.lineEnding)

        def alreadySorted = new AtomicLong(0)
        def processed = new AtomicLong(0)
        def failures = [] as List<Throwable>
        def startTime = System.nanoTime()

        inputFiles.each { path ->
            try {
                Result result = impsort.parseFile(path)
                result.getImports().each {
                    println "Found import: $it"
                }

                if (result.isSorted()) {
                    alreadySorted.getAndIncrement();
                } else {
                    processed.getAndIncrement();
                }
                result.saveSorted(path)
            } catch (Exception e) {
                failures << e
            }
        }

        def totalTime = Duration.ofNanos(System.nanoTime() - startTime);
        def total = alreadySorted.get() + processed.get()

        printf('%22s: %d in %s\n', 'Total Files Processed', total, TimeFormatting.formatDurationTerse(totalTime.toMillis()))
        printf('%22s: %d\n', 'Already Sorted', alreadySorted.get())
        printf('%22s: %d\n', 'Needed Sorting', processed.get())
        printf('%22s: %s\n', 'Files', inputFiles)

        if (failures) {
            def ex = new Exception()
            ex.message = "Failure executing task: $ImpsortPlugin.TASK_ID"
            failures.each {
                ex.addSuppressed(it)
            }

            throw new TaskExecutionException(this, ex)
        }
    }

    @InputFiles
    List<Path> getInputFiles() {
        def dirs = extension.directories ?: [ project.rootDir ]

        return dirs.collect {
            project.fileTree(
                    dir: it,
                    exclude: extension.excludes,
                    include: extension.includes).files
        }.flatten().collect {
            it.toPath()
        } as Collection<Path>
    }
}
