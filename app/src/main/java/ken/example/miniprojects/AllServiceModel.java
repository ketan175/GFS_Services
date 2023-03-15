package ken.example.miniprojects;

public class AllServiceModel {

    String name;
    String email;
    String password;
    String contact;
    String serviceType;
    String city;
    String serviceCharge;
    String uid;
    String image;

    public AllServiceModel() {
    }

    public AllServiceModel(String name, String email, String password, String contact, String serviceType, String city,String serviceCharge,String uid,String image) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.contact = contact;
        this.serviceType = serviceType;
        this.city = city;
        this.serviceCharge = serviceCharge;
        this.uid = uid;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
