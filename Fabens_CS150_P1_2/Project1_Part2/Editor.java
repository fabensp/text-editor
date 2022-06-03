import java.util.Scanner;
import java.io.File;
import java.io.IOException;
/**
 * The face of the data structure. Allows a user to interact with the structure from the command line.
 *
 * @author Peter Fabens
 * @version 3/4/2022
 */
public class Editor
{
    private Document doc; // document to hold all the data
    private Scanner s_in; // scanner for inputs
    private static boolean quit; // should the editor quit out
    
    /**
     * Main method. Handles sending commands to the editor.
     * 
     * @param args  Controls the type of buffer to use in the structure. Options are "linked" and "gap".
     *              Defaults to "linked" if input is invalid or not given.
     */
    public static void main(String[] args)
    {
        Editor e = (args.length == 1) ? new Editor (args[0]) : new Editor ("linked"); // if args exist, pass them
        System.out.println("Welcome to Texty! A word processor for the modern era.");
        System.out.println("('h' for a list of commands!)");
        System.out.print("\n>");
        while (!quit && e.s_in.hasNextLine()) // 'quit' boolean controls whether to keep waiting for cmds
        {
            if (!e.execute(e.s_in.nextLine())) // run the cmd, if it doesnt work spit out error
            {
                System.out.println("Command not recognized! Make sure your syntax is right.");
                System.out.println("Make sure to put a space between command and argument.");
                System.out.print("\n>");
            }
        }
    }
    
    /**
     * Default constructor.
     * 
     * @param type type of text buffer to use.
     */
    public Editor (String type)
    {
        doc = new Document(type);
        s_in = new Scanner(System.in);
        quit = false;
    }
    
    /**
     * Kills the System input scanner, for unit testing purposes.
     */
    public void kill_scanner() { s_in = null; }
    
    /**
     * Parse the command into command keyword and arguments, and then do different things for each command
     * 
     * @param input complete user input
     */
    public boolean execute (String input)
    {
        String cm;
        String args;
        if (input.indexOf(' ') > -1) // if there is a space in the input,
        {
            cm = input.substring(0, input.indexOf(' ')); // everything before the first space is the command
            args = input.substring(input.indexOf(' ') + 1); // and everything after the first space is the arguments
        } else {
            cm = input; // otherwise, the input is just a command on it's own
            args = "";
        }
        
        switch (cm)
        {
            case "h": // help
                System.out.println("====================");
                try {
                    Scanner s_menu = new Scanner (new File ("menu.txt")); // pull text out of menu file
                    while (s_menu.hasNextLine()) // print it all out
                        System.out.println(s_menu.nextLine());
                } catch (IOException e) {
                    System.out.println("Oops! Something went wrong finding the list."); // or maybe not, if something goes bad
                }
                break;
            
            case "q": // quit
                System.out.println("Bye!");
                quit = true;
                break;
                
            case "o": // open file
                while (args.equals("")) // if the user doesn't provide args
                {
                    System.out.print("args: ");
                    try {
                        args = s_in.next(); // try to ask for them
                        s_in.nextLine();
                    } catch (NullPointerException e) {
                        args = "testing"; // if there's no scanner, just default to "testing
                    }
                }
                System.out.println("====================");
                try {
                    String filename = format_filename(args);
                    if (!doc.file_exists(filename)) throw new IOException("File Not Found");
                    doc.load_file(filename); // load file
                    System.out.printf("File '%s' successfully opened.\n", filename);
                    for (int count = 0; count < doc.line_count(); count++) // print out the doc
                    {
                        System.out.printf("%2d| %s\n", (count + 1), doc.toStringLine(count));
                    }
                    System.out.println("====================");
                    cursor_print();
                } catch (IOException e) {
                    System.out.println("Oops! There was an issue opening the file.");
                    System.out.println(e.getMessage());
                }
                break;
            
            case "p": // print
                System.out.println("====================");
                System.out.printf("  [--->  %s  <---]\n", doc.current_file_name()); // header
                for (int count = 0; count < doc.line_count(); count++) // print each line
                {
                    System.out.printf((((count == doc.cursor_line_position()) ? "-" : " ") + "%2d| %s\n"), (count + 1), doc.toStringLine(count));
                }
                break;
            
            case "r": // right
                System.out.println("====================");
                try {
                    if (args.equals("")) doc.cursor_right(); // if no params, just move one
                    else                 doc.cursor_right(Math.min(Integer.parseInt(args), doc.line_length() - doc.cursor_position_in_line())); // if param, move that number
                    cursor_print();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid argument! '" + args + "' isn't an integer." ); // catches error thrown by parseInt
                }
                break;
            
            case "l": // left
                System.out.println("====================");
                try {
                    if (args.equals("")) doc.cursor_left(); // if no params, move 1
                    else                 doc.cursor_left(Math.min(Integer.parseInt(args), doc.cursor_position_in_line())); // if params, use them
                    cursor_print();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid argument! '" + args + "' isn't an integer." ); // catch if param not an int
                }
                break;
                
            case "e": // insert
                while (args.equals("")) // if the user doesn't provide args
                {
                    System.out.print("args: ");
                    try {
                        args = s_in.next(); // try to ask for them
                        s_in.nextLine();
                    } catch (NullPointerException e) {
                        args = "testing"; // if there's no scanner, just default to "testing
                    }
                }
                System.out.println("====================");
                if (!doc.insert_text(args))  System.out.println("Failed!"); // if insert fails, say that
                cursor_print();
                break;
            
            case "c": // save
                System.out.println("====================");
                try {
                    doc.store_file(); // try to store
                    System.out.println("Successfully saved the file.");
                } catch (IOException e) {
                    System.out.println("Oops! There was an issue saving the file."); // if the problem is with the file, say that
                    System.out.println(e.getMessage());
                } catch (NullPointerException e) {
                    System.out.println("No filename set. Use the 's' command if this is a new file."); // if problem is with user, say that
                }
                break;
                
            case "s": // save as
                while (args.equals("")) // if the user doesn't provide args
                {
                    System.out.print("args: ");
                    try {
                        args = s_in.next(); // try to ask for them
                        s_in.nextLine();
                    } catch (NullPointerException e) {
                        args = "testing"; // if there's no scanner, just default to "testing
                    }
                }
                System.out.println("====================");
                
                try {
                    doc.store_file(format_filename(args)); // try to save
                    System.out.println("Successfully saved the file with name " + format_filename(args) + ".");
                } catch (IOException e) {
                    System.out.println("Oops! There was an issue saving the file.");
                    System.out.println(e.getMessage());
                }
                break;
            
            case "rm": // remove
                System.out.println("====================");
                try {
                    if (args.equals(""))    doc.remove_char_toleft(); // if no args, take 1
                    else                    doc.remove_char_toleft(Integer.parseInt(args)); // if args, use them
                    cursor_print();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid argument! '" + args + "' isn't an integer." ); // if args not int, yell at user
                }
                break;
                
            case "d": // down
                System.out.println("====================");
                try {
                    if (args.equals(""))    doc.cursor_down(); // 1 if no arg, arg if arg. if attempting to move past edge of list, just go to end
                    else                    doc.cursor_down(Math.min(Integer.parseInt(args),doc.line_count() - doc.cursor_line_position() - 1));
                    cursor_print();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid argument! '" + args + "' isn't an integer." ); // silly users, trying to move down 'k' lines. stop them!
                }
                break;
                
            case "u": // up
                System.out.println("====================");
                try {
                    if (args.equals(""))    doc.cursor_up(); // move dif amts dependign on args
                    else                    doc.cursor_up(Math.min(Integer.parseInt(args), doc.cursor_line_position()));
                    cursor_print();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid argument! '" + args + "' isn't an integer." ); // catch non int args
                }
                break;
                
            case "aa": // add above
                System.out.println("====================");
                if (doc.insert_line_above()) {
                    doc.cursor_up(); // follow new line
                    cursor_print();
                } else {
                    System.out.println("Failed!");   
                }
                break;
                
            case "ab": // add below
                System.out.println("====================");
                if (doc.insert_line_below()) {
                    doc.cursor_down(); // follow new line
                    cursor_print();
                } else {
                    System.out.println("Failed!");   
                }
                break;
                
            case "dl": // delete lines
                System.out.println("====================");
                try {
                    if (args.equals(""))    doc.remove_line();
                    else {
                        int count = 0;
                        int init_size = doc.line_count();
                        int init_cursor = doc.cursor_line_position();
                        while (count < Math.min(Integer.parseInt(args), init_size - init_cursor)) // remove args number of lines
                        {
                            doc.remove_line();
                            doc.cursor_down(); // move to next line
                            count++;
                        }
                        if (doc.cursor_line_position() < doc.line_count() - 1) doc.cursor_up(); // move to line above what used to to be cursor line
                    }
                    cursor_print();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid argument! '" + args + "' isn't an integer." );
                }
                break;
                
            case "cl": // clear
                System.out.println("====================");
                doc.cursor_move_first_line(); // go to top
                while (doc.remove_line());  // destroy everything
                System.out.println("Document cleared!\n");
                break;
                
            default:
                return false;
        }
        if (!quit) System.out.print(">"); // if still going, put a little input marker
        return true;
    }
    
    /**
     * Returns a string for the cursor representation of the current document line, with a space at the cursor and a ^ pointing at the cursor from below
     */
    public void cursor_print ()
    {
        if (doc.line_count() == 0) {
            System.out.println("Document is empty. Write something!");
            return;
        }
        String out = "";
        String line = doc.toStringLine(); // get line
        int cursor_index = doc.cursor_position_in_line(); // get cursor
        int line_index = doc.cursor_line_position(); // get number to put at the start
        out += line.substring(0, cursor_index) + " " + line.substring(cursor_index) + "\n"; // format text, putting space at cursor
        for (int count = 0; count < cursor_index + 4; count++) out += " "; // put spaces before ^
        out += "^\n";
        System.out.printf("%2d| %s\n", line_index + 1, out); // put it all together
    }
    
    /**
     * Takes a string an normalizes it in to a valid filename, possibly butchering it if the input was bad. Sorry not sorry, learn how to name a file.
     * 
     * @param input string to filenameify. valid extensions are .txt, .csv, and .java. Anything else will be turned in to .txt
     * 
     * @returns formatted filename
     */
    public String format_filename(String input)
    {
        int delim_index = input.indexOf("."); // split passed filename at first .
        String name = (delim_index > -1) ? input.substring(0, delim_index) : input; // everything before . is name
        String type = (delim_index > -1) ? input.substring(delim_index) : ".txt"; // everything after is file extension
        if (!type.equals(".csv") && !type.equals(".java")) type = ".txt"; // if not an accepted type, change to txt
        return name + type;
    }
    
    /** Length of line cursor is on*/
    public int line_length() { return doc.line_length(); }
    /** Length of line specified 
     * @param line index of line to get length of*/
    public int line_length(int line) { return doc.line_length(line); }
    /** Number of lines in document*/
    public int line_count() { return doc.line_count(); }
    /** Name of file currently loaded*/
    public String current_file_name() { return doc.current_file_name(); }
    /** Index of line cursor is on*/
    public int cursor_line_position() { return doc.cursor_line_position(); }
    /** Index of cursor within current line*/
    public int cursor_position_in_line() { return doc.cursor_position_in_line(); }
    /** Text contents of current line*/
    public String toStringLine() { return doc.toStringLine(); }
}
