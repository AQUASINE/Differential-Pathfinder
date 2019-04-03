public class Debug{
    public static final int kDebugLevel = 1;
    public static void print(String text, int debugLevel){
        if (debugLevel >= kDebugLevel){
            System.out.println(text);
        }
    }
}
