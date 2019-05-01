package boop.utcn.minesweeper;


import boop.utcn.minesweeper.GUI.MinesweeperGUI;

public class App
{
    public static void main( String[] args )
    {
        MinesweeperGUI minesweeperGUI = new MinesweeperGUI(9, 9, 10);
        minesweeperGUI.setVisible(true);
    }
}
