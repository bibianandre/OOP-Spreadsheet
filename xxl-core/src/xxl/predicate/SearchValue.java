package xxl.predicate;

import java.util.List;
import java.util.Collection;

import xxl.Cell;
import xxl.storage.StorageUnit;

public class SearchValue implements SearchPredicate{

    private String _input;

    public SearchValue(String input) {
        _input = input;
    }
    /**
     * @param input the range to search
     * @return a map containing the cells within the given value
     */
    public Collection<Cell> search(StorageUnit storage) {
        return storage.getCellsWithValue(_input);
    }
}