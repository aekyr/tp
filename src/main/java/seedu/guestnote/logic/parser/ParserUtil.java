package seedu.guestnote.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import seedu.guestnote.commons.core.index.Index;
import seedu.guestnote.commons.util.StringUtil;
import seedu.guestnote.logic.parser.exceptions.ParseException;
import seedu.guestnote.model.guest.Email;
import seedu.guestnote.model.guest.Name;
import seedu.guestnote.model.guest.Phone;
import seedu.guestnote.model.guest.RoomNumber;
import seedu.guestnote.model.request.Request;
import seedu.guestnote.model.request.UniqueRequestList;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String roomNumber} into a {@code RoomNumber}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code roomNumber} is invalid.
     */
    public static RoomNumber parseRoomNumber(String roomNumber) throws ParseException {
        requireNonNull(roomNumber);
        String trimmedRoomNumber = roomNumber.trim();
        if (!RoomNumber.isValidRoomNumber(trimmedRoomNumber)) {
            throw new ParseException(RoomNumber.MESSAGE_CONSTRAINTS);
        }
        return new RoomNumber(trimmedRoomNumber);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String request} into a {@code Request}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code request} is invalid.
     */
    public static Request parseRequest(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Request.isValidTagName(trimmedTag)) {
            throw new ParseException(Request.MESSAGE_CONSTRAINTS);
        }
        return new Request(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code UniqueRequestList}.
     */
    public static UniqueRequestList parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        UniqueRequestList requestList = new UniqueRequestList();
        for (String tagName : tags) {
            requestList.add(parseRequest(tagName));
        }
        return requestList;
    }
}
