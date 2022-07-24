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

package dev.mbo.accountz.shared.validation

import dev.mbo.accountz.shared.violation.ConstraintViolation
import org.assertj.core.api.Assertions.assertThat
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ValidationTestUtil {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(ValidationTestUtil::class.java)
        fun validateSetOfViolationsContains(violations: Set<ConstraintViolation>, expectedCodes: Set<String>) {
            log.debug("violations: {}", violations)
            assertThat(violations).hasSameSizeAs(expectedCodes)
            expectedCodes.forEach { code ->
                val filtered = violations.filter { violation -> violation.code == code }.toSet()
                assertThat(filtered).withFailMessage("expected violations to contain '$code': $violations").hasSize(1)
            }

        }
    }
}
