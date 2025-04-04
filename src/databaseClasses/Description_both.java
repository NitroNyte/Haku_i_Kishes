package databaseClasses;



public class Description_both {

    private int personID;

    private String description;

    private String date;


    public Description_both(int personID, String description, String date){
        this.personID = personID;
        this.description = description;
        this.date = date;
    }

    public Description_both(String date){
        this.date = date;
    }
    
    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    

}
