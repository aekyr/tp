package seedu.guestnote.logic.parser;

import static seedu.guestnote.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.guestnote.logic.commands.CommandTestUtil.ADD_REQUEST_DESC_FRIEND;
import static seedu.guestnote.logic.commands.CommandTestUtil.ADD_REQUEST_DESC_HUSBAND;
import static seedu.guestnote.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.guestnote.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.guestnote.logic.commands.CommandTestUtil.INVALID_ADD_REQUEST_DESC;
import static seedu.guestnote.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.guestnote.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.guestnote.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.guestnote.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.guestnote.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.guestnote.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.guestnote.logic.commands.CommandTestUtil.REQUEST_DESC_FRIEND;
import static seedu.guestnote.logic.commands.CommandTestUtil.REQUEST_DESC_HUSBAND;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_REQUEST_FRIEND;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_REQUEST_HUSBAND;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_ADD_REQ;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.guestnote.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.guestnote.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.guestnote.testutil.TypicalIndexes.INDEX_FIRST_GUEST;
import static seedu.guestnote.testutil.TypicalIndexes.INDEX_SECOND_GUEST;
import static seedu.guestnote.testutil.TypicalIndexes.INDEX_THIRD_GUEST;

import org.junit.jupiter.api.Test;

import seedu.guestnote.commons.core.index.Index;
import seedu.guestnote.logic.Messages;
import seedu.guestnote.logic.commands.EditCommand;
import seedu.guestnote.logic.commands.EditCommand.EditGuestDescriptor;
import seedu.guestnote.model.guest.Email;
import seedu.guestnote.model.guest.Name;
import seedu.guestnote.model.guest.Phone;
import seedu.guestnote.model.request.Request;
import seedu.guestnote.testutil.EditGuestDescriptorBuilder;

public class EditCommandParserTest {
    private static final String REQUEST_ADD_EMPTY = " " + PREFIX_ADD_REQ;
    private static final String REQUEST_DELETE_EMPTY = " " + PREFIX_ADD_REQ;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, "1" + INVALID_ADD_REQUEST_DESC, Request.MESSAGE_CONSTRAINTS); // invalid request

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // parsing adding an empty request results in error
        assertParseFailure(parser,
                "1" + REQUEST_ADD_EMPTY, Request.MESSAGE_CONSTRAINTS);

        // parsing adding an empty request results in error
        assertParseFailure(parser,
                "1" + REQUEST_DELETE_EMPTY, Request.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser,
                "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_GUEST;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + ADD_REQUEST_DESC_HUSBAND
                + EMAIL_DESC_AMY + NAME_DESC_AMY;

        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY)
                .withRequestsToAdd(VALID_REQUEST_HUSBAND).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_GUEST;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_GUEST;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditGuestDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditGuestDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // requests
        userInput = targetIndex.getOneBased() + ADD_REQUEST_DESC_FRIEND;
        descriptor = new EditGuestDescriptorBuilder().withRequestsToAdd(VALID_REQUEST_FRIEND).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonRequestValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_GUEST;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // mulltiple valid fields repeated
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + REQUEST_DESC_FRIEND + PHONE_DESC_AMY + EMAIL_DESC_AMY + REQUEST_DESC_FRIEND
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + REQUEST_DESC_HUSBAND;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL));
    }

}
