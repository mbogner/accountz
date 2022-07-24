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

import dev.mbo.accountz.domain.bic.validation.BICValidation
import dev.mbo.accountz.domain.iban.validation.IBANCountryBasedLengthValidation
import dev.mbo.accountz.shared.util.MapUtil
import dev.mbo.accountz.shared.validation.Validatable
import dev.mbo.accountz.shared.validation.Validation
import dev.mbo.accountz.shared.violation.ConstraintViolation

class BankDetailsCountryCodeValidation : Validation {

    override fun execute(
        validatable: Validatable<*>?,
        commonErrorCode: String,
        cache: MutableMap<String, Any>
    ): Set<ConstraintViolation> {

        val ibanCountryCode = MapUtil.retrieveRequiredString(IBANCountryBasedLengthValidation.CACHE_COUNTRY_CODE, cache)
        val bicCountryCode = MapUtil.retrieveRequiredString(BICValidation.CACHE_COUNTRY_CODE, cache)

        if (ibanCountryCode != bicCountryCode) {
            return setOf(
                ConstraintViolation(
                    null,
                    null,
                    "$commonErrorCode.iban_cc_not_matching_bic_cc",
                    "country code $ibanCountryCode of iban does not match country code $bicCountryCode of bic",
                    mapOf(
                        "ibanCountryCode" to ibanCountryCode,
                        "bicCountryCode" to bicCountryCode,
                    )
                )
            )
        }

        return emptySet()
    }

}
