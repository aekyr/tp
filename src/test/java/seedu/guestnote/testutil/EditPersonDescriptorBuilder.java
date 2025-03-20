package seedu.guestnote.testutil;

import seedu.guestnote.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.guestnote.model.guest.Email;
import seedu.guestnote.model.guest.Guest;
import seedu.guestnote.model.guest.Name;
import seedu.guestnote.model.guest.Phone;
import seedu.guestnote.model.guest.RoomNumber;
import seedu.guestnote.model.request.Request;
import seedu.guestnote.model.request.UniqueRequestList;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code guest}'s details
     */
    public EditPersonDescriptorBuilder(Guest guest) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(guest.getName());
        descriptor.setPhone(guest.getPhone());
        descriptor.setEmail(guest.getEmail());
        descriptor.setRoomNumber(guest.getRoomNumber());
        UniqueRequestList copiedList = new UniqueRequestList();
        copiedList.setRequests(guest.getRequests());
        descriptor.setRequests(copiedList);
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code RoomNumber} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withRoomNumber(String roomNumber) {
        descriptor.setRoomNumber(new RoomNumber(roomNumber));
        return this;
    }

    /**
     * Parses the {@code requests} into a {@code Set<Request>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withRequestToAdd(String request) {
        descriptor.setRequestToAdd(new Request(request));
        return this;
    }

    /**
     * Parses the {@code requests} into a {@code Set<Request>} and sets them as requests **to remove**
     * in the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withRequestToDelete(String request) {
        descriptor.setRequestToDelete(new Request(request));
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
