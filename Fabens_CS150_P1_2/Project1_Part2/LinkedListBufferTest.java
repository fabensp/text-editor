

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class LinkedListBufferTest.
 *
 * @author  Peter Fabens
 * @version 2/23/2022
 */
public class LinkedListBufferTest
{
    LinkedListBuffer buf = new LinkedListBuffer();
    /**
     * Default constructor for test class LinkedListBufferTest
     */
    public LinkedListBufferTest()
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
        buf = new LinkedListBuffer();
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        buf = null;
    }
    
    /**
     * Test various cases for the load_string() method
     */
    @Test
    public void load_string_test()
    {
        buf.load_string("testing");
        assertEquals("testing", buf + ""); // control confirmation
        
        buf.load_string(""); // what happens when trying empty string? should empty the list
        assertEquals("", buf + "");
        assertEquals(0, buf.length());
    }
    
    /**
     * Test various cases for the cursor movement methods
     */
    @Test
    public void cursor_move_test()
    {
        buf.load_string("testing");
        assertEquals("testing|", buf.cursorString());   // control confirmation
        
        assert(buf.cursor_left());
        assertEquals("testin|g", buf.cursorString());   // left
        
        assert(buf.cursor_right());
        assertEquals("testing|", buf.cursorString());   // right
        
        assert(!buf.cursor_right());                    // confirming cannot go futher right
        
        assert(buf.cursor_left(3));
        assertEquals("test|ing", buf.cursorString());   // left 3
        
        assert(buf.cursor_right(2));
        assertEquals("testin|g", buf.cursorString());   // right 2
        
        assert(buf.cursor_move_start_line());
        assertEquals("|testing", buf.cursorString());   // start of line
        
        assert(!buf.cursor_left());                     // can not go further left
        
        assert(!buf.cursor_left(3));                    // can not go 3 spaces left
        
        assert(!buf.cursor_right(-5));                  // negative numbers do nothing
    }
    
    /**
     * Test remove_char_toleft() with and without parameters
     */
    @Test
    public void remove_test ()
    {
        buf.load_string("testing");
        assertEquals("testing", buf + "");  // control confirmation
        
        assert(buf.remove_char_toleft());
        assertEquals("testin", buf + "");   // remove 1
        
        assert(buf.remove_char_toleft(5));
        assertEquals("t", buf + "");        // remove 5
        
        assert(buf.remove_char_toleft(2));  // remove 2 (even though there is only 1 char)
        assertEquals("", buf + "");
    }
    
    /**
     * Test insert_text() with char and string parameters
     */
    @Test
    public void insert_test ()
    {
        buf.load_string("");
        assertEquals("", buf + "");  // control confirmation
        
        assert(buf.insert_text('t'));
        assertEquals("t", buf + "");
        
        assert(buf.insert_text("esting"));
        assertEquals("testing", buf + "");
        
        assert(!buf.insert_text((char) 0));
        assertEquals("testing", buf + ""); // 0 input, or anything below 33, gets cancelled out
        assert(!buf.insert_text(""));
        assertEquals("testing", buf + "");
    }
}
