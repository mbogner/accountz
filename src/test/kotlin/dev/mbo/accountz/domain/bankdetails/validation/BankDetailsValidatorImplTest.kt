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
import dev.mbo.accountz.domain.bic.BIC
import dev.mbo.accountz.domain.bic.validation.BICValidationTest
import dev.mbo.accountz.domain.bic.validation.BICValidatorImpl
import dev.mbo.accountz.domain.iban.IBAN
import dev.mbo.accountz.domain.iban.validation.IBANChecksumValidationTest
import dev.mbo.accountz.domain.iban.validation.IBANValidatorImpl
import dev.mbo.accountz.shared.validation.ValidationTestUtil
import dev.mbo.accountz.shared.violation.ConstraintViolationException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class BankDetailsValidatorImplTest {

    private val validator: BankDetailsValidator = BankDetailsValidatorImpl(
        IBANValidatorImpl(),
        BICValidatorImpl(),
    )

    @Test
    fun valid() {
        Assertions.assertThat(
            validator.validateBankDetails(
                BankDetails(
                    IBAN(IBANChecksumValidationTest.VALID_IBAN),
                    BIC(BICValidationTest.VALID_BIC)
                ),
                mutableSetOf()
            )
        ).isEmpty()
    }

    @Test
    fun ibanBicNotMatchingCC() {
        ValidationTestUtil.validateSetOfViolationsContains(
            validator.validateBankDetails(
                BankDetails(IBAN(IBANChecksumValidationTest.VALID_IBAN), BIC("SMCOGB2L")),
                mutableSetOf()
            ),
            setOf("${BankDetails.COMMON_ERROR_CODE}.iban_cc_not_matching_bic_cc")
        )
    }

    @Test
    fun ibanBicNotMatchingCCAsException() {
        assertThrows(ConstraintViolationException::class.java) {
            validator.validateBankDetails(
                BankDetails(IBAN(IBANChecksumValidationTest.VALID_IBAN), BIC("SMCOGB2L")),
                mutableSetOf(),
                throwOnFailure = true
            )
        }
    }

}
