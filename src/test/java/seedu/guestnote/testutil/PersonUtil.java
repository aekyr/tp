package seedu.guestnote.testutil;

import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_ADD_REQ;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_DELETE_REQ;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_REQUEST;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_ROOMNUMBER;

import java.util.List;

import seedu.guestnote.logic.commands.AddCommand;
import seedu.guestnote.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.guestnote.model.guest.Guest;
import seedu.guestnote.model.request.Request;



/**
 * A utility class for Guest.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code guest}.
     */
    public static String getAddCommand(Guest guest) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(guest);
    }

    /**
     * Returns the part of command string for the given {@code guest}'s details.
     */
    public static String getPersonDetails(Guest guest) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + guest.getName().fullName + " ");
        sb.append(PREFIX_PHONE + guest.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + guest.getEmail().value + " ");
        sb.append(PREFIX_ROOMNUMBER + guest.getRoomNumber().roomNumber + " ");
        guest.getRequests().stream().forEach(
            s -> sb.append(PREFIX_REQUEST + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getRoomNumber().ifPresent(roomNumber ->
                sb.append(PREFIX_ROOMNUMBER).append(roomNumber.roomNumber).append(" "));
        if (descriptor.getRequestsToAdd().isPresent()) {
            List<Request> requests = descriptor.getRequestsToAdd().get();
            if (requests.isEmpty()) {
                sb.append(PREFIX_ADD_REQ);
            } else {
                requests.forEach(s -> sb.append(PREFIX_ADD_REQ).append(s.tagName).append(" "));
            }
        }
        if (descriptor.getRequestsToAdd().isPresent()) {
            List<Request> requests = descriptor.getRequestsToAdd().get();
            if (requests.isEmpty()) {
                sb.append(PREFIX_DELETE_REQ);
            } else {
                requests.forEach(s -> sb.append(PREFIX_DELETE_REQ).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
