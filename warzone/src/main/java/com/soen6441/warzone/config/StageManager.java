/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soen6441.warzone.config;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is used for managing different stages of application 
 * 
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class StageManager {

    private final Stage d_primaryStage;
    private final SpringFXMLLoader d_springFXMLLoader;

    /**
     * This is a parameterize controller of this class and it'll initialize
     * given values in args.
     *
     * @param p_springFXMLLoader will make object from XML file
     * @param p_stage is a current view
     */
    public StageManager(SpringFXMLLoader p_springFXMLLoader, Stage p_stage) {
        this.d_springFXMLLoader = p_springFXMLLoader;
        this.d_primaryStage = p_stage;
    }

    /**
     * This method will help to switch the scene from one to another
     *
     * @param p_view is scene to show
     * @param p_object
     */
    public void switchScene(final FxmlView p_view, Object p_object) {
        Parent l_viewRootNodeHierarchy = loadViewNodeHierarchy(p_view.getFxmlFile(), p_object);
        show(l_viewRootNodeHierarchy, p_view.getSceneTitle());
    }

    /**
     * This method will show the view of the title given in arguments and set the root node
     *
     * p_rootnode rootnode is a parent node of scene
     *
     * @param p_title is name of the view
     */
    private void show(final Parent p_rootnode, String p_title) {
        Scene l_scene = prepareScene(p_rootnode);

        d_primaryStage.setTitle(p_title);
        d_primaryStage.setScene(l_scene);
        d_primaryStage.sizeToScene();
        d_primaryStage.centerOnScreen();

        try {
            d_primaryStage.show();
        } catch (Exception exception) {
            logAndExit("Unable to show scene for title" + p_title, exception);
        }
    }

    /**
     * This method will prepare scene (view) before loading
     *
     * @param p_rootnode is parent Node of scene
     * @return scene
     */
    private Scene prepareScene(Parent p_rootnode) {
        Scene l_scene = d_primaryStage.getScene();

        if (l_scene == null) {
            l_scene = new Scene(p_rootnode);
        }
        l_scene.setRoot(p_rootnode);
        return l_scene;
    }

    /**
     * Loads the object hierarchy from a FXML document and returns to root node
     * of that hierarchy.
     *
     * @return Parent root node of the FXML document hierarchy
     */
    private Parent loadViewNodeHierarchy(String p_fxmlFilePath, Object p_object) {
        Parent l_rootNode = null;
        try {
            FXMLLoader l_loader = d_springFXMLLoader.load(p_fxmlFilePath);
            l_rootNode = l_loader.load();
        } catch (IOException exception) {
            logAndExit("Unable to load FXML view" + p_fxmlFilePath, exception);
        }
        return l_rootNode;
    }

    /**
     * This method will close the screen
     *
     * @param p_errorMsg
     * @param p_exception
     */
    private void logAndExit(String p_errorMsg, Exception p_exception) {
        Platform.exit();
    }

}