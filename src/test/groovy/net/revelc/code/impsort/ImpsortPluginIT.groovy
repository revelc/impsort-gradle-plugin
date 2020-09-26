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
import spock.lang.Unroll

class ImpsortPluginIT extends ImpsortSpecification {

    @Unroll
    def "can successfully load plugin with gradle v#gradleVersion"() {
        given:
            buildScript << """
                plugins {
                    id 'net.revelc.code.impsort'
                }
            """.stripIndent()
        when:
            runner(gradleVersion)
                .build()
        then:
            noExceptionThrown()
        where:
            gradleVersion << [ '6.6', '6.5' ]
    }

    def "can successfully execute task "() {
        given:
            file('src/main/java/test', 'Test.java') << """
                package test;
                
                class Test {
                }
            """.stripIndent()

            buildScript << """
                plugins {
                    id 'net.revelc.code.impsort'
                }
                impsort {
                    groups = 'java.,javax.'
                }
            """.stripIndent()
        when:
            def result = runner()
                .withArguments(ImpsortPlugin.TASK_ID)
                .build()
        then:
            result.output.contains('Test.java')
            result.task(":$ImpsortPlugin.TASK_ID").outcome == TaskOutcome.SUCCESS
    }
}
