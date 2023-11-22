package xxl.storage;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.stream.Collectors;

import xxl.Cell;
import xxl.Range;

public class StorageUnit extends Storage {
    
    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310081944L;

    private Map<String, Cell> _cells;
    private int _maxLine = 0;
    private int _maxColumn = 0;

    public StorageUnit(int lines, int columns) {
        super();
        _maxLine = lines;
        _maxColumn = columns;
        _cells = new TreeMap<String, Cell>();
        initialize();
    }

    public int getMaxLine() {
        return _maxLine;
    }

    public int getMaxColumn() {
        return _maxColumn;
    }

    protected void initialize() {
        String ref;
        for (int i = 1; i <= _maxLine; i++) {
            for (int j = 1; j <= _maxColumn; j++) {
                ref = i + ";" + j;
                Cell cell = new Cell(ref);
                putCell(cell);
            }
        }
    }

    public boolean containsCell(String ref) {
        if (getCell(ref) != null) {
            return true;
        }
        return false;
    }

    public List<Cell> getCellsFromRange(Range range) {
        List<Cell> rangeCells = new ArrayList<Cell>();
        List<String> rangeAdresses = range.getAdressRange();
        for (String adress : rangeAdresses) {
            Cell cell = getCell(adress);
            rangeCells.add(cell);
        }
        return rangeCells;
    }
    
    public Collection<Cell> getCellsWithValue(String value) {
        return this._cells.values().stream()
                        .filter(c -> c.currentCellValue().equals(value))
                        .collect(Collectors.toList());
    }

    public Collection<Cell> getCellsWithFunction(String function) {
        return this._cells.values().stream()
                        .filter(c -> c.hasFunctionSegment(function))
                        .collect(Collectors.toList());
    }

    public void insertContentsInRange(Range range, String content) {
        List<Cell> rangeCells = getCellsFromRange(range);
        for (Cell cell : rangeCells) {
            cell.insertContent(content, this);
            putCell(cell);
        }
    }

    public void deleteContentsFromRange(Range range) {
        List<Cell> rangeCells = getCellsFromRange(range);
        for (Cell cell : rangeCells) {
            cell.clearContent();
            putCell(cell);
        }
    }

    public void pasteContents(Range range, List<Cell> contents, String rangeType) {
        if (rangeType == "VERTICAL") {
            pasteContentsInVerticalRange(range, contents);
        } else {
            pasteContentsInHorizontalRange(range, contents);
        }
    }

    private void pasteContentsInHorizontalRange(Range range, List<Cell> contents) {
        String first = range.getFirst();
        int column = range.getReferenceParameter(first, "column");
        int items = contents.size();
        int lastColumn = items + column - 1;
        if (lastColumn > _maxColumn) {
            lastColumn = _maxColumn;
        }
        String row = range.createHorizontalRowFromRef(first, lastColumn);
        Range newRange = new Range(row);
        pasteContentsInRange(newRange, contents);
    }

    private void pasteContentsInVerticalRange(Range range, List<Cell> contents) {
        String first = range.getFirst();
        int line = range.getReferenceParameter(first, "line");
        int items = contents.size();
        int lastLine = items + line - 1;
        if (lastLine > _maxLine) {
            lastLine = _maxLine;
        }
        String row = range.createVerticalRowFromRef(first, lastLine);
        Range newRange = new Range(row);
        pasteContentsInRange(newRange, contents);
    }

    private void pasteContentsInRange(Range range, List<Cell> contents) {
        List<Cell> rangeCells = getCellsFromRange(range);
        int i = 0;
        for (Cell cell : rangeCells) {
            cell.setContent(contents.get(i).getContent());
            putCell(cell);
            i++;
        }
    }

    public Cell getCell(String ref) {
        Cell cell = _cells.get(ref);
        return cell;
    }

     /** Overwrites an existing cell, or puts a new cell in the storage
     * @param ref the cell reference
     * @param cell the cell with the given reference to be replaced.
     */
    protected void putCell(Cell cell) {
        _cells.put(cell.getRef(), cell);
    }
}
