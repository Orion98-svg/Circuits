package com.example.circuits;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller {
    public GridPane griglia = new GridPane();
    public Label endGame;
    public Button Reset;
    public Button Solve;
    private static Handler handler;
    public VBox box;
    public ChoiceBox livello;

    public String[] sceltaLivello = {"Facile", "Medio", "Difficile"};

    public void initialize() {


        livello.getItems().addAll(sceltaLivello);

        livello.setValue(livello.getItems().get(0));
        box.getChildren().add(0, griglia);
        gameInitialize(livello.getValue().toString());

        livello.setOnAction(this::setLivello);


    }

    public void setLivello(Event event) {
        String selezione = (String) livello.getValue();
        gameInitialize(selezione);
    }

    public void gameInitialize(String selezione) {

        int dim = 0;
        String mappa = "";

        griglia.getChildren().clear();
        griglia.setDisable(false);
        Reset.setDisable(false);
        Solve.setDisable(false);

        //scelta mappa da caricare
        if (selezione.equals("Facile")) {
            dim = 5;
            mappa = "maps";
        } else if (selezione.equals("Medio")) {
            dim = 7;
            mappa = "maps-medio";
        } else if (selezione.equals("Difficile")) {
            dim = 11;
            mappa = "maps-difficile";
        }

        endGame.setText("");

        Random rand = new Random();

        //Scelta random del file da leggere
        int randomFile = rand.nextInt(10);

        //lettura contenuto File
        String s;
        String stringa = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Orion\\IdeaProjects\\Circuits\\src\\main\\resources\\" + mappa + "\\map" + randomFile + ".txt"));
            s = br.readLine();
            System.out.println("File selezionato: " + randomFile + ".txt");
            while (s != null) {
                stringa = stringa + s + " ";
                s = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String s1[] = stringa.split(" ");

        //for loop scorrimento griglia e creazione oggetti
        Figura[][] matriceFigura = new Figura[dim][dim];

        int indice = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                String figura = s1[indice];
                int tipo = Integer.parseInt(figura);
                int rotazione = rand.nextInt(4);
                Figura f = new Figura(tipo, i, j, rotazione);
                matriceFigura[i][j] = f;
                griglia.add(f, j, i);
                if (indice < s1.length - 1) {
                    indice++;
                }
            }
        }

        griglia.setOnMouseClicked(event -> {
            if (isConnected(matriceFigura)) {
                endGame.setText("Game Solved ✔");
                griglia.setDisable(true);
                Reset.setDisable(true);
                Solve.setDisable(true);
            }
        });
    }


    public boolean isConnected(Figura[][] matrice) {
        boolean connected = false;
        int nReq = 0;
        int nCon = 0;
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[i].length; j++) {
                nReq = 0;
                nCon = 0;

                if (matrice[i][j].isConnectedUp()) {
                    nReq++;
                    if (i - 1 >= 0) {
                        if (matrice[i - 1][j].isConnectedDown()) {
                            nCon++;
                        }
                    }
                }

                if (matrice[i][j].isConnectedDown()) {
                    nReq++;
                    if (i + 1 < matrice.length) {
                        if (matrice[i + 1][j].isConnectedUp()) {
                            nCon++;
                        }
                    }
                }

                if (matrice[i][j].isConnectedLeft()) {
                    nReq++;
                    if (j - 1 >= 0) {
                        if (matrice[i][j - 1].isConnectedRight()) {
                            nCon++;
                        }
                    }
                }
                if (matrice[i][j].isConnectedRight()) {
                    nReq++;
                    if (j + 1 < matrice[i].length) {
                        if (matrice[i][j + 1].isConnectedLeft()) {
                            nCon++;
                        }
                    }
                }

                if (nReq != nCon) {
                    return false;
                }
            }
        }

        if (nReq == nCon) {

            connected = tuttoConnesso(matrice);


        }

        return connected;
    }

    public boolean tuttoConnesso(Figura[][] matrice) {

        int n_Lampadine = 0;
        int n_lamp_Conn = 0;
        Figura centro = new Figura();

        for (Figura[] f : matrice) {
            for(Figura f1: f){
                if (f1.getTipo() == 4){
                    n_Lampadine++;
                }
                if(f1.getTipo() == 0){
                    centro = f1;
                }
            }
        }

        n_lamp_Conn = getNeighbor(matrice, centro, "", n_lamp_Conn);

        if(n_Lampadine == n_lamp_Conn){
            return true;
        }

        return false;
    }

    public int getNeighbor(Figura[][] matrice, Figura fig, String dir, int num_lamp){
        int count = num_lamp;
        if(fig.getTipo() == 4){
            count++;
        }else{
            if(!dir.equals("down")){
                if(fig.isConnectedUp()){
                    Figura f = matrice[fig.getRow()-1][fig.getCol()];
                    count = getNeighbor(matrice, f, "up", count);
                }
            }
            if(!dir.equals("up")){
                if(fig.isConnectedDown()){
                    Figura f = matrice[fig.getRow()+1][fig.getCol()];
                    count = getNeighbor(matrice, f, "down", count);
                }
            }
            if(!dir.equals("right")){
                if(fig.isConnectedLeft()){
                    Figura f = matrice[fig.getRow()][fig.getCol()-1];
                    count = getNeighbor(matrice, f, "left", count);
                }
            }
            if(!dir.equals("left")){
                if(fig.isConnectedRight()){
                    Figura f = matrice[fig.getRow()][fig.getCol()+1];
                    count = getNeighbor(matrice, f, "right", count);
                }
            }
        }
        return count;
    }


    public void createGame() {
        gameInitialize(livello.getValue().toString());
    }

    public void ResetTiles(ActionEvent actionEvent) {
        griglia.getChildren().forEach(node -> {
            if (node instanceof Figura) {
                Figura f = (Figura) node;
                int rotazione = f.getRotazione();
                f.setRotate(rotazione * 90);
            }
        });
    }

    public void callAsp(ActionEvent actionEvent) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {

        handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));

        try {
            ASPMapper.getInstance().registerClass(Figura.class);
            ASPMapper.getInstance().registerClass(Tile.class);
        } catch (ObjectNotValidException | IllegalAnnotationException e1) {
            e1.printStackTrace();
        }

        InputProgram facts = new ASPInputProgram();
        InputProgram dimensione = new ASPInputProgram();
        griglia.getChildren().forEach(node -> {
            if (node instanceof Figura) {
                Figura f = (Figura) node;
                try {
                    facts.addObjectInput(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dimensione.addProgram("dim(" + griglia.getRowCount() + ").");

        handler.addProgram(facts);
        handler.addProgram(dimensione);

        InputProgram encoding = new ASPInputProgram();

        encoding.addFilesPath("encoding/asp");

        handler.addProgram(encoding);

        OptionDescriptor option = new OptionDescriptor("--filter=tile/4");

        handler.addOption(option);

        Output o = handler.startSync();
        AnswerSets answers = (AnswerSets) o;

        AnswerSet a = answers.getAnswersets().get(0);

        griglia.getChildren().forEach(node -> {
            if (node instanceof Figura) {
                Figura f = (Figura) node;
                try {
                    for (Object obj : a.getAtoms()) {
                        if (obj instanceof Tile) {
                            Tile t = (Tile) obj;
                            if (f.getRow() == t.getRow() && f.getCol() == t.getCol()) {
                                f.setRotazione(t.getRotazione());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        endGame.setText("Game Solved ✔");
        griglia.setDisable(true);
        Reset.setDisable(true);
        Solve.setDisable(true);
    }
}