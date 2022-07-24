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

import dev.mbo.accountz.shared.validation.Validatable
import dev.mbo.accountz.shared.validation.Validation
import dev.mbo.accountz.shared.violation.ConstraintViolation

class BICValidation : Validation {

    override fun execute(
        validatable: Validatable<*>?,
        commonErrorCode: String,
        cache: MutableMap<String, Any>
    ): Set<ConstraintViolation> {
        val value = validatable?.getData()
        if (value !is String) return emptySet()

        val bankcode = extractBankcode(value)
        val countryCode = extractCountryCode(value)
        val locationCode = extractLocationCode(value)
        val branchCode = extractBranchCode(value)

        val violations = mutableSetOf<ConstraintViolation>()
        if (isNotAlpha(bankcode)) {
            violations.add(
                ConstraintViolation(
                    commonErrorCode,
                    value,
                    "$commonErrorCode.bank_code_not_alpha",
                    "value $commonErrorCode has invalid bankcode",
                    mapOf("bankcode" to bankcode)
                )
            )
        }
        if (isNotAlpha(countryCode)) {
            violations.add(
                ConstraintViolation(
                    commonErrorCode,
                    value,
                    "$commonErrorCode.country_code_not_alpha",
                    "value $commonErrorCode has invalid country code",
                    mapOf("countryCode" to countryCode)
                )
            )
        }
        if (isNotAlphaNumeric(locationCode)) {
            violations.add(
                ConstraintViolation(
                    commonErrorCode,
                    value,
                    "$commonErrorCode.location_code_not_alpha_numeric",
                    "value $commonErrorCode has invalid location code",
                    mapOf("locationCode" to locationCode)
                )
            )
        }
        if (isNotAlphaNumeric(branchCode, optional = true)) {
            violations.add(
                ConstraintViolation(
                    commonErrorCode,
                    value,
                    "$commonErrorCode.branch_code_not_alpha_numeric",
                    "value $commonErrorCode has invalid branch code",
                    mapOf("branchCode" to branchCode)
                )
            )
        }
        if (violations.isNotEmpty()) {
            return violations
        }

        cache[CACHE_BANK_CODE] = bankcode
        cache[CACHE_COUNTRY_CODE] = countryCode
        cache[CACHE_LOCATION_CODE] = locationCode
        cache[CACHE_BRANCH_CODE] = branchCode
        return emptySet()
    }

    companion object {
        const val CACHE_BANK_CODE = "bicBankcode"
        const val CACHE_COUNTRY_CODE = "bicCountryCode"
        const val CACHE_LOCATION_CODE = "bicLocationCode"
        const val CACHE_BRANCH_CODE = "bicBranchCode"

        private val REGEX_ALPHA_NUMERIC = Regex("^[A-Z\\d]+$")
        private val REGEX_ALPHA_NUMERIC_OPTIONAL = Regex("^[A-Z\\d]*$")
        private val REGEX_ALPHA = Regex("^[A-Z]+$")

        fun extractBankcode(value: String): String {
            return value.substring(0, 4)
        }

        fun extractCountryCode(value: String): String {
            return value.substring(4, 6)
        }

        fun extractLocationCode(value: String): String {
            return value.substring(6, 8)
        }

        fun extractBranchCode(value: String): String {
            return value.substring(8)
        }

        fun isNotAlphaNumeric(arg: String, optional: Boolean = false): Boolean {
            if (optional) return !arg.matches(REGEX_ALPHA_NUMERIC_OPTIONAL)
            return !arg.matches(REGEX_ALPHA_NUMERIC)
        }

        fun isNotAlpha(arg: String): Boolean {
            return !arg.matches(REGEX_ALPHA)
        }
    }
}
