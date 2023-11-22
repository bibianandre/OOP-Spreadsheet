package xxl.intervalfunctions;

import java.io.Serial;

import java.util.List;
import java.util.ArrayList;

import xxl.Cell;
import xxl.storage.StorageUnit;

public class CONCATfunction extends IntervalFunction {
    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310222218L;

    private String _value;

    public CONCATfunction(String content, StorageUnit storage) {
        super(content, storage);
        register();
        _value = compute();
    }

    private void register() {
        for (Cell cell : this.getIntervalCells()) {
            cell.registerObserver(this);
        }
    }

    public String compute() {
        if (getState()) {
            setState(false);
            String concat = "'";
            List<Cell> cells = this.getIntervalCells();
            for (Cell cell : cells) {
                if (cell.currentCellValue().startsWith("'")) {
                    concat += cell.currentCellValue().replace("'", "");
                } 
            }
            _value = concat;
        }
        
        return _value;
    }
}   
