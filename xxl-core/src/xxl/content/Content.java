package xxl.content;

import java.io.Serial;
import java.io.Serializable;

import xxl.storage.StorageUnit;

public abstract class Content implements Serializable {

    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310081946L;

    private String _content;
    private StorageUnit _storage;

    public Content(String content) {
        _content = content;
    }

    public Content(String content, StorageUnit storage) {
        _content = content;
        _storage = storage;
    }
    
    public StorageUnit getStorage() {
        return _storage;
    }

    public String getContent() {
        return _content;
    }

    public String getFunctionName() {
        return _content.replaceAll("[^a-zA-Z]", "");
    }

    public String getFunctionArguments() {
        return _content.replaceAll(".*\\(([^)]*)\\)", "$1");
    }

    @Override
    public String toString() {
        return _content;
    }
    
    public abstract String refValue();
    
    public abstract String getType();

}

