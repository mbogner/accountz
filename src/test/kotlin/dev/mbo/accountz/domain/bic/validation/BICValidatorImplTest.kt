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
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class BICValidatorImplTest {

    private val validator: BICValidator = BICValidatorImpl()


    @Test
    fun valid() {
        Assertions.assertThat(validator.validateBic(BIC(BICValidationTest.VALID_BIC), mutableSetOf())).isEmpty()
    }

    @Test
    fun tooShort() {
        ValidationTestUtil.validateSetOfViolationsContains(
            validator.validateBic(BIC("ASD"), mutableSetOf()),
            setOf("${BIC.COMMON_ERROR_CODE}.min_length")
        )
    }

    @Test
    fun tooLong() {
        ValidationTestUtil.validateSetOfViolationsContains(
            validator.validateBic(BIC("ASDASDASDADSAD"), mutableSetOf()),
            setOf("${BIC.COMMON_ERROR_CODE}.max_length")
        )
    }

    @Test
    fun nullValue() {
        ValidationTestUtil.validateSetOfViolationsContains(
            validator.validateBic(BIC(null), mutableSetOf()),
            setOf("${BIC.COMMON_ERROR_CODE}.null")
        )
    }


}
