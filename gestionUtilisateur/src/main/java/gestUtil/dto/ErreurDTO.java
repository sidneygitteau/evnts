package gestUtil.dto;

public class ErreurDTO {
    private String message;

    public ErreurDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
