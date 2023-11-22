package xxl;

import java.io.Serial;
import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;

public class Range implements Serializable {
    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310081942L;

    private List<String> _adressRange = new ArrayList<String>();
    private String _type = "";

    public Range() { }

    /** Default constructor for a given row */
    public Range(String row) {
        _adressRange = new ArrayList<String>();
        _type = "";
        this.createAdressRange(row);
    }

    public List<String> getAdressRange() {
        return this._adressRange;
    }

    public int getRangeSize() {
        return _adressRange.size();
    }

    public String getRangeType() {
        return this._type;
    }

    public String getFirst() {
        return _adressRange.get(0);
    }

    /** Returns a list of strings with the cell adresses of the given range,
     * or a list with a single cell if the range is of type a;a:a;a.
     */
    private void createAdressRange(String row) {
        String[] holder = row.split(":|;");
          
        int startLine = Integer.parseInt(holder[0]);
        int startColumn = Integer.parseInt(holder[1]);
        int endLine = startLine;
        int endColumn = startColumn;

        if (holder.length == 4) {
            endLine = Integer.parseInt(holder[2]);
            endColumn = Integer.parseInt(holder[3]);
        }

        if ((startLine == endLine)) {
            createHorizontalAdressRange(startColumn, endColumn, startLine);
        } else {
            createVerticalAdressRange(startLine, endLine, startColumn);
        }
    }

    private void createVerticalAdressRange(int startLine, int endLine, int startColumn) {
        String adress;
        if (startLine > endLine) {
            int aux = endLine;
            endLine = startLine;
            startLine = aux;
        }
        for (int i = startLine; i <= endLine; i++) {
            adress = i + ";" + startColumn;
            _adressRange.add(adress);
        }
        this._type = "VERTICAL";
    }

    private void createHorizontalAdressRange(int startColumn, int endColumn, int startLine) {
        String adress;
        if (startColumn > endColumn) {
            int aux = endColumn;
            endColumn = startColumn;
            startColumn = aux;
        } 
        for (int i = startColumn; i <= endColumn; i++) {
            adress = startLine + ";" + i;
            _adressRange.add(adress);
        }

        if (startColumn != endColumn) {
            this._type = "HORIZONTAL";
        }
    }

    /**
     * Creates an horizontal row from the given cell reference to the cell 
     * from the same line with given column.
     * 
     * @param currentRef the cell reference at the start of the row.
     * @param endParameter the column of the cell at the end of the row.
     * @return the expression of the row.
     */
    public String createHorizontalRowFromRef(String currentRef, int endParameter) {
        int line = getReferenceParameter(currentRef, "line");
        String endRef = line + ";" + endParameter;
        return currentRef + ":" + endRef;
    }

    /**
     * Creates a vertical row from the given cell reference to the cell 
     * from the same column with given line.
     * 
     * @param currentRef the cell reference at the start of the row.
     * @param endParameter the line of the cell at the end of the row.
     * @return the expression of the row.
     */
    public String createVerticalRowFromRef(String currentRef, int endParameter) {
        int column = getReferenceParameter(currentRef, "column");
        String endRef = endParameter + ";" + column;
        return currentRef + ":" + endRef;
    }

    /**
     * Obtains the given parameter from the given cell reference
     * 
     * @param ref the cell reference.
     * @param parameter the parameter to obtain ("line" or "column")
     * 
     * @return the obtained parameter as integer.
     */
    public int getReferenceParameter(String ref, String parameter) {
        String[] parameters = ref.split(";");
        int param = switch (parameter) {
            case "line" -> getLine(parameters[0]);
            default -> getColumn(parameters[1]);
        };
        return param;
    }

    private int getLine(String param) {
        int line = Integer.parseInt(param);
        return line;
    }

    private int getColumn(String param) {
        int column = Integer.parseInt(param);
        return column;
    }
}