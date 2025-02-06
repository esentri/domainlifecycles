package io.domainlifecycles.assertion;

class CustomAssertionExceptions {

    static CustomAssertionException defaultCustomAssertionException() {

        return new CustomAssertionException("Failed", CustomAssertionExceptionCode.CUSTOM_ASSERTION_EXCEPTION_CODE_1);
    }
}
