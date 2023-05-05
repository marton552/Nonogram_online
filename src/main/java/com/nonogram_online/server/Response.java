
package com.nonogram_online.server;

/**
 *
 * @author marton552
 */
public class Response {
    private int statusCode;
    private String message;

    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
    
    public boolean equalsStatusCode(int number){
        return this.statusCode == number;
    }
    
}
