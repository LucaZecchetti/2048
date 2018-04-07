/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2048;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.random;
import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Border;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author gioele
 */
public class Game1 extends JFrame implements KeyListener, Runnable{
    
    /**
     * Creates new form Game
     */
    private int caselle;
    private Container c;
    private JPanel jPanel1;
    private JButton campo[][];
    private JFrame window;
    private JLabel punteggio;
    private JLabel jLpunteggio;
    private JButton salva;
    private JButton menu;
    private JLabel gameOver;
    private int percentuale4 = 20;
    private long ritardo = 1000000;
    protected boolean spostamentoAttivo = false;
    protected int thAttivi = 0;
    private boolean isRestartable = false;
    private JFrame padre;
    

    
    public Game1(JFrame padre){
        this.padre = padre;
        this.caselle = init.getCaselle();
        addKeyListener(this);
        myInit();
        if(init.carica())
            carica();
        

    }
    
    public void carica(java.awt.event.ActionEvent evt){
        for(int i=0; i<caselle; i++){
            for(int j=0; j<caselle; j++)
                campo[i][j].setText("");
        }
        carica();
    }
 
    public void carica(){
        System.out.println("caricamento da file");
        File file = new File("partita.gio");
        if(file.exists()){
            FileReader f;
            try {
                f = new FileReader(file);
                BufferedReader b;
                b = new BufferedReader(f);
                String a;
                try {
                        if(Integer.parseInt(b.readLine()) == caselle)
                        {
                            for(int j=0; j<caselle; j++){
                                a = b.readLine();
                                int righe=0;
                                for(int i=0; i<a.length(); i++){
                                    if(a.charAt(i)==';'){
                                        int k=i-1;
                                        while(k>0 && a.charAt(k)!=';')
                                            k--;
                                        
                                        if(k!=0)
                                            k++;
                                        String txt=a.substring(k,i);
                                        if(txt.equals("-")){
                                            campo[j][righe].setText("");
                                        }
                                        else
                                            campo[j][righe].setText(txt);
                                        righe++;
                                    }
                                }
                                
                            }
                            punteggio.setText(b.readLine());
                            
                            
                        }
 
                        } catch (IOException ex) {
                            
                        }
                
            } catch (FileNotFoundException ex) {
                
            }
            
            
        }
        new Colore(campo, caselle, window, this);
        stampa();
    }
    
    public void generaNumero(){
        
        boolean valid = false;
        boolean perso = true;
        int righe=0;
        int colonne=0;
        int n=0;
        Random random = new Random();
 
        int i=0;
        while(!valid){
            i++;
            if(i==1000)
                return;
            righe = random.nextInt(caselle);
            colonne = random.nextInt(caselle);
            
            if(campo[righe][colonne].getText().equals("")){
                valid = true;
            }
        }
        if (random.nextInt(100)<percentuale4)
            n=4;
        else
            n=2;
        campo[righe][colonne].setText(""+n);
        
        new Colore(campo, caselle, window, this);
    }
    
    
    private void stampa(){
        System.out.println("----------");
        for(int i=0; i<caselle; i++){
            System.out.println("");
            for(int j=0; j<caselle; j++){
                if(campo[i][j].getText().equals(""))
                    System.out.print("-");
                else
                    System.out.print(campo[i][j].getText());
            }
        }
    }
    
    public void setOption(){
        try {
            FileReader f = new FileReader("option.gio");
            
            BufferedReader b;
            b = new BufferedReader(f);
            
            try {
                ritardo = Long.parseLong(b.readLine());
                System.out.println("ritardo da file:"+ritardo);
                percentuale4 = Integer.parseInt(b.readLine());
                System.out.println("pecentuale4:"+percentuale4);
                
            } catch (IOException ex) {
                
            }
        } catch (FileNotFoundException ex) {
            percentuale4 = 20;
            ritardo = 1000000;
        }
    }
    
    public void myInit(){
        setOption();
        window = new JFrame("2048");
        window.setVisible(true);
        c = window.getContentPane();
        if(caselle > 3)
            c.setLayout(new GridLayout(caselle+1,caselle));
        else
            c.setLayout(new GridLayout(caselle+2,caselle));
        campo = new JButton[caselle][caselle];
        
        for(int i=0; i<caselle; i++){
            for(int j=0; j<caselle; j++){
                campo[i][j] = new JButton("");
                campo[i][j].setBorderPainted( false );
                campo[i][j].setFocusPainted( false );
                campo[i][j].addKeyListener(this);
            }
        }
        
        
        for(int i=0; i<caselle; i++){
            for(int j=0; j<caselle; j++){
                c.add(campo[i][j]);
            }
        }
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(100*caselle,100*caselle+100);
        c.addKeyListener(this);
        c.setFocusable(true);
        
        jLpunteggio = new JLabel("punteggio:");
        c.add(jLpunteggio);
        
        punteggio = new JLabel("0");
        c.add(punteggio);
        
        salva = new JButton("mossa prec.");
        salva.setFocusPainted( false );
        salva.addKeyListener(this);
        salva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carica(evt);
            }
        });
        c.add(salva);
        
        menu = new JButton("menu");
        menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAction(evt);
            }
        });
        c.add(menu);
        
        //new Colore(campo, caselle, window, this);
        
        generaNumero();
        generaNumero();
        
        

    }
    
    private void menuAction(java.awt.event.ActionEvent evt){
        window.setVisible(false);
        padre.setVisible(true);
    }
    
    public void salva(){
        
        try {
            File file = new File("partita.gio");
            if(file.exists())
                file.delete();
            file.createNewFile();
            
            FileWriter fw = new FileWriter(file);
            String a="";
            a+=caselle;
            a+='\n';
            for(int i=0; i<caselle; i++){
                for(int j=0; j<caselle; j++){
                    if(campo[i][j].getText().equals(""))
                        a+="-";
                    else
                        a+=campo[i][j].getText();
                    
                    a+=";";
                }
                a+='\n';
            }
            a+=punteggio.getText();
            fw.write(a);
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            
        }
    }
    
    private void restart(){
        if(gameOver != null){
        window.remove(gameOver);
        window.validate();
        window.repaint();
                }
        setOption();
        
        c = new Container();
        window.setContentPane(c);
        if(caselle > 3)
            c.setLayout(new GridLayout(caselle+1,caselle));
        else
            c.setLayout(new GridLayout(caselle+2,caselle));
        campo = new JButton[caselle][caselle];
        
        for(int i=0; i<caselle; i++){
            for(int j=0; j<caselle; j++){
                campo[i][j] = new JButton("");
                campo[i][j].setBorderPainted( false );
                campo[i][j].setFocusPainted( false );
                campo[i][j].addKeyListener(this);
            }
        }
        
        
        for(int i=0; i<caselle; i++){
            for(int j=0; j<caselle; j++){
                c.add(campo[i][j]);
            }
        }
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(100*caselle,100*caselle+100);
        c.addKeyListener(this);
        c.setFocusable(true);
        
        jLpunteggio = new JLabel("punteggio:");
        c.add(jLpunteggio);
        
        punteggio = new JLabel("0");
        c.add(punteggio);
        
        salva = new JButton("salva stato partita");
        salva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //salvaAction(evt);
            }
        });
        c.add(salva);
        
        menu = new JButton("menu");
        menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAction(evt);
            }
        });
        c.add(menu);
        
        //new Colore(campo, caselle, window, this);

        generaNumero();
        generaNumero();
        
        

    }
    
    public void lose(){
        for(int i=0; i<caselle; i++)
            for(int j=0; j<caselle; j++)
                window.remove(campo[i][j]);
        window.remove(punteggio);
        window.remove(jLpunteggio);
        window.remove(salva);
        window.remove(menu);
        window.validate();
        window.repaint();
        Container c1 = new Container();
        gameOver = new JLabel("game over clicca Space per riniziare");
        gameOver.addKeyListener(this);
        isRestartable = true;
        if(window.getComponentCount() == 1)
        window.add(gameOver);
        window.validate();
        window.repaint();
        
    }
    

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

            Thread sposta = null;
            int keyCode = e.getKeyCode();
            
            switch(keyCode){
                case KeyEvent.VK_UP:
                    sposta = new Thread(new Sposta(campo,caselle,percentuale4,ritardo, this,"su", punteggio, window));
                    break;
                case KeyEvent.VK_DOWN:
                    sposta = new Thread(new Sposta(campo,caselle,percentuale4,ritardo, this,"giu", punteggio,window));
                    break;
                case KeyEvent.VK_LEFT:
                    sposta = new Thread(new Sposta(campo,caselle,percentuale4,ritardo, this,"sx", punteggio, window));
                    break;
                case KeyEvent.VK_RIGHT :
                    sposta = new Thread(new Sposta(campo,caselle,percentuale4,ritardo, this,"dx", punteggio, window));
                    break;
                    
                    
                case KeyEvent.VK_SPACE:
                    if(isRestartable)
                        restart();
                    break;
            }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void run() {
        
    }
}
//da fare: grafica per le opzioni, salvataggio su file, classe con le eccezioni
//note sitemare lo sleep creato da me
//note sotto funziona