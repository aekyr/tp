package seedu.guestnote.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.guestnote.model.guest.Guest;
import seedu.guestnote.model.guest.Phone;

/**
 * An UI component that displays information of a {@code Guest}.
 */
public class GuestCard extends UiPart<Region> {

    private static final String FXML = "GuestListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on GuestNote level 4</a>
     */

    public final Guest guest;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private FlowPane roomNumber;
    @FXML
    private FlowPane status;
    @FXML
    private FlowPane requests;


    /**
     * Creates a {@code GuestCode} with the given {@code Guest} and index to display.
     */
    public GuestCard(Guest guest, int displayedIndex) {
        super(FXML);
        this.guest = guest;
        id.setText(displayedIndex + ". ");
        name.setText(" " + guest.getName().fullName + " ");
        phone.setText(guest.getPhone().map(Phone::toString).orElse("Phone: Not Added"));
        email.setText(guest.getEmail().value);
        roomNumber.getChildren().add(new Label("#" + guest.getRoomNumber().roomNumber));
        Label statusLabel = new Label(guest.getStatus().name().replace("_", " "));
        statusLabel.getStyleClass().add("status-label");

        switch (guest.getStatus()) {

        case BOOKED:
            statusLabel.getStyleClass().add("status-booking");
            break;
        case CHECKED_IN:
            statusLabel.getStyleClass().add("status-checkedin");
            break;
        case CHECKED_OUT:
            statusLabel.getStyleClass().add("status-checkedout");
            break;

        default:
            break;
        }


        status.getChildren().add(statusLabel);
        requests.prefWrapLengthProperty().bind(cardPane.widthProperty().subtract(30));

        final int[] counter = {1};
        guest.getRequests()
                .forEach(request -> {
                    String text = counter[0] + ". " + request.requestName;
                    String safeText = insertSoftWraps(text, 15);

                    Label requestLabel = new Label(safeText);
                    requestLabel.setWrapText(true);
                    requestLabel.setMinWidth(0);
                    requestLabel.setMaxWidth(150);
                    requestLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    requestLabel.setMinHeight(Region.USE_PREF_SIZE);
                    requests.getChildren().add(requestLabel);
                    counter[0]++;
                });

    }

    private String insertSoftWraps(String text, int maxChunkLength) {
        StringBuilder result = new StringBuilder();
        int count = 0;
        for (char c : text.toCharArray()) {
            result.append(c);
            count++;
            if (count >= maxChunkLength) {
                result.append('\u200B'); // zero-width space
                count = 0;
            }
        }
        return result.toString();
    }
}
