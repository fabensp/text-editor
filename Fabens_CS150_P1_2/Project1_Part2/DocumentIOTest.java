import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;

/**
 * The test class DocumentIOTest.
 *
 * @author  Peter Fabens
 * @version 2/20/2022
 */
public class DocumentIOTest
{
    @Test
    @DisplayName("read_line")
    public void read_lineTest() throws IOException
    {
        DocumentIO test_doc = new DocumentIO();
        test_doc.open_file("testing.txt");
        assert(test_doc.read_line().equals("testing line 1"));
        assert(test_doc.read_line().equals("reading line 2"));
        assert(test_doc.read_line().equals("scanned line 3"));
    }
    
    @Test
    @DisplayName("write_file")
    public void write_fileTest() throws IOException
    {
        DocumentIO test_doc = new DocumentIO();
        test_doc.open_file("testing.txt");
        test_doc.write_file("testing line 1\nreading line 2\nscanned line 3\noverwritten");
        assert(test_doc.read_line().equals("testing line 1"));
        assert(test_doc.read_line().equals("reading line 2"));
        assert(test_doc.read_line().equals("scanned line 3"));
        assert(test_doc.read_line().equals("overwritten"));
        test_doc.write_file("testing line 1\nreading line 2\nscanned line 3");
        assert(test_doc.read_line().equals("testing line 1"));
        assert(test_doc.read_line().equals("reading line 2"));
        assert(test_doc.read_line().equals("scanned line 3"));
        assertFalse(test_doc.has_more_lines());
    }
    
    @Test
    @DisplayName("open_file")
    public void open_fileTest() throws IOException
    {
        DocumentIO test_doc = new DocumentIO();
        test_doc.open_file("testing.txt");
        assert(test_doc.file_name().equals("testing.txt"));
        test_doc.open_file("text.txt");
        assert(test_doc.file_name().equals("text.txt"));
        test_doc.open_file("nothing.txt");
        assert(test_doc.file_name().equals("nothing.txt"));
    }
}
