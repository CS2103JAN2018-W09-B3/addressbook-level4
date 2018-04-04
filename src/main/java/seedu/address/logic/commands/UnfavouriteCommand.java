package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.student.Student;
import seedu.address.model.student.exceptions.DuplicateStudentException;
import seedu.address.model.student.exceptions.StudentNotFoundException;
import seedu.address.model.student.miscellaneousinfo.ProfilePicturePath;
import seedu.address.model.tag.Tag;

//@@author yapni
/**
 * Remove a Student from favourites
 */
public class UnfavouriteCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "unfav";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Remove a student from favourites. "
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Student removed from favourites: %1$s";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists in the address book.";

    private final Index targetIndex;

    private Student studentToUnfavourite;
    private Student editedStudent;

    public UnfavouriteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireAllNonNull(studentToUnfavourite, editedStudent);

        try {
            model.updateStudent(studentToUnfavourite, editedStudent);
        } catch (StudentNotFoundException pnfe) {
            throw new AssertionError("The target student cannot be missing");
        } catch (DuplicateStudentException e) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, editedStudent));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Student> lastShownList = model.getFilteredStudentList();

        if (targetIndex.getZeroBased() >= lastShownList.size() || targetIndex.getZeroBased() < 0) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        studentToUnfavourite = lastShownList.get(targetIndex.getZeroBased());
        editedStudent = createEditedStudent(studentToUnfavourite);
    }

    /**
     * Create and return a copy of the target {@Code Student} to favourite with its' Favourite attribute set to false.
     */
    private static Student createEditedStudent(Student target) {

        Name name = target.getName();
        Phone phone = target.getPhone();
        Email email = target.getEmail();
        Address address = target.getAddress();
        Set<Tag> tags = target.getTags();

        ProgrammingLanguage programmingLanguage = target.getProgrammingLanguage();
        Favourite fav = new Favourite(false);
        Dashboard dashboard = target.getDashboard();
        ProfilePicturePath profilePicturePath = target.getProfilePicturePath();

        requireNonNull(target);

        return new StudentBuilder(target).withFavourite(false).build();
    }
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UnfavouriteCommand // instanceof handles null
                && ((UnfavouriteCommand) other).targetIndex == this.targetIndex);

    }
}
