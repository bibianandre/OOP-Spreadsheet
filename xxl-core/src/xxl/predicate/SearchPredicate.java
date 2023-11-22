package xxl.predicate;

import java.util.Collection;

import xxl.Cell;
import xxl.storage.StorageUnit;

public interface SearchPredicate {

    /**
     * @param input the predicate to search
     * @return a map containing the cells whose parameters (value, reference
     *         or function) match the search.
     */
    public Collection<Cell> search(StorageUnit storage);
}
