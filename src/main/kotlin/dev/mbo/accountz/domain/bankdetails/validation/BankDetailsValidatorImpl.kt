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

package dev.mbo.accountz.domain.bankdetails.validation

import dev.mbo.accountz.domain.bankdetails.BankDetails
import dev.mbo.accountz.domain.bic.validation.BICValidator
import dev.mbo.accountz.domain.iban.validation.IBANValidator
import dev.mbo.accountz.shared.validation.AbstractValidator
import dev.mbo.accountz.shared.violation.ConstraintViolation
import java.util.UUID

class BankDetailsValidatorImpl(
    private val ibanValidator: IBANValidator,
    private val bicValidator: BICValidator,
) : AbstractValidator(), BankDetailsValidator {

    override fun validateBankDetails(
        bankDetails: BankDetails,
        violations: MutableSet<ConstraintViolation>,
        throwOnFailure: Boolean,
        valId: UUID,
        cache: MutableMap<String, Any>,
    ): Set<ConstraintViolation> {

        ibanValidator.validateIban(bankDetails.iban, violations, false, valId, cache)
        bicValidator.validateBic(bankDetails.bic, violations, false, valId, cache)
        validate(
            bankDetails,
            BankDetails.VALIDATIONS,
            BankDetails.COMMON_ERROR_CODE,
            violations,
            valId,
            cache
        )

        throwOnFailure(violations, throwOnFailure)
        return violations
    }
}
