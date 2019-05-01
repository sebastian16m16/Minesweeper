package boop.utcn.minesweeper.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MinesweeperGUI extends JFrame {

    int width, height, nrOfBombs;
    int[][] blx;
    JToggleButton[][] blocks;
    boolean start = false;
    boolean gameON = true;


    private JPanel mainPanel;

    public MinesweeperGUI(final int width, final int height, int nrOfBombs){
        this.height = height;
        this.width = width;
        this.nrOfBombs = nrOfBombs;
        setSize(height*100, width*100);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(mainPanel);
        mainPanel.setLayout(new GridLayout(height, width));

        this.blocks = new JToggleButton[height][width];
        this.blx = new int[height][width];

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean found = false;
                int i= 0, j=0;
                for(i= 0; i< height; i++){
                    for(j=0; j< width; j++){
                        if(e.getSource() == blocks[i][j]){
                            found = true;
                            break;
                        }
                    }
                    if(found) break;
                }
                if(gameON) {
                    blocks[i][j].setSelected(true);
                    if (!start) {
                        blx[i][j] = -2;
                        spawnBombs(i, j);
                        start = true;

                    }
                    if(blx[i][j] != -1){
                        open(i, j);
                        reveal();
                    }else
                        lose();
                    checkWin();
                    reveal();

                }else
                    reveal();
            }
        };



        for(int i=0; i< height; i++){
            for(int j=0; j< width; j++){
                blocks[i][j] = new JToggleButton();
                blocks[i][j].setSize(mainPanel.getWidth()/width,mainPanel.getHeight()/height);
                mainPanel.add(blocks[i][j]);
                blocks[i][j].setLocation(j*mainPanel.getWidth()/width,i*mainPanel.getHeight()/height);
                blocks[i][j].addActionListener(actionListener);
            }
        }
    }

    public void open(int y, int x){
        if(y < 0 || x < 0 || x > width - 1 || y > height - 1 || blx[y][x] != 0) return;
        int bombs = 0;

        for(int i=y-1; i<=y+1; i++) {
            for (int j = x - 1; j <= x + 1; j++) {
                if (!(j < 0 || i < 0 || j > width - 1 || i > height - 1) && blx[i][j] == -1) {
                    bombs++;
                }
            }
        }

            if(bombs == 0){
                blx[y][x] = -2;
                for(int  i=y-1; i<=y+1; i++){
                    for(int j= x-1; j <=x + 1; j++) {
                        if(!(j < 0 || i < 0 || j > width -1 || i > height - 1))
                            if(i!=y || j!= x){
                                open(i,j);
                            }
                    }
                }
            }else
                blx[y][x] = bombs;
    }

    public void reveal(){
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){

                if(blx[i][j] == 0){
                    blocks[i][j].setText("");
                    blocks[i][j].setSelected(false);
                }

                if(blx[i][j] == -2){
                    blocks[i][j].setText("");
                    blocks[i][j].setSelected(true);
                }
                if(blx[i][j] > 0){
                    blocks[i][j].setText(""+blx[i][j]);
                    blocks[i][j].setSelected(true);
                }
                if(!gameON && blx[i][j] == -1){
                    blocks[i][j].setSelected(false);
                }
            }
        }
    }


    public void spawnBombs(int y, int x){
        for(int k=0; k<nrOfBombs; k++){
            int i, j;
            do{
                i = (int) (Math.random()*(width-0.01));
                j = (int) (Math.random()*(height-0.01));
            }while(blx[i][j] == -1 || (i == y && j==x));
            blx[i][j] = -1;
            blocks[i][j].setText("Boom");
        }
    }

    public void lose(){
        gameON = false;
        for(int i=0; i<height; i++){
            for(int j =0; j<width; j++){
                if(blx[i][j] == -1){
                    blocks[i][j].setText("BOOM");
                }
            }
        }
        JOptionPane.showMessageDialog(null, "GAME OVER!");
    }

    public void checkWin(){

        boolean won = true;

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(blx[i][j] == 0){
                    won = false;
                    break;
                }
            }
            if(!won) break;
        }
        if(won) JOptionPane.showMessageDialog(null, "You win!");
    }


}
