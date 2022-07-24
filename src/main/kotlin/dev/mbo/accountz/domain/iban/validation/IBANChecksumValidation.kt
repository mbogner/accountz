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

import dev.mbo.accountz.shared.util.MapUtil
import dev.mbo.accountz.shared.util.StringUtil
import dev.mbo.accountz.shared.validation.Validatable
import dev.mbo.accountz.shared.validation.Validation
import dev.mbo.accountz.shared.violation.ConstraintViolation
import java.math.BigDecimal

class IBANChecksumValidation : Validation {

    override fun execute(
        validatable: Validatable<*>?,
        commonErrorCode: String,
        cache: MutableMap<String, Any>
    ): Set<ConstraintViolation> {
        val value = validatable?.getData()
        if (value !is String) {
            return emptySet()
        }
        val countryCode = MapUtil.retrieveRequiredString(IBANCountryBasedLengthValidation.CACHE_COUNTRY_CODE, cache)
        val checksum: Int
        try {
            checksum = extractCheckSum(value)
        } catch (exc: NumberFormatException) {
            return setOf(
                ConstraintViolation(
                    commonErrorCode,
                    value,
                    "$commonErrorCode.checksum_not_int",
                    "checksum in $commonErrorCode $value is not numeric"
                )
            )
        }

        val reminder = extractReminder(value)
        val numericCountryCode = translateCountryCode(countryCode)
        val reminderWithNumericCC = "${reminder}${numericCountryCode}"
        val parsedBeforeModulo: BigDecimal
        try {
            parsedBeforeModulo = BigDecimal(reminderWithNumericCC)
        } catch (exc: NumberFormatException) {
            return setOf(
                ConstraintViolation(
                    commonErrorCode,
                    value,
                    "$commonErrorCode.back_code_account_not_numeric",
                    "bank code or account number in $commonErrorCode $value is not numeric"
                )
            )
        }
        val modulo97 = parsedBeforeModulo.remainder(MODULUS)
        val calculatedChecksum = ISO_SUB.minus(modulo97).toInt()

        if (calculatedChecksum != checksum) {
            return setOf(
                ConstraintViolation(
                    commonErrorCode,
                    value,
                    "$commonErrorCode.wrong_checksum",
                    "checksum in $commonErrorCode $value is not correct - " +
                            "received $checksum but calculated $calculatedChecksum"
                )
            )
        }

        cache[CACHE_CHECKSUM] = checksum
        cache[CACHE_BANK_CODE_ACCOUNT] = reminder
        cache[CACHE_NUMERIC_COUNTRY_CODE] = numericCountryCode
        return emptySet()
    }

    companion object {
        val MODULUS = BigDecimal("97")
        val ISO_SUB = BigDecimal("98")
        const val CACHE_CHECKSUM = "ibanChecksum"
        const val CACHE_NUMERIC_COUNTRY_CODE = "ibanNumericCountryCode"
        const val CACHE_BANK_CODE_ACCOUNT = "ibanBankCodeAccount"

        fun extractCheckSum(value: String): Int {
            return value.substring(2, 4).toInt()
        }

        fun extractReminder(value: String): String {
            return value.substring(4)
        }

        fun translateCharacters(arg: Char): Int {
            var c = 'A'
            var i = 1

            while (c <= 'Z') {
                if (arg == c) return i
                i++
                c++
            }
            throw IllegalArgumentException("invalid character within country code: $arg")
        }

        fun translateCountryCode(countryCode: String): String {
            val chars = countryCode.toCharArray()
            val c0 = StringUtil.prefixIntWith(translateCharacters(chars[0]) + 9, '0', 2)
            val c1 = StringUtil.prefixIntWith(translateCharacters(chars[1]) + 9, '0', 2)
            return "${c0}${c1}00"
        }
    }
}
