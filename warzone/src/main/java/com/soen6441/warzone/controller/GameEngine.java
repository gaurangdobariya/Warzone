package com.soen6441.warzone.controller;

import com.soen6441.warzone.model.*;
import com.soen6441.warzone.service.GameEngineService;
import com.soen6441.warzone.service.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * This Class is made to handle Game Engine controller request
 *
 * @author <a href="mailto:patelvicky1995@gmail.com">Vicky Patel</a>
 */
@Controller
public class GameEngine extends Thread implements Initializable {

    @FXML
    public TextArea d_TerritoryListText;
    @FXML
    public TextArea d_ContinentText;
    @FXML
    public TextArea d_TerritoryPlayerArmyText;
    @FXML
    private Button d_BtnExit;
    @FXML
    private Button d_FireCommand;
    @FXML
    private TextField d_CommandLine;

    @FXML
    private TextArea d_FireCommandList;


    @Autowired
    GeneralUtil d_GeneralUtil;
    @Autowired
    GamePlay d_gamePlay;

    @Autowired
    private GameEngineService d_gameEngineSevice;

    @Autowired
    GeneralUtil d_generalUtil;
    /**
     * used to add flag for all players whether they want to add issue or not
     */
    private static int[] PlayerFlag;

    /**
     * used to give turn to each player
     */
    private static int PlayCounter = 0;

    /**
     * This method will exit the game and close the stage
     *
     * @param event will represents value sent from view
     */
    @FXML
    public void exitGame(ActionEvent event) {

        Stage stage = (Stage) d_BtnExit.getScene().getWindow();
        stage.close();
    }

    /**
     * This is the initialization method of this controller
     *
     * @param location  of the FXML file
     * @param resources is properties information
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * This method is used to get fire command from user and put it as a
     * parameter in validation
     *
     * @param event
     */
    public void getData(ActionEvent event) {
        String s = d_CommandLine.getText();
        if (d_GeneralUtil.validateIOString(s, "deploy\\s[a-zA-Z]+\\s[0-9]*") || s.equalsIgnoreCase("done")) {
            CommandResponse l_commandResponse = issuingPlayer(s);
            d_FireCommandList.appendText(l_commandResponse.toString());
            while (true) {
                int l_j = 0;
                for (int l_i = 0; l_i < PlayerFlag.length; l_i++) {
                    if (PlayerFlag[l_i] == 1) {
                        l_j++;
                    }
                }
                if (l_j == PlayerFlag.length) {
                    d_FireCommand.setText("Execute Orer");
                    //execute order function
                    break;
                }


                PlayCounter++;
                if (PlayCounter == PlayerFlag.length) {
                    PlayCounter = 0;
                }
                if (PlayerFlag[PlayCounter] == 1) {
                    System.out.println("fdgfdg");
                    continue;
                } else if (PlayerFlag[PlayCounter] == 0) {
                    d_FireCommand.setText("issue order for " + d_gamePlay.getPlayerList().get(PlayCounter).getD_playerName());
                    d_CommandLine.clear();
                    break;
                }
            }
            System.out.println(PlayCounter + "   " + PlayerFlag[PlayCounter]);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error box");
            alert.setContentText("Invalid Commad!!");

            alert.showAndWait();
        }


    }

    public void setGamePlay(GamePlay p_gameConfig) {
        d_gamePlay = p_gameConfig;
        d_FireCommand.setText("issue order for " + d_gamePlay.getPlayerList().get(0).getD_playerName());
        PlayerFlag = new int[d_gamePlay.getPlayerList().size()];
        Arrays.fill(PlayerFlag, 0);

        mainGameLoop();
    }

    private void mainGameLoop() {
        //Call for reinforcement
        d_gamePlay = d_gameEngineSevice.assignReinforcements(d_gamePlay);
        //issuingPlayer(d_gamePlay);

        //call for next order
    }

    /**
     * This method is used to store the user input of orders in player's lst of orders
     * @param p_command String that has been given by user
     * @return return the response in term of wether order is added or player is completed with orders or not
     */
    public CommandResponse issuingPlayer(String p_command) {
        Player l_player = d_gamePlay.getPlayerList().get(PlayCounter);
        if (p_command.equalsIgnoreCase("done")) {
            PlayerFlag[PlayCounter] = 1;
            String l_response = l_player.getD_playerName() + " : done with issuing orders";
            d_generalUtil.prepareResponse(true, l_response);
            return d_generalUtil.getResponse();
        } else {
            String[] l_commands = p_command.split("\\s+");
            DeployOrder l_dorder = new DeployOrder();
            if (l_player.getD_orders() == null) {
                List<Order> l_order = new ArrayList<Order>();

                l_dorder.setD_CountryName(l_commands[1]);
                l_dorder.setD_noOfArmies(Integer.parseInt(l_commands[2]));
                l_order.add(l_dorder);
                d_gamePlay.getPlayerList().get(PlayCounter).setD_orders(l_order);
            } else {
                l_dorder.setD_noOfArmies(Integer.parseInt(l_commands[2]));
                l_dorder.setD_CountryName(l_commands[1]);
                d_gamePlay.getPlayerList().get(PlayCounter).getD_orders().add(l_dorder);

            }
            d_generalUtil.prepareResponse(true, p_command);
            return d_generalUtil.getResponse();
        }


    }


}
