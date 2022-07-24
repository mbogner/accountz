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
import dev.mbo.accountz.shared.validation.ValidationTestUtil
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class IBANValidatorImplTest {

    private val validator: IBANValidator = IBANValidatorImpl()

    @Test
    fun valid() {
        Assertions.assertThat(validator.validateIban(IBAN(IBANChecksumValidationTest.VALID_IBAN), mutableSetOf()))
            .isEmpty()
    }

    @Test
    fun badChecksum() {
        ValidationTestUtil.validateSetOfViolationsContains(
            validator.validateIban(IBAN("DE19888666554444333322"), mutableSetOf()),
            setOf("${IBAN.COMMON_ERROR_CODE}.wrong_checksum")
        )
    }

    @Test
    fun nullValue() {
        ValidationTestUtil.validateSetOfViolationsContains(
            validator.validateIban(IBAN(null), mutableSetOf()),
            setOf("${IBAN.COMMON_ERROR_CODE}.null")
        )
    }

    @Test
    fun unknownCountryCode() {
        ValidationTestUtil.validateSetOfViolationsContains(
            validator.validateIban(IBAN("IBAN1234782937482374"), mutableSetOf()),
            setOf("${IBAN.COMMON_ERROR_CODE}.unknown_country_code")
        )
    }

    @Test
    fun tooShort() {
        ValidationTestUtil.validateSetOfViolationsContains(
            validator.validateIban(IBAN("IBAN1"), mutableSetOf()),
            setOf("${IBAN.COMMON_ERROR_CODE}.country_code_min_length")
        )
    }

}
