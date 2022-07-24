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

import dev.mbo.accountz.domain.bic.BIC
import dev.mbo.accountz.domain.iban.IBAN
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class NotNullValidationTest {

    private val bean = NotNullValidation()

    @Test
    fun valid() {
        assertThat(bean.execute(IBAN(""), IBAN.COMMON_ERROR_CODE, mutableMapOf())).isEmpty()
    }

    @Test
    fun invalidIban() {
        ValidationTestUtil.validateSetOfViolationsContains(
            bean.execute(IBAN(null), IBAN.COMMON_ERROR_CODE, mutableMapOf()),
            setOf("${IBAN.COMMON_ERROR_CODE}.null")
        )
    }

    @Test
    fun invalidBic() {
        ValidationTestUtil.validateSetOfViolationsContains(
            bean.execute(BIC(null), BIC.COMMON_ERROR_CODE, mutableMapOf()),
            setOf("${BIC.COMMON_ERROR_CODE}.null")
        )
    }
}
