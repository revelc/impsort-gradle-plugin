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


import org.gradle.testkit.runner.TaskOutcome

class ImpsortPluginIT extends ImpsortSpecification {

    def "can successfully load plugin"() {
        given:
            newFile('build.gradle', """
                plugins {
                    id 'net.revelc.code.impsort'
                }
            """)
        when:
            runner()
                .build()
        then:
            noExceptionThrown()
    }

    def "can successfully execute task"() {
        given:
            newFile('build.gradle', """
                plugins {
                    id 'net.revelc.code.impsort'
                }
                impsort {
                    groups = 'java.,javax.'
                }
            """)
        when:
            def result = runner()
                .withArguments(ImpsortPlugin.TASK_ID)
                .build()
        then:
            result.output.contains('java.,javax.')
            result.task(":$ImpsortPlugin.TASK_ID").outcome == TaskOutcome.SUCCESS
    }
}
