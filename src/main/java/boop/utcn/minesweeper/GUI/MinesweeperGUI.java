package boop.utcn.minesweeper.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinesweeperGUI extends JFrame {

    int width = 9, height = 9, nrOfBombs = 10;
    int[][] blx;
    JToggleButton[][] blocks;
    boolean start = false;
    boolean gameON = true;


    private JPanel mainPanel;

    public MinesweeperGUI(){


        setSize(height*100, width*100);
        setLocationRelativeTo(null);

        JPanel generalPanel = new JPanel(new BorderLayout());
        JMenuBar jMenuBar = new JMenuBar();
        JButton newGame = new JButton("New Game");
        final JTextField heightField = new JTextField();
        final JTextField widthField = new JTextField();
        final JTextField nrOfBombsField = new JTextField();
        JLabel heightLabel = new JLabel("    Height: ");
        JLabel widthLabel = new JLabel("    Width: ");
        JLabel nrOfBombsLabel = new JLabel("    Bombs: ");

        jMenuBar.add(newGame);
        jMenuBar.add(heightLabel);
        jMenuBar.add(heightField);
        jMenuBar.add(widthLabel);
        jMenuBar.add(widthField);
        jMenuBar.add(nrOfBombsLabel);
        jMenuBar.add(nrOfBombsField);

        generalPanel.add(jMenuBar, BorderLayout.NORTH);



        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(height, width));

        add(mainPanel);
        generalPanel.add(mainPanel, BorderLayout.CENTER);
        add(generalPanel);

        heightField.setText("0");
        widthField.setText("0");
        nrOfBombsField.setText("0");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.blocks = new JToggleButton[height][width];
        this.blx = new int[height][width];



        final ActionListener actionListener = new ActionListener() {
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
                        blx[i][j] = 0;
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


        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameON = true;
                height = Integer.parseInt(heightField.getText());
                width = Integer.parseInt(widthField.getText());
                nrOfBombs = Integer.parseInt(nrOfBombsField.getText());


                if(height > 9 && width > 9){
                    blx = new int[height][width];
                    blocks = new JToggleButton[height][width];
                    repaint(actionListener);
                    start = false;


                    setSize(height*100, width*100);
                    reveal();
                }else{
                    height = 9;
                    width = 9;
                    blx = new int[height][width];
                    blocks = new JToggleButton[height][width];
                    repaint(actionListener);
                    start = false;
                    setSize(height*100, width*100);
                    reveal();
                }

                if(nrOfBombs < 10){
                    nrOfBombs = 10;
                }
            }
        });



        repaint(actionListener);
    }

    public void open(int y, int x){
        int bombs = 0;

        if(y < 0 || x < 0 || x > width - 1 || y > height - 1 || blx[y][x] != 0) return;

        //check bombs near clicked box
        for(int i=y-1; i<=y+1; i++) {
            for (int j = x - 1; j <= x + 1; j++) {
                if (!(j < 0 || i < 0 || j > width - 1 || i > height - 1) && blx[i][j] == -1) {
                    bombs++; // get nr of bombs near the box
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
                if(blx[i][j] == -3){
                    blocks[i][j].setBackground(Color.red);
                    blocks[i][j].setSelected(false);
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
            }while(blx[i][j] == -1 || (i == y && j==x) || (i == y+1 && j == x+1) || (i == y-1 && j == x-1) || (i == y+1 && j == x-1) || (i == y-1 && j == x+1) ||
                    (i == y && j == x+1) || (i == y && j == x-1) || (i == y+1 && j == x) || (i == y-1 && j == x));
            blx[i][j] = -1;
            //blocks[i][j].setText("Boom");
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
        if(won) {
            JOptionPane.showMessageDialog(null, "You win!");
            gameON = false;
        }
    }

    public void repaint(ActionListener actionListener){

        mainPanel.removeAll();
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



}
