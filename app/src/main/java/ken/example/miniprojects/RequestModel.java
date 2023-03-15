package ken.example.miniprojects;

public class RequestModel {
    String userMessage,date;
    String userUid,workerUid,status;

    public RequestModel() {

    }

    public RequestModel(String userMessage, String date,String userUid,String workerUid,String status) {
        this.userMessage = userMessage;
        this.date = date;
        this.userUid = userUid;
        this.workerUid = workerUid;
        this.status = status;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getWorkerUid() {
        return workerUid;
    }

    public void setWorkerUid(String workerUid) {
        this.workerUid = workerUid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
