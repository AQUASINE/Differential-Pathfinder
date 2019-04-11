import com.github.sh0nk.matplotlib4j.Plot;
        import com.github.sh0nk.matplotlib4j.PythonExecutionException;

        import java.io.IOException;
        import java.util.ArrayList;

public class Debug{
    public static final int kDebugLevel = 5;
    public static final boolean kGraphsEnabled = true;
    public static void print(Object text, int debugLevel){
        if (debugLevel <= kDebugLevel){
            System.out.println(text);
        }
    }

    public static void graph(ArrayList list0, ArrayList list1, int debugLevel){
        if (kGraphsEnabled && debugLevel <= kDebugLevel) {
            Plot plt = Plot.create();
            plt.plot()
                    .add(list0,list1);
            try{
                plt.show();
            }
            catch (IOException | PythonExecutionException e){
                e.printStackTrace();
            }
        }
    }
}
