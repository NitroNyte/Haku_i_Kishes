package databaseClasses;



public class Payment_tax {

    private int personID_tax;
    private String payment_tax;
    private String data_tax;
    private String paymentData_tax;
    private int transactionID_tax;
    private String paymentStatus_tax;
    private String notice_tax;


    public String getPaymentData() {
        return paymentData_tax;
    }

    public void setPaymentData(String paymentData_tax) {
        this.paymentData_tax = paymentData_tax;
    }

    public String getPaymentStatus() {
        return paymentStatus_tax;
    }

    public void setPaymentStatus(String paymentStatus_tax) {
        this.paymentStatus_tax = paymentStatus_tax;
    }

    public String getData() {
        return data_tax;
    }

    public void setData(String data_tax) {
        this.data_tax = data_tax;
    }

    public Payment_tax(int personID_tax, String payment_tax, String data_tax, String paymentData_tax,
            int transactionID_tax, String paymentStatus_tax) {
        this.personID_tax = personID_tax;
        this.payment_tax = payment_tax;
        this.transactionID_tax = transactionID_tax;
        this.data_tax = data_tax;
        this.paymentData_tax = paymentData_tax;
        this.paymentStatus_tax = paymentStatus_tax;
    }

    public Payment_tax(int personID_tax, String payment_tax, String data_tax, String paymentData_tax,
            int transactionID_tax, String paymentStatus_tax, String notice_tax) {
        this.personID_tax = personID_tax;
        this.payment_tax = payment_tax;
        this.transactionID_tax = transactionID_tax;
        this.data_tax = data_tax;
        this.paymentData_tax = paymentData_tax;
        this.paymentStatus_tax = paymentStatus_tax;
        this.notice_tax = notice_tax;
    }

    public String getPayment() {
        return payment_tax;
    }

    public void setPayment(String payment_tax) {
        this.payment_tax = payment_tax;
    }

    public int getTransactionID() {
        return transactionID_tax;
    }

    public void setTransactionID(int transactionID_tax) {
        this.transactionID_tax = transactionID_tax;
    }

    public int getPersonID() {
        return personID_tax;
    }

    public void setPersonID(int personID_tax) {
        this.personID_tax = personID_tax;
    }

    public String getNotice() {
        return notice_tax;
    }

    public void setNotice(String notice_tax) {
        this.notice_tax = notice_tax;
    }

}
