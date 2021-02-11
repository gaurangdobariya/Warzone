package com.soen6441.warzone.controller;

import com.soen6441.warzone.config.FxmlView;
import com.soen6441.warzone.config.StageManager;
import com.soen6441.warzone.service.MapHandlingInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.*;


@Controller
public class MapController implements Initializable {


    @FXML
    private TextField d_ExecuteCommand;


    @Lazy
    @Autowired
    StageManager d_stageManager;

    @Autowired
    private MapHandlingInterface d_maphandlinginterface;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    ;

    /**
     * This method will redirect user to Home page
     *
     * @param event will represents value sent from view
     */
    @FXML
    void backToWelcome(ActionEvent event) {

        d_stageManager.switchScene(FxmlView.HOME, null);
    }

    /**
     * This method is used to get data from user and put it as a parameter in validation
     * @param event
     */
    @FXML
    void getData(ActionEvent event) {
        //just for testing
        String s = d_ExecuteCommand.getText();
        System.out.println(s);
        d_maphandlinginterface.validateCommand(s);
        d_ExecuteCommand.clear();
    }
}
