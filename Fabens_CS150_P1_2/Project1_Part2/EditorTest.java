import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Editor tests.
 *
 * @author  Peter Fabens
 * @version 3/11/2022
 */
public class EditorTest
{
    String type = "linked";
    Editor e;
    /**
     * Default constructor for test class EditorTest
     */
    public EditorTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        e = new Editor (type);
        e.kill_scanner();
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        e = null;
    }
    
    /**
     * Test open file
     */
    @Test
    public void o_test ()
    {
        e.execute("o etest.txt"); // normal case
        assertEquals("etest.txt", e.current_file_name());
        e.execute("o menu"); // normal case
        assertEquals("menu.txt", e.current_file_name());
        e.execute("o"); // normally this would prompt the user for a filename, but since the test can't use System.in that won't work. So it defaults to "testing"
        assertEquals("testing.txt", e.current_file_name());
        e.execute("o this_file_dne.txt");
        assertEquals("testing.txt", e.current_file_name());
    }
    
    /**
     * Test insert
     */
    @Test
    public void e_test ()
    {
        e.execute("e hello world ");
        assertEquals("hello world ", e.toStringLine());
        e.execute("e"); // normally this would prompt the user for an input, but since the test can't use System.in that won't work. So it defaults to "testing"
        assertEquals("hello world testing", e.toStringLine());
    }
    
    /**
     * Test remove
     */
    @Test
    public void rm_test ()
    {
        e.execute("e hello world ");
        assertEquals("hello world ", e.toStringLine());
        e.execute("rm"); // normal case
        assertEquals("hello world", e.toStringLine());
        e.execute("rm 6"); // special, spicy case
        assertEquals("hello", e.toStringLine());
        e.execute("rm 100"); // ludicrous case, should just do what it can and stop at the edge
        assertEquals("", e.toStringLine());
    }
    
    /**
     * Test left
     */
    @Test
    public void l_test ()
    {
        e.execute("o etest.txt");
        
        assertEquals(10, e.cursor_position_in_line());
        e.execute("l"); // normal case
        assertEquals(9, e.cursor_position_in_line());
        e.execute("l 6"); // special, spicy case
        assertEquals(3, e.cursor_position_in_line());
        e.execute("l 100"); // ludicrous case, should just do what it can and stop at the edge
        assertEquals(0, e.cursor_position_in_line());
    }
    
    /**
     * Test right
     */
    @Test
    public void r_test ()
    {
        e.execute("o etest.txt");
        e.execute("l 10");
        
        assertEquals(0, e.cursor_position_in_line());
        e.execute("r"); // normal case
        assertEquals(1, e.cursor_position_in_line());
        e.execute("r 6"); // special, spicy case
        assertEquals(7, e.cursor_position_in_line());
        e.execute("r 100"); // ludicrous case, should just do what it can and stop at the edge
        assertEquals(10, e.cursor_position_in_line());
    }
    
    /**
     * Test down
     */
    @Test
    public void d_test ()
    {
        e.execute("o menu.txt");
        e.execute("l 10");
        
        assertEquals(0, e.cursor_line_position());
        e.execute("d"); // normal case
        assertEquals(1, e.cursor_line_position());
        e.execute("d 6"); // special, spicy case
        assertEquals(7, e.cursor_line_position());
        e.execute("d 100"); // ludicrous case, should just do what it can and stop at the edge
        assertEquals(26, e.cursor_line_position());
    }
    
    /**
     * Test up
     */
    @Test
    public void u_test ()
    {
        e.execute("o menu.txt");
        e.execute("d 26");
        
        assertEquals(26, e.cursor_line_position());
        e.execute("u"); // normal case
        assertEquals(25, e.cursor_line_position());
        e.execute("u 6"); // special, spicy case
        assertEquals(19, e.cursor_line_position());
        e.execute("u 100"); // ludicrous case, should just do what it can and stop at the edge
        assertEquals(0, e.cursor_line_position());
    }
    
    /**
     * Test add below
     */
    @Test
    public void ab_test ()
    {
        assertEquals(0, e.line_count());
        assertEquals(0, e.cursor_line_position()); // is info right when no lines?
        e.execute("ab"); 
        assertEquals(1, e.line_count());
        assertEquals(0, e.cursor_line_position()); // normal tests, to see that cursor moves with new line
        
        e.execute("ab"); 
        assertEquals(2, e.line_count());
        assertEquals(1, e.cursor_line_position());
        e.execute("ab"); 
        assertEquals(3, e.line_count());
        assertEquals(2, e.cursor_line_position());
    }
    
    /**
     * Test add above
     */
    @Test
    public void aa_test ()
    {
        assertEquals(0, e.line_count());
        assertEquals(0, e.cursor_line_position()); // is info right when no lines?
        e.execute("aa"); 
        assertEquals(1, e.line_count());
        assertEquals(0, e.cursor_line_position()); // normal tests, to see that cursor moves with new line
        e.execute("aa"); 
        assertEquals(2, e.line_count());
        assertEquals(0, e.cursor_line_position());
        e.execute("aa"); 
        assertEquals(3, e.line_count());
        assertEquals(0, e.cursor_line_position());
    }
    
    /**
     * Test delete line
     */
    @Test
    public void dl_test ()
    {
        e.execute("o menu.txt");
        e.execute("d 5");
        
        assertEquals(27, e.line_count());
        assertEquals(5, e.cursor_line_position()); 
        e.execute("dl"); 
        assertEquals(26, e.line_count());
        assertEquals(4, e.cursor_line_position()); 
        e.execute("dl 5"); 
        assertEquals(21, e.line_count());
        assertEquals(3, e.cursor_line_position());
        e.execute("dl 100"); 
        assertEquals(3, e.line_count());
        assertEquals(2, e.cursor_line_position());
    }
    
    /**
     * Test clear lines
     */
    @Test
    public void cl_test ()
    {
        assertEquals(0, e.line_count());
        assertEquals(0, e.cursor_line_position()); // is info right when no lines?
        
        e.execute("cl"); // does clear work when no lines?
        
        assertEquals(0, e.line_count());
        assertEquals(0, e.cursor_line_position());
        
        e.execute("o menu.txt");
        
        assertEquals(27, e.line_count());
        assertEquals(0, e.cursor_line_position());
        
        e.execute("d 4");
        
        e.execute("cl"); // does clear work normally?
        
        assertEquals(0, e.line_count());
        assertEquals(0, e.cursor_line_position());
    }
    
    /**
     * Test format_filename
     */
    @Test
    public void filename_test ()
    {
        assertEquals("data.txt", e.format_filename("data"));
        assertEquals("data.txt", e.format_filename("data.txt"));
        assertEquals("data.csv", e.format_filename("data.csv"));
        assertEquals("data.java", e.format_filename("data.java"));
        assertEquals("data.txt", e.format_filename("data.png"));
        assertEquals("data.txt", e.format_filename("data.png.txt"));
    }
}
