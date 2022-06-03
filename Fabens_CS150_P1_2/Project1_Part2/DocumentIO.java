import java.io.*;
import java.util.Scanner;
/**
 * File IO for document
 *
 * @author Peter Fabens
 * @version 2/20/2022
 */
public class DocumentIO implements DocumentIOInterface
{
    private Scanner doc_in;
    private File doc_file;
    private PrintStream doc_out;
    private String file_name;
    
    /**
     * Initializes DocumentIO with everything empty.
     */
    public DocumentIO()
    {
        doc_file    = null;
        doc_out     = null;
        doc_in      = null;
        file_name   = "";
    }
    
    /**
     * Opens file for interaction
     * 
     * @param file_name filename of file to open 
     * 
     * @throws IOException Throws various types of IOException common to opening files.
     */
    public void open_file(String file_name) throws IOException
    {
        doc_file = new File (file_name); // opening a file just changes the file reference, it doesn't truly open anything.
        this.file_name = file_name;
    }
    
    /**
     * Closes file
     * 
     * @throws IOException Can throw IOException for reasons common to opening files.
     */
    public void close_file() throws IOException
    {
        if (doc_out != null) // if there's a doc printer or a scanner, try to close it and remove the reference
        {
            doc_out.close();
            doc_out = null;
        }
        if (doc_in != null)
        {
            doc_in.close();
            doc_in = null;
        }
    }
    
    /**
     * Returns the next line in the file
     * 
     * @throws IOException Throws IOExceptions for normal file access reasons.
     * @throws NullPointerException if attempting to scan lines from a file that hasn't been identified.
     */
    public String read_line() throws IOException, NullPointerException
    {
        if (doc_in == null) // if the scanner hasn't been initialized, do that.
            doc_in = new Scanner (doc_file); // will throw nullpointer if there's no file to scan
        return doc_in.nextLine();
    }
    
    /**
     * Returns whether the file being read has more lines to read
     * 
     * @throws IOException Throws IOExceptions for normal file access reasons.
     */
    public boolean has_more_lines() throws IOException{
        if (doc_in == null){
            doc_in  = new Scanner (doc_file); // if scanner hasn't been created yet, create it.
        }
        return doc_in.hasNextLine();
    }
    
    /**
     * Writes the input to the current file, entirely overwriting whatever was there before. Also closes any scanners on the file, since they will be old and outdated. This means scanners will reset if they are invoked afterwards.
     * 
     * @throws IOException Throws IOExceptions for normal file access reasons.
     */
    public void write_file(String text) throws IOException
    {
        PrintStream doc_out = new PrintStream(doc_file); // create a new printstream on the file
        doc_out.print(text); // print input to the printstream
        close_file(); // close printstream, saving the changes and overwriting whatever was previously in it. we also want to close any scanners so that they aren't reading old data.
    }
    
    /**
     * Return name of currently open file
     */
    public String file_name(){
        return file_name;
    }
    
    /**
     * Returns whether a given file exists
     * 
     * @param file filename to check
     */
    public boolean file_exists(String file) {
        return (new File(file)).exists();
    }
}
