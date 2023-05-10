package com.example.circuits;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("tile")
public class Tile extends Figura{

    @Param(0)
    public int tipo;
    @Param(1)
    public int rotazione;
    @Param(2)
    public int row;
    @Param(3)
    public int col;

    public Tile() {
    }

    public Tile(int tipo, int posx, int posy, int  rot) {
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

    @Override
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public int getTipo() {
        return tipo;
    }

    @Override
    public void setRotazione(int rotazione) {
        this.rotazione = rotazione;
        this.setRotate(rotazione*90);
        if(rotazione > 0) {
            for (int i = 0; i < rotazione; i++) {
                ruotaMatrice();
            }
        }
    }

    @Override
    public int getRotazione() {
        return rotazione;
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public int getCol() {
        return col;
    }


}
