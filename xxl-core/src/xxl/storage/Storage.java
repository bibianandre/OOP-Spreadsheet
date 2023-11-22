package xxl.storage;

import java.io.Serial;
import java.io.Serializable;

import xxl.Cell;

public abstract class Storage implements Serializable {
    
    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310212024L;

    protected abstract void putCell(Cell cell);

    protected abstract void initialize();

}