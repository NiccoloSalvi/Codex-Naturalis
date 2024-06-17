package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;

/**
 * This class represents a DownReverseL objective in the game.
 * It extends PositionObjective class.
 * It maintains the color1 and color2 of the objective.
 */
public class DownReverseLObjective extends PositionObjective {
    private Color color1;
    private Color color2;

    /**
     * Sets the first color of the objective.
     * @param color The color to set.
     */
    public void setColor1(Color color) {
        this.color1 = color;
    }

    /**
     * Sets the second color of the objective.
     * @param color The color to set.
     */
    public void setColor2(Color color) {
        this.color2 = color;
    }

    /**
     * Returns the first color of the objective.
     * @return The first color of the objective.
     */
    public Color getColor1() {
        return color1;
    }

    /**
     * Returns the second color of the objective.
     * @return The second color of the objective.
     */
    public Color getColor2() {
        return color2;
    }

    /**
     * Returns the type of the objective.
     * @return The type of the objective.
     */
    public String getType() {
        return "DownReverseL";
    }

    /**
     * Calculates the points for the given player state.
     * @param p The player state to calculate points for.
     */
    public void CalculatePoints(PlayerState p) {
        int rows = p.getMatrix().length;
        int cols = p.getMatrix()[0].length;

        for (int i = 2; i < rows ; i++) {
            for (int j = 1; j < cols; j++) {
                // per ogni carta controllo solo la diagonale in alto a dx e se non la ho giá usata
                if (p.getMatrix()[i][j] != -1 && p.getMatrix()[i-2][j] != -1 && p.getMatrix()[i-3][j-1] != -1 &&
                        p.getMatrix()[i][j] < 90 && p.getMatrix()[i-2][j] < 90 && p.getMatrix()[i-3][j-1] < 90)
                {
                    if (this.getCard(p, p.getMatrix()[i][j]).getColor() == color1 &&
                            this.getCard(p, p.getMatrix()[i -2][j]).getColor() == color1 &&
                            this.getCard(p, p.getMatrix()[i - 3][j - 1]).getColor() == color2) {
                        if (!this.getIDusati().contains(p.getMatrix()[i][j]) &&
                                !this.getIDusati().contains(p.getMatrix()[i - 2][j]) &&
                                !this.getIDusati().contains(p.getMatrix()[i - 3][j - 1])) {
                            this.addIDusato(p.getMatrix()[i][j]);
                            this.addIDusato(p.getMatrix()[i - 2][j]);
                            this.addIDusato(p.getMatrix()[i - 3][j - 1]);
                            this.setCompleted();
                        }
                    }
                }
            }
        }
        p.addScore(getCompleted() * getPoints());
        this.resetCompleted();
    }

}