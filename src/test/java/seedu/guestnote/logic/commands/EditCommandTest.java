package seedu.guestnote.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.guestnote.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.guestnote.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_REQUEST_EXTRAPILLOW;
import static seedu.guestnote.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.guestnote.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.guestnote.logic.commands.CommandTestUtil.showGuestAtIndex;
import static seedu.guestnote.testutil.TypicalGuests.getTypicalGuestNote;
import static seedu.guestnote.testutil.TypicalIndexes.INDEX_EIGHTH_GUEST;
import static seedu.guestnote.testutil.TypicalIndexes.INDEX_FIRST_GUEST;
import static seedu.guestnote.testutil.TypicalIndexes.INDEX_SECOND_GUEST;

import org.junit.jupiter.api.Test;

import seedu.guestnote.commons.core.index.Index;
import seedu.guestnote.logic.Messages;
import seedu.guestnote.logic.commands.EditCommand.EditGuestDescriptor;
import seedu.guestnote.model.GuestNote;
import seedu.guestnote.model.Model;
import seedu.guestnote.model.ModelManager;
import seedu.guestnote.model.UserPrefs;
import seedu.guestnote.model.guest.Guest;
import seedu.guestnote.testutil.EditGuestDescriptorBuilder;
import seedu.guestnote.testutil.GuestBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalGuestNote(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Guest editedGuest = new GuestBuilder().withRequests("Extra Pillow").build();
        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder(editedGuest).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_GUEST, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_GUEST_SUCCESS, Messages.format(editedGuest));

        Model expectedModel = new ModelManager(new GuestNote(model.getGuestNote()), new UserPrefs());
        expectedModel.setGuest(model.getFilteredGuestList().get(0), editedGuest);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastGuest = Index.fromOneBased(model.getFilteredGuestList().size());
        Guest lastGuest = model.getFilteredGuestList().get(indexLastGuest.getZeroBased());

        GuestBuilder personInList = new GuestBuilder(lastGuest);
        Guest editedGuest = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withRequests(VALID_REQUEST_EXTRAPILLOW).build();

        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withRequestsToAdd(VALID_REQUEST_EXTRAPILLOW).build();
        EditCommand editCommand = new EditCommand(indexLastGuest, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_GUEST_SUCCESS,
                Messages.format(editedGuest));

        Model expectedModel = new ModelManager(new GuestNote(model.getGuestNote()), new UserPrefs());
        expectedModel.setGuest(lastGuest, editedGuest);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_GUEST, new EditGuestDescriptor());
        Guest editedGuest = model.getFilteredGuestList().get(INDEX_FIRST_GUEST.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_GUEST_SUCCESS, Messages.format(editedGuest));

        Model expectedModel = new ModelManager(new GuestNote(model.getGuestNote()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showGuestAtIndex(model, INDEX_FIRST_GUEST);

        Guest guestInFilteredList =
                model.getFilteredGuestList().get(INDEX_FIRST_GUEST.getZeroBased());
        Guest editedGuest = new GuestBuilder(guestInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_GUEST,
                new EditGuestDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_GUEST_SUCCESS,
                Messages.format(editedGuest));

        Model expectedModel = new ModelManager(new GuestNote(model.getGuestNote()), new UserPrefs());
        showGuestAtIndex(expectedModel, INDEX_FIRST_GUEST);
        expectedModel.setGuest(model.getFilteredGuestList().get(0), editedGuest);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateGuestUnfilteredList_failure() {
        Guest firstGuest = model.getFilteredGuestList().get(INDEX_FIRST_GUEST.getZeroBased());
        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder(firstGuest).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_GUEST, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_GUEST);
    }

    @Test
    public void execute_duplicateGuestFilteredList_failure() {
        showGuestAtIndex(model, INDEX_FIRST_GUEST);

        // edit guest in filtered list into a duplicate in guestnote book
        Guest guestInList = model.getGuestNote().getGuestList().get(INDEX_SECOND_GUEST.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_GUEST,
                new EditGuestDescriptorBuilder(guestInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_GUEST);
    }

    /**
     * Tests the execution of editing a guest that originally does not have a phone number.
     * This test ensures that the phone number is successfully updated to the new value
     * when an edit command is executed on the guest.
     */
    @Test
    public void execute_originalGuestWithNoPhone_editSuccess() {
        Guest editedGuest = new GuestBuilder().withPhone("81234567").build();
        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder(editedGuest).build();
        EditCommand editCommand = new EditCommand(INDEX_EIGHTH_GUEST, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_GUEST_SUCCESS, Messages.format(editedGuest));

        Model expectedModel = new ModelManager(new GuestNote(model.getGuestNote()), new UserPrefs());
        expectedModel.setGuest(model.getFilteredGuestList().get(7), editedGuest);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Tests the execution of editing a guest that originally has a different phone number.
     * This test ensures that the phone number is successfully updated to the new value
     * when an edit command is executed on the guest.
     */
    @Test
    public void execute_originalGuestWithPhone_editSuccess() {
        Guest editedGuest = new GuestBuilder().withPhone("81234567").withRequests("Extra Pillow").build();
        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder(editedGuest).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_GUEST, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_GUEST_SUCCESS, Messages.format(editedGuest));

        Model expectedModel = new ModelManager(new GuestNote(model.getGuestNote()), new UserPrefs());
        expectedModel.setGuest(model.getFilteredGuestList().get(0), editedGuest);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidGuestIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredGuestList().size() + 1);
        EditGuestDescriptor descriptor =
                new EditGuestDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_GUEST_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of guestnote book
     */
    @Test
    public void execute_invalidGuestIndexFilteredList_failure() {
        showGuestAtIndex(model, INDEX_FIRST_GUEST);
        Index outOfBoundIndex = INDEX_SECOND_GUEST;
        // ensures that outOfBoundIndex is still in bounds of guestnote book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getGuestNote().getGuestList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditGuestDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_GUEST_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateRequest_failure() {
        Guest guestWithRequest = new GuestBuilder().withRequests("friend").build();
        model.setGuest(model.getFilteredGuestList().get(0), guestWithRequest);

        // Attempt to add "dinner" again -> should throw DuplicateRequestException
        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder()
                .withRequestsToAdd("friend")
                .build();

        EditCommand editCommand = new EditCommand(INDEX_FIRST_GUEST, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_REQUEST + ": [friend]");
    }

    @Test
    public void execute_addRequestAndDeleteRequestIndex_success() {
        // Set up the guest with initial requests
        Guest originalGuest = new GuestBuilder()
                .withName("Alex Yeoh")
                .withPhone("99272758")
                .withEmail("alexyeoh@example.com")
                .withRoomNumber("23-32")
                .withRequests("Late check in", "Hello4")
                .build();
        model.setGuest(model.getFilteredGuestList().get(0), originalGuest);

        // Prepare expected guest after editing
        Guest editedGuest = new GuestBuilder()
                .withName("Alex Yeoh")
                .withPhone("99272758")
                .withEmail("alexyeoh@example.com")
                .withRoomNumber("23-32")
                .withRequests("Late check in", "Hello5") // Hello4 is removed (index 2), Hello5 is added
                .build();

        // Delete index 2 removes "Hello4", add "Hello5"
        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder()
                .withRequestsToAdd("Hello5")
                .withRequestIndexesToDelete("2")
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_GUEST, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_GUEST_SUCCESS, Messages.format(editedGuest));

        Model expectedModel = new ModelManager(new GuestNote(model.getGuestNote()), new UserPrefs());
        expectedModel.setGuest(model.getFilteredGuestList().get(0), editedGuest);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteIndexOutOfBoundsAndAddRequest_failure() {
        // Set up the guest with 3 initial requests
        Guest originalGuest = new GuestBuilder()
                .withName("Alex Yeoh")
                .withRequests("hello", "hello2", "hello3")
                .build();
        model.setGuest(model.getFilteredGuestList().get(0), originalGuest);

        // Attempt to delete index 4 (invalid), then add "hello4"
        // Deletion of index 4 should give error
        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder()
                .withRequestsToAdd("hello4")
                .withRequestIndexesToDelete("4")
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_GUEST, descriptor);
        String expectedMessage = "This request does not exist in the guest: Index Number 4";
        assertCommandFailure(editCommand, model, expectedMessage);
    }

    @Test
    public void execute_deleteNotFoundRequestAndAddRequest_failure() {
        // Set up the guest with 3 initial requests
        Guest originalGuest = new GuestBuilder()
                .withName("Alex Yeoh")
                .withRequests("hello", "hello2", "hello3")
                .build();
        model.setGuest(model.getFilteredGuestList().get(0), originalGuest);

        // Attempt to delete "hello4" (invalid), then add "hello5"
        // Deletion of request "hello4" should give error
        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder()
                .withRequestsToAdd("hello5")
                .withRequestsToDelete("hello4")
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_GUEST, descriptor);
        String expectedMessage = "This request does not exist in the guest: [hello4]";
        assertCommandFailure(editCommand, model, expectedMessage);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_GUEST, DESC_AMY);

        // same values -> returns true
        EditGuestDescriptor copyDescriptor = new EditGuestDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_GUEST, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_GUEST, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_GUEST, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditGuestDescriptor editGuestDescriptor = new EditGuestDescriptor();
        EditCommand editCommand = new EditCommand(index, editGuestDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", "
                + "editGuestDescriptor="
                + editGuestDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
