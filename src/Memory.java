import java.util.ArrayList;
import java.util.List;

public class Memory
{
    public static Element[] elements;

    public static List<List<User>> result;

    public static int matched = 0;

    public static int target = 60000;

    public static void initialize()
    {
        elements = new Element[1000];
        for(int i = 0; i < elements.length; i++)
        {
            elements[i] = new Element(i);
        }

        result = new ArrayList<>();
    }
}
