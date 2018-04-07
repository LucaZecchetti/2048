/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2048;

import java.awt.Color;
import java.awt.Image;
import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author gioele
 */
public class Sposta extends Thread {

    private int caselle;
    private JButton campo[][];
    private int ritardo;
    private int percentuale4;
    private Game1 padre;
    private String direzione;
    private boolean finito = true;
    private boolean spostamento = false;
    private JLabel punteggio;
    private long wait;
    private int numeroth;
    private int thAttiviprima;
    private JFrame window; //serve solo alla classe colore
    boolean sommable[]; //serve per sapere se la casella successiva è sommabile o meno
    
    public Sposta(JButton campo[][], int caselle, int percentuale4, long ritardo, Game1 padre, String direzione, JLabel punteggio, JFrame window){
        this.direzione = direzione;
        this.campo = campo;
        this.caselle = caselle;
        this.percentuale4 = percentuale4;
        this.wait = ritardo;
        this.padre = padre;
        this.punteggio = punteggio;
        this.window = window; //serve solo alla classe colore
        this.numeroth = padre.thAttivi;
        thAttiviprima = padre.thAttivi+1;
        sommable = new boolean[caselle];
        
        for(int i=0; i<caselle; i++)
            sommable[i] = true;
        
        start();
    }
    
    private void azzeraSommable(){
        for(int i=0; i<caselle; i++)
            sommable[i] = true;
    }
      
    private boolean giu(){
    
        for(int j=0; j<caselle; j++){
            azzeraSommable();
            for(int i=caselle-2; i>=0; i--){
                if(!campo[i][j].getText().equals("")){
                    int k=i;
                        while(k!=caselle-1 && campo[k+1][j].getText().equals("")){//sposto finche lo abbordo
                            campo[k+1][j].setText(campo[k][j].getText());
                            campo[k][j].setText("");
                            aspetta();
                            k++;
                            new Colore(campo,caselle,window,padre);
                            spostamento = true;
                        }
                        if(k!=caselle-1 && campo[k][j].getText().equals(campo[k+1][j].getText()) && sommable[k+1]){ // somma
                            campo[k+1][j].setText(Integer.parseInt(campo[k][j].getText())*2+"");
                            campo[k][j].setText("");
                            sommable[k+1] = false;
                            new Colore(campo,caselle,window,padre);
                            spostamento = true;
                            punti(k+1,j);
                        }
                        
                    }
                }
            }
        return spostamento;
        }
    
    private boolean su(){
  
        for(int j=0; j<caselle; j++){
            azzeraSommable();
            for(int i=0; i<caselle; i++){
                if(!campo[i][j].getText().equals("")){
                    int k=i;
                        while(k!=0 && campo[k-1][j].getText().equals("")){//sposto finche lo abbordo
                            campo[k-1][j].setText(campo[k][j].getText());
                            campo[k][j].setText("");
                            new Colore(campo,caselle,window,padre);
                            aspetta();
                            k--;
                            spostamento = true;
                        }
                        if(k!=0 && campo[k][j].getText().equals(campo[k-1][j].getText())  && sommable[k] ){ // somma
                            campo[k-1][j].setText(Integer.parseInt(campo[k][j].getText())*2+"");
                            campo[k][j].setText("");
                            sommable[k] = false;
                            new Colore(campo,caselle,window,padre);
                            spostamento = true;
                            punti(k-1,j);
                        }
                    }
                }
            }
        return spostamento;
    }
    
    private boolean dx(){

        for(int i=0; i<caselle; i++){
            azzeraSommable();
            for(int j=caselle-1; j>=0; j--){
                if(!campo[i][j].getText().equals("")){
                    int k=j;
                        while(k!=caselle-1 && campo[i][k+1].getText().equals("")){//sposto finche lo abbordo
                            campo[i][k+1].setText(campo[i][k].getText());
                            campo[i][k].setText("");
                            new Colore(campo,caselle,window,padre);
                            
                            aspetta();
                            k++;
                            spostamento = true;
                        }
                        if(k!=caselle-1 && campo[i][k].getText().equals(campo[i][k+1].getText()) && sommable[k] ){ // somma
                            campo[i][k+1].setText(Integer.parseInt(campo[i][k].getText())*2+"");
                            campo[i][k].setText("");
                            sommable[k] = false;
                            new Colore(campo,caselle,window,padre);
                            spostamento = true;
                            punti(i,k+1);
                        }
                    }
            }
        }
        return spostamento;
    }
    
    private boolean sx(){
        for(int i=0; i<caselle; i++){
            azzeraSommable();
            for(int j=0; j<caselle; j++){
                if(!campo[i][j].getText().equals("")){
                    int k=j;
                        while(k!=0 && campo[i][k-1].getText().equals("")){//sposto finche lo abbordo
                            campo[i][k-1].setText(campo[i][k].getText());
                            campo[i][k].setText("");
                            
                            new Colore(campo,caselle,window,padre);
                            aspetta();
                            k--;
                            spostamento = true;
                        }
                        if(k!=0 && campo[i][k].getText().equals(campo[i][k-1].getText()) && sommable[k] ){ // somma
                            campo[i][k-1].setText(Integer.parseInt(campo[i][k].getText())*2+"");
                            campo[i][k].setText("");
                            sommable[k] = false;
                            new Colore(campo,caselle,window,padre);
                            spostamento = true;
                            punti(i,k-1);
                        }
                    }
            }
        }
        return spostamento;
    }
    
    private void punti(int i, int j){
        if(punteggio.getText().equals("0")){
           punteggio.setText(campo[i][j].getText());
            }
        else
        punteggio.setText((Integer.parseInt(punteggio.getText())+Integer.parseInt(campo[i][j].getText()))+"");
    }
    
  
    private void aspetta(){

    if(padre.thAttivi >= 2)
        wait = 1; // velocizzo le animazioni nel caso ci sia una seconda richesta prima di servire la seconda richiesta
    
    
    long start = System.nanoTime();
    long end=0;
    do{
        //System.out.println("sto aspettando!");
        end = System.nanoTime();
    }while(start + (wait) >= end);
    //System.out.println(end - start);
    }
    
    
    private boolean lose(){
        boolean perso = true;
        for(int i=0; i< caselle; i++){
            for(int j=0; j<caselle; j++){
                if(campo[i][j].getText().equals(""))
                    return false;
            }
        }
        
        for(int j=0; j<caselle; j++){
            for(int i=0; i<caselle-1; i++){
                if(campo[i][j].getText().equals(campo[i+1][j].getText()))
                    return false;
            }
        }
        
        for(int i=0; i<caselle; i++){
            for(int j=0; j<caselle-1; j++){
                if(campo[i][j].getText().equals(campo[i][j+1].getText()))
                    return false;
            }
        }
        return true;
    }
    
    @Override
    public void run(){
            padre.thAttivi++;
            
            while(numeroth != 0){
                //System.out.println(padre.thAttivi+"numeroth: "+numeroth);
                if(padre.thAttivi< thAttiviprima){
                    numeroth--;
                }
                
                try {
                    sleep(1);
                } catch (InterruptedException ex) {
                    
                }
            }
            
            if(direzione.equals("giu") || direzione.equals("su") || direzione.equals("dx") || direzione.equals("sx")){ //azione per tutti le direzioni
                padre.salva();
                azzeraSommable();
                spostamento = false;
            }

            
            switch(direzione){
                case "giu":
                    if(giu())
                        padre.generaNumero();
                    break;
                case "su":
                    if(su())
                        padre.generaNumero();
                    break;
                case "dx":
                    if(dx())
                        padre.generaNumero();
                    break;
                case "sx":
                    if(sx())
                        padre.generaNumero();
                    break;
            }
            
            //System.out.println("fine");
            if(lose())
                padre.lose();
            
            padre.thAttivi--;
           
            
    }
}
