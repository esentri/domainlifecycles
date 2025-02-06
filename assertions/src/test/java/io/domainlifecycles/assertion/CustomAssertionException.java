package io.domainlifecycles.assertion;

class CustomAssertionException extends RuntimeException {

    private final CustomAssertionExceptionCode errorCode;

    CustomAssertionException(final String message, final CustomAssertionExceptionCode errorCode) {

        super(message);
        this.errorCode = errorCode;
    }

    CustomAssertionExceptionCode exceptionCode() {

        return errorCode;
    }
}

