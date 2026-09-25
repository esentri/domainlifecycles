package io.domainlifecycles.persistence.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DLCPersistenceExceptionTest {

    @Test
    public void testFailWithDetailOnly() {
        var ex = DLCPersistenceException.fail("Something went wrong!");

        assertThat(ex.getMessage()).isEqualTo("Something went wrong!");
        assertThat(ex.getCause()).isNull();
    }

    @Test
    public void testFailWithDetailAndCause() {
        var cause = new IllegalStateException("root cause");

        var ex = DLCPersistenceException.fail("Something went wrong!", cause);

        assertThat(ex.getMessage()).isEqualTo("Something went wrong!");
        assertThat(ex.getCause()).isSameAs(cause);
    }

    @Test
    public void testFailWithFormattedArgs() {
        var ex = DLCPersistenceException.fail("Table '%s' has no column named '%s'.", "ORDER", "TOTAL_PRICE");

        assertThat(ex.getMessage()).isEqualTo("Table 'ORDER' has no column named 'TOTAL_PRICE'.");
        assertThat(ex.getCause()).isNull();
    }

    @Test
    public void testFailWithFormattedArgsAndCause() {
        var cause = new IllegalStateException("root cause");

        var ex = DLCPersistenceException.fail("Failed for id '%s'.", cause, 42);

        assertThat(ex.getMessage()).isEqualTo("Failed for id '42'.");
        assertThat(ex.getCause()).isSameAs(cause);
    }

    @Test
    public void testDetailContainingALiteralPercentSignIsNotFormattedWhenNoArgsAreGiven() {
        // fail(String) delegates to the varargs overload with an empty args array; format() only calls
        // String.format when args.length > 0, so a literal '%' in the message is not mistaken for a
        // format specifier and does not blow up with a MissingFormatArgumentException.
        var ex = DLCPersistenceException.fail("Discount must be 100% applied at most.");

        assertThat(ex.getMessage()).isEqualTo("Discount must be 100% applied at most.");
    }

    @Test
    public void testFailRequiresANonNullDetailMessage() {
        assertThatThrownBy(() -> DLCPersistenceException.fail((String) null))
            .isInstanceOf(NullPointerException.class);
    }
}
