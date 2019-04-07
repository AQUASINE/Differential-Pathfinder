public class Debug{
    public static final int kDebugLevel = 4;
    public static void print(Object text, int debugLevel){
        if (debugLevel <= kDebugLevel){
            System.out.println(text);
        }
    }
}
