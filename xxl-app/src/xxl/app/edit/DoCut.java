package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

/**
 * Cut command.
 */
class DoCut extends Command<Spreadsheet> {

    DoCut(Spreadsheet receiver) {
        super(Label.CUT, receiver);
        addStringField("range", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        String range = stringField("range");
        try {
            _receiver.cut(range); 
        } catch (xxl.exceptions.InvalidCellRefException e) {
            throw new InvalidCellRangeException(e.getRef());
        }
    }
}
