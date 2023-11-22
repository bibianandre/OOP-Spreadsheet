package xxl.intervalfunctions;

import java.io.Serial;
import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;

import xxl.Cell;
import xxl.Range;
import xxl.util.Observer;
import xxl.storage.StorageUnit;
import xxl.content.ContentFunction;

public abstract class IntervalFunction extends ContentFunction implements Observer {
    
    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310121437L;

    private List<Cell> _rangeCells;
    private boolean _toUpdate = false;

    public IntervalFunction(String content, StorageUnit storage) {
        super(content, storage);
        _rangeCells = new ArrayList<Cell>();
        parseFunctionArgs(content, storage);
        update();
    }

    protected List<Cell> getIntervalCells() {
        return this._rangeCells;
    }

    protected boolean getState() {
        return _toUpdate;
    }

    protected void setState(boolean state) {
        _toUpdate = state;
    }

    @Override
    protected void parseFunctionArgs(String content, StorageUnit storage) {
        Range range = new Range(this.getFunctionArguments());
        _rangeCells = storage.getCellsFromRange(range); 
    }

    public abstract String compute();

    public void update() {
        if (_toUpdate == false) {
            setState(true);;
        }
    }
}