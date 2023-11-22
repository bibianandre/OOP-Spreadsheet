package xxl.storage;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import xxl.Cell;
import xxl.Range;

public class CutBuffer extends Storage {
    
    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310232344L;
    
    private Range _range;
    private List<Cell> _cells;

    public CutBuffer() {
        super();
        _cells = new ArrayList<Cell>();
        _range = new Range();
    }

    protected void initialize() {
        new CutBuffer();
    }

    public String getCutBufferType() {
        return _range.getRangeType();
    }

    public void copyToCutBuffer(List<Cell> cells, Range range) {
        initialize();
        switch(range.getRangeType()) {
            case "HORIZONTAL" -> copyHorizontal(cells);
            case "VERTICAL" -> copyVertical(cells);
            default -> copyHorizontal(cells); //single cell
        }
    }

    private void copyHorizontal(List<Cell> cells) {
        int i = 1;
        String firstRef = "1;" + i;
        String lastRef = firstRef;
        for (Cell element : cells) {
            Cell cell = new Cell(lastRef);
            cell.setContent(element.getContent());
            putCell(cell);
            i++;
            lastRef = "1;" + i;
        }
        _range = new Range(firstRef + ":" + lastRef);
    }

    private void copyVertical(List<Cell> cells) {
        int i = 1;
        String firstRef = i + ";1" ;
        String lastRef = firstRef;
        for (Cell element : cells) {
            Cell cell = new Cell(lastRef);
            cell.setContent(element.getContent());
            putCell(cell);
            i++;
            lastRef = i + ";1";
        }
        _range = new Range(firstRef + ":" + lastRef);
    }

    public List<Cell> pasteFromCutBuffer(Range range) {
        if (canPaste(range)) {
            return _cells;
        }
        return null;
    }

    private boolean canPaste(Range range) {
        if (_cells.size() < 1) {
            return false;
        }
        if (range.getRangeSize() == 1) {
            return true;
        }
        if ((range.getRangeType() == getCutBufferType()) && (range.getRangeSize() == _cells.size())) {
            return true;
        }
        return false;
    }

    protected void putCell(Cell cell) {
        _cells.add(cell);
    }

    public List<Cell> showCutBuffer() {
        return _cells;
    }
}