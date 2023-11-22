package xxl.app.search;

import java.util.List;
import java.util.ArrayList;

import pt.tecnico.uilib.Display;
import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;
import xxl.predicate.SearchFunction;

/**
 * Command for searching function names.
 */
class DoShowFunctions extends Command<Spreadsheet> {

    DoShowFunctions(Spreadsheet receiver) {
        super(Label.SEARCH_FUNCTIONS, receiver);
        addStringField("function", Prompt.searchFunction());
    }

    @Override
    protected final void execute() {
        String function = stringField("function");
        _display.popup(_receiver.mainSearch(new SearchFunction(function))); 
    }

}