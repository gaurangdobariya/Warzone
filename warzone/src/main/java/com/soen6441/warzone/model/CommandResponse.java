/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soen6441.warzone.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * This class represents structure of response of all the commands .
 * <p>
 * Three annotations (Getter, Setter and ToString), you can see on the top of
 * the class are lombok dependencies to automatically generate getter, setter
 * and tostring method in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter
@Component
@NoArgsConstructor
public class CommandResponse {

    /**
     * d_isValid will be used to verify whether command is valid or not
     */
    private boolean d_isValid;

    /**
     * d_responseString will be used for setting any message in response
     */
    private String d_responseString;

    /**
     * object to the response of the command
     *
     * @param p_isValid validation parameter
     * @param p_responseString response parameter
     */
    public CommandResponse(boolean p_isValid, String p_responseString) {
        this.d_isValid = p_isValid;
        this.d_responseString = p_responseString;
    }

    /**
     * used to get the response and validation together in string
     *
     * @return returns the formatted string with the validation of command and
     * response to that validation
     */
    @Override
    public String toString() {
        String l_validity;
        if (d_isValid) {
            l_validity = "Command Executed Sucessfully";
        } else {
            l_validity = "Command Execution failed";
        }
        if (d_responseString == null) {
            d_responseString = "Command Fails For some reason";
        }
        return "Validity :: " + l_validity + "\n" + "Message :: \n" + d_responseString + "\n";
    }

}
