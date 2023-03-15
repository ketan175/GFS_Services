package ken.example.miniprojects;

public class HistoryModel {

    String userUid;

    String date;
    String name;
    String serviceType;
    String image;
    Long totalRating;
    String averageRating;
    String problem;
    String serviceCharge;
    String status;

    public HistoryModel() {

    }


    public HistoryModel(String date,String problem,String serviceCharge,String status, String name, String serviceType, String image, Long totalRating, String averageRating) {
        this.date = date;
        this.name = name;
        this.serviceType = serviceType;
        this.image = image;
        this.totalRating = totalRating;
        this.averageRating = averageRating;
        this.problem = problem;
        this.serviceCharge = serviceCharge;
        this.status = status;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Long totalRating) {
        this.totalRating = totalRating;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
//