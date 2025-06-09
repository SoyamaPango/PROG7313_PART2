package com.example.prog7313_part3

class ValidationUtils {
    companion object {

        const val EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$"

        /**
         * Validates an email address using a regex pattern
         * @param email The email string to validate
         * @return A ValidationResult containing success state and any error message
         */
        fun validateEmail(email: String): ValidationResult {
            if (email.isEmpty()) {
                return ValidationResult(false, "Email cannot be empty")
            }

            return if (!email.matches(EMAIL_REGEX.toRegex())) {
                ValidationResult(false, "Invalid email format")
            } else {
                ValidationResult(true)
            }
        }

        /**
         * Validates a password against security requirements
         * @param password The password string to validate
         * @return A ValidationResult containing success state and any error message
         */
        fun validatePassword(password: String): ValidationResult {
            if (password.isEmpty()) {
                return ValidationResult(false, "Password cannot be empty")
            }

            if (password.length < 8) {
                return ValidationResult(false, "Password must be at least 8 characters")
            }

            return if (!password.matches(PASSWORD_REGEX.toRegex())) {
                ValidationResult(false, "Password must contain uppercase, lowercase, and numbers")
            } else {
                ValidationResult(true)
            }
        }
    }

    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )
}