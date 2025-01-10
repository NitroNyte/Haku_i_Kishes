public class ChurchDocumentUser {
    private String data_user;
    private String payment_user;
    private String paymentData_user;
    private int transaction_user;
    private String releasedFromPayment_user;



    public String getReleasedFromPayment() {
        return releasedFromPayment_user;
    }
    public void setReleasedFromPayment(String releasedFromPayment_user) {
        this.releasedFromPayment_user = releasedFromPayment_user;
    }
    public ChurchDocumentUser(String data_user, String payment_user, String paymentData_user, int transaction_user, String releasedFromPayment_user){
        this.data_user = data_user;
        this.payment_user = payment_user;
        
        this.paymentData_user = paymentData_user;
        this.transaction_user = transaction_user;
        this.releasedFromPayment_user = releasedFromPayment_user;
    }




    public String getPaymentData() {
        return paymentData_user;
    }
    public void setPaymentData(String paymentData_user) {
        this.paymentData_user = paymentData_user;
    }

    public String getData() {
        return data_user;
    }
    public void setData(String data_user) {
        this.data_user = data_user;
    }
    public String getPayment() {
        return payment_user;
    }
    public void setPayment(String payment_user) {
        this.payment_user = payment_user;
    }
   
    public int getTransaction() {
        return transaction_user;
    }
    public void setTransaction(int transaction_user) {
        this.transaction_user = transaction_user;
    }
}
