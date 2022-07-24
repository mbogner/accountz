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

import dev.mbo.accountz.domain.iban.IBAN
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LengthValidationTest {

    private val bean5 = LengthValidation(exact = 5)
    private val beanMinMax = LengthValidation(min = 8, max = 11)

    @Test
    fun valid5() {
        assertThat(bean5.execute(IBAN("12345"), IBAN.COMMON_ERROR_CODE, mutableMapOf())).isEmpty()
    }

    @Test
    fun validMinMax() {
        assertThat(beanMinMax.execute(IBAN("12345678"), IBAN.COMMON_ERROR_CODE, mutableMapOf())).isEmpty()
    }

    @Test
    fun tooShort() {
        ValidationTestUtil.validateSetOfViolationsContains(
            bean5.execute(IBAN("1234"), IBAN.COMMON_ERROR_CODE, mutableMapOf()),
            setOf("${IBAN.COMMON_ERROR_CODE}.exact_length")
        )
    }

    @Test
    fun tooShortMinMax() {
        ValidationTestUtil.validateSetOfViolationsContains(
            beanMinMax.execute(IBAN("1234"), IBAN.COMMON_ERROR_CODE, mutableMapOf()),
            setOf("${IBAN.COMMON_ERROR_CODE}.min_length")
        )
    }

    @Test
    fun tooLong() {
        ValidationTestUtil.validateSetOfViolationsContains(
            bean5.execute(IBAN("123456"), IBAN.COMMON_ERROR_CODE, mutableMapOf()),
            setOf("${IBAN.COMMON_ERROR_CODE}.exact_length")
        )
    }

    @Test
    fun nullValue() {
        bean5.execute(IBAN(null), IBAN.COMMON_ERROR_CODE, mutableMapOf()).isEmpty()
    }
}
