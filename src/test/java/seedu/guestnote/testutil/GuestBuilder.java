package seedu.guestnote.testutil;

import seedu.guestnote.model.guest.Email;
import seedu.guestnote.model.guest.Guest;
import seedu.guestnote.model.guest.Name;
import seedu.guestnote.model.guest.Phone;
import seedu.guestnote.model.guest.RoomNumber;
import seedu.guestnote.model.guest.Status;
import seedu.guestnote.model.request.UniqueRequestList;
import seedu.guestnote.model.util.SampleDataUtil;

/**
 * A utility class to help with building Guest objects.
 */
public class GuestBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ROOM_NUMBER = "01-01";
    public static final Status DEFAULT_STATUS = Status.BOOKED;

    private Name name;
    private Phone phone;
    private Email email;
    private RoomNumber roomNumber;
    private Status status;
    private UniqueRequestList requests;

    /**
     * Creates a {@code GuestBuilder} with the default details.
     */
    public GuestBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        roomNumber = new RoomNumber(DEFAULT_ROOM_NUMBER);
        status = DEFAULT_STATUS;
        requests = new UniqueRequestList();
    }

    /**
     * Initializes the GuestBuilder with the data of {@code guestToCopy}.
     */
    public GuestBuilder(Guest guestToCopy) {
        name = guestToCopy.getName();
        phone = guestToCopy.getPhone().orElse(new Phone("")); // Default phone if absent
        email = guestToCopy.getEmail();
        roomNumber = guestToCopy.getRoomNumber();
        status = guestToCopy.getStatus();
        requests = new UniqueRequestList();
        requests.setRequests(guestToCopy.getRequests());
    }

    /**
     * Sets the {@code Name} of the {@code Guest} that we are building.
     */
    public GuestBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code requests} into a {@code Set<Request>} and set it to the {@code Guest} that we are building.
     */
    public GuestBuilder withRequests(String ... requests) {
        this.requests = SampleDataUtil.getRequestList(requests);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Guest} that we are building.
     */
    public GuestBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Guest} that we are building.
     */
    public GuestBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code RoomNumber} of the {@code Guest} that we are building.
     */
    public GuestBuilder withRoomNumber(String roomNumber) {
        this.roomNumber = new RoomNumber(roomNumber);
        return this;
    }

    /**
     * Sets the {@code Status} of the {@code Guest} that we are building.
     */
    public GuestBuilder withStatus(Status status) {
        this.status = status;
        return this;
    }

    public Guest build() {
        return new Guest(name, phone, email, roomNumber, status, requests);
    }

}
