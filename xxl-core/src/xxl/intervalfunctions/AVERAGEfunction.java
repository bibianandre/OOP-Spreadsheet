package xxl.intervalfunctions;

import java.io.Serial;

import java.util.List;
import java.util.ArrayList;

import xxl.Cell;
import xxl.storage.StorageUnit;

public class AVERAGEfunction extends IntervalFunction {
    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310222213L;

    private String _value;

    public AVERAGEfunction(String content, StorageUnit storage) {
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
            int sum = 0;
            List<Cell> cells = this.getIntervalCells();
            for (Cell cell : cells) {
                if (!cell.currentCellValue().startsWith("'") && !cell.currentCellValue().startsWith("#")) {
                    sum += Integer.parseInt(cell.currentCellValue());
                } else {
                    return "#VALUE";
                }
            }
            _value = String.valueOf(sum / cells.size());
        }
        
        return _value;
    }
}