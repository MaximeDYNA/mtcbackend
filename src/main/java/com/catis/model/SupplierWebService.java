/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.wilnzi.leuna.backoffice.backend.backend.services.core;

/**
 *
 * @author Wilfried NZIWOUE <wilchiadjeu@gmail.com>
 */
import cm.wilnzi.leuna.backoffice.backend.backend.mvc.controller.Controller;
import cm.wilnzi.leuna.backoffice.backend.backend.mvc.model.ResponseElement;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/supplier")
public class SupplierWebService {

    Controller controller;
    Boolean status = true;
    JsonObject output = null;
    JsonObject jsonObject;
    JsonParser parser;

    
    public SupplierWebService() {
        parser = new JsonParser();
        controller = new Controller();
    }

    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response listSupplier(String message) {
         if ((message = controller.validate(message)) != null && controller.authentication(message)) {
            try {
                controller.insertLog("POST", "/supplier/list", message);
                jsonObject = parser.parse(message).getAsJsonObject();
                ArrayList<String> waitedParameter = new ArrayList<>();
//                waitedParameter.add("tableName");
                waitedParameter = controller.verifyParameter(jsonObject, waitedParameter);
                if (waitedParameter != null && !waitedParameter.isEmpty()) {
                    output = new JsonObject();
                    output.addProperty("waitedParameter", new Gson().toJson(waitedParameter));
                    status = true;
                } else {
                    output = controller.listSupplier(jsonObject);
                    status = false;
                }

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

        } else {
            status = true;
        }
        return ResponseElement.createResponse(output, status);
    }
    
    @POST
    @Path("/view")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response viewSupplier(String message) {
         if ((message = controller.validate(message)) != null && controller.authentication(message)) {
            try {
                controller.insertLog("POST", "/supplier/view", message);
                jsonObject = parser.parse(message).getAsJsonObject();
                ArrayList<String> waitedParameter = new ArrayList<>();
                waitedParameter.add("pseudoUser");
                waitedParameter.add("supplierCode");
                waitedParameter = controller.verifyParameter(jsonObject, waitedParameter);
                if (waitedParameter != null && !waitedParameter.isEmpty()) {
                    output = new JsonObject();
                    output.addProperty("waitedParameter", new Gson().toJson(waitedParameter));
                    status = true;
                } else {
                    output = controller.viewSupplier(jsonObject);
                    status = false;
                }

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

        } else {
            status = true;
        }
        return ResponseElement.createResponse(output, status);
    }
    
    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response validate(String message) {
         if ((message = controller.validate(message)) != null && controller.authentication(message)) {
            try {
                controller.insertLog("POST", "/supplier/validate", message);
                jsonObject = parser.parse(message).getAsJsonObject();
                ArrayList<String> waitedParameter = new ArrayList<>();
                waitedParameter.add("pseudoUser");
                waitedParameter.add("supplierCode");
                waitedParameter = controller.verifyParameter(jsonObject, waitedParameter);
                if (waitedParameter != null && !waitedParameter.isEmpty()) {
                    output = new JsonObject();
                    output.addProperty("waitedParameter", new Gson().toJson(waitedParameter));
                    status = true;
                } else {
                    output = controller.validateSupplier(jsonObject);
                    status = false;
                }

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

        } else {
            status = true;
        }
        return ResponseElement.createResponse(output, status);
    }
    
    
    
    
    
}
