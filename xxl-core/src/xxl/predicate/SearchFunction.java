package xxl.predicate;

import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import xxl.Cell;
import xxl.storage.StorageUnit;

public class SearchFunction implements SearchPredicate{

    private String _input;

    private class CellsComparator implements Comparator<Cell> {
        @Override
        public int compare(Cell cell1, Cell cell2) {
            String function1 = cell1.getFunctionNameInCell();
            String function2 = cell2.getFunctionNameInCell();
            return function1.compareTo(function2);
        }
    }

    public SearchFunction(String input) {
        _input = input;
    }

    /**
     * @param input the range to search
     * @return a map containing the cells with the given function segment
     */
    public Collection<Cell> search(StorageUnit storage) {
        Collection<Cell> cells = storage.getCellsWithFunction(_input);
        List<Cell> sortedCells = new ArrayList<>(cells);
        Collections.sort(sortedCells, new CellsComparator());
        return sortedCells;
    }
}
