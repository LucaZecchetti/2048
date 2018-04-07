/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2048;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author gioele
 */
public class Colore extends Thread{
    
    private JButton[][] campo;
    private int caselle;
    private Thread th;
    private JFrame window;
    private Game1 padre;
    
    public Colore(JButton[][] campo, int caselle, JFrame window, Game1 padre){
        this.campo = campo;
        this.caselle = caselle;
        this.window = window;
        this.padre = padre;
        th = new Thread(this);
        th.start();
        
    }
    
    @Override
    public void run(){
        int r = 0;
        int g = 0;
        int b = 0;
        int valueColor;
        int a=0;
        int n = 1;
        
       
                for(int i=0; i<caselle; i++){
                    for(int j=0; j<caselle; j++){
                        try{
                        n=Integer.parseInt(campo[i][j].getText());
                    }catch(java.lang.NumberFormatException e){
                            }
                        double t= n;
                        double grand = 1;
                        double acc = 0.10;
                        while(t/10 > 1){
                            t = t/10;
                            acc = acc*2.3;
                            grand+=acc;
                        }
                        campo[i][j].setFont(new Font("Arial",Font.BOLD, (int)(window.getHeight()/caselle/2/grand)));
                    }
                }
            
            try{
            for(int i=0; i<caselle; i++){
                for(int j=0; j<caselle; j++){
                    if(!campo[i][j].getText().equals("")){
                        valueColor = Integer.parseInt(campo[i][j].getText());
                        valueColor *= 50; // indice di aumento del colore
                        if(valueColor >765){
                            r = 255;
                            g = 255;
                            b = 255;
                            campo[i][j].setForeground(Color.white);
                        }
                        else if(valueColor > 510){
                            r = 255;
                            g = 255;
                            b = valueColor - 510;
                            campo[i][j].setForeground(Color.white);
                        }
                        else if(valueColor > 255){
                            r = 255;
                            g = valueColor - 255;
                            b = 0;
                            campo[i][j].setForeground(Color.black);
                        }
                        else{
                            campo[i][j].setForeground(Color.black);
                            r = valueColor;
                            g = 0;
                            b = 0;
                        }
                        // per testing campo[i][j].setText(Integer.parseInt(campo[i][j].getText())*2+"");
                        r= Math.abs(r-255);
                        g= Math.abs(g-255);
                        b= Math.abs(b-255);
                        Color c = new Color(r,g,b);
                        campo[i][j].setBackground(c);
                    }
                    else
                        campo[i][j].setBackground(Color.white);
                }
            }
        }
        catch(java.lang.NumberFormatException e){}
        }

        }
    

