package xxl;

import java.io.Serial;
import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;

import xxl.util.Observer;
import xxl.util.Subject;

import xxl.content.*;
import xxl.storage.StorageUnit;

public class Cell implements Serializable, Subject {
    
    /* Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202310161816L; 

    private final String _ref;
    private Content _content;
    private List<Observer> _observers; 

    public Cell(String ref) {
        _ref = ref;
	    _content = new ContentEmpty();
        _observers = new ArrayList<Observer>(); 
    }
    
    public String getRef() {
        return _ref;
    }
  
    public Content getContent() {
        return _content;
    }

    public void setContent(Content content) {
        _content = content;
    }

    public void registerObserver(Observer o) { 
        _observers.add(o); 
    }
         
    public void removeObserver(Observer o) {
        int i = _observers.indexOf(o);
        if (i >= 0) { _observers.remove(i); }
    }
          
    public void notifyObservers() {
        for (Observer observer: _observers) {
            observer.update();
        }
    }

    private void cellChanged() {
        notifyObservers();
    }
  
    private String assertContentType(String stringToCheck) {
        if (stringToCheck.startsWith("'")) {
            return "string";
        } 
        if (stringToCheck.matches("^[+-]?\\d+$")) {
            return "integer";
        } 
        if (stringToCheck.matches("^=\\d+;\\d+$")) {
            return "ref";
        } 
        return "function";
    }

    public void insertContent(String content, StorageUnit storage) {

        String type = assertContentType(content); 

	    this._content = switch (type) {
	        case "integer"-> new ContentInteger(content); 
            case "string"-> new ContentString(content); 
            case "ref" -> new ContentRef(content, storage); 
            case "function" -> new ContentFunction(content, storage); 
            default ->  new ContentEmpty(); 
	    };
        cellChanged();
    }

    public void clearContent() {
        _content = new ContentEmpty();
        cellChanged();
    }

    public String currentCellValue() {
        return _content.refValue();
    }

    public String getFunctionNameInCell() {
        return _content.getFunctionName();
    }

    public boolean hasFunctionSegment(String segment) {
        String function = getFunctionNameInCell();
        if (function.contains(segment)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this._ref + "|" + _content.toString();
    }
}