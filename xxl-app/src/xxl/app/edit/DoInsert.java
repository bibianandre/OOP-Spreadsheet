package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

/**
 * Class for inserting data.
 */
class DoInsert extends Command<Spreadsheet> {

    DoInsert(Spreadsheet receiver) {
        super(Label.INSERT, receiver);
        addStringField("range", Prompt.address());
        addStringField("content", Prompt.content());
    }

    @Override
    protected final void execute() throws CommandException {
        String range = stringField("range");
        String content = stringField("content");
        try {
            _receiver.insertContents(range, content);
        } catch (xxl.exceptions.InvalidCellRefException e) {
            throw new InvalidCellRangeException(e.getRef());
        } catch (xxl.exceptions.InvalidFunctionException e) {
            throw new UnknownFunctionException(e.getFunction());
        } catch (xxl.exceptions.InvalidFunctionArgsException | xxl.exceptions.UnknownEntryTypeException e) {
            e.printStackTrace();
        } 
    }
}

