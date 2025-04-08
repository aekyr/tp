package seedu.guestnote.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.guestnote.model.GuestNote;
import seedu.guestnote.model.Model;

/**
 * Clears the guestnote book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Guest list has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setGuestNote(new GuestNote());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
