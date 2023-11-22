package xxl.binaryfunctions;

import java.io.Serial;
import java.io.Serializable;
import xxl.content.ContentFunction;
import xxl.storage.StorageUnit;
import xxl.util.Observer;

public abstract class BinaryFunction extends ContentFunction implements Observer {
    
    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310121438L;

    private boolean _toUpdate;
    private String _arg1;
    private String _arg2;

    public BinaryFunction(String content, StorageUnit storage) {
        super(content, storage);
        parseFunctionArgs(content, storage);
        update();
    }

    protected String firstArg() {
        return _arg1;
    }

    protected String secondArg() {
        return _arg2;
    }

    protected boolean getState() {
        return _toUpdate;
    }

    protected void setState(boolean state) {
        _toUpdate = state;
    }

    @Override
    protected void parseFunctionArgs(String content, StorageUnit storage) {
        String[] args = this.getFunctionArguments().split(",");
        _arg1 = args[0];
        _arg2 = args[1];
    }

    public abstract String compute();

    public void update() {
        setState(true);
    }

}

