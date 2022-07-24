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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class IBANCountryBasedLengthValidationTest {

    private val bean = IBANCountryBasedLengthValidation()

    @Test
    fun valid() {
        val cache = mutableMapOf<String, Any>()
        assertThat(bean.execute(IBAN("AT123478293748237400"), IBAN.COMMON_ERROR_CODE, cache)).isEmpty()
        assertThat(cache[IBANCountryBasedLengthValidation.CACHE_COUNTRY_CODE]).isEqualTo("AT")
    }

    @Test
    fun nullValue() {
        val cache = mutableMapOf<String, Any>()
        assertThat(bean.execute(IBAN(null), IBAN.COMMON_ERROR_CODE, cache)).isEmpty()
        assertThat(cache[IBANCountryBasedLengthValidation.CACHE_COUNTRY_CODE]).isNull()
    }

    @Test
    fun tooShort() {
        ValidationTestUtil.validateSetOfViolationsContains(
            bean.execute(IBAN("AT123"), IBAN.COMMON_ERROR_CODE, mutableMapOf()),
            setOf("${IBAN.COMMON_ERROR_CODE}.country_code_min_length")
        )
    }

    @Test
    fun lowerBound() {
        ValidationTestUtil.validateSetOfViolationsContains(
            bean.execute(IBAN("AT12347829374823740"), IBAN.COMMON_ERROR_CODE, mutableMapOf()),
            setOf("${IBAN.COMMON_ERROR_CODE}.invalid_length_for_country")
        )
    }

    @Test
    fun upperBound() {
        ValidationTestUtil.validateSetOfViolationsContains(
            bean.execute(IBAN("AT1234782937482374000"), IBAN.COMMON_ERROR_CODE, mutableMapOf()),
            setOf("${IBAN.COMMON_ERROR_CODE}.invalid_length_for_country")
        )
    }

    @Test
    fun unknownCountryCode() {
        ValidationTestUtil.validateSetOfViolationsContains(
            bean.execute(IBAN("ZZ348237482374892374"), IBAN.COMMON_ERROR_CODE, mutableMapOf()),
            setOf("${IBAN.COMMON_ERROR_CODE}.unknown_country_code")
        )
    }

    @Test
    fun extractCountryCode() {
        assertThat(IBANCountryBasedLengthValidation.extractCountryCode("at123")).isEqualTo("AT")
        assertThrows(IllegalArgumentException::class.java) {
            IBANCountryBasedLengthValidation.extractCountryCode("a")
        }
        assertThrows(IllegalArgumentException::class.java) {
            IBANCountryBasedLengthValidation.extractCountryCode("")
        }
    }

    @Test
    fun vars() {
        assertThat(IBANCountryBasedLengthValidation.IBAN_LENGTHS).isNotEmpty
        assertThat(IBANCountryBasedLengthValidation.CACHE_COUNTRY_CODE).isNotBlank
    }

}
