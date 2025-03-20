package seedu.guestnote.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_REQUEST;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_ROOMNUMBER;

import seedu.guestnote.commons.util.ToStringBuilder;
import seedu.guestnote.logic.Messages;
import seedu.guestnote.logic.commands.exceptions.CommandException;
import seedu.guestnote.model.Model;
import seedu.guestnote.model.guest.Guest;

/**
 * Adds a guest to the guestnote book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a guest to the guestnote book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ROOMNUMBER + "ROOMNUMBER "
            + "[" + PREFIX_REQUEST + "REQUEST]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ROOMNUMBER + "123 "
            + PREFIX_REQUEST + "friends "
            + PREFIX_REQUEST + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New guest added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This guest already exists in the guestnote book";

    private final Guest toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Guest}
     */
    public AddCommand(Guest guest) {
        requireNonNull(guest);
        toAdd = guest;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
