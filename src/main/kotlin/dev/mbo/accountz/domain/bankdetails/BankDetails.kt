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

package dev.mbo.accountz.domain.bankdetails

import dev.mbo.accountz.domain.bankdetails.validation.BankDetailsCountryCodeValidation
import dev.mbo.accountz.domain.bic.BIC
import dev.mbo.accountz.domain.iban.IBAN
import dev.mbo.accountz.shared.validation.Validatable

data class BankDetails(
    val iban: IBAN,
    val bic: BIC,
) : Validatable<Array<Validatable<*>>> {

    companion object {
        val COMMON_ERROR_CODE = BankDetails::class.java.simpleName.lowercase()
        val VALIDATIONS = setOf(
            BankDetailsCountryCodeValidation(),
        )
    }

    override fun getData(): Array<Validatable<*>> {
        return arrayOf(iban, bic)
    }
}
