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

internal class IBANChecksumValidationTest {

    companion object {
        const val VALID_IBAN = "DE18888666554444333322"
    }

    private val bean = IBANChecksumValidation()

    @Test
    fun vars() {
        assertThat(IBANChecksumValidation.MODULUS).isNotNull
        assertThat(IBANChecksumValidation.ISO_SUB).isNotNull
        assertThat(IBANChecksumValidation.CACHE_CHECKSUM).isNotBlank
        assertThat(IBANChecksumValidation.CACHE_BANK_CODE_ACCOUNT).isNotBlank
        assertThat(IBANChecksumValidation.CACHE_NUMERIC_COUNTRY_CODE).isNotBlank
    }

    @Test
    fun valid() {
        val cache = mutableMapOf<String, Any>(IBANCountryBasedLengthValidation.CACHE_COUNTRY_CODE to "DE")
        assertThat(bean.execute(IBAN(VALID_IBAN), IBAN.COMMON_ERROR_CODE, cache)).isEmpty()
        assertThat(cache[IBANCountryBasedLengthValidation.CACHE_COUNTRY_CODE]).isEqualTo("DE")
        assertThat(cache[IBANChecksumValidation.CACHE_CHECKSUM]).isEqualTo(18)
        assertThat(cache[IBANChecksumValidation.CACHE_NUMERIC_COUNTRY_CODE]).isEqualTo("131400")
        assertThat(cache[IBANChecksumValidation.CACHE_BANK_CODE_ACCOUNT]).isEqualTo("888666554444333322")
    }

    @Test
    fun nullValue() {
        assertThat(bean.execute(IBAN(null), IBAN.COMMON_ERROR_CODE, mutableMapOf())).isEmpty()
    }

    @Test
    fun checksumNotInt() {
        val cache = mutableMapOf<String, Any>(IBANCountryBasedLengthValidation.CACHE_COUNTRY_CODE to "DE")
        ValidationTestUtil.validateSetOfViolationsContains(
            bean.execute(IBAN("DEDE888666554444333322"), IBAN.COMMON_ERROR_CODE, cache),
            setOf("${IBAN.COMMON_ERROR_CODE}.checksum_not_int")
        )
    }

    @Test
    fun invalidCountryCodeCache() {
        val cache = mutableMapOf<String, Any>(IBANCountryBasedLengthValidation.CACHE_COUNTRY_CODE to 11)
        assertThrows(IllegalStateException::class.java) {
            bean.execute(IBAN(VALID_IBAN), IBAN.COMMON_ERROR_CODE, cache)
        }
    }

    @Test
    fun unparseableNumbers() {
        val cache = mutableMapOf<String, Any>(IBANCountryBasedLengthValidation.CACHE_COUNTRY_CODE to "DE")
        ValidationTestUtil.validateSetOfViolationsContains(
            bean.execute(IBAN("DE1888866655444433332A"), IBAN.COMMON_ERROR_CODE, cache),
            setOf("${IBAN.COMMON_ERROR_CODE}.back_code_account_not_numeric")
        )
    }

    @Test
    fun extractCheckSum() {
        assertThat(IBANChecksumValidation.extractCheckSum("AT12345")).isEqualTo(12)
    }

    @Test
    fun extractReminder() {
        assertThat(IBANChecksumValidation.extractReminder("AT12345")).isEqualTo("345")
    }

    @Test
    fun translateCharacters() {
        assertThat(IBANChecksumValidation.translateCharacters('A')).isEqualTo(1)
        assertThat(IBANChecksumValidation.translateCharacters('B')).isEqualTo(2)
        assertThat(IBANChecksumValidation.translateCharacters('Z')).isEqualTo(26)
        assertThrows(IllegalArgumentException::class.java) {
            IBANChecksumValidation.translateCharacters('1')
        }
    }

    @Test
    fun translateCountryCode() {
        // 1 + 9, 2 + 9, 00
        assertThat(IBANChecksumValidation.translateCountryCode("AB")).isEqualTo("101100")
        // 2 + 9, 3 + 9, 00
        assertThat(IBANChecksumValidation.translateCountryCode("BC")).isEqualTo("111200")
        // 1 + 9, 26 + 9, 00
        assertThat(IBANChecksumValidation.translateCountryCode("AZ")).isEqualTo("103500")
    }

}
