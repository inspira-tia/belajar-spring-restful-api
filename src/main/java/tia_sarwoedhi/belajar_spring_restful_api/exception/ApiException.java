package tia_sarwoedhi.belajar_spring_restful_api.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message){
        super(message);
    }

}
