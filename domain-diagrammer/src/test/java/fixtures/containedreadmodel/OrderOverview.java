package fixtures.containedreadmodel;

import io.domainlifecycles.domain.types.ReadModel;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Contains other read models: one required, one optional, a list of at least one and a list of any number.
 */
public class OrderOverview implements ReadModel {

    private String title;

    @NotNull
    private CustomerView customer;

    private Optional<DeliveryView> delivery;

    @NotEmpty
    private List<LineView> lines;

    private List<NoteView> notes;
}
