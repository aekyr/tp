package seedu.guestnote.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_ADD_REQ;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_REQUEST;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_ROOMNUMBER;
import static seedu.guestnote.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.guestnote.commons.core.index.Index;
import seedu.guestnote.logic.commands.exceptions.CommandException;
import seedu.guestnote.model.GuestBook;
import seedu.guestnote.model.Model;
import seedu.guestnote.model.guest.Guest;
import seedu.guestnote.model.guest.NameContainsKeywordsPredicate;
import seedu.guestnote.model.guest.Status;
import seedu.guestnote.testutil.EditGuestDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_REQUEST_HUSBAND = "husband";
    public static final String VALID_REQUEST_FRIEND = "friend";
    public static final String VALID_ROOMNUMBER_AMY = "12-03";
    public static final String VALID_ROOMNUMBER_BOB = "12-04";
    public static final Status VALID_STATUS_AMY = Status.BOOKING;
    public static final Status VALID_STATUS_BOB = Status.BOOKING;

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ROOMNUMBER_DESC_AMY = " " + PREFIX_ROOMNUMBER + VALID_ROOMNUMBER_AMY;
    public static final String ROOMNUMBER_DESC_BOB = " " + PREFIX_ROOMNUMBER + VALID_ROOMNUMBER_BOB;
    public static final String REQUEST_DESC_FRIEND = " " + PREFIX_REQUEST + VALID_REQUEST_FRIEND;
    public static final String ADD_REQUEST_DESC_FRIEND = " " + PREFIX_ADD_REQ + VALID_REQUEST_FRIEND;
    public static final String REQUEST_DESC_HUSBAND = " " + PREFIX_REQUEST + VALID_REQUEST_HUSBAND;
    public static final String ADD_REQUEST_DESC_HUSBAND = " " + PREFIX_ADD_REQ + VALID_REQUEST_HUSBAND;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_REQUEST_DESC = " " + PREFIX_REQUEST + "hubby*"; // '*' not allowed in requests
    public static final String INVALID_ROOMNUMBER_DESC = " " + PREFIX_ROOMNUMBER + "12"; // missing last digit
    public static final String INVALID_ADD_REQUEST_DESC = " " + PREFIX_ADD_REQ + "hubby*";
    // '*' not allowed in requests

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditGuestDescriptor DESC_AMY;
    public static final EditCommand.EditGuestDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditGuestDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withRoomNumber(VALID_ROOMNUMBER_AMY)
                .withRequestsToAdd(VALID_REQUEST_FRIEND).build();
        DESC_BOB = new EditGuestDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withRoomNumber(VALID_ROOMNUMBER_AMY)
                .withRequestsToAdd(VALID_REQUEST_HUSBAND, VALID_REQUEST_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the guestnote book, filtered guest list and selected guest in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        GuestBook expectedGuestBook = new GuestBook(actualModel.getAddressBook());
        List<Guest> expectedFilteredList = new ArrayList<>(actualModel.getFilteredGuestList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedGuestBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredGuestList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the guest at the given {@code targetIndex} in the
     * {@code model}'s guestnote book.
     */
    public static void showGuestAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredGuestList().size());

        Guest guest = model.getFilteredGuestList().get(targetIndex.getZeroBased());
        final String[] splitName = guest.getName().fullName.split("\\s+");
        model.updateFilteredGuestList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredGuestList().size());
    }

}
