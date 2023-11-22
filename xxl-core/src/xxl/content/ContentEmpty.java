package xxl.content;

import java.io.Serial;

public class ContentEmpty extends Content {

    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310120950L;
    
    public ContentEmpty() {
        super("");
    }

    public String getType() {
        return "empty";
    }

    @Override
    public String refValue() {
        return "#VALUE";
    }
}
