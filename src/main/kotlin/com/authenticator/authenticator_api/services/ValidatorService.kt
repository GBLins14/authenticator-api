package com.authenticator.authenticator_api.services

import org.springframework.stereotype.Service

@Service
class ValidatorService {
    private val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
    private val cpfRegex = "^\\d{11}$".toRegex()
    private val phoneRegex = "^\\d{10,11}$".toRegex()

    fun isValidEmail(email: String): Boolean {
        return emailRegex.matches(email)
    }

    fun cleanCpfOrCnpj(cpf: String): String {
        return cpf.replace(Regex("[^0-9]"), "")
    }

    fun isValidCpf(cpf: String): Boolean {
        val cleanedCpf = cleanCpfOrCnpj(cpf)
        if (!cpfRegex.matches(cleanedCpf)) return false

        if (cleanedCpf.all { it == cleanedCpf[0] }) return false

        try {
            var d1 = 0
            var d2 = 0
            for (i in 0..8) {
                d1 += cleanedCpf[i].toString().toInt() * (10 - i)
            }

            d1 = 11 - (d1 % 11)
            if (d1 > 9) d1 = 0

            if (cleanedCpf[9].toString().toInt() != d1) return false

            for (i in 0..9) {
                d2 += cleanedCpf[i].toString().toInt() * (11 - i)
            }

            d2 = 11 - (d2 % 11)
            if (d2 > 9) d2 = 0

            return cleanedCpf[10].toString().toInt() == d2
        } catch (_: NumberFormatException) {
            return false
        }
    }

    fun isValidCnpj(cnpj: String): Boolean {
        val cleanedCnpj = cleanCpfOrCnpj(cnpj)

        if (cleanedCnpj.length != 14) return false

        if (cleanedCnpj.all { it == cleanedCnpj[0] }) return false

        try {
            val weight1 = intArrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
            var sum1 = 0
            for (i in 0..11) {
                sum1 += cleanedCnpj[i].toString().toInt() * weight1[i]
            }

            var d1 = sum1 % 11
            d1 = if (d1 < 2) 0 else 11 - d1

            if (cleanedCnpj[12].toString().toInt() != d1) return false

            val weight2 = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
            var sum2 = 0
            for (i in 0..12) {
                sum2 += cleanedCnpj[i].toString().toInt() * weight2[i]
            }

            var d2 = sum2 % 11
            d2 = if (d2 < 2) 0 else 11 - d2

            return cleanedCnpj[13].toString().toInt() == d2

        } catch (_: NumberFormatException) {
            return false
        }
    }

    fun isValidPhone(phone: String): Boolean {
        val cleanedPhone = phone.replace(Regex("[^0-9]"), "")
        return phoneRegex.matches(cleanedPhone)
    }
}