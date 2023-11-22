package xxl.app.search;

import java.util.List;
import java.util.ArrayList;

import pt.tecnico.uilib.Display;
import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;
import xxl.predicate.SearchValue;

/**
 * Command for searching content values.
 */
class DoShowValues extends Command<Spreadsheet> {

    DoShowValues(Spreadsheet receiver) {
        super(Label.SEARCH_VALUES, receiver);
        addStringField("value", Prompt.searchValue());
    }

    @Override
    protected final void execute() {
        String value = stringField("value");
        _display.popup(_receiver.mainSearch(new SearchValue(value)));
        
    }

}
