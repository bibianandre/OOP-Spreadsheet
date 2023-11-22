package xxl.app.edit;

import pt.tecnico.uilib.Display;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

/**
 * Class for showing ranges.
 */
class DoShow extends Command<Spreadsheet> {

    DoShow(Spreadsheet receiver) {
        super(Label.SHOW, receiver);
        addStringField("range", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        String range = stringField("range");
        try {
            _display.popup(_receiver.showRange(range)); 
        } catch (xxl.exceptions.InvalidCellRefException e) {
            throw new InvalidCellRangeException(e.getRef());
        }
    }
}