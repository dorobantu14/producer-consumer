import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ProductTask extends RecursiveTask<Void> {

    String src;
    Data orderData;

    List<Data> finished;
    public ProductTask(String path, Data orderData, List<Data> finished) {
        this.src = path;
        this.orderData = orderData;
        this.finished = finished;
    }

    @Override
    protected Void compute() {
        int noProd = orderData.getNoProducts();
        String idCom = orderData.getCommandId();

        List<String> lines = FileHelper.readAllLinesOfFile(src +"/order_products.txt");

        List<Data> listData = new ArrayList<>();

        for (String line : lines) {
            List<String> splitted = List.of(line.split(","));
            listData.add(new Data(splitted.get(0), splitted.get(1)));
        }

        int crtProd = 0;
        int i = 0;
        int maxi = listData.size();

        while (i < maxi && crtProd < noProd) {
            Data crtData = listData.get(i);
            if (crtData.getCommandId().equals(idCom)) {
                crtProd++;
                synchronized (ProductTask.class) {
                    FileHelper.markAsShipped("order_products_out.txt", idCom, crtData.getProductId());
                }
            }
            i++;
        }

        synchronized (finished) {
            finished.add(orderData);
            finished.notify();
        }

        return null;
    }
}
