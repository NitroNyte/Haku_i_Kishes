public class Person_tax {
     int id_tax;
     String name_tax;
     String fatherName_tax;
     String surname_tax;
     String region_tax;
     String phoneNum_tax;


    public Person_tax(int id_tax, String name_tax, String fatherName_tax, String surname_tax, String region_tax, String phoneNum_tax) {
        this.id_tax = id_tax;
        this.name_tax = name_tax;
        this.fatherName_tax = fatherName_tax;
        this.surname_tax = surname_tax;
        this.region_tax = region_tax;
        this.phoneNum_tax = phoneNum_tax;
    }

    // Getters and Setters
    public int getId() { 
        return id_tax; 
        }

    public String getName() { 
        return name_tax; 
        }

    public String getSurname() { 
        return surname_tax;
        }

    public String getRegion() {
        return region_tax;
        }
    
    public String getPhoneNum() {
        return phoneNum_tax;
        }

    public void setId(int id_tax){
         this.id_tax = id_tax; 
        }

    public void setName(String name_tax) { 
        this.name_tax = name_tax;
        }

    public void setSurname(String surname_tax) { 
        this.surname_tax = surname_tax;
        }

    public void setRegion(String region_tax) {
        this.region_tax = region_tax;
        }

    public void setPhoneNum(String phoneNum_tax) {
        this.phoneNum_tax = phoneNum_tax;
        }


  
    public String getFatherName() {
        return fatherName_tax;
    }

    public void setFatherName(String fatherName_tax) {
        this.fatherName_tax = fatherName_tax;
    }




}
