package ken.example.miniprojects;

public class ServicesModel {
    int logoImage;

    public int getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(int logoImage) {
        this.logoImage = logoImage;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    String serviceName;

    public ServicesModel(int logoImage, String serviceName) {
        this.logoImage = logoImage;
        this.serviceName = serviceName;
    }
}
