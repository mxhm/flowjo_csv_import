
package com.plugins;

import java.awt.*;
import java.io.*;

import com.treestar.lib.core.WorkspacePluginInterface;
import com.treestar.lib.xml.SElement;
import com.treestar.lib.parsing.interpreter.CSVReader;

import javax.swing.*;

public class CSVImport implements WorkspacePluginInterface {

    @Override
    public String getServerUrl() {
        return "http://localhost:8080/CSVImport";
    }

    @Override
    public SElement getElement() {
        SElement result = new SElement("CSVImport");
        return result;
    }

    @Override
    public boolean openWorkspace(SElement workspaceElement) {

        // show annotation import dialog when opening workspace
        showDialog(workspaceElement);

//        // add gui for debug output
//        JTextArea textArea = new JTextArea(); // Output text area
//        GuiOutputStream rawout = new GuiOutputStream(textArea);
//        // Set new stream for System.out
//        System.setOut(new PrintStream(rawout, true));
//        JFrame window = new JFrame("Console output");
//        window.add(new JScrollPane(textArea));
//        window.setSize(500, 500);
//        window.setVisible(true);

        return true;
    }

    @Override
    public void save(SElement workspaceElement) {
    }

    @Override
    public void endSession() {
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    private void showDialog(SElement workspaceElement) {

        // Creating the main window of our application
        final JFrame frame = new JFrame();

        // Release the window and quit the application when it has been closed
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        FileDialog fd = new FileDialog(frame, "Choose a csv file to import or cancel", FileDialog.LOAD);
        fd.setVisible(true);

        String filepath = fd.getDirectory() + fd.getFile();
        if (filepath == null)
            System.out.println("Not importing csv file");
        else
            try {
                System.out.println("You chose " + filepath);
                annotateFromCSV(workspaceElement, filepath);
            } catch (Exception ex) {
                // TODO not doing anything
                System.out.println("Exception reading csv file (1)");
                System.out.println(ex);
            }

        // Displaying the window
        frame.setVisible(true);
    }

    private void annotateFromCSV(SElement workspaceElement, String csvPath) {

        try {

            SElement sampleList = workspaceElement.getChild("SampleList");
            CSVReader reader = new CSVReader(new FileReader(csvPath));

            String[] nextLine;
            String[] header = new String[0];
            int lineNumber = 0;
            while ((nextLine = reader.readNext()) != null) {

                if (lineNumber == 0) {
                    header = nextLine;
                }
                lineNumber++;

                for (SElement sample : sampleList.getChildren()) {
                    // TODO #1 should build an index of SampleNodes once and then add to the correct node in linear time
                    //  not a problem right now, but will be if we iterate over thousands of samples.
                    if (sample.getChild("SampleNode").getAttribute("name").equals(nextLine[0])) {
                        for (int i = 1; i < header.length; i++) {
                            SElement keywordsNode = sample.getChild("Keywords");
                            SElement keyword = new SElement("Keyword");
                            keyword.setString("name", header[i]);
                            keyword.setString("value", nextLine[i]);
                            keywordsNode.addContent(keyword);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception reading csv file (2)");
            System.out.println(e);
        }
        // for debugging
        //System.out.println(workspaceElement);
    }

    private class GuiOutputStream extends OutputStream {
        // let's us redirect the System.out.println for debugging
        JTextArea textArea;

        public GuiOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int data) throws IOException {
            textArea.append(new String(new byte[]{(byte) data}));
        }
    }

}