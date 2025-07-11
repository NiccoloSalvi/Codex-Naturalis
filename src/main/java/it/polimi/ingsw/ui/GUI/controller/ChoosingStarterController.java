package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.core.model.message.response.StarterSideSelectedMessage;
import it.polimi.ingsw.ui.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import it.polimi.ingsw.core.model.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ChoosingStarterController extends GUI {
    @FXML
    private Button buttonFrontSide, buttonBackSide;
    @FXML
    private ImageView frontSide, backSide;
    private CardGame starterCard;
    private Stage stage;

    private static boolean settedSide = false;

    public void initialize(){
        settedSide = false;
    }

    /**
     * Sets the starter card images in the user interface based on the provided starter card.
     *
     * @param starterCard The starter card containing front and back cover image paths.
     */
    public void setStarterCard(CardGame starterCard){
        String imageFront = starterCard.getFrontCover();
        String imageBack = starterCard.getBackCover();
        Image front = new Image(imageFront);
        Image back = new Image(imageBack);
        frontSide.setImage(front);
        backSide.setImage(back);
    }

    /**
     * Sets up event handlers for choosing the front or back side of the starter card.
     * When a button is clicked, updates the client with the chosen side and displays visual feedback.
     */
    public void chooseStarterSide(){
        //quando clicco il bottone mando update al client della scelta adottata
        buttonFrontSide.setOnAction(e -> {
            if(!settedSide){
            buttonFrontSide.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");

                viewModel.getMyStarterCard().setSide(true);
                viewModel.getMyMatrix()[40][40] = viewModel.getMyStarterCard().getId();
                client.sendMessage(new StarterSideSelectedMessage("starterSideSelected", true));

            settedSide = true;
            }
            else {
                this.showErrorPopUp("You have already chosen the side of the card", (Stage) buttonFrontSide.getScene().getWindow());
            }
        });



        buttonBackSide.setOnAction(e -> {
            if(!settedSide){
            buttonBackSide.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");

                viewModel.getMyStarterCard().setSide(false);
                viewModel.getMyMatrix()[40][40] = viewModel.getMyStarterCard().getId();
                client.sendMessage(new StarterSideSelectedMessage("starterSideSelected", false));
            settedSide = true;
            }
            else {
                this.showErrorPopUp("You have already chosen the side of the card", (Stage) buttonBackSide.getScene().getWindow());
            }
        });
    }

    public void nextscene(ActionEvent actionEvent) throws IOException {
        if(settedSide) {
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            this.changeScene("scenes/BoardScene.fxml", stage);
        }
        else {
            this.showErrorPopUp("You have to choose the side of the card", (Stage) buttonBackSide.getScene().getWindow());
        }
    }
}
