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

package dev.mbo.accountz.shared

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class KotlinTest {

    private fun defaultArgSample(
        arg: UUID = UUID.randomUUID()
    ): UUID {
        return arg
    }

    @Test
    fun test() {
        assertThat(defaultArgSample()).isNotEqualTo(defaultArgSample())

    }
}
