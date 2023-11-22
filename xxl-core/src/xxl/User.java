package xxl;

import java.io.Serial;
import java.io.Serializable;

import java.util.List;
import java.util.LinkedList;

import xxl.Spreadsheet;

public class User implements Serializable {
    
    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310081943L;

    private String _name;
    private List<Spreadsheet> _usersheets;

    public User(String name) {
        _name = name;
        _usersheets = new LinkedList<Spreadsheet>();
    }
    
    public String getUserName() {
        return _name;
    }

    public void addNewSheet(Spreadsheet spreadsheet) {
        _usersheets.add(spreadsheet);
    }
}
