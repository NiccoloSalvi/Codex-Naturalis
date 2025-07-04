package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.chat.Message;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.model.message.response.AngleSelectedMessage;
import it.polimi.ingsw.core.model.message.response.CardSelectedMessage;
import it.polimi.ingsw.core.model.message.response.ExitGame;
import it.polimi.ingsw.core.model.message.response.SelectedDrewCard;
import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.ui.GUI.GUI;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;


public class BoardViewController extends GUI {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    ImageView iconaScreen;
    @FXML
    BorderPane boardPane;

    @FXML
    VBox playersRecapVbox;

    @FXML
    private GridPane gridPane;

    private Font myFont = Font.font("Poor Richard", 15);

    private boolean test= false;

    @FXML
    private VBox containerVBoxRight;
    @FXML
    private VBox[] playersVBoxesRecap = new VBox[4];
    @FXML
    private HBox[] playersRow1 = new HBox[4];
    @FXML
    private HBox[] playersRow2 = new HBox[4];
    @FXML
    private HBox[] playersRow3 = new HBox[4];
    @FXML
    private HBox[] hBoxesFungi = new HBox[4];
    @FXML
    private HBox[] hBoxesInsect = new HBox[4];
    @FXML
    private HBox[] hBoxesAnimals = new HBox[4];
    @FXML
    private HBox[] hBoxesPlant = new HBox[4];
    @FXML
    private HBox[] hBoxesQuill = new HBox[4];
    @FXML
    private HBox[] hBoxesManuscript = new HBox[4];
    @FXML
    private HBox[] hBoxesInkwell = new HBox[4];
    @FXML
    private Label[] numFungi = new Label[4];
    @FXML
    private Label[] numInsect = new Label[4];
    @FXML
    private Label[] numAnimals = new Label[4];
    @FXML
    private Label[] numPlant = new Label[4];
    @FXML
    private Label[] numQuill = new Label[4];
    @FXML
    private Label[] numManuscript = new Label[4];
    @FXML
    private Label[] numInkwell = new Label[4];
    @FXML
    private Label[] playersPoints = new Label[4];
    @FXML
    private Image[] playersPawn = new Image[4];
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ImageView card1ImageView, card2ImageView, card3ImageView;
    @FXML
    private ImageView obj1ImageView;
    @FXML
    private ImageView objSecretImageView;
    @FXML
    private ImageView obj2ImageView;
    @FXML
    private ImageView deckRBackImageView, deckRFront1ImageView, deckRFront2ImageView, deckRBackEco1, deckRBackEco2;
    @FXML
    private ImageView deckGBackImageView, deckGFront1ImageView, deckGFront2ImageView, deckGBackEco1, deckGBackEco2;
    @FXML
    private Button buttonDeckGBack, buttonDeckRBack, buttonDeckRFront1, buttonDeckRFront2, buttonDeckGFront1, buttonDeckGFront2;
    @FXML
    private Button buttonSide;
    @FXML
    private Button buttonCard1, buttonCard2, buttonCard3;

    @FXML
    private Button[] handButtons = {buttonCard1, buttonCard2, buttonCard3};
    @FXML
    private Button[] deckVisibleButtons ={ buttonDeckRFront1, buttonDeckRFront2, buttonDeckGFront1, buttonDeckGFront2};
    @FXML
    private Button[] deckButtons ={buttonDeckRBack, buttonDeckGBack, buttonDeckRFront1, buttonDeckRFront2, buttonDeckGFront1, buttonDeckGFront2};
    @FXML
    private ImageView[] handImages = {card1ImageView, card2ImageView, card3ImageView};
    @FXML
    private Label labelTurn;
    @FXML
    private ImageView iconChat;
    @FXML
    private ImageView icon_loading;
    @FXML
    private VBox rightVBox;
    @FXML
    private HBox centerBoardContainer;

    private static Boolean cardSelected = false;
    private static Boolean cardDrawn = false;
    private static Boolean cardPlaced = false;
    //side of the card in the hand
    private static Boolean side = true;
    private static String buttonCardSelectedId;
    private static String angleChosen;

    //useful to save temporarly the card i want to position because i need it in an another method
    //it contains also the side choosed
    private static CardGame cardToPlace;
    //where i want to position the card
    private static Integer[] positionToPlace;
    //usefull to save temporary the path of the card image
    private String cardFront;
    //usefull to save the image of the card that the player whant to play, is back or front side because I take it from its button selected
    private static CardGame cardToPlay;
    private static int numTurns = 0;

    private static ChatController chatController;
    private static OpponentsCodexController opponentsCodexController;


    /**
     * Initializes various boolean flags and variables for the controller.
     *
     * <p>This method initializes several boolean flags and variables used to track different states and selections
     * within the controller. It sets {@code test}, {@code cardSelected}, {@code cardDrawn}, {@code cardPlaced},
     * {@code side}, {@code buttonCardSelectedId}, {@code angleChosen}, and {@code numTurns} to their initial states
     * or values. This method is typically called during the initialization phase of the controller.</p>
     */
    public void initialize() {
        test = false;
        cardSelected = false;
        cardDrawn = false;
        cardPlaced = false;
        side = true;
        buttonCardSelectedId = null;
        angleChosen = null;
        numTurns = 0;
    }



    /**
     * Sets up the game board interface with initial components and data.
     *
     * <p>This method initializes and configures various UI elements on the game board, including player recap containers,
     * hand display, starter card placement, objective loading, and initial deck setup. It prepares the board for gameplay
     * by populating it with necessary components and displaying initial game state information.</p>
     *
     * <p>Specifically, it does the following:</p>
     * <ul>
     *     <li>Sets up a container for player recap information.</li>
     *     <li>Loads the player's initial hand.</li>
     *     <li>Places the player's chosen starter card on the board.</li>
     *     <li>Loads and displays the player's secret and common objectives.</li>
     *     <li>Initializes and displays initial decks of resource and gold cards.</li>
     *     <li>Displays a message indicating waiting status for other players.</li>
     *     <li>Shows a loading icon or indicator.</li>
     * </ul>
     *
     * <p>This method is typically called when initializing the game board UI to set up its initial state.</p>
     */
    public void setUpBoard() {
        VBox container = new VBox();
        container.setMaxHeight(600);
        container.setMaxWidth(200);
        container.setMinHeight(600);
        container.setMinWidth(200);
        container.alignmentProperty().set(Pos.CENTER);
        playersRecapVbox = new VBox();
        playersRecapVbox.setMaxHeight(400);
        playersRecapVbox.setMaxWidth(200);
        playersRecapVbox.setMinHeight(400);
        playersRecapVbox.setMinWidth(200);
        playersRecapVbox.getStyleClass().add("containerPoints");
        //playersRecapVbox.styleProperty().set("-fx-background-color: #E3DBB5");
        playersRecapVbox.alignmentProperty().set(Pos.CENTER);
        setPlayersRecapVbox(playersRecapVbox);
        container.getChildren().add(playersRecapVbox);
        boardPane.setLeft(container);

        //I load the first hand of the player
        this.updateHand(this.viewModel.getMyHand());
        cardToPlace = this.viewModel.getMyCodex().get(0);

        //I load the starter card choosed by the player
        this.placeCard(cardToPlace, new Integer[]{40, 40});

        //I load the objectives of the player
        String objSecretCover = this.viewModel.getSecretObj().getFrontCover();
        String obj1Cover = this.viewModel.getCommonObj().get(0).getFrontCover();
        String obj2Cover = this.viewModel.getCommonObj().get(1).getFrontCover();
        objSecretImageView.setImage(new Image(objSecretCover));
        obj1ImageView.setImage(new Image(obj1Cover));
        obj2ImageView.setImage(new Image(obj2Cover));

        //loading the initial decks
        List<Card> initialDecks = new ArrayList<>();
        initialDecks.addAll(this.viewModel.getResourceCardsVisible());
        initialDecks.addAll(this.viewModel.getGoldCardsVisible());
        initialDecks.add(this.viewModel.getDeckRBack());
        initialDecks.add(this.viewModel.getDeckGBack());
        this.updateDecks(initialDecks);

        message("Waiting for the other players...");
        loadingIcon();

    }


    /**
     * Sets up the player recap display within the specified VBox.
     *
     * <p>This method initializes and configures UI elements for displaying player-specific information, including resource counts,
     * points, and player icons. It sets up individual rows for each player, populating them with labels and icons that reflect the
     * current state of the game for each player.</p>
     *
     * <p>The recap display includes:</p>
     * <ul>
     *     <li>Player identification and associated pawn icon.</li>
     *     <li>Resource counts for animals, insects, fungi, plants, quills, manuscripts, and inkwells.</li>
     *     <li>Total points accumulated by each player.</li>
     * </ul>
     *
     * <p>The method ensures that the player's own recap is displayed in a special layout, while other players' recaps are
     * displayed differently, including buttons to view their codex.</p>
     *
     * <p>This method is typically called during the initialization of the game board UI to set up and display player-specific
     * information in a recap format.</p>
     *
     * @param playersRecapVbox The VBox container where the player recap information will be displayed.
     */
    public void setPlayersRecapVbox(VBox playersRecapVbox){
        //NB: the order in the array of labels is the same of the order of the players in the game
        for (int i=0; i<viewModel.getPlayers().size(); i++){
            numAnimals[i] = (new Label("0 "));
            numAnimals[i].setFont(myFont);
            numInsect[i] = (new Label("0 "));
            numInsect[i].setFont(myFont);
            numFungi[i] = (new Label("0 "));
            numFungi[i].setFont(myFont);
            numPlant[i] = (new Label("0 "));
            numPlant[i].setFont(myFont);
            numQuill[i] = (new Label("0 "));
            numQuill[i].setFont(myFont);
            numManuscript[i] = (new Label("0 "));
            numManuscript[i].setFont(myFont);
            numInkwell[i] = (new Label("0 "));
            numInkwell[i].setFont(myFont);
            playersPoints[i] = (new Label(" pt. : 0"));
            playersPoints[i].setFont(myFont);

            playersVBoxesRecap[i] = (new VBox());
            playersVBoxesRecap[i].alignmentProperty().set(Pos.CENTER);
            playersVBoxesRecap[i].setPrefHeight(130);
            playersVBoxesRecap[i].setPrefWidth(200);

            playersRow1[i] = (new HBox());
            playersRow1[i].alignmentProperty().set(Pos.CENTER);
            playersRow1[i].setPrefHeight(43);
            playersRow1[i].setPrefWidth(200);
            String text = "Player: " + client.getModelView().getPlayers().get(i);
            String color = client.getModelView().getPlayerPawns().get(client.getModelView().getPlayers().get(i)).toString();
            ImageView imageView = new ImageView("/images/pawn/" + color + ".png");
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            Button pawnButton = new Button();
            pawnButton.setGraphic(imageView);
            pawnButton.getStyleClass().add("icon_button_close");
            pawnButton.setUserData(client.getModelView().getPlayers().get(i));
            pawnButton.setMaxHeight(25);
            pawnButton.setMaxWidth(25);


            playersRow1[i].getChildren().add(new Label(text));
            playersRow1[i].getChildren().add(playersPoints[i]);
            playersRow1[i].getChildren().add(pawnButton);

            playersRow2[i] = (new HBox() );
            playersRow2[i].alignmentProperty().set(Pos.CENTER);
            playersRow2[i].setPrefHeight(43);
            playersRow2[i].setPrefWidth(200);

            hBoxesAnimals[i] = ( new HBox());
            imageView = new ImageView("images/resources/animal.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesAnimals[i].getChildren().add(imageView);
            hBoxesAnimals[i].getChildren().add(numAnimals[i]);

            hBoxesInsect[i] = (new HBox() );
            imageView = new ImageView("images/resources/insect.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesInsect[i].getChildren().add(imageView);
            hBoxesInsect[i].getChildren().add(numInsect[i]);

            hBoxesFungi[i] = (new HBox() );
            imageView = new ImageView("images/resources/fungi.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesFungi[i].getChildren().add(imageView);
            hBoxesFungi[i].getChildren().add(numFungi[i]);

            hBoxesPlant[i] = ( new HBox());
            imageView = new ImageView("images/resources/plant.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesPlant[i].getChildren().add(imageView);
            hBoxesPlant[i].getChildren().add(numPlant[i]);

            playersRow2[i].getChildren().add(hBoxesAnimals[i]);
            playersRow2[i].getChildren().add(hBoxesInsect[i]);
            playersRow2[i].getChildren().add(hBoxesFungi[i]);
            playersRow2[i].getChildren().add(hBoxesPlant[i]);

            playersRow3[i] = ( new HBox());
            playersRow3[i].alignmentProperty().set(Pos.CENTER);
            playersRow3[i].setPrefHeight(43);
            playersRow3[i].setPrefWidth(200);

            hBoxesQuill[i] = ( new HBox());
            imageView = new ImageView("images/resources/quill.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesQuill[i].getChildren().add(imageView);
            hBoxesQuill[i].getChildren().add(numQuill[i]);

            hBoxesManuscript[i] = (new HBox());
            imageView = new ImageView("images/resources/manuscript.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesManuscript[i].getChildren().add(imageView);
            hBoxesManuscript[i].getChildren().add(numManuscript[i]);

            hBoxesInkwell[i] = (new HBox());
            imageView = new ImageView("images/resources/inkwell.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesInkwell[i].getChildren().add(imageView);
            hBoxesInkwell[i].getChildren().add(numInkwell[i]);

            playersRow3[i].getChildren().add(hBoxesQuill[i]);
            playersRow3[i].getChildren().add(hBoxesManuscript[i]);
            playersRow3[i].getChildren().add(hBoxesInkwell[i]);

            //poi sarà se la i è uguale al numero ID del giocatore di cui è la board (client)
            //ho dovuto mettere un booleano perchè non so come mai ma entrava nell'if due volte
            if(viewModel.getPlayers().get(i).equals(viewModel.getMyUsername() ) && test==false){
                containerVBoxRight.getChildren().add(playersRow1[i]);
                containerVBoxRight.getChildren().add(playersRow2[i]);
                containerVBoxRight.getChildren().add(playersRow3[i]);
                test=true;
            }

            else if(! viewModel.getPlayers().get(i).equals(viewModel.getMyUsername() )){
                pawnButton.setOnAction(this::viewOpponentsCodex);
                playersVBoxesRecap[i].getChildren().add(playersRow1[i]);
                playersVBoxesRecap[i].getChildren().add(playersRow2[i]);
                playersVBoxesRecap[i].getChildren().add(playersRow3[i]);
                playersRecapVbox.getChildren().add(playersVBoxesRecap[i]);

            }


        }
    }

    /**
     * Displays a specified card on the game board grid at the given position.
     *
     * <p>This method adds an ImageView of the specified card to the GridPane of the game board, positioning it
     * according to the provided coordinates. The method checks whether the card is front-facing or back-facing
     * and retrieves the appropriate image to display.</p>
     *
     * <p>If the card is front-facing, its front cover image is displayed; otherwise, its back cover image is used.</p>
     *
     * <p>The displayed card image is sized to fit within the specified dimensions (83x120 pixels).</p>
     *
     * <p>Additionally, this method adjusts the scroll pane's horizontal and vertical values to center the view
     * on the newly placed card within the grid.</p>
     *
     * @param cardToPlace The card object to be displayed on the game board.
     * @param position An array containing the x and y coordinates where the card should be placed in the grid.
     */
    //dato l'id della carta,front e back e la posizione, la stampo nella board
    public void placeCard(CardGame cardToPlace, Integer[] position){
        if(cardToPlace.isFrontSide()){
            cardFront = cardToPlace.getFrontCover();
        }
        else{
            cardFront = cardToPlace.getBackCover();
        }
        ImageView image = new ImageView(new Image(cardFront));
        image.setFitHeight(83);
        image.setFitWidth(120);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(image, position[0], position[1]);


        scrollPane.setHvalue(0.5);
        scrollPane.setVvalue(0.5);

    }


    /**
     * Updates the display of decks on the game board with the provided list of cards.
     *
     * <p>This method updates the UI representation of various decks with the images of the cards provided in the
     * {@code updatedDecks} list. It sets the images for both front and back covers of the resource and gold decks,
     * as well as the action handlers and styles for buttons associated with each deck.</p>
     *
     * <p>The method retrieves specific card images from the updated decks list and assigns them to corresponding
     * ImageViews for display on the game board UI. It also configures event handlers for each button associated
     * with the decks to show error pop-ups and apply visual effects when hovered over.</p>
     *
     * @param updatedDecks A list of Card objects containing updated information for the game's resource and gold decks.
     */
    //quando chiamo questo metodo modifico la carta del mazzo (resource r oppure gold g)
    //in posizione: 0 -> girata (il back), 1 -> esposta 1, 2 -> esposta 2)
    public void updateDecks (List<Card> updatedDecks){

        for(Card c : updatedDecks){
           // System.out.println(c.getId() + "\n");
        }

        String imageCardRBack = updatedDecks.get(5).getBackCover();
        deckGBackImageView.setImage(new Image(imageCardRBack));
        deckGBackEco1.setImage(new Image(imageCardRBack));
        deckGBackEco2.setImage(new Image(imageCardRBack));
        buttonDeckGBack.setUserData(updatedDecks.get(5).getId());
        buttonDeckGBack.getStyleClass().add("buttonCard");


        String imageCardGBack = updatedDecks.get(4).getBackCover();
        deckRBackImageView.setImage(new Image(imageCardGBack));
        deckRBackEco1.setImage(new Image(imageCardGBack));
        deckRBackEco2.setImage(new Image(imageCardGBack));
        buttonDeckRBack.setUserData(updatedDecks.get(4).getId());
        buttonDeckRBack.getStyleClass().add("buttonCard");

        String imageCardRFront1 = updatedDecks.get(0).getFrontCover();
        deckRFront1ImageView.setImage(new Image(imageCardRFront1));
        buttonDeckRFront1.setUserData(updatedDecks.get(0).getId());
        buttonDeckRFront1.getStyleClass().add("buttonCard");

        String imageCardRFront2 = updatedDecks.get(1).getFrontCover();
        deckRFront2ImageView.setImage(new Image(imageCardRFront2));
        buttonDeckRFront2.setUserData(updatedDecks.get(1).getId());
        buttonDeckRFront2.getStyleClass().add("buttonCard");

        String imageCardGFront1 = updatedDecks.get(2).getFrontCover();
        deckGFront1ImageView.setImage(new Image(imageCardGFront1));
        buttonDeckGFront1.setUserData(updatedDecks.get(2).getId());
        buttonDeckGFront1.getStyleClass().add("buttonCard");

        String imageCardGFront2 = updatedDecks.get(3).getFrontCover();
        deckGFront2ImageView.setImage(new Image(imageCardGFront2));
        buttonDeckGFront2.setUserData(updatedDecks.get(3).getId());
        buttonDeckGFront2.getStyleClass().add("buttonCard");

        buttonDeckGBack.setOnAction(e -> {
            this.showErrorPopUp("You can't draw a card now", (Stage) buttonDeckGBack.getScene().getWindow());
        });
        buttonDeckGBack.setOnMouseEntered(e -> {
            buttonDeckGBack.setStyle(
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonDeckGBack.setOnMouseExited(e -> {
            buttonDeckGBack.setStyle("-fx-border-color: #4b4401 ;\n" +
                    "-fx-effect: none;");
        });
        buttonDeckRBack.setOnAction(e -> {
            this.showErrorPopUp("You can't draw a card now", (Stage) buttonDeckRBack.getScene().getWindow());
        });
        buttonDeckRBack.setOnMouseEntered(e -> {
            buttonDeckRBack.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonDeckRBack.setOnMouseExited(e -> {
            buttonDeckRBack.setStyle("-fx-border-color: #4b4401 ;\n" +
                    "-fx-effect: none;");
        });
        buttonDeckRFront1.setOnAction(e -> {
            this.showErrorPopUp("You can't draw a card now", (Stage) buttonDeckRFront1.getScene().getWindow());
        });
        buttonDeckRFront1.setOnMouseEntered(e -> {
            buttonDeckRFront1.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonDeckRFront1.setOnMouseExited(e -> {
            buttonDeckRFront1.setStyle("-fx-border-color:#4b4401 ;\n" +
                    "-fx-effect: none;");
        });
        buttonDeckRFront2.setOnAction(e -> {
            this.showErrorPopUp("You can't draw a card now", (Stage) buttonDeckRFront2.getScene().getWindow());
        });
        buttonDeckRFront2.setOnMouseEntered(e -> {
            buttonDeckRFront2.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonDeckRFront2.setOnMouseExited(e -> {
            buttonDeckRFront2.setStyle("-fx-border-color: #4b4401 ;\n" +
                    "-fx-effect: none;");
        });
        buttonDeckGFront1.setOnAction(e -> {
            this.showErrorPopUp("You can't draw a card now", (Stage) buttonDeckGFront1.getScene().getWindow());
        });
        buttonDeckGFront1.setOnMouseEntered(e -> {
            buttonDeckGFront1.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonDeckGFront1.setOnMouseExited(e -> {
            buttonDeckGFront1.setStyle("-fx-border-color: #4b4401 ;\n" +
                    "-fx-effect: none;");
        });
        buttonDeckGFront2.setOnAction(e -> {
            this.showErrorPopUp("You can't draw a card now", (Stage) buttonDeckRFront2.getScene().getWindow());
        });
        buttonDeckGFront2.setOnMouseEntered(e -> {
            buttonDeckGFront2.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonDeckGFront2.setOnMouseExited(e -> {
            buttonDeckGFront2.setStyle("-fx-border-color: #4b4401 ;\n" +
                    "-fx-effect: none;");
        });


    }

    /**
     * Toggles the full-screen mode of the application window associated with the given ActionEvent.
     *
     * <p>If the window is currently in full-screen mode, it switches to normal window mode and updates
     * the screen icon image to indicate full-screen availability. If the window is not in full-screen mode,
     * it sets the window to full-screen mode and updates the screen icon image to indicate minimizing screen.</p>
     *
     * @param actionEvent The ActionEvent triggering the method call, used to obtain the source node's window.
     */
    public void setFullScreen(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
            iconaScreen.setImage(new Image("icons/iconaFullScreen.png"));
        } else {
            stage.setFullScreen(true);
            iconaScreen.setImage(new Image("icons/iconaMinimizeScreen.png"));
        }
    }


    /**
     * Toggles the display between the front and back sides of three cards in the user interface.
     * If the button's current text is "view back side", it switches the cards to display their back sides
     * and updates the button text to "view front side". If the button's current text is "view front side",
     * it switches the cards to display their front sides and updates the button text to "view back side".
     *
     * @param actionEvent The ActionEvent triggering the method call, typically fired by a button click.
     */
    public void setSide(ActionEvent actionEvent) {
        if(buttonSide.getText().equals("view back side")){

            String imageCard1 = viewModel.getMyHand().get(0).getBackCover();
            String imageCard2 = viewModel.getMyHand().get(1).getBackCover();
            String imageCard3 = viewModel.getMyHand().get(2).getBackCover();
            viewModel.getMyHand().get(0).setSide(false);
            viewModel.getMyHand().get(1).setSide(false);
            viewModel.getMyHand().get(2).setSide(false);
            this.side = false;
            card1ImageView.setImage(new Image(imageCard1));
            card2ImageView.setImage(new Image(imageCard2));
            card3ImageView.setImage(new Image(imageCard3));

            buttonSide.setText("view front side");
        }
        else{

            String imageCard1 = viewModel.getMyHand().get(0).getFrontCover();
            String imageCard2 = viewModel.getMyHand().get(1).getFrontCover();
            String imageCard3 = viewModel.getMyHand().get(2).getFrontCover();

            viewModel.getMyHand().get(0).setSide(true);
            viewModel.getMyHand().get(1).setSide(true);
            viewModel.getMyHand().get(2).setSide(true);
            this.side = true;
            card1ImageView.setImage(new Image(imageCard1));
            card2ImageView.setImage(new Image(imageCard2));
            card3ImageView.setImage(new Image(imageCard3));

            buttonSide.setText("view back side");
        }
    }

    /**
     * Opens a scoreboard popup window when triggered by an action event, typically from a button click.
     *
     * @param actionEvent The ActionEvent triggering the method call, usually from a button click.
     */
    public void viewScoreboard(ActionEvent actionEvent) {
        Stage popUpStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        this.showScoreboardPopUp(popUpStage);
    }

    /**
     * Opens a chat popup window when triggered by an action event, typically from a button click.
     *
     * @param actionEvent The ActionEvent triggering the method call, usually from a button click.
     * @throws IOException If an error occurs while loading the chat popup.
     */
    public void visualizeChat(ActionEvent actionEvent) throws IOException {
        try{
            chatOpen = true;
            Stage popUpStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            this.viewChat(popUpStage);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the GUI to display the current player's hand of cards.
     *
     * @param hand The list of cards representing the player's hand.
     */
    public void updateHand(List<Card> hand) {
        this.side = true;
        buttonSide.setText("view back side");


        String imageCard1 = hand.get(0).getFrontCover();
        card1ImageView.setImage(new Image(imageCard1));
        //I set in the button the id con the card it is referred to
        buttonCard1.setUserData(hand.get(0).getId());
        buttonCard1.getStyleClass().clear();
        buttonCard1.getStyleClass().add("buttonCard");
        buttonCard1.setStyle("-fx-effect: none;");
        buttonCard1.setOnAction(e -> {
            this.showErrorPopUp("You can't play the card now, it's not your turn", (Stage) buttonCard1.getScene().getWindow());
        });
        buttonCard1.setOnMouseEntered(e -> {
            buttonCard1.setStyle("-fx-border-color: #e51f1f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonCard1.setOnMouseExited(e -> {
            buttonCard1.setStyle("-fx-border-color: #4b4401 ;\n" +
                    "-fx-effect: none;");
        });

        String imageCard2 = hand.get(1).getFrontCover();
        card2ImageView.setImage(new Image(imageCard2));
        buttonCard2.setUserData(hand.get(1).getId());
        buttonCard2.getStyleClass().clear();
        buttonCard2.getStyleClass().add("buttonCard");
        buttonCard2.setStyle("-fx-effect: none;");
        buttonCard2.setOnAction(e -> {
            this.showErrorPopUp("You can't play the card now, it's not your turn", (Stage) buttonCard2.getScene().getWindow());
        });
        buttonCard2.setOnMouseEntered(e -> {
            buttonCard2.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonCard2.setOnMouseExited(e -> {
            buttonCard2.setStyle("-fx-border-color:#4b4401 ;\n" +
                    "-fx-effect: none;");
        });

        String imageCard3 = hand.get(2).getFrontCover();
        card3ImageView.setImage(new Image(imageCard3));
        buttonCard3.setUserData(hand.get(2).getId());
        buttonCard3.getStyleClass().clear();
        buttonCard3.getStyleClass().add("buttonCard");
        buttonCard3.setStyle("-fx-effect: none;");
        buttonCard3.setOnAction(e -> {
            this.showErrorPopUp("You can't play the card now, it's not your turn", (Stage) buttonCard3.getScene().getWindow());
        });
        buttonCard3.setOnMouseEntered(e -> {
            buttonCard3.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonCard3.setOnMouseExited(e -> {
            buttonCard3.setStyle("-fx-border-color:#4b4401 ;\n" +
                    "-fx-effect: none;");
        });
    }

    public void onButtonPressed() {

    }

    /**
     * Allows the player to select a card to play from their hand.
     * Updates the GUI to indicate which card is selected.
     *
     * @param cardsOnlyBack Object containing information about cards that can only be played with their back side.
     */
    public void selectCardToPlay(PlayableCardIds cardsOnlyBack){
        this.cardSelected = false;
        this.message("IT'S YOUR TURN!\nSelect the card you want to play");
        //quando clicco il bottone mando update al client della scelta adottata

        buttonCard1.setOnAction(e -> {
            Boolean playableFront = true;
            if(cardsOnlyBack.getPlayingHandIdsBack().contains((int)buttonCard1.getUserData())){
                playableFront = false;
            }
            if(!cardSelected){
                buttonCard1.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                if(!playableFront && this.side){
                    this.showErrorPopUp("You can't play this card front, only its back", (Stage) buttonCard1.getScene().getWindow());
                }
                else{
                    CardSelection cs = new CardSelection((int)buttonCard1.getUserData(), this.side);
                    cardToPlay = client.getModelView().getMyHand().get(0);
                    client.getModelView().setMyPlayingCard((Card)cardToPlay, this.side);
                    buttonCardSelectedId = buttonCard1.getId();
                    client.sendMessage(new CardSelectedMessage("cardSelection", cs));
                    cardSelected = true;
                }
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonCard1.getScene().getWindow());
            }

        });
        buttonCard1.setOnMouseEntered(e -> {
            buttonCard1.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonCard1.setOnMouseExited(e -> {
            buttonCard1.setStyle("-fx-border-color: #4b4401 ;\n" +
                    "-fx-effect: none;");
        });


        buttonCard2.setOnAction(e -> {
            Boolean playableFront = true;
            if(cardsOnlyBack.getPlayingHandIdsBack().contains((int)buttonCard2.getUserData())){
                playableFront = false;
            }
            if(!cardSelected){
                buttonCard2.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                if(!playableFront && this.side){
                    this.showErrorPopUp("You can't play this card front, only its back", (Stage) buttonCard1.getScene().getWindow());
                }
                else{
                    CardSelection cs = new CardSelection((int)buttonCard2.getUserData(),this.side );
                    cardToPlay = client.getModelView().getMyHand().get(1);
                    client.getModelView().setMyPlayingCard((Card)cardToPlay, this.side);
                    buttonCardSelectedId = buttonCard2.getId();
                    client.sendMessage(new CardSelectedMessage("cardSelection", cs));
                    cardSelected = true;
                }
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonCard2.getScene().getWindow());
            }

        });
        buttonCard2.setOnMouseEntered(e -> {
            buttonCard2.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonCard2.setOnMouseExited(e -> {
            buttonCard2.setStyle("-fx-border-color: #4b4401 ;\n" +
                    "-fx-effect: none;");
        });

        buttonCard3.setOnAction(e -> {
            Boolean playableFront = true;
            if(cardsOnlyBack.getPlayingHandIdsBack().contains((int)buttonCard3.getUserData())){
                playableFront = false;
            }
            if(!cardSelected){
                if(!playableFront && this.side){
                    this.showErrorPopUp("You can't play this card front, only its back", (Stage) buttonCard1.getScene().getWindow());
                }
                else{
                    buttonCard3.setStyle("-fx-border-color: #52e51f;\n" +
                            "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                    CardSelection cs = new CardSelection((int)buttonCard3.getUserData(), this.side);
                    cardToPlay = client.getModelView().getMyHand().get(2);
                    client.getModelView().setMyPlayingCard((Card)cardToPlay, this.side);
                    buttonCardSelectedId = buttonCard3.getId();
                    client.sendMessage(new CardSelectedMessage("cardSelection", cs));
                    cardSelected = true;
                }
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonCard3.getScene().getWindow());
            }
        });
        buttonCard3.setOnMouseEntered(e -> {
            buttonCard3.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonCard3.setOnMouseExited(e -> {
            buttonCard3.setStyle("-fx-border-color: #4b4401 ;\n" +
                    "-fx-effect: none;");
        });


    }

    /**
     * Prompts the player to select a position to place a card on the game grid,
     * based on available angles.
     *
     * @param angles List of Coordinates representing available positions and angles.
     */
    public void askForAngle(List<Coordinate> angles){
        this.cardPlaced = false;
        this.message("Select where you want \n to play the card");

        if(buttonCard1.getId().equals(buttonCardSelectedId)){
            buttonCard1.getStyleClass().clear();
            buttonCard1.setStyle(
                    "    -fx-effect: dropshadow(one-pass-box,  #52e51f, 10, 0.4, 0, 0);");
        }
        else if(buttonCard2.getId().equals(buttonCardSelectedId)){
            buttonCard2.getStyleClass().clear();
            buttonCard2.setStyle(
                    "    -fx-effect: dropshadow(one-pass-box,  #52e51f, 10, 0.4, 0, 0);");
        }
        else if(buttonCard3.getId().equals(buttonCardSelectedId)){
            buttonCard3.getStyleClass().clear();
            buttonCard3.setStyle(
                    "    -fx-effect: dropshadow(one-pass-box,  #52e51f, 10, 0.4, 0, 0);");
        }
        buttonCard1.setOnAction(e -> {
            this.showErrorPopUp("You already selected a card to play", (Stage) buttonCard1.getScene().getWindow());
            });


        buttonCard1.setOnMouseEntered(e -> {
            buttonCard1.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });

        buttonCard1.setOnMouseExited(e -> {
            if(buttonCard1.getId().equals(buttonCardSelectedId)){
                buttonCard1.setStyle(
                        "    -fx-effect: dropshadow(one-pass-box,  #52e51f, 10, 0.4, 0, 0);");
            }
            else{
                buttonCard1.setStyle("-fx-border-color: #4b4401 ;\n" +
                        "-fx-effect: none;");
            }
        });


        buttonCard2.setOnAction(e -> {
            this.showErrorPopUp("You already selected a card to play", (Stage) buttonCard2.getScene().getWindow());
        });


        buttonCard2.setOnMouseEntered(e -> {
            buttonCard2.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });

        buttonCard2.setOnMouseExited(e -> {
           if(buttonCard2.getId().equals(buttonCardSelectedId)){
               buttonCard2.setStyle(
                       "    -fx-effect: dropshadow(one-pass-box,  #52e51f, 10, 0.4, 0, 0);");
           }
           else{
               buttonCard2.setStyle("-fx-border-color: #4b4401 ;\n" +
                       "-fx-effect: none;");
           }
        });


        buttonCard3.setOnAction(e -> {
            this.showErrorPopUp("You already selected a card to play", (Stage) buttonCard3.getScene().getWindow());
        });


        buttonCard3.setOnMouseEntered(e -> {
            buttonCard3.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });

        buttonCard3.setOnMouseExited(e -> {
            if(buttonCard3.getId().equals(buttonCardSelectedId)){
                buttonCard3.setStyle(
                        "    -fx-effect: dropshadow(one-pass-box,  #52e51f, 10, 0.4, 0, 0);");
            }
            else{
                buttonCard3.setStyle("-fx-border-color: #4b4401 ;\n" +
                        "-fx-effect: none;");
            }
        });

        List<Integer[]> tempButtons = new ArrayList<>();
       for(Coordinate c : angles){
           int cardToAttach = c.getX();
           int angle = c.getY();
           int[][] matrix = viewModel.getMyMatrix();
           for(int i=0; i< matrix.length; i++){
               for(int j=0; j<matrix.length; j++){
                   if(matrix[i][j] == cardToAttach){
                       positionToPlace = new Integer[]{i,j};
                   }
               }
           }
           Button placeHere = new Button();
           placeHere.getStyleClass().add("buttonCard");
           placeHere.setStyle("-fx-background-color: rgba(215,222,9,0.3);\n" +
                   "-fx-opacity: 0.8;");
           placeHere.setOnMouseEntered(e -> {
               placeHere.setStyle( "-fx-opacity: 0.5;");
           });
              placeHere.setOnMouseExited(e -> {
                placeHere.setStyle("-fx-background-color: rgba(215,222,9,0.3);\n" +
                          "-fx-opacity: 0.8;");
              });

           placeHere.setText(cardToAttach+"."+angle);
           switch (angle){
               case 0:
                   positionToPlace[0] = positionToPlace[0]-1;
                   positionToPlace[1] = positionToPlace[1]+1;
                   gridPane.add(placeHere, positionToPlace[0], positionToPlace[1]);
                   break;
               case 1:
                     positionToPlace[0] = positionToPlace[0]-1;
                     positionToPlace[1] = positionToPlace[1]-1;
                    gridPane.add(placeHere, positionToPlace[0], positionToPlace[1]);
                   break;
               case 2:
                   positionToPlace[0] = positionToPlace[0]+1;
                     positionToPlace[1] = positionToPlace[1]-1;
                     gridPane.add(placeHere, positionToPlace[0], positionToPlace[1]);
                   break;
               case 3:
                     positionToPlace[0] = positionToPlace[0]+1;
                     positionToPlace[1] = positionToPlace[1]+1;
                   gridPane.add(placeHere, positionToPlace[0], positionToPlace[1]);
                   break;
           }
           tempButtons.add(positionToPlace);
           placeHere.setUserData(positionToPlace);
              placeHere.setOnAction(e -> {
                if(!cardPlaced){
                     cardPlaced = true;
                     angleChosen = placeHere.getText();
                     client.getModelView().addCardToCodex(client.getModelView().getMyPlayingCard());
                     client.sendMessage(new AngleSelectedMessage("angleSelection", new CardToAttachSelected(angleChosen, client.getModelView().getMyCodex())));
                    this.matrixUpdated(tempButtons, (Integer[]) placeHere.getUserData());
                }
                else{
                     this.showErrorPopUp("You have already chosen the position to place the card", (Stage) placeHere.getScene().getWindow());
                }
              });
       }
    }

    /**
     * Updates the game grid after placing a card, removes temporary buttons, and updates the card images.
     *
     * @param tempButtons           List of temporary buttons to remove from the grid.
     * @param positionToPlaceCard   Position on the grid where the card is placed.
     */
    public void matrixUpdated(List<Integer[]> tempButtons, Integer[] positionToPlaceCard){
        for(Integer[] i : tempButtons){
            //System.out.println("\nbutton removed from position: "+i[0]+", "+i[1]);
            gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node).equals(i[1]) && GridPane.getColumnIndex(node).equals(i[0]));
        }
        //System.out.printf("Card placed in position: %d, %d", positionToPlaceCard[0], positionToPlaceCard[1]);
      //  System.out.printf("Card id to place: %d", cardToPlay.getId());
        placeCard(cardToPlay, positionToPlaceCard);
        client.getModelView().putInMyMatrix(positionToPlaceCard[0], positionToPlaceCard[1],cardToPlay.getId());

        if(buttonCard1.getId().equals(buttonCardSelectedId)){
            card1ImageView.setImage(null);
        }
        else if(buttonCard2.getId().equals(buttonCardSelectedId)){
            card2ImageView.setImage(null);
        }
        else if(buttonCard3.getId().equals(buttonCardSelectedId)){
            card3ImageView.setImage(null);
        }
    }

    /**
     * Allows the player to select a card to draw from available decks.
     * Updates the GUI and sends the chosen card information to the client.
     */
    public void drawFromDecks() {
        this.cardDrawn = false;
        this.message("Select the card to draw");
        //quando clicco il bottone mando update al client della scelta adottata
        buttonDeckGBack.setOnAction(e -> {
            if(!cardDrawn){
                buttonDeckGBack.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                cardDrawn = true;
                client.sendMessage(new SelectedDrewCard("drawCard", "B"));
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonCard1.getScene().getWindow());
            }

        });
        buttonDeckGBack.setOnMouseEntered(e -> {
            buttonDeckGBack.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonDeckGBack.setOnMouseExited(e -> {
            buttonDeckGBack.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });
        buttonDeckRBack.setOnAction(e -> {
            if(!cardDrawn){
                buttonDeckRBack.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                cardDrawn = true;
                client.sendMessage(new SelectedDrewCard("drawCard", "A"));
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonCard1.getScene().getWindow());
            }
        });
        buttonDeckRBack.setOnMouseEntered(e -> {
            buttonDeckRBack.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonDeckRBack.setOnMouseExited(e -> {
            buttonDeckRBack.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });

        buttonDeckRFront1.setOnAction(e -> {
            if(!cardDrawn){
                buttonDeckRFront1.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                cardDrawn = true;
                String temp = ""+buttonDeckRFront1.getUserData();
                client.sendMessage(new SelectedDrewCard("drawCard", temp));
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonDeckRFront1.getScene().getWindow());
            }
        });
        buttonDeckRFront1.setOnMouseEntered(e -> {
            buttonDeckRFront1.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonDeckRFront1.setOnMouseExited(e -> {
            buttonDeckRFront1.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });

        buttonDeckRFront2.setOnAction(e -> {
            if(!cardDrawn){
                buttonDeckRFront2.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                cardDrawn = true;
                String temp = ""+buttonDeckRFront2.getUserData();
                client.sendMessage(new SelectedDrewCard("drawCard", temp));
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonDeckRFront2.getScene().getWindow());
            }
        });
        buttonDeckRFront2.setOnMouseEntered(e -> {
            buttonDeckRFront2.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonDeckRFront2.setOnMouseExited(e -> {
            buttonDeckRFront2.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });

        buttonDeckGFront1.setOnAction(e -> {
            if(!cardDrawn){
                buttonDeckGFront1.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                cardDrawn = true;
                String temp = ""+buttonDeckGFront1.getUserData();
                client.sendMessage(new SelectedDrewCard("drawCard", temp));
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonDeckGFront1.getScene().getWindow());
            }
        });
        buttonDeckGFront1.setOnMouseEntered(e -> {
            buttonDeckGFront1.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonDeckGFront1.setOnMouseExited(e -> {
            buttonDeckGFront1.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });

        buttonDeckGFront2.setOnAction(e -> {
            if(!cardDrawn){
                buttonDeckGFront2.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                cardDrawn = true;
                String temp = ""+buttonDeckGFront2.getUserData();
                client.sendMessage(new SelectedDrewCard("drawCard", temp));
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonDeckGFront2.getScene().getWindow());
            }
        });
        buttonDeckGFront2.setOnMouseEntered(e -> {
            buttonDeckGFront2.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonDeckGFront2.setOnMouseExited(e -> {
            buttonDeckGFront2.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });
    }

    /**
     * Updates the message displayed in the turn label and manages visual elements based on the number of turns.
     *
     * @param message The message to display in the turn label.
     */
    public void message(String message){
        numTurns++;
        if(numTurns==2){
            rightVBox.getChildren().remove(icon_loading);
        }
        labelTurn.setText(message);
    }


    /**
     * Updates the player state in the UI based on the specified user target.
     *
     * @param userTarget The username of the player whose state is to be updated.
     */
    public void updatePlayerstate(String userTarget) {
        Integer numberOrder = viewModel.getPlayerOrder().get(userTarget);
        Map<Resource,Integer> resources = viewModel.getPlayerStates().get(userTarget).getPersonalResources();
        int score = viewModel.getPlayerStates().get(userTarget).getScore();
        numAnimals[numberOrder].setText(resources.get(Resource.ANIMAL)+"");
        numInsect[numberOrder].setText(resources.get(Resource.INSECT)+"");
        numFungi[numberOrder].setText(resources.get(Resource.FUNGI)+"");
        numPlant[numberOrder].setText(resources.get(Resource.PLANT)+"");
        numQuill[numberOrder].setText(resources.get(Resource.QUILL)+"");
        numManuscript[numberOrder].setText(resources.get(Resource.MANUSCRIPT)+"");
        numInkwell[numberOrder].setText(resources.get(Resource.NOUN)+"");
        playersPoints[numberOrder].setText(" pt:"+score);
    }

    /**
     * Updates the UI elements displaying the player's own state (resources and score).
     * Retrieves information from the ViewModel associated with the current user's username.
     */
    public void updateMyPlayerstate(){
        Integer numberOrder = viewModel.getPlayerOrder().get(viewModel.getMyUsername()) ;
        Map<Resource,Integer> resources = viewModel.getMyResources();
        int myScore = viewModel.getMyScore();
        numAnimals[numberOrder].setText(resources.get(Resource.ANIMAL)+"");
        numInsect[numberOrder].setText(resources.get(Resource.INSECT)+"");
        numFungi[numberOrder].setText(resources.get(Resource.FUNGI)+"");
        numPlant[numberOrder].setText(resources.get(Resource.PLANT)+"");
        numQuill[numberOrder].setText(resources.get(Resource.QUILL)+"");
        numManuscript[numberOrder].setText(resources.get(Resource.MANUSCRIPT)+"");
        numInkwell[numberOrder].setText(resources.get(Resource.NOUN)+"");
        playersPoints[numberOrder].setText("pt:"+myScore);
    }

    /**
     * Opens a new chat window as a modal dialog on top of the specified Stage.
     *
     * @param chatStage The Stage on which the chat window is based.
     * @throws IOException If an error occurs while loading the Chat.fxml file.
     */
    public void viewChat( Stage chatStage) throws IOException {
        double x = chatStage.getX();
        double y = chatStage.getY();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scenes/Chat.fxml"));
            Parent root = loader.load();
            chatController = loader.getController();
            ScaleTransition st = new ScaleTransition(javafx.util.Duration.millis(50), root);
            st.setInterpolator(Interpolator.EASE_BOTH);
            st.setFromX(0);
            st.setFromY(0);
            st.setToX(1);
            st.setToY(1);
            chatController.setImageChat(iconChat);
            Stage stage1 = new Stage();
            stage1.setTitle("Chat");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage1.initModality(Modality.APPLICATION_MODAL);
            stage1.initStyle(StageStyle.TRANSPARENT);
            stage1.setResizable(false);
            stage1.setScene(scene);
            stage1.show();
            stage1.setX(x + 150);
            stage1.setY(y + 150);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Updates the chat interface with the given message.
     * If the chat window is open, the message is passed to the chatController to update the chat display.
     * If the chat window is closed, it updates the iconChat with a notification icon.
     *
     * @param message The Message object to be displayed in the chat interface.
     */
    public void updateChat(Message message) {
        if(chatOpen){
            chatController.updateChat(message);
        }
        else{
            iconChat.setImage((new Image("icons/iconNotificationChat.png")));
        }
    }

    /**
     * Opens a popup window to display the codex of opponents based on the action event triggered by a button.
     *
     * @param actionEvent The ActionEvent triggered by clicking a button to view opponents' codex.
     */
    public void viewOpponentsCodex(ActionEvent actionEvent) {
        Stage popUpStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Button buttonCodex = (Button) actionEvent.getSource();
        double x = popUpStage.getX();
        double y = popUpStage.getY();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scenes/OpponentsCodex.fxml"));
            Parent root = loader.load();
            opponentsCodexController = loader.getController();
            ScaleTransition st = new ScaleTransition(javafx.util.Duration.millis(50), root);
            st.setInterpolator(Interpolator.EASE_BOTH);
            st.setFromX(0);
            st.setFromY(0);
            st.setToX(1);
            st.setToY(1);
            Stage stage1 = new Stage();
            stage1.setTitle("Other players' codex");
            opponentsCodexController.initialize((String) buttonCodex.getUserData());

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage1.initModality(Modality.APPLICATION_MODAL);
            stage1.initStyle(StageStyle.TRANSPARENT);
            stage1.setResizable(false);
            stage1.setScene(scene);
            stage1.show();
            stage1.setX(x + 150);
            stage1.setY(y + 150);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sets up and starts an animated loading icon.
     * Loads an image for the loading icon, configures its size, and applies a rotation animation to create a spinning effect.
     */
    private void loadingIcon() {
        icon_loading.setImage(new Image("icons/icons8-loading-80.png"));
        icon_loading.setFitHeight(30);
        icon_loading.setFitWidth(30);
        RotateTransition translate = new RotateTransition();
        translate.setNode(icon_loading);
        translate.setDuration(javafx.util.Duration.seconds(2));
        translate.setCycleCount(TranslateTransition.INDEFINITE);
        translate.setInterpolator(javafx.animation.Interpolator.LINEAR);
        translate.setByAngle(360);
        translate.play();
    }

    /**
     * Displays the end game ranking and handles UI cleanup and closure after a specified delay.
     * Clears the centerBoardContainer, constructs a VBox to display game results or waiting message,
     * and sets up a timer to close the GUI after 5 seconds.
     *
     * @param ranking The list of pairs containing player names and their scores, or null if a player left the game.
     */
    public void endGameRanking(List<Pair<String, Integer>> ranking){
      centerBoardContainer.getChildren().clear();
      VBox endGame = new VBox();
        endGame.setAlignment(Pos.CENTER);
        endGame.setSpacing(20);
        endGame.setPadding(new Insets(40,20,40,20));
        endGame.getStyleClass().add("containerPoints");
        endGame.setMaxHeight(300);
        Label endGameLabel;
        if(ranking!= null) {
            endGameLabel = new Label("Game over!\nResults:\n");
        }
        else{
            endGameLabel = new Label("A player left the game, waiting to join another game...\n");
        }
        endGameLabel.getStyleClass().add("rankLabel");
        endGameLabel.setAlignment(Pos.CENTER);
        endGameLabel.setFont(Font.font("Poor Richard", FontWeight.BOLD, 20));
        endGame.getChildren().add(endGameLabel);
        if(ranking!= null) {
            for (int i = 1; i < ranking.size() + 1; i++) {
                Label position = new Label();
                position.setWrapText(true);
                position.getStyleClass().add("rankTextArea");
                position.setText("#" + i + " " + ranking.get(i - 1).getKey() + " Points: " + ranking.get(i - 1).getValue());
                endGame.getChildren().add(position);
            }
        }
        centerBoardContainer.getChildren().add(endGame);

        if(timer!=null){
            timer.cancel();
            //System.out.println("Timer cancelled");
        }
        if(timers!=null){
            for(Timer t: timers){
                t.cancel();
            }
        }

        // Create a PauseTransition, after 5 seconds the gui closes so that the player can decide to play another game and if so also he can decide to play it in cli mode
        PauseTransition pause = new PauseTransition(Duration.seconds(5)); // 5-second delay
        pause.setOnFinished(event -> closeGui()); // Method to be called after the delay
        pause.play(); // Start the PauseTransition

    }

    /**
     * Closes the current GUI window associated with the application.
     * Retrieves the Stage of the current window using a control element (buttonDeckGBack in this case),
     * and closes the Stage to terminate the application.
     */
    public void closeGui(){
        //javafx.application.Platform.exit();
        Stage stage = (Stage) buttonDeckGBack.getScene().getWindow();
        stage.close();
    }


    /**
     * Initiates the process to exit the current game session.
     * Sends an exit game message to the server, cancels any active timers,
     * and closes the GUI window associated with the game.
     *
     * @param actionEvent The action event that triggers the exit from the game.
     */
    public void exitFromGame(ActionEvent actionEvent) {
        client.sendMessage(new ExitGame("exitGame", null));
        System.out.println("You have been disconnected from the game");
        if(timer!=null){
            timer.cancel();
            //System.out.println("Timer cancelled");
        }
        if(timers!=null){
            for(Timer t: timers){
                t.cancel();
            }
        }
        closeGui();
    }
}


