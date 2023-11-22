package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

/**
 * Paste command.
 */
class DoPaste extends Command<Spreadsheet> {

    DoPaste(Spreadsheet receiver) {
        super(Label.PASTE, receiver);
        addStringField("range", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        String range = stringField("range");
        try {
            _receiver.paste(range); 
        } catch (xxl.exceptions.InvalidCellRefException e) {
            throw new InvalidCellRangeException(e.getRef());
        }
    }
}
