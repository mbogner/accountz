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

import dev.mbo.accountz.shared.validation.Validatable
import dev.mbo.accountz.shared.validation.Validation
import dev.mbo.accountz.shared.violation.ConstraintViolation

class IBANCountryBasedLengthValidation : Validation {

    override fun execute(
        validatable: Validatable<*>?,
        commonErrorCode: String,
        cache: MutableMap<String, Any>
    ): Set<ConstraintViolation> {
        val value = validatable?.getData()
        if (value !is String) return emptySet()
        if (value.length < 10) {
            return setOf(
                ConstraintViolation(
                    commonErrorCode,
                    value,
                    "$commonErrorCode.country_code_min_length",
                    "value $commonErrorCode can't be valid with a length of ${value.length}",
                    mapOf("length" to value.length)
                )
            )
        }
        val countryCode = extractCountryCode(value)
        val expectedLength = IBAN_LENGTHS[countryCode] ?: return setOf(
            ConstraintViolation(
                commonErrorCode,
                value,
                "$commonErrorCode.unknown_country_code",
                "value $commonErrorCode starts with an unknown country code $countryCode",
                mapOf("countryCode" to countryCode)
            )
        )
        if (value.length != expectedLength) {
            return setOf(
                ConstraintViolation(
                    commonErrorCode,
                    value,
                    "$commonErrorCode.invalid_length_for_country",
                    "value $commonErrorCode for country code $countryCode should have length " +
                            "$expectedLength but actual length is ${value.length}",
                    mapOf(
                        "countryCode" to countryCode,
                        "expectedLength" to expectedLength,
                        "length" to value.length,
                    )
                )
            )
        }
        cache[CACHE_COUNTRY_CODE] = countryCode
        return emptySet()
    }

    companion object {
        const val CACHE_COUNTRY_CODE = "ibanCountryCode"
        val IBAN_LENGTHS: Map<String, Int> = mapOf(
            "AT" to 20, // Austria
            "AD" to 24, // Andorra
            "BE" to 16, // Belgium
            "BG" to 22, // Bulgaria
            "DK" to 18, // Denmark
            "DE" to 22, // Germany
            "EE" to 20, // Estland
            "FI" to 18, // Finnland
            "FR" to 27, // France
            "GI" to 23, // Gibraltar
            "GR" to 27, // Greece
            "GB" to 22, // Great Britain
            "IE" to 22, // Ireland
            "IS" to 26, // Island
            "IT" to 27, // Italy
            "LV" to 21, // Latvia
            "LT" to 20, // Lithuania
            "LU" to 20, // Luxemburg
            "MT" to 31, // Malta
            "NL" to 18, // Netherlands
            "NO" to 15, // Norway
            "PL" to 28, // Poland
            "PT" to 25, // Portugal
            "RO" to 24, // Romania
            "SE" to 24, // Sweden
            "CH" to 21, // Switzerland
            "SK" to 24, // Slovakia
            "SI" to 19, // Slovenia
            "ES" to 24, // Spain
            "CZ" to 24, // Check Republic
            "HU" to 28, // Hungary
            "CY" to 28, // Cyprus
        )

        fun extractCountryCode(value: String): String {
            if (value.length < 2) {
                throw IllegalArgumentException("length of value '$value' must be >= 2 to get the first two characters")
            }
            return value.substring(0, 2).uppercase()
        }

    }
}
