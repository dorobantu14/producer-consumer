public class Data {
    private String commandId;
    private String productId;
    private int noProducts;

    Data(String commandId, String productId) {
        this.commandId = commandId;
        this.productId = productId;
        noProducts = 0;
    }

    Data(String commandId, int noProducts) {
        this.commandId = commandId;
        this.noProducts = noProducts;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getNoProducts() {
        return noProducts;
    }

    public void setNoProducts(int noProducts) {
        this.noProducts = noProducts;
    }
}
