package io.github.isaac.gui;


import io.github.isaac.listeners.IDialogInterface;
import io.github.isaac.model.DataModel;

import javax.swing.*;
import java.awt.event.*;

public class CreateGenerationDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldStringName;
    private JTextField textFieldSimpleChinese;
    private JTextField textFieldTraditionalChinese;
    private JTextField textFieldEnglish;

    private IDialogInterface mDialogInterface;

    public CreateGenerationDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setDialogInterface(IDialogInterface dialogInterface) {
        if (dialogInterface == null) {
            return;
        }
        mDialogInterface = dialogInterface;
    }

    private void onOK() {
        if(mDialogInterface != null) {
            DataModel dataModel = new DataModel(textFieldStringName.getText(),
                    textFieldSimpleChinese.getText(),
                    textFieldTraditionalChinese.getText(),
                    textFieldEnglish.getText());
            mDialogInterface.onOk(dataModel);
        }
    }

    private void onCancel() {
       if (mDialogInterface != null) {
           mDialogInterface.onCancel();
       }
    }

    public static void main(String[] args) {
        CreateGenerationDialog dialog = new CreateGenerationDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
