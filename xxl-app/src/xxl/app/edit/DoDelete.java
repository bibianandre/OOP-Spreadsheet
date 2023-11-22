package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

/**
 * Delete command.
 */
class DoDelete extends Command<Spreadsheet> {

    DoDelete(Spreadsheet receiver) {
        super(Label.DELETE, receiver);
        addStringField("range", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        String range = stringField("range");
        try {
            _receiver.deleteContents(range);
        } catch (xxl.exceptions.InvalidCellRefException e) {
            throw new InvalidCellRangeException(e.getRef());
        } 
    }
}
