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

package dev.mbo.accountz.domain.iban.validation

import dev.mbo.accountz.domain.iban.IBAN
import dev.mbo.accountz.shared.violation.ConstraintViolation
import java.util.UUID

interface IBANValidator {
    fun validateIban(
        iban: IBAN,
        violations: MutableSet<ConstraintViolation> = mutableSetOf(),
        throwOnFailure: Boolean = false,
        valId: UUID = UUID.randomUUID(),
        cache: MutableMap<String, Any> = mutableMapOf(),
    ): Set<ConstraintViolation>
}
