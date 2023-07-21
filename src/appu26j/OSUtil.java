package appu26j;

public class OSUtil
{
    private static final boolean windows = System.getProperty("os.name").toLowerCase().contains("win");;

    public static boolean isOnWindows()
    {
        return windows;
    }
}
