package xxl.content;

import java.io.Serial;

import xxl.Cell;
import xxl.storage.StorageUnit;
import xxl.util.Observer;

public class ContentRef extends Content implements Observer {
    
    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310120935L;

    private String _value;
    private Cell _cell;
    private boolean _updated;

    public ContentRef(String content, StorageUnit storage) {
        super(content, storage);
        _updated = false;
    }

    public String getType() {
        return "ref";
    }

    public void getValue(String content, StorageUnit storage) {
        _cell = storage.getCell(content.replace("=", ""));
        _cell.registerObserver(this);
        _value = _cell.currentCellValue();
    }

    public void update() {
        _updated = false;
    }

    @Override
    public String toString() {
        return refValue() + this.getContent();
    }

    @Override
    public String refValue() {
        if (_updated == false) {
            getValue(this.getContent(), this.getStorage());
            _updated = true;
        }
        return _value;
    }
}
