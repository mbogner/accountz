# AccountZ

This project defines validations for IBAN and BIC on a local basis. No frameworks or external servers required.

The only defined dependency is slf4j-api to integrate into a user's logging.

The base implementation for the validation can be found under `dev.mbo.accountz.validator.AccountZValidatorImpl`. It
implements all defined validator interfaces.
