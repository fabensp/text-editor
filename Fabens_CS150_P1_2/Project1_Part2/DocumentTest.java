import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;

/**
 * The test class DocumentTest.
 *
 * @author  Peter Fabens
 * @version 2/20/2022
 */
public class DocumentTest
{
    @Test
    @DisplayName("load_file")
    public void load_fileTest () throws IOException
    {
        Document test_doc = new Document ("");
        assert(test_doc.load_file("testing.txt"));
        assert(test_doc.toStringDocument().equals("testing line 1\nreading line 2\nscanned line 3"));
        test_doc = new Document (""); // reset doc
        assert(test_doc.insert_text("don't even try"));
        assertEquals("don't even try", test_doc.toStringDocument());
        assert(test_doc.store_file("testing.txt"));
        assert(test_doc.load_file("testing.txt"));
        assertEquals("don't even try", test_doc.toStringDocument());
        assert(test_doc.insert_text(" to fight it"));
        assert(test_doc.store_file()); // is able to store without parameters since the file was referenced before
        assert(test_doc.load_file("testing.txt"));
        assert(test_doc.toStringDocument().equals("don't even try to fight it"));
    }
    // I'm not going to test the rest of the methods, as they are all just calls of the same BufferStructure methods
    // See other test methods for the other functionality.
}
