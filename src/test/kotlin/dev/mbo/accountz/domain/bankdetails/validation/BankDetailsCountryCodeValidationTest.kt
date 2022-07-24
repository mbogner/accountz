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
import dev.mbo.accountz.domain.bic.validation.BICValidation
import dev.mbo.accountz.domain.iban.IBAN
import dev.mbo.accountz.domain.iban.validation.IBANCountryBasedLengthValidation
import dev.mbo.accountz.shared.validation.ValidationTestUtil
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class BankDetailsCountryCodeValidationTest {

    private val bean = BankDetailsCountryCodeValidation()

    @Test
    fun valid() {
        val cache = mutableMapOf<String, Any>(
            IBANCountryBasedLengthValidation.CACHE_COUNTRY_CODE to "DE",
            BICValidation.CACHE_COUNTRY_CODE to "DE",
        )
        Assertions.assertThat(
            bean.execute(
                BankDetails(IBAN(null), BIC(null)), BankDetails.COMMON_ERROR_CODE, cache
            )
        ).isEmpty()
    }

    @Test
    fun mismatch() {
        val cache = mutableMapOf<String, Any>(
            IBANCountryBasedLengthValidation.CACHE_COUNTRY_CODE to "DE",
            BICValidation.CACHE_COUNTRY_CODE to "EN",
        )
        ValidationTestUtil.validateSetOfViolationsContains(
            bean.execute(
                BankDetails(IBAN(null), BIC(null)), BankDetails.COMMON_ERROR_CODE, cache
            ),
            setOf("${BankDetails.COMMON_ERROR_CODE}.iban_cc_not_matching_bic_cc")
        )
    }
}
