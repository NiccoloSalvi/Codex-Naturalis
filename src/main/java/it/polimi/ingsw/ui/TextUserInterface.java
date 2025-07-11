package it.polimi.ingsw.ui;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.chat.Chat;
import it.polimi.ingsw.core.model.chat.Message;
import it.polimi.ingsw.core.model.chat.MessagePrivate;
import it.polimi.ingsw.core.model.chat.MessagePrivate;
import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.model.message.response.*;
import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.network.ClientAbstract;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.util.*;

import static java.lang.Integer.parseInt;

public class TextUserInterface implements UserInterfaceStrategy {
    private Scanner scanner = new Scanner(System.in);
    private int cardWidth = 7;
    private int cardHeight = 3;
    private int matrixDimension = 81;
    private ClientAbstract gameClient;
    private int numberOfCardPlaced = 1;

    public TextUserInterface(ClientAbstract gameClient) {
        this.gameClient = gameClient;
    }

    @Override
    public void visualiseStarterCardLoaded(Card card) {
        visualizeStarterCard(card);
        // gameClient.sendMessage(new StarterSideSelectedMessage("starterSideSelected", side));
    }

    /**
     * Displays the back of a given card in the console with specific formatting.
     *
     * <p>This method determines the type of the card (ResourceCard or GoldCard) and retrieves
     * its corresponding ANSI color for display. It then prints the card's back resource and ID
     * with specific ANSI color formatting in the console.</p>
     *
     * @param card1 The card whose back is to be displayed.
     */
    @Override
    public void displayCardBack(Card card1) {
        Card card = card1 instanceof ResourceCard ? (ResourceCard) card1 : (GoldCard) card1;
        AnsiColor ANSIColor = getANSIColorForCard(card1);

        System.out.println(ANSIColor.WHITE_BACKGROUND + " " + ANSIColor + "  " + card.getBackResources().getFirst().toString().charAt(0) + "  " + AnsiColor.WHITE_BACKGROUND + " " + ANSIColor.RESET);
        if (card.getId() > 9)
            System.out.println(ANSIColor + "  " + AnsiColor.BOLD + card.getId() + "   " + AnsiColor.RESET);
        else
            System.out.println(ANSIColor + "   " + AnsiColor.BOLD + card.getId() + "   " + AnsiColor.RESET);
        System.out.println(AnsiColor.WHITE_BACKGROUND + " " + ANSIColor + "     " + AnsiColor.WHITE_BACKGROUND + " " + AnsiColor.RESET);

        System.out.println();
    }

    /**
     * Generates a formatted string representation of the resources on the card's front corners.
     *
     * <p>This method constructs a string that represents the resources on the specified corners
     * of the card. It includes specific formatting based on the card type (ResourceCard or GoldCard)
     * and the provided ANSI color. The string is used to display the card's resources with appropriate
     * formatting in the console.</p>
     *
     * @param card The card whose resources are to be displayed.
     * @param index1 The index of the first corner to be checked for resources.
     * @param index2 The index of the second corner to be checked for resources.
     * @param ANSIColor The ANSI color to be used for formatting the display.
     * @return A string representing the formatted resources on the card's front corners.
     */
    public String displayResources(Card card, int index1, int index2, AnsiColor ANSIColor) {
        String upResources = "";
        upResources += ANSIColor;

        if (card.getFrontCorners().containsKey(index1))
            if (!card.getFrontCorners().get(index1).isEmpty())
                upResources += card.getFrontCorners().get(index1).getResource().toString().charAt(0); //
            else
                upResources += ANSIColor.WHITE_BACKGROUND + " " + ANSIColor;
        else
            upResources += " ";

        if (card instanceof ResourceCard c) {
            if (index1 == 1 && c.getPoint() > 0)
                upResources += "  " + ANSIColor.YELLOW + c.getPoint() + "  " + ANSIColor.RESET;
            else
                upResources += "     ";
        } else if (card instanceof GoldCard c) {
            if (index1 == 1) {
                if (c.getPoint().getResource() != Resource.NO_RESOURCE)
                    upResources += " " + ANSIColor.YELLOW + c.getPoint().getQta() + c.getPoint().getResource().toString().charAt(0) + "  " + AnsiColor.RESET;
                else
                    upResources += "  " + ANSIColor.YELLOW + c.getPoint().getQta() + "  " + ANSIColor.RESET;
            } else if (index1 == 0) {
                if (c.getRequirements().size() == 1)
                    upResources += " " + c.getRequirements().getFirst().getQta() + c.getRequirements().getFirst().getResource().toString().charAt(0) + " ";
                else
                    for (Requirement r : c.getRequirements())
                        upResources += Integer.toString(r.getQta()) + r.getResource().toString().charAt(0);
                upResources += " ";
            }
        }

        upResources += ANSIColor;
        if (card.getFrontCorners().containsKey(index2))
            if (!card.getFrontCorners().get(index2).isEmpty())
                upResources += card.getFrontCorners().get(index2).getResource().toString().charAt(0);
            else
                upResources += ANSIColor.WHITE_BACKGROUND + " " + ANSIColor.RESET;
        else
            upResources += ANSIColor + " " + ANSIColor.RESET;

        upResources += ANSIColor.RESET;

        return upResources;
    }

    /**
     * Retrieves the appropriate ANSI color for a given card based on its color and type.
     *
     * <p>This method determines the ANSI color to be used for displaying the card
     * based on the card's color and whether it is a ResourceCard or a GoldCard. Each
     * color has a specific ANSI background, and GoldCards have distinct backgrounds
     * compared to ResourceCards.</p>
     *
     * @param card1 The card for which the ANSI color is to be determined.
     * @return The ANSI color corresponding to the card's color and type.
     */
    public AnsiColor getANSIColorForCard(Card card1) {
        AnsiColor ANSIColor;

        if (card1.getColor() == Color.RED)
            ANSIColor = card1 instanceof GoldCard ? AnsiColor.GOLD_RED_BACKGROUND : AnsiColor.RED_BACKGROUND;
        else if (card1.getColor() == Color.BLUE)
            ANSIColor = card1 instanceof GoldCard ? AnsiColor.GOLD_BLUE_BACKGROUND : AnsiColor.BLUE_BACKGROUND;
        else if (card1.getColor() == Color.PURPLE)
            ANSIColor = card1 instanceof GoldCard ? AnsiColor.GOLD_PURPLE_BACKGROUND : AnsiColor.PURPLE_BACKGROUND;
        else if (card1.getColor() == Color.GREEN)
            ANSIColor = card1 instanceof GoldCard ? AnsiColor.GOLD_GREEN_BACKGROUND : AnsiColor.GREEN_BACKGROUND;
        else
            ANSIColor = AnsiColor.YELLOW_BACKGROUND;

        return ANSIColor;
    }

    public void displayStarterCardBack(ResourceCard card) {
        System.out.println(displayResourcesStarter(card, 1, 2));
        System.out.println(AnsiColor.YELLOW_BACKGROUND + "  " + AnsiColor.BOLD + card.getId() + "   " + AnsiColor.RESET);
        System.out.println(displayResourcesStarter(card, 0, 3));
        System.out.println();
    }

    /**
     * Generates a formatted string representation of the starter resources on a ResourceCard.
     *
     * <p>This method constructs a string that represents the starter resources on the specified
     * corners of the ResourceCard. It includes specific formatting using ANSI color codes for display
     * purposes. The string is used to display the card's resources with appropriate formatting in the console.</p>
     *
     * @param card The ResourceCard whose resources are to be displayed.
     * @param index1 The index of the first corner to be checked for resources.
     * @param index2 The index of the second corner to be checked for resources.
     * @return A string representing the formatted resources on the card's starter resources.
     */
    public String displayResourcesStarter(ResourceCard card, int index1, int index2) {
        String upResources = AnsiColor.YELLOW_BACKGROUND.toString();

        if (card.getBackCorners().containsKey(index1)) {
            if (!card.getBackCorners().get(index1).isEmpty())
                upResources += card.getBackCorners().get(index1).getResource().toString().charAt(0) + " ";
            else
                upResources += AnsiColor.WHITE_BACKGROUND + " " + AnsiColor.YELLOW_BACKGROUND + " ";
        } else
            upResources += "  ";

        if (index1 == 1) {
            if (card.getBackResources().size() == 1)
                upResources += AnsiColor.YELLOW_BACKGROUND + " " + card.getBackResources().get(0).toString().charAt(0) + "  " + AnsiColor.RESET;
            else if (card.getBackResources().size() == 2)
                upResources += AnsiColor.YELLOW_BACKGROUND.toString() + card.getBackResources().get(0).toString().charAt(0) + card.getBackResources().get(1).toString().charAt(0) + "  " + AnsiColor.RESET;
            else
                upResources += AnsiColor.YELLOW_BACKGROUND.toString() + card.getBackResources().get(0).toString().charAt(0) + card.getBackResources().get(1).toString().charAt(0) + card.getBackResources().get(2).toString().charAt(0) + " " + AnsiColor.RESET;
        } else
            upResources += "    ";

        upResources += AnsiColor.YELLOW_BACKGROUND;
        if (card.getBackCorners().containsKey(index2))
            if (!card.getBackCorners().get(index2).isEmpty())
                upResources += card.getBackCorners().get(index2).getResource().toString().charAt(0);
            else
                upResources += AnsiColor.WHITE_BACKGROUND + " " + AnsiColor.RESET;
        else
            upResources += AnsiColor.YELLOW_BACKGROUND + " " + AnsiColor.RESET;

        upResources += AnsiColor.RESET;
        return upResources;
    }


    /**
     * Places a card on the game board at the specified coordinate.
     *
     * <p>This method increases the count of placed cards, determines the position and orientation
     * of the card, and updates the game board accordingly. If no coordinate is provided, the card
     * is placed at the center of the board. The card's display is updated with ANSI color formatting
     * and the card's resources and ID are set on the game board.</p>
     *
     * @param card The card to be placed on the game board.
     * @param leftUpCorner The top-left corner coordinate where the card will be placed.
     */
    public void placeCard(Card card, Coordinate leftUpCorner) {
        numberOfCardPlaced++;
        if (leftUpCorner == null)
            leftUpCorner = new Coordinate(matrixDimension / 2 * cardWidth - 5, matrixDimension / 2 * cardHeight - 5);
        int x = leftUpCorner.getX();
        int y = leftUpCorner.getY();
        boolean whichCard = true;

        Card c;
        if (card instanceof ResourceCard) {
            whichCard = false;
            c = (ResourceCard) card;
        } else {
            c = (GoldCard) card;
        }

        String ANSIColor = getANSIColorForCard(c).toString();
        for (int i = 0; i < 7; i++) {
            gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y][x + i].setCard(c);
            if (c.isFrontSide())
                gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y][x + i].setCharacter(displayResourcesNoColor(card, 1, 2).charAt(i));
            gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y][x + i].setColor(ANSIColor);

            gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y + 1][x + i].setCard(c);
            gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y + 1][x + i].setColor(ANSIColor + AnsiColor.BOLD);

            gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y + 2][x + i].setCard(c);
            if (c.isFrontSide())
                gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y + 2][x + i].setCharacter(displayResourcesNoColor(card, 0, 3).charAt(i));
            gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y + 2][x + i].setColor(ANSIColor);
        }

        if (!whichCard) {
            ResourceCard resc = (ResourceCard) card;
            if (c.isFrontSide() && resc.getPoint() > 0)
                gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y][x + 3].setColor(AnsiColor.YELLOW + ANSIColor);
        } else {
            GoldCard gold = (GoldCard) card;
            if (gold.getPoint().getResource() != Resource.NO_RESOURCE)
                gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y][x + 3].setColor(AnsiColor.YELLOW + ANSIColor);
        }

        if (!c.isFrontSide() || (c.getFrontCorners().containsKey(0) && c.getFrontCorners().get(0).isEmpty()))
            gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y + 2][x].setColor(AnsiColor.WHITE_BACKGROUND.toString());

        if (!c.isFrontSide() || (c.getFrontCorners().containsKey(1) && c.getFrontCorners().get(1).isEmpty()))
            gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y][x].setColor(AnsiColor.WHITE_BACKGROUND.toString());

        if (!c.isFrontSide() || (c.getFrontCorners().containsKey(2) && c.getFrontCorners().get(2).isEmpty()))
            gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y][x + 6].setColor(AnsiColor.WHITE_BACKGROUND.toString());

        if (!c.isFrontSide() || (c.getFrontCorners().containsKey(3) && c.getFrontCorners().get(3).isEmpty()))
            gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y + 2][x + 6].setColor(AnsiColor.WHITE_BACKGROUND.toString());

        if (!c.isFrontSide()) {
            if (c.getBackResources().size() == 1)
                gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y][x + 3].setCharacter(c.getBackResources().get(0).toString().charAt(0));
            else if (c.getBackResources().size() == 2) {
                gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y][x + 2].setCharacter(c.getBackResources().get(0).toString().charAt(0));
                gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y][x + 3].setCharacter(c.getBackResources().get(1).toString().charAt(0));
            } else {
                gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y][x + 2].setCharacter(c.getBackResources().get(0).toString().charAt(0));
                gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y][x + 3].setCharacter(c.getBackResources().get(1).toString().charAt(0));
                gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y][x + 4].setCharacter(c.getBackResources().get(2).toString().charAt(0));
            }
        }

        // fix with card ids with multiple digits
        if (c.getId() > 9) {
            String idString = String.valueOf(c.getId());
            gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y + 1][x + 2].setCharacter(idString.charAt(0));
            gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y + 1][x + 3].setCharacter(idString.charAt(1));
        } else
            gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername()) [y + 1][x + 3].setCharacter((char) (c.getId() + '0'));
    }

    /**
     * Generates a string representation of the resources on the card's front corners without ANSI color formatting.
     *
     * <p>This method constructs a string that represents the resources on the specified corners
     * of the card without any ANSI color formatting. The string is used to display the card's resources
     * in a plain text format.</p>
     *
     * @param card1 The card whose resources are to be displayed.
     * @param index1 The index of the first corner to be checked for resources.
     * @param index2 The index of the second corner to be checked for resources.
     * @return A string representing the resources on the card's front corners in plain text.
     */
    public String displayResourcesNoColor(Card card1, int index1, int index2) {
        Card card = (Card) card1;
        String upResources = "";

        if (card.getFrontCorners().containsKey(index1))
            if (!card.getFrontCorners().get(index1).isEmpty())
                upResources += card.getFrontCorners().get(index1).getResource().toString().charAt(0);
            else
                upResources += " ";
        else
            upResources += " ";

        if (card1 instanceof ResourceCard c) {
            if (index1 == 1 && c.getPoint() > 0)
                upResources += "  " + c.getPoint() + "  ";
            else
                upResources += "     ";
        } else if (card1 instanceof GoldCard c) {
            if (index1 == 1) {
                if (c.getPoint().getResource() != Resource.NO_RESOURCE)
                    upResources += " " + c.getPoint().getQta() + c.getPoint().getResource().toString().charAt(0) + "  ";
                else
                    upResources += "  " + c.getPoint().getQta() + "  ";
            } else if (index1 == 0) {
                if (c.getRequirements().size() == 1)
                    upResources += " " + c.getRequirements().getFirst().getQta() + c.getRequirements().getFirst().getResource().toString().charAt(0) + " ";
                else
                    for (Requirement r : c.getRequirements())
                        upResources += Integer.toString(r.getQta()) + r.getResource().toString().charAt(0);

                upResources += " ";
            }
        }

        if (card.getFrontCorners().containsKey(index2))
            if (!card.getFrontCorners().get(index2).isEmpty())
                upResources += card.getFrontCorners().get(index2).getResource().toString().charAt(0);
            else
                upResources += " ";
        else
            upResources += " ";

        return upResources;
    }

    /**
     * Displays the game board in the console with current card placements and colors.
     *
     * <p>This method constructs a visual representation of the game board based on the current
     * state of the player's board. It iterates over the board's matrix, appending the characters
     * and ANSI color codes for each card to a StringBuilder, which is then printed to the console.
     * The visual board includes a border and handles empty spaces appropriately.</p>
     */
    public void displayBoard() {
        String ANSI_BACKGROUND_BROWN_DARK = "\u001B[48;5;94m";  // Marrone scuro
        String ANSI_BACKGROUND_BROWN_LIGHT = "\u001B[48;5;101m"; // Marrone chiaro
        Card card;
        StringBuilder toprint = new StringBuilder();
        toprint.append("+");
        for (int i = 0; i < 2*numberOfCardPlaced*cardWidth; i++)
            toprint.append("-");
        toprint.append("+\n");

        for (int i = (matrixDimension/2-numberOfCardPlaced-1)*cardHeight; i <  (matrixDimension/2+numberOfCardPlaced)*cardHeight; i++) {
            toprint.append("|");
            for (int j = (matrixDimension/2-numberOfCardPlaced)*cardWidth; j <  (matrixDimension/2+numberOfCardPlaced)*cardWidth; j++) {
                card = gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername())[i][j].getCard();
                if (card != null)
                    toprint.append(gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername())[i][j].getColor() + gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername())[i][j].getCharacter() + AnsiColor.RESET);
                else
                    toprint.append(gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername())[i][j].getCharacter());
                    //toprint.append(ANSI_BACKGROUND_BROWN_LIGHT + gameClient.getModelView().getPlayerBoards().get(gameClient.getModelView().getMyUsername())[i][j].getCharacter());
            }
            toprint.append("|\n");
            toprint.append(AnsiColor.RESET);
        }

        toprint.append("+");
        for (int i = 0; i <  2*numberOfCardPlaced*cardWidth; i++)
            toprint.append("-");
        toprint.append("+");
        System.out.println(toprint.toString());
        gameClient.sendMessage(new updateBoards("updateBoard", toprint.toString()));
    }

    public void getBoardString(String requested) {
        Card card;
        StringBuilder toprint = new StringBuilder();
        toprint.append("+");
        for (int i = 0; i < gameClient.getModelView().getPlayerBoards().get(requested)[0].length; i++)
            toprint.append("-");
        toprint.append("+\n");

        for (int i = 0; i < gameClient.getModelView().getPlayerBoards().get(requested).length; i++) {
            toprint.append("|");
            for (int j = 0; j < gameClient.getModelView().getPlayerBoards().get(requested)[i].length; j++) {
                card = gameClient.getModelView().getPlayerBoards().get(requested)[i][j].getCard();
                if (card != null)
                    toprint.append(gameClient.getModelView().getPlayerBoards().get(requested)[i][j].getColor() + gameClient.getModelView().getPlayerBoards().get(requested)[i][j].getCharacter() + AnsiColor.RESET);
                else
                    toprint.append(gameClient.getModelView().getPlayerBoards().get(requested)[i][j].getCharacter());
            }
            toprint.append("|\n");
            toprint.append(AnsiColor.RESET);
        }

        toprint.append("+");
        for (int i = 0; i < gameClient.getModelView().getPlayerBoards().get(requested)[0].length; i++)
            toprint.append("-");
        toprint.append("+");
        System.out.println(toprint.toString());
    }

    /**
     * Prompts the player to select a card from their hand to play.
     *
     * <p>This method displays each card in the player's hand, then prompts the player to enter the ID
     * of the card they wish to play. It validates the input to ensure it matches the allowed card IDs
     * in either the front or back side format. Once a valid input is received, it returns the card
     * selection.</p>
     *
     * @param ids The PlayableCardIds object containing the IDs of cards that can be played.
     * @param hand The list of Card objects representing the player's hand.
     * @return A CardSelection object representing the player's choice of card and whether the front or back side is selected.
     */
    public CardSelection askCardSelection(PlayableCardIds ids, List<Card> hand) {
        for (Card card : hand) {
            if (ids.getPlayingHandIds().contains(card.getId()))
                displayCard(card);
            displayCardBack(card);
        }

        String message = "Inserisci l'id della carta che vuoi giocare (";
        for (int i = 0; i < ids.getPlayingHandIds().size(); i++) {
            message += ids.getPlayingHandIds().get(i) + ".f / " + ids.getPlayingHandIds().get(i) + ".b";
            if (i != ids.getPlayingHandIds().size() - 1)
                message += " / ";
        }

        if (!ids.getPlayingHandIdsBack().isEmpty())
            message += " / ";

        for (int i = 0; i < ids.getPlayingHandIdsBack().size(); i++) {
            message += ids.getPlayingHandIdsBack().get(i) + ".b";
            if (i != ids.getPlayingHandIdsBack().size() - 1)
                message += " / ";
        }

        message += "): ";
        System.out.print(message);

        boolean validInput = false;
        String input = "";
        while (!validInput) {
            input = scanner.nextLine();
            input = input.trim();

            try {
                // Verifica se l'input termina con ".b" e controlla la validità
                if (input.endsWith(".b")) {
                    int id = parseInt(input.substring(0, input.length() - 2)); // Rimuove ".b" e prova a convertire in int
                    if (ids.getPlayingHandIdsBack().contains(id) || ids.getPlayingHandIds().contains(id)) {
                        validInput = true; // L'ID è valido e nell'elenco idsBack
                    }
                } else if (input.endsWith(".f")) {
                    // Controlla se l'input è un ID valido in ids (senza ".b")
                    int id = parseInt(input.endsWith(".f") ? input.substring(0, input.length() - 2) : input); // Rimuove ".f" se presente e prova a convertire in int
                    if (ids.getPlayingHandIds().contains(id)) {
                        validInput = true; // L'ID è valido e nell'elenco ids
                    }
                }
            } catch (NumberFormatException e) {
                // Gestisce il caso in cui l'input non sia un numero
                validInput = false;
            }

            if (!validInput) {
                System.out.println("Input non valido. Riprova.");
                System.out.print(message); // Mostra nuovamente il messaggio di richiesta input
            }
        }

        String[] splitInput = input.split("\\.");

        return new CardSelection(parseInt(splitInput[0]), splitInput[1].equals("f"));
    }


    public void selectFromMenu() {
        gameClient.sendMessage(new checkConnection("checkConnection", null));
        if (gameClient.getModelView().isMyTurn() != true) {
            System.out.println("Select an option: \n");
            System.out.println("\t1. Visualize messages\n");
            System.out.println("\t2. Send message\n");
            System.out.println("\t3. Continue with the game\n");
            System.out.println("\t4. Visualize scoreboard\n");
            System.out.println("\t5. Visualize other players codex\n");
            System.out.println("\t6. Exit the game\n");
            System.out.print("> ");
            String input;
            input = scanner.nextLine();
            Boolean validInput = false;
            while (!validInput) {
                if (input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4") || input.equals("5") || input.equals("6"))
                    validInput = true;
                else {
                    System.out.println("Invalid input! Retry: ");
                    input = scanner.nextLine();
                }
            }
            switch (input) {
                case "1" -> {
                    gameClient.getModelView().setMyUnreadedMessages(0);
                    displayChat(gameClient.getModelView().getChat(), gameClient.getModelView().getMyUsername());
                    gameClient.sendMessage(new DisplayMenu("displayMenu", null));
                    //selectFromMenu();
                }
                case "2" -> {
                    System.out.print("Receiver ( All");
                    for (String user : gameClient.getModelView().getPlayers()) {
                        if (user != gameClient.getModelView().getMyUsername())
                            System.out.print(" / " + user);
                    }
                    System.out.print(" ): ");
                    input = "";
                    input = scanner.nextLine().trim();
                    while (!input.equals("All") && !gameClient.getModelView().getPlayers().contains(input)) {
                        System.out.print("Invalid Input! Retry: ");
                        input = scanner.nextLine();
                    }
                    if (input.equals("All")) {
                        System.out.print("Write a message to all: ");
                        input = "";
                        input = scanner.nextLine();
                        Message m = new Message(input, gameClient.getModelView().getMyUsername());
                        //gameClient.getModelView().getChat().addMsg(m);
                        gameClient.sendMessage(new messageBroadcast("messageToAll", m));
                    } else {
                        System.out.print("Message to " + input + ": ");
                        String text = scanner.nextLine();
                        MessagePrivate m = new MessagePrivate(text, gameClient.getModelView().getMyUsername(), input);
                        //gameClient.getModelView().getChat().addMsg(m);
                        gameClient.sendMessage(new messagePrivate("messageToUser", m));
                    }
                    gameClient.sendMessage(new DisplayMenu("displayMenu", null));
                    //selectFromMenu();
                }
                case "3" -> {
                    if (gameClient.getModelView().getMyUnreadedMessages() > 0)
                        System.out.println("New message received! You have not read it yet\n");

                    if (!gameClient.getModelView().isMyTurn())
                        System.out.println("Wait for your turn...\n");

                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                            @Override
                            public void run () {
                                try {
                                    gameClient.sendMessage(new checkConnection("checkConnection", null));
                                }catch(Exception e){
                                    timer.cancel();
                            }
                        }
                    };
                    timer.schedule(task, 0, 5000);
                }
                case "4" -> {
                    //gameClient.sendMessage(new DisplayScoreboard("displayScoreboard", null));
                    displayScoreboard();
                    gameClient.sendMessage(new DisplayMenu("displayMenu", null));
                    //selectFromMenu();
                }
                case "5" -> {
                    System.out.println("Choose the player: ");
                    for (String player : gameClient.getModelView().getPlayers()) {
                        if (!player.equals(gameClient.getModelView().getMyUsername()))
                            System.out.println(player + ", ");
                    }
                    input = "";
                    input = scanner.nextLine();
                    while (!gameClient.getModelView().getPlayers().contains(input) || input.equals(gameClient.getModelView().getMyUsername())) {
                        System.out.print("Invalid Input! Retry: ");
                        input = scanner.nextLine();
                    }
                    //List<String> usernames = new ArrayList<>();
                    System.out.println(gameClient.getModelView().getBoardToPrint().get(input));
                    gameClient.sendMessage(new DisplayMenu("displayMenu", null));
                    //selectFromMenu();
                }
                case "6" -> {
                    //disconnect player from the game
                    gameClient.sendMessage(new ExitGame("exitGame", null));
                    System.out.println("You have been disconnected from the game");
                }
            }
        }
    }


    public void displayScoreboard() {
        System.out.println("Scoreboard:\n");
        for (String player : gameClient.getModelView().getPlayers()) {
            int score;
            if(player.equals(gameClient.getModelView().getMyUsername()))
                score = gameClient.getModelView().getMyScore();
            else
                score = gameClient.getModelView().getPlayerStates().get(player).getScore();
            System.out.print(player + ": " +score + " ");
            for (int i = 0; i < score; i++) {
                System.out.print("■");
            }
            System.out.println("\n");
        }
        System.out.println();
    }

    /**
     * Displays the scoreboard in the console.
     *
     * <p>This method prints the current scores of all players in the game. It retrieves the
     * scores from the game client model view and prints each player's name along with their
     * score. For each point, a filled square (■) is printed to visually represent the score.</p>
     */
    public void displayChat(Chat chat, String username) {
        System.out.println();
        for (Message m : chat.getMsgs()) {
            if (m instanceof MessagePrivate) {
                MessagePrivate mPrivate = (MessagePrivate) m;
                if (mPrivate.getSender().equals(username))
                    System.out.println(m.getTime().getHour() + ":" + m.getTime().getMinute() + " " + " [private with "+ ((MessagePrivate)m).whoIsReceiver()  +"] you: " + m.getText());
                else
                    System.out.println(m.getTime().getHour() + ":" + m.getTime().getMinute() + " " + " [private] " + m.getSender() + ": " + m.getText());
            } else {
                if (m.getSender().equals(username))
                    System.out.println(m.getTime().getHour() + ":" + m.getTime().getMinute() + " you: " + m.getText());
                else
                    System.out.println(m.getTime().getHour() + ":" + m.getTime().getMinute() + " " + m.getSender() + ": " + m.getText());
            }
        }
        System.out.println();
    }

    /**
     * Handles the current player's turn.
     *
     * <p>This method prompts the player to select a card to play from their hand, sets the
     * selected card as the current playing card, and sends a message to the game client
     * with the selected card information.</p>
     *
     * @param ids The PlayableCardIds object containing the IDs of cards that can be played during the current turn.
     */
    public void currentTurn(PlayableCardIds ids) {
        System.out.println("It's your turn!\n");
        CardSelection cs = askCardSelection(ids, gameClient.getModelView().getMyHand());

        gameClient.getModelView().setMyPlayingCard(gameClient.getModelView().getMyHand().stream().filter(c -> c.getId() == cs.getId()).findFirst().orElse(null), cs.getSide());
        //gameClient.getModelView().getMyHand().remove(gameClient.getModelView().getMyPlayingCard());

        gameClient.sendMessage(new CardSelectedMessage("cardSelection", cs));
    }

    @Override
    public void updateDecks(List<Card> updatedDecks) {

    }

    @Override
    public void updateMyPlayerState() {
        // only for GUI
    }

    @Override
    public void updatePlayerState(String player) {
        // only for GUI
    }

    @Override
    public void lastTurn() {
        System.out.println("Last turn, play carefully!\n");
    }

    @Override
    public void endGame(List<Pair<String, Integer>> data) {
        if(data!=null) {
            System.out.println("Game over!\nResults:\n");
            for (int i = 1; i < data.size() + 1; i++) {
                System.out.println("#" + i + " " + data.get(i - 1).getKey() + " Points: " + data.get(i - 1).getValue());
            }
        }else{
            System.out.println("Game over! A player left the game\n");
        }
    }

    public void showNotYourTurn() {
        gameClient.getModelView().setMyTurn(false);
        // display message

        if (gameClient.getModelView().getMyUnreadedMessages() > 0)
            System.out.println("\nThere are " + gameClient.getModelView().getMyUnreadedMessages() + " messages you have not read\n\n");
        selectFromMenu();
    }

    public String displayAngle(List<Coordinate> angles) {
        String out = "Seleziona la carta e l'angolo (";
        for (Coordinate angle : angles) {
            out += angle.getX() + "." + angle.getY();
            if (angles.indexOf(angle) != angles.size() - 1)
                out += " / ";
        }

        System.out.print(out + "): ");

        // continue to ask for input until the input is valid and the angle is in the list
        String input = scanner.nextLine();
        while (!input.matches("\\d+\\.\\d+") || !angles.contains(new Coordinate(parseInt(input.split("\\.")[0]), parseInt(input.split("\\.")[1])))) {
            System.out.print("Input non valido, riprova: ");
            input = scanner.nextLine();
        }

        return input;
    }

    public Coordinate placeBottomRight(Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() + cardWidth - 1,
                card.getCentre().getY() + cardHeight - 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeTopRight(Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() + cardWidth - 1,
                card.getCentre().getY() - cardHeight + 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeBottomLeft(Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() - cardWidth + 1,
                card.getCentre().getY() + cardHeight - 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeTopLeft(Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() - cardWidth + 1,
                card.getCentre().getY() - cardHeight + 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public void visualizeStarterCard(Card card) {
        displayCard(card);
        displayStarterCardBack((ResourceCard) card);
    }

    public void setStarterSide() {
        System.out.println("Choose front side or back side of starter card (f/b): ");
        String input = scanner.nextLine();
        while (!input.equals("f") && !input.equals("b")) {
            System.out.print("Invalid Input! Retry: ");
            input = scanner.nextLine();
        }

        gameClient.getModelView().getMyStarterCard().setSide(input.equals("f"));
        placeCard(gameClient.getModelView().getMyStarterCard(), null);
        displayBoard();

        gameClient.sendMessage(new StarterSideSelectedMessage("starterSideSelected", input.equals("f")));
    }

    @Override
    public void displayCard(Card card) {
        Card c = card instanceof ResourceCard ? (ResourceCard) card : (GoldCard) card;
        AnsiColor ANSIColor = getANSIColorForCard(c);

        System.out.println(displayResources(c, 1, 2, ANSIColor));
        if (c.getId() > 9)
            System.out.println(ANSIColor + "  " + AnsiColor.BOLD + c.getId() + "   " + AnsiColor.RESET);
        else
            System.out.println(ANSIColor + "   " + AnsiColor.BOLD + c.getId() + "   " + AnsiColor.RESET);
        System.out.println(displayResources(c, 0, 3, ANSIColor));

        System.out.println();
    }

    public void displayObjectiveCard(Objective objective ){

        String BLACK_TEXT = "\u001B[30m";
        String WHITE_TEXT = "\u001B[37m";
        AnsiColor ANSIColor = getANSIColorForObjective(objective);
        String type = objective.getType();
        switch (type) {
            case "L" -> {
                objective = (LObjective) objective;
                for(int i= 0; i< cardHeight; i++){
                    for( int j= 0; j< cardWidth; j++) {
                        if (i == 0 && j == 3 || i == 1 && j == 3 ) {
                            System.out.print(ANSIColor + " " + ANSIColor.RESET);
                        } else if ( i == 2 && j == 4){
                            AnsiColor color = getAnsiFromColor( ((LObjective) objective).getColor2() );
                            System.out.print(color + " " + ANSIColor.RESET);
                        } else if (i==0 && j==6){
                            System.out.print(AnsiColor.WHITE_BACKGROUND + BLACK_TEXT + ((LObjective) objective).getPoints()  + AnsiColor.RESET);
                        }else {
                            System.out.print(AnsiColor.WHITE_BACKGROUND + " " + ANSIColor.RESET);
                        }
                    }
                    System.out.println();
                }
            }
            case "ReverseL" -> {
                objective = (ReverseLObjective) objective;
                for(int i= 0; i< cardHeight; i++){
                    for( int j= 0; j< cardWidth; j++) {
                        if (i == 0 && j == 3 || i == 1 && j == 3 ) {
                            System.out.print(ANSIColor + " " + ANSIColor.RESET);
                        } else if ( i == 2 && j == 2){
                            AnsiColor color = getAnsiFromColor( ((ReverseLObjective) objective).getColor2() );
                            System.out.print(color + " " + ANSIColor.RESET);
                        } else if (i==0 && j==6){
                            System.out.print(AnsiColor.WHITE_BACKGROUND + BLACK_TEXT + ((ReverseLObjective) objective).getPoints() + AnsiColor.RESET);
                        }else {
                            System.out.print(AnsiColor.WHITE_BACKGROUND + " " + ANSIColor.RESET);
                        }
                    }
                    System.out.println();
                }
            }
            case "DownL" -> {
                objective = (DownLObjective) objective;
                for(int i= 0; i< cardHeight; i++){
                    for( int j= 0; j< cardWidth; j++) {
                        if (i == 1 && j == 3 || i == 2 && j == 3 ) {
                            System.out.print(ANSIColor + " " + ANSIColor.RESET);
                        } else if ( i == 0 && j == 4){
                            AnsiColor color = getAnsiFromColor( ((DownLObjective) objective).getColor2() );
                            System.out.print(color + " " + ANSIColor.RESET);
                        } else if (i==0 && j==6){
                            System.out.print(AnsiColor.WHITE_BACKGROUND + BLACK_TEXT + ((DownLObjective) objective).getPoints() + AnsiColor.RESET);
                        }else {
                            System.out.print(AnsiColor.WHITE_BACKGROUND  + " " + ANSIColor.RESET);
                        }
                    }
                    System.out.println();
                }
            }
            case "DownReverseL" -> {
                objective = (DownReverseLObjective) objective;
                for(int i= 0; i< cardHeight; i++){
                    for( int j= 0; j< cardWidth; j++) {
                        if (i == 2 && j == 3 || i == 1 && j == 3 ) {
                            System.out.print(ANSIColor + " " + ANSIColor.RESET);
                        } else if ( i == 0 && j == 2){
                            AnsiColor color = getAnsiFromColor( ((DownReverseLObjective) objective).getColor2() );
                            System.out.print(color + " " + ANSIColor.RESET);
                        } else if (i==0 && j==6){
                            System.out.print(AnsiColor.WHITE_BACKGROUND + BLACK_TEXT + ((DownReverseLObjective) objective).getPoints() + AnsiColor.RESET);
                        }else {
                            System.out.print(AnsiColor.WHITE_BACKGROUND  + " " + ANSIColor.RESET);
                        }
                    }
                    System.out.println();
                }
            }
            case "SxDiagonal" -> {
                for(int i= 0; i< cardHeight; i++){
                    for( int j= 0; j< cardWidth; j++) {
                        if (i == 0 && j == 2 || i == 1 && j == 3 || i == 2 && j == 4) {
                            System.out.print(ANSIColor + " " + ANSIColor.RESET);
                        } else if (i==0 && j==6){
                            System.out.print(AnsiColor.WHITE_BACKGROUND + BLACK_TEXT + ((SxDiagonalObjective) objective).getPoints() + AnsiColor.RESET);
                        }else {
                            System.out.print(AnsiColor.WHITE_BACKGROUND + " " + ANSIColor.RESET);
                        }
                    }
                    System.out.println();
                }
            }
            case "DxDiagonal" -> {
                for(int i= 0; i< cardHeight; i++){
                    for( int j= 0; j< cardWidth; j++) {
                        if (i == 0 && j == 4 || i == 1 && j == 3 || i == 2 && j == 2) {
                            System.out.print(ANSIColor + " " + ANSIColor.RESET);
                        }else if (i==0 && j==6){
                        System.out.print(AnsiColor.WHITE_BACKGROUND + BLACK_TEXT + ((DxDiagonalObjective) objective).getPoints() + AnsiColor.RESET);
                        }else{
                            System.out.print(AnsiColor.WHITE_BACKGROUND  + " " + ANSIColor.RESET);
                        }
                    }
                    System.out.println();
                }
            }
            case "Resource" -> {
                objective = (ResourceObjective) objective;
                for(int i= 0; i< cardHeight; i++){
                    for( int j= 0; j< cardWidth; j++) {
                        if (i == 2 && j == 1){
                            String resource;
                            j--;
                            for (Requirement r : ((ResourceObjective) objective).getRequirements()) {
                                resource = Integer.toString(r.getQta()) + r.getResource().toString().charAt(0);
                                System.out.print(ANSIColor + WHITE_TEXT + resource + AnsiColor.RESET);
                                j++;
                                j++;
                            }
                        }else if (i==0 && j==6){
                            System.out.print(ANSIColor + WHITE_TEXT + ((ResourceObjective) objective).getPoints() + AnsiColor.RESET);
                        }else{
                            System.out.print(ANSIColor + " " + ANSIColor.RESET);
                        }
                    }
                    System.out.println();
                }
            }
            default -> {
                System.out.println("Error in type of objective");
            }
        }
        System.out.println("\n");
    }

    public AnsiColor getANSIColorForObjective(Objective card1) {
        AnsiColor ANSIColor;

        if (card1.getColor() == Color.RED)
            ANSIColor = AnsiColor.RED_BACKGROUND;
        else if (card1.getColor() == Color.BLUE)
            ANSIColor =  AnsiColor.BLUE_BACKGROUND;
        else if (card1.getColor() == Color.PURPLE)
            ANSIColor = AnsiColor.PURPLE_BACKGROUND;
        else if (card1.getColor() == Color.GREEN)
            ANSIColor = AnsiColor.GREEN_BACKGROUND;
        else
            ANSIColor = AnsiColor.YELLOW_BACKGROUND;

        return ANSIColor;
    }

    public AnsiColor getAnsiFromColor(Color color) {
        AnsiColor ANSIColor;
        if (color == Color.RED)
            ANSIColor = AnsiColor.RED_BACKGROUND;
        else if (color == Color.BLUE)
            ANSIColor =  AnsiColor.BLUE_BACKGROUND;
        else if (color == Color.PURPLE)
            ANSIColor = AnsiColor.PURPLE_BACKGROUND;
        else if (color == Color.GREEN)
            ANSIColor = AnsiColor.GREEN_BACKGROUND;
        else
            ANSIColor = AnsiColor.YELLOW_BACKGROUND;
        return ANSIColor;
    }

    public void displayCommonObjective(List<Objective> objectives) {
        System.out.println("Game's Common objectives:\n");
        for (Objective objective : objectives) {
            displayObjectiveCard(objective);
        }
    }

    public void chooseObjective(List<Objective> objectives) {
        System.out.println("Secret objectives received:");
        System.out.println();
        for (Objective objective : objectives) {
            // TODO: Fix and implement displayObjective method
            System.out.println(objective.getId());
            displayObjectiveCard(objective);
        }
        System.out.println("Choose an objective card to keep: ");
        List<String> ids = new ArrayList<>();
        for (Objective objective : objectives) {
            ids.add(Integer.toString(objective.getId()));
        }

        String cardId = scanner.nextLine();
        while (!ids.contains(cardId)) {
            System.out.println("Invalid Input! Retry: ");
            cardId = scanner.nextLine();
        }
        // get card from objective list given the id
        int finalCardId = parseInt(cardId);

        gameClient.sendMessage(new SelectedObjMessage("chooseSecretObjective", objectives.stream().filter(o -> o.getId() == finalCardId).findFirst().orElse(null)));
        // return objectives.stream().filter(o -> o.getId() == finalCardId).findFirst().orElse(null);
    }

    public void displayHand(List<Card> hand) {
        System.out.println("Your hand:\n");
        for (Card card : hand) {
            displayCard(card);
            displayCardBack(card);
        }
    }

    public void place(Card cardToPlay, Card targetCard, int cornerSelected) {
        if (cornerSelected == 0)
            placeCard(cardToPlay, placeBottomLeft(targetCard, cardToPlay));
        else if (cornerSelected == 1)
            placeCard(cardToPlay, placeTopLeft(targetCard, cardToPlay));
        else if (cornerSelected == 2)
            placeCard(cardToPlay, placeTopRight(targetCard, cardToPlay));
        else
            placeCard(cardToPlay, placeBottomRight(targetCard, cardToPlay));

    }

    public void askWhereToDraw(List<Card> cards) {
        List<Integer> ids = new ArrayList<>();
        String m = "(";
        for (Card c : cards) {
            displayCard(c);
            displayCardBack(c);
            ids.add(c.getId());
            m += c.getId() + " / ";
        }
        String mex = "Vuoi perscare una di queste carte o vuoi pescare da A (Resource) o da B (Gold)? ";
        mex += m.substring(0, m.length() - 3) + " / A / B): ";
        System.out.println(mex);

        String input = scanner.nextLine();

        Boolean validInput= false;

        while (!validInput) {
            if (input.equals("A") || input.equals("B")) {
                validInput = true;
            } else {
                try {
                    if (ids.contains(parseInt(input))) {
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    validInput = false;
                }
            }

            if (!validInput) {
                System.out.println("Input non valido. Riprova.");
                input = scanner.nextLine();
            }
        }

        gameClient.sendMessage(new SelectedDrewCard("drawCard", input));
    }

    public void displayPawn(Color pawn) {
        String red = "\033[91m";
        String green = "\033[92m";
        String yellow = "\033[93m";
        String blue = "\033[94m";
        String reset = "\033[0m";
        String colorAnsi = "";
        switch (pawn) {
            case RED -> colorAnsi = red;
            case GREEN -> colorAnsi = green;
            case YELLOW -> colorAnsi = yellow;
            case BLUE -> colorAnsi = blue;
        }
        String cube = colorAnsi + "█";
        String printPawn = String.format(
                "%s%s%s%s%s%s\n", cube, cube, cube, cube, cube, reset);
        System.out.print(printPawn);
        System.out.println("Pawn assigned: " + pawn);
    }

    public String askUsername() {
        System.out.println("Insert your username: ");
        String username = scanner.nextLine().trim();
        while (username == null || username.isEmpty()) {
            System.out.println("Invalid Input! Retry: ");
            username = scanner.nextLine().trim();
        }
        return username;
    }

    public String askJoinCreate() {
        String input = scanner.nextLine();
        while (!input.equals("join") && !input.equals("create")) {
            System.out.println("Invalid Input! Retry: ");
            input = scanner.nextLine();
        }
        return input;
    }

    public String askUI(){
        System.out.println("Choose the interface (cli/gui):");
        String input = scanner.nextLine();
        while (!input.equals("cli") && !input.equals("gui")) {
            System.out.println("Invalid Input! Retry: ");
            input = scanner.nextLine();
        }
        return input;
    }

    /**
     * Prompts the user to enter a game ID for joining or creating a game.
     *
     * <p>This method asks the user to input a game ID based on the action specified (join or create).
     * If the action is "join", it checks if the entered game ID exists in the provided list of game IDs.
     * If the action is "create", it ensures the entered game ID is not already in use. The method
     * continues to prompt the user until a valid game ID is entered.</p>
     *
     * @param joinCreate A string indicating the action, either "join" or "create".
     * @param gameIds A string containing the list of existing game IDs.
     * @return The validated game ID entered by the user.
     */
    public String askGameId(String joinCreate, String gameIds) {
        String gameId = "";
        if( joinCreate.equals("join")) {
            gameId = scanner.nextLine().trim();
            while(!gameIds.contains("ID: "+gameId)) {
                System.out.println("Invalid Input! Retry: ");
                gameId = scanner.nextLine().trim();
            }
        } else if(joinCreate.equals("create")) {
            gameId = scanner.nextLine().trim();
            while(gameIds.contains("ID: "+gameId)) {
                System.out.println("Id unavailable! Retry: ");
                gameId = scanner.nextLine().trim();
            }
        }
        return gameId;
    }

    /**
     * Prompts the user to enter the number of players for the game.
     *
     * <p>This method asks the user to input the number of players for the game. It validates the input
     * to ensure the number of players is between 2 and 4. If the input is invalid, it continues to prompt
     * the user until a valid number is entered.</p>
     *
     * @return The validated number of players entered by the user.
     */
    public int askNumberOfPlayers() {
        int numPlayers = scanner.nextInt();
        while (numPlayers < 2 || numPlayers > 4) {
            System.out.println("Invalid Input! Retry: ");
            numPlayers = scanner.nextInt();
        }
        scanner.nextLine();
        return numPlayers;
    }

    public void noFreeAngles(){
        System.out.println("No free angles available, you can't play");
    }

    /**
     * Displays the available angles for placing a card and handles the placement process.
     *
     * <p>This method shows the player the available angles for attaching their playing card,
     * removes the playing card from the player's hand, and then places the card at the selected
     * angle on the target card in the player's codex. It updates the game state and notifies
     * the game client of the selected angle.</p>
     *
     * @param angles A list of Coordinate objects representing the available angles for card placement.
     */
    public void showAvailableAngles(List<Coordinate> angles) {
        String input = displayAngle(angles);
        gameClient.getModelView().getMyHand().remove(gameClient.getModelView().getMyPlayingCard());

        // get card from player's hand by id
        // TODO: create object for handling card selection
        String[] splitCardToPlay = input.split("\\.");
        int cardToAttachId = parseInt(splitCardToPlay[0]);

        // card where to attach the selected card
        Card targetCard = gameClient.getModelView().getMyCodex().stream().filter(c -> c.getId() == cardToAttachId).findFirst().orElse(null);
        place(gameClient.getModelView().getMyPlayingCard(), targetCard, parseInt(splitCardToPlay[1]));
        gameClient.getModelView().addCardToCodex(gameClient.getModelView().getMyPlayingCard());

        displayBoard();

        gameClient.sendMessage(new AngleSelectedMessage("angleSelection", new CardToAttachSelected(input, gameClient.getModelView().getMyCodex())));
    }

    /**
     * Displays the player's personal resources in the console.
     *
     * <p>This method prints out the player's resources, showing each resource type and
     * the corresponding quantity. It iterates through the provided map of resources and
     * prints each entry.</p>
     *
     * @param resources A map where the key is a Resource object representing the type of resource,
     *                  and the value is an integer representing the quantity of that resource.
     */
    public void displayPersonalResources(Map<Resource, Integer> resources) {
        System.out.println("Your resources:\n");
        for (Map.Entry<Resource, Integer> entry : resources.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();
    }

    @Override
    public void setClient(ClientAbstract client) {

    }

    @Override
    public void setViewModel(ModelView modelView) {

    }

    @Override
    public void newChatMessage(Message message) {
    }

    public void gameStarted(){

    }
}