package seedu.guestnote.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_ADD_REQ;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_DELETE_REQ;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_DELETE_REQ_INDEX;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_ROOMNUMBER;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.guestnote.commons.core.index.Index;
import seedu.guestnote.commons.util.CollectionUtil;
import seedu.guestnote.commons.util.ToStringBuilder;
import seedu.guestnote.logic.Messages;
import seedu.guestnote.logic.commands.exceptions.CommandException;
import seedu.guestnote.model.Model;
import seedu.guestnote.model.guest.Email;
import seedu.guestnote.model.guest.Guest;
import seedu.guestnote.model.guest.Name;
import seedu.guestnote.model.guest.Phone;
import seedu.guestnote.model.guest.RoomNumber;
import seedu.guestnote.model.guest.Status;
import seedu.guestnote.model.request.Request;
import seedu.guestnote.model.request.UniqueRequestList;
import seedu.guestnote.model.request.exceptions.DuplicateRequestException;
import seedu.guestnote.model.request.exceptions.RequestNotFoundException;

/**
 * Edits the details of an existing guest in the guestnote book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the guest identified "
            + "by the index number used in the displayed guest list. "
            + "\nAt least one of the optional fields below (e.g., name, email, request) must be "
            + "provided.\n"
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ROOMNUMBER + "ROOMNUMBER] "
            + "[" + PREFIX_ADD_REQ + "ADD_REQUEST]...\n "
            + "[" + PREFIX_DELETE_REQ + "DELETE_REQUEST]... "
            + "[" + PREFIX_DELETE_REQ_INDEX + "DELETE_REQUEST_INDEX]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_GUEST_SUCCESS = "Edited Guest: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_GUEST = "This guest already exists in the guestnote book.";

    public static final String MESSAGE_DUPLICATE_REQUEST = "Duplicate request detected";
    public static final String MESSAGE_REQUEST_NOT_FOUND = "This request does not exist in the guest";

    private final Index index;
    private final EditGuestDescriptor editGuestDescriptor;

    /**
     * @param index of the guest in the filtered guest list to edit
     * @param editGuestDescriptor details to edit the guest with
     */
    public EditCommand(Index index, EditGuestDescriptor editGuestDescriptor) {
        requireNonNull(index);
        requireNonNull(editGuestDescriptor);

        this.index = index;
        this.editGuestDescriptor = new EditGuestDescriptor(editGuestDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Guest> lastShownList = model.getFilteredGuestList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_GUEST_DISPLAYED_INDEX);
        }

        Guest guestToEdit = lastShownList.get(index.getZeroBased());
        Guest editedGuest;
        try {
            editedGuest = createEditedGuest(guestToEdit, editGuestDescriptor);
        } catch (DuplicateRequestException e) {
            throw new CommandException(MESSAGE_DUPLICATE_REQUEST + ": " + e.getErrorRequest());
        } catch (RequestNotFoundException e) {
            throw new CommandException(MESSAGE_REQUEST_NOT_FOUND + ": " + e.getErrorRequest());
        }

        if (!guestToEdit.isSameGuest(editedGuest) && model.hasGuest(editedGuest)) {
            throw new CommandException(MESSAGE_DUPLICATE_GUEST);
        }

        model.setGuest(guestToEdit, editedGuest);

        return new CommandResult(String.format(MESSAGE_EDIT_GUEST_SUCCESS, Messages.format(editedGuest)));
    }

    /**
     * Creates and returns a {@code Guest} with the details of {@code guestToEdit}
     * edited with {@code editGuestDescriptor}.
     */
    private static Guest createEditedGuest(Guest guestToEdit, EditGuestDescriptor editGuestDescriptor) {
        assert guestToEdit != null;

        Name updatedName = editGuestDescriptor.getName().orElse(guestToEdit.getName());
        Phone updatedPhone = editGuestDescriptor.getPhone().orElse(guestToEdit.getPhone().orElse(new Phone("")));
        Email updatedEmail = editGuestDescriptor.getEmail().orElse(guestToEdit.getEmail());
        RoomNumber updatedRoomNumber = editGuestDescriptor.getRoomNumber().orElse(guestToEdit.getRoomNumber());
        Status updatedStatus = guestToEdit.getStatus();

        // Extract existing requests and apply additions/removals
        UniqueRequestList updatedRequests = new UniqueRequestList();
        updatedRequests.setRequests(guestToEdit.getRequests());

        editGuestDescriptor.getRequestIndexesToDelete().ifPresent(indexes -> {
            List<Request> requests = updatedRequests.asUnmodifiableObservableList();
            for (Index idx : indexes) {
                int zeroBasedIdx = idx.getZeroBased();
                if (zeroBasedIdx < 0 || zeroBasedIdx >= requests.size()) {
                    throw new RequestNotFoundException("Index Number " + idx.getOneBased());
                }
            }
            List<Request> requestsToDeleteList = new ArrayList<>(
                    indexes.stream().map(index -> requests.get(index.getZeroBased())).toList());

            updatedRequests.removeAll(requestsToDeleteList);
        });
        editGuestDescriptor.getRequestsToDelete().ifPresent(updatedRequests::removeAll);
        editGuestDescriptor.getRequestsToAdd().ifPresent(updatedRequests::addAll);

        return new Guest(updatedName, updatedPhone, updatedEmail, updatedRoomNumber, updatedStatus, updatedRequests);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editGuestDescriptor.equals(otherEditCommand.editGuestDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editGuestDescriptor", editGuestDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the guest with. Each non-empty field value will replace the
     * corresponding field value of the guest.
     */
    public static class EditGuestDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private RoomNumber roomNumber;
        private UniqueRequestList requests;
        private List<Request> requestsToAdd;
        private List<Request> requestsToDelete;
        private List<Index> requestIndexesToDelete;

        public EditGuestDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code requests} is used internally.
         */
        public EditGuestDescriptor(EditGuestDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setRoomNumber(toCopy.roomNumber);
            setRequests(toCopy.requests);
            setRequestsToAdd(toCopy.requestsToAdd);
            setRequestsToDelete(toCopy.requestsToDelete);
            setRequestIndexesToDelete(toCopy.requestIndexesToDelete);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, roomNumber,
                    requestsToAdd, requestsToDelete, requestIndexesToDelete);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setRoomNumber(RoomNumber roomNumber) {
            this.roomNumber = roomNumber;
        }

        public Optional<RoomNumber> getRoomNumber() {
            return Optional.ofNullable(roomNumber);
        }

        public void setRequests(UniqueRequestList requests) {
            this.requests = (requests != null) ? new UniqueRequestList() : null;
            if (requests != null) {
                this.requests.setRequests(requests);
            }
        }

        public Optional<UniqueRequestList> getRequests() {
            return (requests != null)
                    ? Optional.of(requests)
                    : Optional.empty();
        }

        /**
         * Sets {@code requestsToAdd} to this object's {@code requestsToAdd}.
         * A defensive copy of {@code requestsToAdd} is used internally.
         */
        public void setRequestsToAdd(List<Request> requestsToAdd) {
            this.requestsToAdd = requestsToAdd;
        }

        /**
         * Sets {@code requestsToDelete} to this object's {@code requestsToDelete}.
         * A defensive copy of {@code requestsToDelete} is used internally.
         */
        public void setRequestsToDelete(List<Request> requestsToDelete) {
            this.requestsToDelete = requestsToDelete;
        }

        /**
         * Sets {@code requestIndexesToDelete} to this object's {@code requestIndexesToDelete}.
         * A defensive copy of {@code requestIndexesToDelete} is used internally.
         */
        public void setRequestIndexesToDelete(List<Index> requestIndexesToDelete) {
            this.requestIndexesToDelete = requestIndexesToDelete;
        }

        /**
         * Returns an unmodifiable request set for adding, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code requests} is null.
         */
        public Optional<List<Request>> getRequestsToAdd() {
            return (requestsToAdd != null)
                    ? Optional.of(requestsToAdd)
                    : Optional.empty();
        }

        /**
         * Returns an unmodifiable request set for deleting, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code requests} is null.
         */
        public Optional<List<Request>> getRequestsToDelete() {
            return (requestsToDelete != null)
                    ? Optional.of(requestsToDelete)
                    : Optional.empty();
        }

        /**
         * Returns an unmodifiable list of indexes for deleting, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code requestIndexesToDelete} is null.
         */
        public Optional<List<Index>> getRequestIndexesToDelete() {
            return (requestIndexesToDelete != null)
                    ? Optional.of(requestIndexesToDelete)
                    : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditGuestDescriptor)) {
                return false;
            }

            EditGuestDescriptor editGuestDescriptor = (EditGuestDescriptor) other;
            return Objects.equals(name, editGuestDescriptor.name)
                    && Objects.equals(phone, editGuestDescriptor.phone)
                    && Objects.equals(email, editGuestDescriptor.email)
                    && Objects.equals(roomNumber, editGuestDescriptor.roomNumber)
                    && Objects.equals(requestsToAdd, editGuestDescriptor.requestsToAdd)
                    && Objects.equals(requestsToDelete, editGuestDescriptor.requestsToDelete)
                    && Objects.equals(requestIndexesToDelete, editGuestDescriptor.requestIndexesToDelete);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("roomNumber", roomNumber)
                    .add("requestsToAdd", requestsToAdd)
                    .add("requestsToDelete", requestsToDelete)
                    .add("requestIndexesToDelete", requestIndexesToDelete)
                    .toString();
        }
    }
}
