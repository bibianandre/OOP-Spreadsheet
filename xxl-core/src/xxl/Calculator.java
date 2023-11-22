package xxl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Map;
import java.util.TreeMap;

import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.UnrecognizedEntryException;
import xxl.exceptions.InvalidDimensionException;
import xxl.User;

/**
 * Class representing a spreadsheet application.
 */
public class Calculator {

    /** The current spreadsheet. */
    private Spreadsheet _spreadsheet = null;

    /* The name of the current file storing the spreadsheet. */
    private String _filename;

    /* The users of the application. */
    private Map<String, User> _users =  new TreeMap<String, User>();

    /* Default constructor. Starts the application, but does not initialize a spreadsheet. 
     * Default active user is "root".
    */
    public Calculator() {
        if (_users.get("root") == null)
            _users.put("root", new User("root"));
        _filename = null;
    }

    /** Creates a spreadsheet from user input. 
     * @param lines the intended number of lines for the new spreadsheet.
     * @param columns the intended number of columns for the new spreadsheet
     * 
     * @throws InvalidDimensionsException if one of the parameters above happen 
     * to be invalid (negative, for example).
    */
    public Spreadsheet createSpreadsheet(int lines, int columns) throws InvalidDimensionException {
        assertNewSpreadsheet(lines, columns);
        Spreadsheet newsheet = new Spreadsheet();
        newsheet.setSpreadsheetLines(lines);
        newsheet.setSpreadsheetColumns(columns);
        newsheet.setSpreadsheetAtributtes(lines, columns);
        newsheet.setChanged(true);
        _spreadsheet = newsheet;
        return _spreadsheet;
    }

    /** Check if the input given dimensions for a new spreadsheet are valid. 
     * @param lines the spreadsheet to be number of lines
     * @param columns the spreadsheet to be number of columns
     * 
     * @throws InvalidDimensionsException if one of the parameters above happen to be invalid
     * (negative, for example).
    */
    private void assertNewSpreadsheet(int lines, int columns) throws InvalidDimensionException {
        if (lines <= 0) {
            throw new InvalidDimensionException(lines);
        } else if (columns <= 0) {
            throw new InvalidDimensionException(columns);
        }
    }

    /** @return The current spreadsheet */
    public Spreadsheet getSpreadsheet() {
        return _spreadsheet;
    }

    /**
     * Saves the serialized application's state into the file associated to the current spreadsheet.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
     * @throws MissingFileAssociationException if the current spreadsheet does not have a file.
     * @throws IOException if there is some error while serializing the state of the spreadsheet to disk.
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        if (_filename == null) {
            throw new MissingFileAssociationException();
        }

        if (_spreadsheet.hasChanged()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(
              new BufferedOutputStream(new FileOutputStream(_filename)))) {
                oos.writeObject(_spreadsheet);
            }
            _spreadsheet.setChanged(false);
        }
    }

    /**
     * Saves the serialized application's state into the specified file. The current spreadsheet is
     * associated to this file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current spreadsheet does not have a file.
     * @throws IOException if there is some error while serializing the state of the spreadsheet to disk.
     */
    public void saveAs(String filename) throws FileNotFoundException, IOException {
        _filename = filename;
        try {
            save();
        } catch (MissingFileAssociationException e) {
            // this shouldn't happen since we expect the filename to be valid
            // after we prompt the user
            e.printStackTrace();
        }
    }

    /**
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException {
        try (ObjectInputStream ois = new ObjectInputStream(
          new BufferedInputStream(new FileInputStream(filename)))) {
            _spreadsheet = (Spreadsheet) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new UnavailableFileException(filename);
        }
        _filename = filename;
        _spreadsheet.setChanged(false);
    }

    /**
     * Read text input file and create domain entities..
     *
     * @param filename name of the text input file
     * @throws ImportFileException
     */
    public void importFile(String filename) throws ImportFileException {
        if (_spreadsheet == null) {
            _spreadsheet = new Spreadsheet();
        }
        try {
            _spreadsheet.importFile(filename);
        } catch (IOException | UnrecognizedEntryException e) {
            throw new ImportFileException(filename, e);
        }
        _spreadsheet.changed();
    }
}