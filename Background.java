import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Background extends JPanel {
    private JFrame frame;
    private JScrollPane scroller;
    private JButton backbutton;
    private String path;
    private String temppath;
    private File currentFile;
    private JPopupMenu rcwindowm;
    private JMenuItem newfile;
    private JMenuItem newfolder;
    private String OS;

    /**
     * Creates the file explorer based on the operating system
     */
    public Background(){
        frame = new JFrame("Daniel's Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700,500);

        backbutton = new JButton("Back");
        backbutton.addActionListener(new BackButtonListener());

        rcwindowm = new JPopupMenu();
        newfile = new JMenuItem("New .txt File");
        newfolder = new JMenuItem("New Folder");
        rcwindowm.add(newfile);
        rcwindowm.add(newfolder);
        newfile.addMouseListener(new NewFileListener());
        newfolder.addMouseListener(new NewFolderListener());

        scroller = new JScrollPane(this);

        setPathFromOS();
        
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.addMouseListener(new rightClickListener());

        refreshBrowser();

        frame.add(scroller);
        frame.add(BorderLayout.SOUTH, backbutton);
        frame.setVisible(true);
    }

    /**
     * Refreshes the window to display the current directory
     */
    public void refreshBrowser(){
        this.removeAll();
        currentFile = new File(path);
        for(String s: currentFile.list()){
            JButton b = new JButton(s);
            b.addActionListener(new ButtonListeners(b));
            this.add(b);
        }
        this.revalidate();
        frame.repaint();
    }

    /**
     * Sets the initial path based on the operating system
     */
    private void setPathFromOS(){
        OS = System.getProperty("os.name");
        if(OS.startsWith("Windows")){
            path = "C:/";
        }else if(OS.startsWith("Mac")){
            path = "/";
        }else if (OS.startsWith("Linux")){
            path = "/home";
        }
    }

    class rightClickListener implements MouseListener{
        @Override
        public void mouseExited(MouseEvent e){
        }

        @Override
        public void mouseEntered(MouseEvent e){
        }

        @Override
        public void mouseReleased(MouseEvent e){
        }

        /**
         * Displays the right click menu
         * @param e The mouse event triggering the event
         */
        @Override
        public void mousePressed(MouseEvent e){
            //shows the right click menu
            if(e.getButton() == MouseEvent.BUTTON3){
                rcwindowm.show(frame.getComponent(0), e.getX(), e.getY());
            }
            if(e.getButton() == MouseEvent.BUTTON1 && rcwindowm.isVisible()){
                rcwindowm.setVisible(false);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e){
        }
    }

    class ButtonListeners implements ActionListener {
        String buttontext;
        public ButtonListeners(JButton b){
            buttontext = b.getText();
        }

        @Override
        public void actionPerformed(ActionEvent a){
            temppath = path;
            path = path + buttontext + "/";
            File checkFile = new File(path);
            if(checkFile.isFile()){
                if(Desktop.isDesktopSupported()){
                    Desktop d = Desktop.getDesktop();
                    try{
                        d.open(checkFile);
                        path = temppath;
                    }catch(IOException e){
                        JOptionPane.showMessageDialog(null, "Error, this file cannot be opened");
                        path = temppath;
                        refreshBrowser();
                    }
                }
            }
            try{
                refreshBrowser();
            }catch(NullPointerException e){
                JOptionPane.showMessageDialog(null, "error, you cant access that");
                path = temppath;
                refreshBrowser();
            }
        }
    }
    class BackButtonListener implements ActionListener{
        @Override 
        public void actionPerformed(ActionEvent a){
            try{
                temppath = path;
                path = currentFile.getParentFile() + "/";
                refreshBrowser();
            }catch(NullPointerException e){
                JOptionPane.showMessageDialog(null, "You are Already in the Root Directory!");
                path = temppath;
                refreshBrowser();
            }
        }
    }
    class NewFolderListener implements MouseListener{
        @Override
        public void mouseExited(MouseEvent e){
        }

        @Override
        public void mouseEntered(MouseEvent e){
        }

        @Override
        public void mouseReleased(MouseEvent e){
        }
        @Override
        public void mousePressed(MouseEvent e){
            rcwindowm.setVisible(false);
            String nfname = JOptionPane.showInputDialog(frame, "Input New Folder Name:", null);
            if(nfname!=null && nfname.trim().length()!=0){
                new File(path+"/"+nfname).mkdirs();

                refreshBrowser();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e){
        }
    }
    class NewFileListener implements MouseListener{
        @Override
        public void mouseExited(MouseEvent e){
        }

        @Override
        public void mouseEntered(MouseEvent e){
        }

        @Override
        public void mouseReleased(MouseEvent e){
        }
        @Override
        public void mousePressed(MouseEvent e){
            rcwindowm.setVisible(false);
            String nfname = JOptionPane.showInputDialog(frame, "Input New File Name:", null);
            if(nfname!=null && nfname.trim().length()!=0){
                if(nfname.length()<4){
                    nfname = nfname + ".txt";
                }else{
                    if(!nfname.substring(nfname.length()-4).equals(".txt")){
                        nfname = nfname + ".txt";
                    }
                }
                File newfile = new File(path + "/" + nfname);
                try{
                    PrintWriter out = new PrintWriter(newfile);
                }catch(FileNotFoundException fnfe){
                }

                refreshBrowser();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e){
        }
    }

    public static void main(String[] args){
        Background b = new Background();
    }
}
