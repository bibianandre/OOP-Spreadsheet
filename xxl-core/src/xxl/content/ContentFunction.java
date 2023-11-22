package xxl.content;

import xxl.storage.StorageUnit;
import xxl.binaryfunctions.*;
import xxl.intervalfunctions.*;

import java.io.Serial;

public class ContentFunction extends Content {
    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310120949L;

    private String _function;

    public ContentFunction(String content, StorageUnit storage) {
        super(content, storage);
        _function = content;
    }

    public String getType() {
        return "function";
    }

    public String computeFunction() {
        String function = getFunctionName(); //obtain only the letters
        switch (function) {
            case "ADD"       : return add();
            case "SUB"       : return sub();
            case "MUL"       : return mul();
            case "DIV"       : return div();
            case "AVERAGE"   : return average();
            case "PRODUCT"   : return product();
            case "CONCAT"    : return concat();
            default          : return coalesce();
        }
    }

    public String add() {
        BinaryFunction _add = new ADDfunction(_function, super.getStorage());
        return _add.compute();
    }

    public String sub() {
        BinaryFunction _sub = new SUBfunction(_function, super.getStorage());
        return _sub.compute();
    }

    public String mul() {
        BinaryFunction _mul = new MULfunction(_function, super.getStorage());
        return _mul.compute();
    }

    public String div() {
        BinaryFunction _div = new DIVfunction(_function, super.getStorage());
        return _div.compute();
    }
    
    public String average() {
        IntervalFunction _average = new AVERAGEfunction(_function, super.getStorage());
        return _average.compute();
    }

    public String product() {
        IntervalFunction _product = new PRODUCTfunction(_function, super.getStorage());
        return _product.compute();
    }

    public String concat() {
        IntervalFunction _concat = new CONCATfunction(_function, super.getStorage());
        return _concat.compute();
    }

    public String coalesce() {
        IntervalFunction _coalesce = new COALESCEfunction(_function, super.getStorage());
        return _coalesce.compute();
    }

    @Override
    public String toString() {
        return refValue() + this.getContent();
    }

    @Override
    public String refValue() {
        return computeFunction();
    }

    protected void parseFunctionArgs(String content, StorageUnit storage) { }
} 