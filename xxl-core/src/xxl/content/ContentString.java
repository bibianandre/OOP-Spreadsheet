package xxl.content;

import java.io.Serial;

public class ContentString extends Content {

    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310120940L;

    public ContentString(String content) {
        super(content);
    }

    public String getType() {
        return "string";
    }

    @Override
    public String refValue() {
        return this.toString();
    }
}
