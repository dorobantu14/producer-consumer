import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

class OrdersTask extends RecursiveTask<Void> {
    private final String src;

    private int LEN_EMAG_PROC;
    private int start;
    private int end;
    private int id;

    public OrdersTask(String path, int i, int LEN_EMAG_PROC) {
        this.src = path;
        this.LEN_EMAG_PROC = LEN_EMAG_PROC;
        this.id = i;
    }

    private int computeStart(int id, int N, int P) {
        return (int) (id * (double) N / P);
    }

    private int computeEnd(int id, int N, int P) {
        return (int) Math.min((id + 1) * (double) N / P, N);
    }

    @Override
    protected Void compute() {

        List<String> lines = FileHelper.readAllLinesOfFile(src + "/orders.txt");
        List<Data> orderData = new ArrayList<>();
        List<ProductTask> productTasks = new ArrayList<>();
        List<Data> finished = new ArrayList<>();
        synchronized (finished) {

            Path filePath = Paths.get(src + "/orders.txt");
            Charset charset = StandardCharsets.UTF_8;
            try {
                lines = Files.readAllLines(filePath, charset);
            } catch (IOException ex) {
                System.out.format("I/O error: %s%n", ex);
            }

            for (String line : lines) {
                List<String> splitted = List.of(line.split(","));
                orderData.add(new Data(splitted.get(0), Integer.parseInt(splitted.get(1))));
            }

            this.start = computeStart(id, lines.size(), LEN_EMAG_PROC);
            this.end = computeEnd(id, lines.size(), LEN_EMAG_PROC);

            int printed = 0;

            for (int i = start; i < end; ++i) {
                if (orderData.get(i).getNoProducts() > 0)
                    productTasks.add(new ProductTask(src, orderData.get(i), finished));
            }

            for (ProductTask task : productTasks) {
                task.fork();
            }

            while (printed < productTasks.size()) {
                try {
                    finished.wait();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for (Data dat : finished) {
                    synchronized (OrdersTask.class) {
                        FileHelper.markAsShipped("orders_out.txt", dat.getCommandId(), dat.getNoProducts());
                    }

                    printed++;
                }
                finished.clear();
            }

            for (ProductTask task : productTasks) {
                task.join();
            }

            return null;
        }
    }
}