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

import groovy.transform.ToString

import java.nio.charset.StandardCharsets

@ToString(ignoreNulls = true)
class ImpsortPluginExtension {
    String sourceEncoding = StandardCharsets.UTF_8.name()
    String staticGroups = '*'
    String groups = '*'
    boolean staticAfter
    File sourceDirectory
    File testSourceDirectory
    File[] directories
    String[] includes
    String[] excludes
    boolean removeUnused
    boolean treatSamePackageAsUnused
    boolean breadthFirstComparator
    LineEnding lineEnding = LineEnding.AUTO
}
