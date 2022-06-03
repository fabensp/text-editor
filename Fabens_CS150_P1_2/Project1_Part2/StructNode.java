
/**
 * Holds a list element and a reference to the next Node.
 *
 * @author Peter Fabens
 * @version 2/28/2022
 */
public class StructNode
{
    private GapBufferInterface  item;
    private StructNode          next;
    public StructNode (GapBufferInterface item)
    {
        next = null;
        this.item = item;
    }
    public void s_next      (StructNode next)   { this.next = next; }
    public StructNode  next ()                  { return next;      }
    public GapBufferInterface  item ()          { return item;      }
}
