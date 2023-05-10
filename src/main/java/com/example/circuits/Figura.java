package com.example.circuits;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

@Id("figura")
public class Figura extends Rectangle {
    private static final int[][] origine = {{0, 1, 0}, {1, 5, 1}, {0, 0, 0}};
    private static final int[][] lineaT = {{0, 1, 0}, {1, 0, 1}, {0, 0, 0}};
    private static final int[][] lineaL = {{0, 1, 0}, {1, 0, 0}, {0, 0, 0}};
    private static final int[][] lineaD = {{0, 1, 0}, {0, 0, 0}, {0, 1, 0}};
    private static final int[][] luce = {{0, 1, 0}, {0, 2, 0}, {0, 0, 0}};

    @Param(0)
    public int tipo;
    @Param(1)
    public int rotazione;
    @Param(2)
    public int row;
    @Param(3)
    public int col;
    private int[][] richieste = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

    public Figura() {
    }

    public Figura(int tipo, int posx, int posy, int  rot) {
        this.tipo = tipo;
        row = posx;
        col = posy;
        assegnaImmagine(tipo);
        assegnaMatrice(tipo);
        rotazione = rot;
        this.setRotate(rotazione * 90);

        if(rotazione > 0) {
            for (int i = 0; i < rotazione; i++) {
                ruotaMatrice();
            }
        }

        this.setOnMouseClicked(e -> {
            this.setRotate(this.getRotate() + 90);
            ruotaMatrice();
            if (this.getRotate() == 360) this.setRotate(0);
        });

    }

    void assegnaImmagine(int tipo) {
        this.setFill(new ImagePattern(new Image("C:\\Users\\Orion\\IdeaProjects\\Circuits\\src\\main\\resources\\giocoPROGETTO\\" + tipo + ".JPG")));
        this.setWidth(50);
        this.setHeight(50);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(1);
    }

    void assegnaMatrice(int tipo) {
        switch (tipo) {
            case 0:
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        richieste[i][j] = origine[i][j];
                    }
                }
                break;
            case 1:
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        richieste[i][j] = lineaD[i][j];
                    }
                }
                break;
            case 2:
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        richieste[i][j] = lineaL[i][j];
                    }
                }
                break;
            case 3:
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        richieste[i][j] = lineaT[i][j];
                    }
                }
                break;
            case 4:
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        richieste[i][j] = luce[i][j];
                    }
                }
                break;
        }
    }

    public void ruotaMatrice() {
        int n = richieste.length;

        // Transpose the matrix
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int temp = richieste[i][j];
                richieste[i][j] = richieste[j][i];
                richieste[j][i] = temp;
            }
        }

        // Reverse each row
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n / 2; j++) {
                int temp = richieste[i][j];
                richieste[i][j] = richieste[i][n - j - 1];
                richieste[i][n - j - 1] = temp;
            }
        }
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
        assegnaImmagine(tipo);
        assegnaMatrice(tipo);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int x) {
        row = x;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int y) {
        col = y;
    }

    public int getRotazione() { return rotazione; }
    public void setRotazione(int rot) {
        rotazione = rot;
        this.setRotate(rotazione*90);
        if(rotazione > 0) {
            for (int i = 0; i < rotazione; i++) {
                ruotaMatrice();
            }
        }
    }
    public boolean isConnectedLeft(){
        return richieste[1][0] == 1;
    }
    public boolean isConnectedRight(){
        return richieste[1][2] == 1;
    }
    public boolean isConnectedUp(){
        return richieste[0][1] == 1;
    }
    public boolean isConnectedDown(){
        return richieste[2][1] == 1;
    }

    @Override
    public String toString() {
       return "figura("+ tipo + "," + rotazione + "," + row + "," + col + ")";
    }
}
