package xxl.content;

import java.io.Serial;

public class ContentInteger extends Content {

    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310222225L;

    public ContentInteger(String content) {
        super(content);
    }

    public String getType() {
        return "integer";
    }
    
    @Override
    public String refValue() {
        return this.toString();
    }
}