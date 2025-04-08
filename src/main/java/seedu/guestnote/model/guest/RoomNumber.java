package seedu.guestnote.model.guest;

import static java.util.Objects.requireNonNull;
import static seedu.guestnote.commons.util.AppUtil.checkArgument;

/**
 * Represents a RoomNumber in the guestnote book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class RoomNumber {

    public static final String MESSAGE_CONSTRAINTS =
            "Room numbers should contain two two-digit numbers separated by a hyphen (eg. XX-YY)"
                    + ", where XX (floor number) and YY (room number) are both between 01 and 99, "
                    + "and it should not be blank";

    /*
     * The first character of the room number must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "^(?!00)\\d{2}-(?!00)\\d{2}$";

    public final String roomNumber;

    /**
     * Constructs a {@code RoomNumber}.
     *
     * @param roomNumber A valid room number.
     */
    public RoomNumber(String roomNumber) {
        requireNonNull(roomNumber);
        checkArgument(isValidRoomNumber(roomNumber), MESSAGE_CONSTRAINTS);
        this.roomNumber = roomNumber;
    }

    /**
     * Returns true if a given string is a valid room number.
     */
    public static boolean isValidRoomNumber(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return roomNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RoomNumber)) {
            return false;
        }

        RoomNumber otherRoomNumber = (RoomNumber) other;
        return roomNumber.equals(otherRoomNumber.roomNumber);
    }

    @Override
    public int hashCode() {
        return roomNumber.hashCode();
    }

}
