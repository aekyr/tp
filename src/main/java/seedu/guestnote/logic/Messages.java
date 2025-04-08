package seedu.guestnote.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.guestnote.logic.parser.Prefix;
import seedu.guestnote.model.guest.Guest;
import seedu.guestnote.model.guest.Phone;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_GUEST_DISPLAYED_INDEX = "The guest index provided is invalid";
    public static final String MESSAGE_GUESTS_LISTED_OVERVIEW = "%1$d guests listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
            "Multiple values were provided for the following fields, but only one is allowed: ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code guest} for display to the user.
     */
    public static String format(Guest guest) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Name: ")
                .append(guest.getName())
                .append("; Phone: ")
                .append(guest.getPhone().orElse(new Phone("")))
                .append("; Email: ")
                .append(guest.getEmail())
                .append("; Room Number: ")
                .append(guest.getRoomNumber())
                .append("; Request(s): ");
        guest.getRequests().forEach(builder::append);
        return builder.toString();
    }

}
