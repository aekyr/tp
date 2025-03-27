package seedu.guestnote.testutil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import seedu.guestnote.logic.commands.EditCommand.EditGuestDescriptor;
import seedu.guestnote.model.guest.Email;
import seedu.guestnote.model.guest.Guest;
import seedu.guestnote.model.guest.Name;
import seedu.guestnote.model.guest.Phone;
import seedu.guestnote.model.guest.RoomNumber;
import seedu.guestnote.model.request.Request;
import seedu.guestnote.model.request.UniqueRequestList;

/**
 * A utility class to help with building EditGuestDescriptor objects.
 */
public class EditGuestDescriptorBuilder {

    private EditGuestDescriptor descriptor;

    public EditGuestDescriptorBuilder() {
        descriptor = new EditGuestDescriptor();
    }

    public EditGuestDescriptorBuilder(EditGuestDescriptor descriptor) {
        this.descriptor = new EditGuestDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditGuestDescriptor} with fields containing {@code guest}'s details
     */
    public EditGuestDescriptorBuilder(Guest guest) {
        descriptor = new EditGuestDescriptor();
        descriptor.setName(guest.getName());
        descriptor.setPhone(guest.getPhone());
        descriptor.setEmail(guest.getEmail());
        descriptor.setRoomNumber(guest.getRoomNumber());
        UniqueRequestList copiedList = new UniqueRequestList();
        copiedList.setRequests(guest.getRequests());
        descriptor.setRequests(copiedList);
    }

    /**
     * Sets the {@code Name} of the {@code EditGuestDescriptor} that we are building.
     */
    public EditGuestDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditGuestDescriptor} that we are building.
     */
    public EditGuestDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditGuestDescriptor} that we are building.
     */
    public EditGuestDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code RoomNumber} of the {@code EditGuestDescriptor} that we are building.
     */
    public EditGuestDescriptorBuilder withRoomNumber(String roomNumber) {
        descriptor.setRoomNumber(new RoomNumber(roomNumber));
        return this;
    }

    /**
     * Parses the {@code requests} into a {@code Set<Request>} and sets them as requests **to add**
     * in the {@code EditGuestDescriptor} that we are building.
     */
    public EditGuestDescriptorBuilder withRequestsToAdd(String... requests) {
        List<Request> requestList = new ArrayList<>(Stream.of(requests).map(Request::new).toList());
        descriptor.setRequestsToAdd(requestList);
        return this;
    }

    /**
     * Parses the {@code requests} into a {@code Set<Request>} and sets them as requests **to remove**
     * in the {@code EditGuestDescriptor} that we are building.
     */
    public EditGuestDescriptorBuilder withRequestsToDelete(String... requests) {
        List<Request> requestList = new ArrayList<>(Stream.of(requests).map(Request::new).toList());
        descriptor.setRequestsToDelete(requestList);
        return this;
    }

    public EditGuestDescriptor build() {
        return descriptor;
    }
}
