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

package dev.mbo.accountz.domain.bic.validation

import dev.mbo.accountz.domain.bic.BIC
import dev.mbo.accountz.shared.validation.ValidationTestUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BICValidationTest {
    companion object {
        val VALID_BIC = "SMCODE2L"
    }

    private val bean = BICValidation()

    @Test
    fun valid() {
        val cache = mutableMapOf<String, Any>()
        assertThat(bean.execute(BIC(VALID_BIC), BIC.COMMON_ERROR_CODE, cache)).isEmpty()
        assertThat(cache[BICValidation.CACHE_BANK_CODE]).isEqualTo("SMCO")
        assertThat(cache[BICValidation.CACHE_COUNTRY_CODE]).isEqualTo("DE")
        assertThat(cache[BICValidation.CACHE_LOCATION_CODE]).isEqualTo("2L")
        assertThat(cache[BICValidation.CACHE_BRANCH_CODE]).isEqualTo("")
    }

    @Test
    fun invalidBankcode() {
        val cache = mutableMapOf<String, Any>()
        ValidationTestUtil.validateSetOfViolationsContains(
            bean.execute(BIC("SM.ODE2L"), BIC.COMMON_ERROR_CODE, cache),
            setOf("${BIC.COMMON_ERROR_CODE}.bank_code_not_alpha")
        )
    }

    @Test
    fun invalidCountryCode() {
        val cache = mutableMapOf<String, Any>()
        ValidationTestUtil.validateSetOfViolationsContains(
            bean.execute(BIC("SMCOD.2L"), BIC.COMMON_ERROR_CODE, cache),
            setOf("${BIC.COMMON_ERROR_CODE}.country_code_not_alpha")
        )
    }

    @Test
    fun invalidLocationCode() {
        val cache = mutableMapOf<String, Any>()
        ValidationTestUtil.validateSetOfViolationsContains(
            bean.execute(BIC("SMCODE.L"), BIC.COMMON_ERROR_CODE, cache),
            setOf("${BIC.COMMON_ERROR_CODE}.location_code_not_alpha_numeric")
        )
    }

    @Test
    fun invalidBranchCode() {
        val cache = mutableMapOf<String, Any>()
        ValidationTestUtil.validateSetOfViolationsContains(
            bean.execute(BIC("SMCODE9L1.E"), BIC.COMMON_ERROR_CODE, cache),
            setOf("${BIC.COMMON_ERROR_CODE}.branch_code_not_alpha_numeric")
        )
    }

    @Test
    fun nullValue() {
        val cache = mutableMapOf<String, Any>()
        assertThat(bean.execute(BIC(null), BIC.COMMON_ERROR_CODE, cache)).isEmpty()
        assertThat(cache).isEmpty()
    }

    @Test
    fun extractBankcode() {
        assertThat(BICValidation.extractBankcode(VALID_BIC)).isEqualTo("SMCO")
    }

    @Test
    fun extractCountryCode() {
        assertThat(BICValidation.extractCountryCode(VALID_BIC)).isEqualTo("DE")
    }

    @Test
    fun extractLocationCode() {
        assertThat(BICValidation.extractLocationCode(VALID_BIC)).isEqualTo("2L")
    }

    @Test
    fun extractBranchCode() {
        assertThat(BICValidation.extractBranchCode(VALID_BIC)).isEqualTo("")
    }
}
