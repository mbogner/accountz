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

class LengthValidation constructor(
    private val exact: Int? = null,
    private val min: Int? = null,
    private val max: Int? = null,
) : Validation {

    override fun execute(
        validatable: Validatable<*>?, commonErrorCode: String, cache: MutableMap<String, Any>
    ): Set<ConstraintViolation> {
        val violations = mutableSetOf<ConstraintViolation>()
        val value = validatable?.getData()
        if (value !is String) return emptySet()
        val len = value.length

        if (null != exact && len != exact) {
            return setOf(
                ConstraintViolation(
                    commonErrorCode,
                    value,
                    "$commonErrorCode.exact_length",
                    "value $commonErrorCode must have length $exact",
                    mapOf("exactLength" to exact)
                )
            )
        }

        if (null != min && len < min) {
            violations.add(
                ConstraintViolation(
                    commonErrorCode,
                    value,
                    "$commonErrorCode.min_length",
                    "value $commonErrorCode must have minimum length of $min but only has $len",
                    mapOf("len" to len, "min" to min)
                )
            )
        }
        if (null != max && len > max) {
            violations.add(
                ConstraintViolation(
                    commonErrorCode,
                    value,
                    "$commonErrorCode.max_length",
                    "value $commonErrorCode must have maximum length of $min but has $len",
                    mapOf("len" to len, "max" to max)
                )
            )
        }

        return violations
    }
}
