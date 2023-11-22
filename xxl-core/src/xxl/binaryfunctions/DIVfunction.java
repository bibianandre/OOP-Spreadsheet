package xxl.binaryfunctions;

import java.io.Serial;
import xxl.storage.StorageUnit;

public class DIVfunction extends BinaryFunction {
    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310121520L;

    private String _value;
    private String _arg1;
    private String _arg2;

    public DIVfunction(String content, StorageUnit storage) {
        super(content, storage);
        register();
        _value = compute();
    }

    private void register() {
        _arg1 = firstArg();
        _arg2 = secondArg();
        if (this.getStorage().containsCell(firstArg())) {
            this.getStorage().getCell(firstArg()).registerObserver(this);
            _arg1 = this.getStorage().getCell(firstArg()).currentCellValue();
        }
        if (this.getStorage().containsCell(secondArg())) {
            this.getStorage().getCell(secondArg()).registerObserver(this);
            _arg2 = this.getStorage().getCell(secondArg()).currentCellValue();
        }
    }

    private void getFirstArgValue() {
        if (this.getStorage().containsCell(firstArg())) {
            _arg1 = this.getStorage().getCell(firstArg()).currentCellValue();
        }
    }

    private void getSecondArgValue() {
        if (this.getStorage().containsCell(secondArg())) {
            _arg2 = this.getStorage().getCell(secondArg()).currentCellValue();
        }
    }

    public String compute() {
        if (getState()) {
            setState(false);
            getFirstArgValue();
            getSecondArgValue();
            if (_arg1.equals("#VALUE") || _arg2.equals("#VALUE") || _arg1.startsWith("'") || _arg2.startsWith("'")) {
                return "#VALUE";
            }
            int sum =  Integer.parseInt(_arg1) / Integer.parseInt(_arg2);
            _value = String.valueOf(sum);
        }
        
        return _value;
    }
}