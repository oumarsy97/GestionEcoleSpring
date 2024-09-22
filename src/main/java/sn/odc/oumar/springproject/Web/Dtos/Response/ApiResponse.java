package sn.odc.oumar.springproject.Web.Dtos.Response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse<T> {

    private String status;
    private HttpStatus code;
    private String message;
    private T data;

    public ApiResponse(String status, HttpStatus code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", HttpStatus.CREATED, message, data);
    }

    public static <T> ApiResponse<T> error(HttpStatus code, String message, T data) {
        return new ApiResponse<>("error", code, message, data);
    }
}
