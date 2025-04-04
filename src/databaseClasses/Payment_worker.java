package databaseClasses;



public class Payment_worker {

    private int personID_worker;
    private String payment_worker;
    private String data_worker;
    private String paymentData_worker;
    private int transactionID_worker;
    private String paymentStatus_worker;
    private String notice_worker;

    public String getNotice() {
        return notice_worker;
    }

    public void setNotice(String notice_worker) {
        this.notice_worker = notice_worker;
    }

    public String getPaymentData() {
        return paymentData_worker;
    }

    public void setPaymentData(String paymentData_worker) {
        this.paymentData_worker = paymentData_worker;
    }

    public String getPaymentStatus() {
        return paymentStatus_worker;
    }

    public void setPaymentStatus(String paymentStatus_worker) {
        this.paymentStatus_worker = paymentStatus_worker;
    }

    public String getData() {
        return data_worker;
    }

    public void setData(String data_worker) {
        this.data_worker = data_worker;
    }

    public Payment_worker(int personID_worker, String payment_worker, String data_worker, String paymentData_worker,
            int transactionID_worker, String paymentStatus_worker) {
        this.personID_worker = personID_worker;
        this.payment_worker = payment_worker;
        this.transactionID_worker = transactionID_worker;
        this.data_worker = data_worker;
        this.paymentStatus_worker = paymentStatus_worker;
        this.paymentData_worker = paymentData_worker;
    }



    public Payment_worker(int personID_worker, String payment_worker, String data_worker, String paymentData_worker,
            int transactionID_worker, String paymentStatus_worker ,String notice_worker) {
        this.personID_worker = personID_worker;
        this.payment_worker = payment_worker;
        this.transactionID_worker = transactionID_worker;
        this.data_worker = data_worker;
        this.paymentStatus_worker = paymentStatus_worker;
        this.paymentData_worker = paymentData_worker;
        this.notice_worker = notice_worker;
    }

    public String getPayment() {
        return payment_worker;
    }

    public void setPayment(String payment_worker) {
        this.payment_worker = payment_worker;
    }

    public int getTransactionID() {
        return transactionID_worker;
    }

    public void setTransactionID(int transactionID_worker) {
        this.transactionID_worker = transactionID_worker;
    }

    public int getPersonID() {
        return personID_worker;
    }

    public void setPersonID(int personID_worker) {
        this.personID_worker = personID_worker;
    }

}
