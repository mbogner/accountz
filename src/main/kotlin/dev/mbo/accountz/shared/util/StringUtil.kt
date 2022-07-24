/*
 * Copyright 2022 mbo.dev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.mbo.accountz.shared.util

class StringUtil private constructor() {
    companion object {
        fun prefixIntWith(i: Int, prefix: Char, len: Int): String {
            if (i < 0) {
                throw IllegalArgumentException("i has to be positive")
            }
            if (len < 1) {
                throw IllegalArgumentException("len has to be >0")
            }
            var result = "$i"
            while (result.length < len) {
                result = "$prefix$result"
            }
            return result
        }
    }
}
