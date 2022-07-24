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
import dev.mbo.accountz.shared.violation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

abstract class AbstractValidator {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun throwOnFailure(violations: Set<ConstraintViolation>, throwEnabled: Boolean) {
        if (throwEnabled && violations.isNotEmpty()) {
            throw ConstraintViolationException(violations)
        }
    }

    fun validate(
        validatable: Validatable<*>?,
        validations: Set<Validation>,
        commonErrorCode: String,
        violations: MutableSet<ConstraintViolation>,
        valId: UUID,
        cache: MutableMap<String, Any>,
    ) {
        log.debug("validate {} {} (valId={})", commonErrorCode, validatable, valId)
        for (validation in validations) {
            log.debug(
                "validate {} with validation {} (valId={}, cache={})",
                validatable,
                validation.javaClass.simpleName,
                valId,
                cache
            )
            violations.addAll(validation.execute(validatable, commonErrorCode, cache))
            log.debug(
                "validated {} with validation {} (valId={}, cache={})",
                validatable,
                validation.javaClass.simpleName,
                valId,
                cache
            )
            if (violations.isNotEmpty()) {
                log.debug("validations failed. stopping validation chain")
                break
            }
        }
        if (violations.isNotEmpty()) {
            log.debug("validation of {} failed: {} (valId={})", validatable, violations, valId)
        }
    }

}
