import java.util.concurrent.ForkJoinPool;

public class Tema2 {
    static int P;
    static String src;
    static int LEN_EMAG_PROC;

    public static void main(String[] args) {

        P = Integer.parseInt(args[1]);
        src = args[0];

        LEN_EMAG_PROC = P / 2;

        ForkJoinPool fjp = new ForkJoinPool(P);
        for (int i = 0; i < LEN_EMAG_PROC; ++i) {
            fjp.invoke(new OrdersTask(src, i, LEN_EMAG_PROC));
        }
        fjp.shutdown();
    }
}
