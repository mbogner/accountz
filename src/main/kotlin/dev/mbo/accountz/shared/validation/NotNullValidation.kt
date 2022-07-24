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

class NotNullValidation : Validation {
    override fun execute(
        validatable: Validatable<*>?,
        commonErrorCode: String,
        cache: MutableMap<String, Any>
    ): Set<ConstraintViolation> {
        if (validatable?.getData() == null) {
            return setOf(
                ConstraintViolation(
                    commonErrorCode,
                    null,
                    "$commonErrorCode.null",
                    "value $commonErrorCode must not be null"
                )
            )
        }
        return emptySet()
    }
}
