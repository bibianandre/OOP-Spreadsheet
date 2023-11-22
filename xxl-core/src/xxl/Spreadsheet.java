package xxl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import xxl.exceptions.ImportFileException;
import xxl.exceptions.InvalidCellRefException;
import xxl.exceptions.UnrecognizedEntryException;
import xxl.exceptions.InvalidDimensionException;
import xxl.exceptions.InvalidFunctionException;
import xxl.exceptions.InvalidFunctionArgsException;
import xxl.exceptions.UnknownEntryTypeException;

import xxl.User;
import xxl.Range;
import xxl.Cell;
import xxl.storage.StorageUnit;
import xxl.storage.CutBuffer;
import xxl.predicate.SearchPredicate;

/**
 * Class representing a spreadsheet.
 */
public class Spreadsheet implements Serializable {

    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202308312359L;

    /* The spreadsheet's number of lines. */
    private int _lines;

    /* The spreadsheet's number of columns. */
    private int _columns;

    /* The spreadsheet's cutbuffer */
    private CutBuffer _cutbuffer;

    /* The spreadsheet's cell storage unit. */
    private StorageUnit _mainstorage;

    /* Stores the spreadsheet's users, sorted by their user name. */
    private Map<String, User> _users = new TreeMap<String, User>();

    /* Were the spreadsheet contents changed since last saved or created? */
    private boolean _changed;

    /* Current entry from the file being imported. */
    private String _entry;
    
    /* Spreadsheet constructor. */
    public Spreadsheet() {
        _lines = 0;
        _columns = 0;
        _mainstorage = null;
        _entry = "";
        if (_users.get("root") == null) {
            _users.put("root", new User("root"));  
        } 
    }

    /** Sets spreadsheet's number of lines. 
     * @param lines the number of lines.
    */
    protected void setSpreadsheetLines(int lines) {
        this._lines = lines;
    }

    /** Sets spreadsheet's number of columns. 
     * @param columns the number of columns.
    */
    protected void setSpreadsheetColumns(int columns) {
        this._columns = columns;
    }

    /** Sets spreadsheet's remaining attributes: initializes the cell 
     * storage unit with the given dimensions and creates user association. 
     * @param lines the spreadsheet's number of lines.
     * @param columns the spreadsheet's numebr of columns.
     * 
     * The default spreadsheet user is of username "root"
     */ 
    protected void setSpreadsheetAtributtes(int lines, int columns) {
        this._mainstorage = new StorageUnit(lines, columns);
        this._cutbuffer = new CutBuffer();
        this._users.get("root").addNewSheet(this);
    }

    /**
     * Sets the _changed flag to the received value.
     *
     * @param changed The value for the _changed flag
     */
    public void setChanged(boolean changed) {
        _changed = changed;
    }

    /**
     * Turns the _changed flag to 'true' to indicate that the spreadsheet has 
     * changed since last saved or created.
     */
    public void changed() {
        setChanged(true);
    }

    /**
     * Current state of the spreadsheet since last saved/created.
     *
     * @return The current value of the _changed flag.
     */
    public boolean hasChanged() {
        return _changed;
    }

    /**
     * Insert specified content in specified range. Changes state of spreadsheet.
     *
     * @param rangeSpecification the cell(s) to be populated with content
     * @param contentSpecification the content to be stored
     * 
     * @throws InvalidCellRefException in case the given range specification is 
     *                                 not valid.
     * @throws InvalidFunctionException in case the content specification is
     *                                  of a function with an invalid name.
     * @throws InvalidFunctionArgsException in case the function content specification 
     *                                      has invalid arguments for its type.
     * @throws UnknownEntryTypeException in case the content specification is invalid for any
     *                                    content type.
     */
    public void insertContents(String rangeSpecification, String contentSpecification) throws InvalidCellRefException, 
    InvalidFunctionException, InvalidFunctionArgsException, UnknownEntryTypeException {  
        assertRange(rangeSpecification); 
        assertContent(contentSpecification);
        Range range = new Range(rangeSpecification);
        _mainstorage.insertContentsInRange(range, contentSpecification);
        this.changed();
    }

    /**
     * Deletes content from cells in specified range. Changes state of spreadsheet.
     *
     * @param rangeSpecification the cell range to be cleared of its content.
     * 
     * @throws InvalidCellRefException in case the given range specification is 
     *                                 not valid.
     */
    public void deleteContents(String rangeSpecification) throws InvalidCellRefException {
        assertRange(rangeSpecification); 
        Range range = new Range(rangeSpecification);
        _mainstorage.deleteContentsFromRange(range);
        this.changed();
    }

    /** Obtain the list of cells that correspond to the given adress range. 
     * 
     * @param input the given cell range
     * @return a list containing the cells from the specified range.
     * @throws InvalidCellRefException in case the cell reference or interval
     *                                 given as input are invalid or do not
     *                                 exist.
     */
    public List<Cell> showRange(String rangeSpecification) throws InvalidCellRefException {
        assertRange(rangeSpecification);
        Range range = new Range(rangeSpecification);
        return _mainstorage.getCellsFromRange(range);
    }

    /**
     * Copies a given range of cells to the cutbuffer.
     *
     * @param rangeSpecification the range of cells to be copied.
     * @throws InvalidCellRefException in case the given range is invalid.
     */
    public void copy(String rangeSpecification) throws InvalidCellRefException {
        List<Cell> toCopy = showRange(rangeSpecification);
        Range range = new Range(rangeSpecification);
        _cutbuffer.copyToCutBuffer(toCopy, range);
    }
 
    /**
     * Pastes the content from the cutbuffer into a given cell range.
     * Changes the state of the spreadsheet.
     *
     * @param rangeSpecification the target range of cells.
     * @throws InvalidCellRefException in case the given range is invalid.
     */
    public void paste(String rangeSpecification) throws InvalidCellRefException {
        assertRange(rangeSpecification);
        Range range = new Range(rangeSpecification);
        List<Cell> toPaste = _cutbuffer.pasteFromCutBuffer(range);
        _mainstorage.pasteContents(range, toPaste, _cutbuffer.getCutBufferType());
        this.changed();
    }

    /**
     * Copies the contents from a given cell range into the cutbuffer and then
     * deletes the contents from the given range. Changes state of the spreadsheet.
     *
     * @param rangeSpecification the target range of cells.
     * @throws InvalidCellRefException in case the given range is invalid.
     */
    public void cut(String rangeSpecification) throws InvalidCellRefException {
        copy(rangeSpecification);
        deleteContents(rangeSpecification);
        this.changed();
    }

    /**
     * Gets all the cells currently stored in the cutbuffer.  
     *
     * @return the cells on a {@link Collection}
     */
    public Collection<Cell> showCutBuffer() {
        return _cutbuffer.showCutBuffer();
    }

    /**
     * Gets all the cells that satisfy the given predicate.  
     *
     * @return the cells on a {@link Collection}
     */
    public Collection<Cell> mainSearch(SearchPredicate p) {
        return p.search(this._mainstorage);
    }

    /**
     * Read text input file and create/parse corresponding domain entities
     * (lines, columns and cell content).
     *
     * @param filename of the text input file
     * @throws UnrecognizedEntryException if some entry (line) is not correct
     * @throws IOException if an IO error occurred when processing the file.
     */
    protected void importFile(String filename) throws IOException, UnrecognizedEntryException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String s = reader.readLine();  
            String one = new String(s.getBytes(), "UTF-8");
            parseSheetDimensions(one);
            s = reader.readLine(); 
            String two = new String(s.getBytes(), "UTF-8");
            parseSheetDimensions(two);
            this.setSpreadsheetAtributtes(this._lines, this._columns);
            while ((s = reader.readLine()) != null) {
                String entry = new String(s.getBytes(), "UTF-8");
                setCurrentEntry(entry);
                String[] fields = entry.split("\\|"); //cells and contents.
                parseEntry(fields);
                
            }
            reader.close();
        }
    }

    /**
     * Sets the current file entry being parsed.
     *
     * @param currententry The current entry being parsed.
     */
    private void setCurrentEntry(String currententry) {
        _entry = currententry;
    }

    /**
     * Parses the given content in the given cell reference.
     * 
     * @param fields the fields containing the cell reference and the content,
     *               respectively.
     * @throws UnrecognizedEntryException in case the imported fields are incorrect.
     */
    private void parseEntry(String[] fields) throws UnrecognizedEntryException {
        String reference = fields[0];
        String content = fields[1];
        if (content != "") {
            try {
                insertContents(reference, content);
            } catch (UnknownEntryTypeException | InvalidCellRefException | 
            InvalidFunctionException | InvalidFunctionArgsException e) {
                throw new UnrecognizedEntryException(content, e);
            }
        }
    }
        

    /** Obtains entry from text file to set up the spreadsheet lines and columns.
     * 
     * @param entry the entry containing the parameter (line or columns)
     * @throws UnrecognizedEntryException if the entry is incorrect given 
     *                                    the context.
     */
    private void parseSheetDimensions(String entry) throws UnrecognizedEntryException {
        String fields[] = entry.split("=");
        try {
            switch (fields[0]) {
                case "linhas" -> parseLines(fields);
                case "colunas" -> parseColumns(fields);
                default -> throw new UnknownEntryTypeException(fields[0]);
            }
        } catch (UnknownEntryTypeException e) {
            throw new UnrecognizedEntryException(entry, e);
        }
        
    }

    /** Parses the number of lines of the current spreadsheet.
     * 
     * @param entry the first entry read from the file
     * @throws UnrecognizedEntryException if the entry format is unknown to the
     *                                    program.
     */
    private void parseLines(String[] fields) throws UnrecognizedEntryException {
        try {
            assertIntEntry(Integer.parseInt(fields[1]));
        } catch (NumberFormatException | InvalidDimensionException e) {
            throw new UnrecognizedEntryException(_entry, e);
        }
        this.setSpreadsheetLines(Integer.parseInt(fields[1]));
    }

    /** Parses the number of columns of the current spreadsheet.
     * 
     * @param entry the second entry read from the file
     * @throws UnrecognizedEntryException if the entry format is unknown to the
     *                                    program.
     */
    private void parseColumns(String[] fields) throws UnrecognizedEntryException {
        try {
            assertIntEntry(Integer.parseInt(fields[1]));
        } catch (NumberFormatException | InvalidDimensionException e) {
            throw new UnrecognizedEntryException(_entry, e);
        }
        this.setSpreadsheetColumns(Integer.parseInt(fields[1]));
    }

    /** Checks if the type integer entry (lines or columns) is a valid one.
     * 
     * @param entry the int entry (lines or columns)
     * @throws InvalidDimensionException if the entry happens to be of invalid type.
     */
    private void assertIntEntry(int number) throws InvalidDimensionException {
        if (number <= 0) {
            throw new InvalidDimensionException(number);
        }
    }

    /** Checks if the input for a new range is valid.
     * 
     * @param range the string input for a new range
     * @throws InvalidCellRefException if the input is an invalid cell reference
     *                                 or an invalid interval format.
     */
    private void assertRange(String range) throws InvalidCellRefException {
        if (range.contains(":")) {
            assertInterval(range);
        } else {
            assertCell(range);
        }
    }

    /** Checks if the single cell reference is a valid and/or non-null.
     * 
     * @param ref the cell reference
     * @throws InvalidCellRefException in case of invalid reference pattern or 
     *                                 non existent cell.
     */
    private void assertCell(String ref) throws InvalidCellRefException {
        if (!_mainstorage.containsCell(ref)) {
            throw new InvalidCellRefException(ref);
        }
    }

    /** Checks if the given interval is a valid one and non-null.
     * 
     * @param interval the intended row of cells
     * @throws InvalidCellRefException in case of invalid interval pattern or if
     * the resulting interval is not vertical or horizontal.
     */
    private void assertInterval(String interval) throws InvalidCellRefException {
        if (interval.length() < 7) {
            throw new InvalidCellRefException(interval);
        } 
        String[] holder = interval.split(":");
        if (holder.length != 2) {
            throw new InvalidCellRefException(interval);
        } else if (!_mainstorage.containsCell(holder[0]) || !_mainstorage.containsCell(holder[1])) {
            throw new InvalidCellRefException(interval);
        }
        String first[] = holder[0].split(";");
        String second[] = holder[1].split(";");
        if (!first[0].equals(second[0]) && !first[1].equals(second[1])) {
            throw new InvalidCellRefException(interval);
        }
    }

    /** Checks if the input content or imported content to be saved in a cell has 
     * correct format:
     * - if it starts with '=' -> Reference content or Function
     * - if it starts with ' -> String content
     * - if it has integer pattern -> Integer content
     * 
     * @param content the content to be asserted.
     * @throws InvalidFunctionException in case the given content is of an invalid
     *                                  function name or type. 
     * @throws InvalidFunctionArgsException in case the function arguments are invalid for 
     *                                      the given function type.
     * @throws UnknownEntryTypeException if the content does not match any of the valid 
     *                                   string formats.
     */
    private void assertContent(String content) throws InvalidFunctionException, InvalidFunctionArgsException,  
    UnknownEntryTypeException, InvalidCellRefException {
        if (!isString(content) && !isInteger(content) && !content.startsWith("=")) {
             throw new UnknownEntryTypeException(content);  
        } 
        assertReference(content);
        assertFunction(content);
        assertFunctionArgs(content);
    }

    private void assertReference(String reference) throws InvalidCellRefException {
        if (isReference(reference)) {
            String expression = reference.substring(1);
            assertCell(expression);
        }
    }

    /**
     * Assertion for a content input of type function. Asserts if the function
     * name is valid, and then asserts its arguments 
     *
     * @param function the string input for the function + arguments
     * @throws InvalidFunctionException in case the given function name is invalid
     * @throws InvalidFunctionArgsException in case the function arguments are invalid 
     *                                      for the function type.
     */
    private void assertFunction(String function) throws InvalidFunctionException {
        if (function.startsWith("=") && !isReference(function)) {
            if (!isBinaryFunction(function) && !isIntervalFunction(function)) {
                throw new InvalidFunctionException(function);
            }
        }
    }

    /**
     * Assertion for the given function arguments.
     *
     * @param function the string input for the function + arguments
     * @throws InvalidFunctionArgsException in case the given arguments are invalid
     *                                      for the given function.
     */
    private void assertFunctionArgs(String function) throws InvalidFunctionArgsException, InvalidCellRefException {
        String entry = function.replaceAll(".*\\(([^)]*)\\)", "$1");
        if (isBinaryFunction(function)) {
            String[] args = entry.split(",");
            if (args.length != 2) {
                throw new InvalidFunctionArgsException(function);
            }
            if (args[0].contains(";")) {
                assertCell(args[0]);
            } else if (!isInteger(args[0])) {
                throw new InvalidFunctionArgsException(args[0]);
            }
            if (args[1].contains(";")) {
                assertCell(args[1]);
            } else if (!isInteger(args[1])) {
                throw new InvalidFunctionArgsException(args[1]);
            }
        }
        if (isIntervalFunction(function)) {
            if (!entry.contains(":")) {
                throw new InvalidFunctionArgsException(entry);
            }
            assertInterval(entry);
        }
    }

    /**
     * Checks if the given input is candidate for string type content.
     *
     * @param input the string input for content string.
     * @return true if the string matches the pattern, false otherwise.
     */
    private boolean isString(String input) {
        if (input.startsWith("'")) {
            return true;       
        }
        return false;
    }

    /**
     * Checks if the given input is candidate for an integer type content.
     *
     * @param input the string input for content integer.
     * @return true if the string matches the pattern, false otherwise.
     */
    private boolean isInteger(String input) {
        if (input.matches("^[+-]?\\d+$")) {
            return true;       
        }
        return false;
    }

    /**
     * Checks if the given input is candidate for a cell reference type content.
     *
     * @param input the string input for content cell reference.
     * @return true if the string matches the pattern, false otherwise.
     */
    private boolean isReference(String input) {
        if (input.matches("^=\\d+;\\d+$")) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the given input is candidate for binary function type content.
     *
     * @param input the string input for binary function
     * @return true if the string matches the pattern, false otherwise.
     */
    private boolean isBinaryFunction(String input) {
        String functionToCheck = input.replaceAll("[^a-zA-Z]", "");
        if (!functionToCheck.startsWith("ADD") && !functionToCheck.startsWith("SUB") && 
            !functionToCheck.startsWith("DIV") && !functionToCheck.startsWith("MUL")) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the given input is candidate for an interval function type content.
     *
     * @param input the string input for an interval function
     * @return true if the string matches the pattern, false otherwise.
     */
    private boolean isIntervalFunction(String input) {
        String functionToCheck = input.replaceAll("[^a-zA-Z]", "");
        if (!functionToCheck.startsWith("PRODUCT") && !functionToCheck.startsWith("AVERAGE") &&
            !functionToCheck.startsWith("CONCAT") && !functionToCheck.startsWith("COALESCE")) {
            return false;
        }
        return true;
    }
}