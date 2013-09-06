import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * РџСЂРѕРіСЂР°РјРјР° РїРѕРёСЃРєР° РєР»СЋС‡РµРІРѕРіРѕ СЃР»РѕРІР° РІРЅСѓС‚СЂРё С„Р°Р№Р»Р° РІ СѓРєР°Р·Р°РЅРЅРѕР№ РїР°РїРєРµ
 */
public class MainForm {
    private JTextField keywordTextField;
    private JButton searchButton;
    private JList resultList;
    private JPanel mainPanel;
    private JTextField folderNameTextField;
    private JButton browseButton;

    public MainForm() {
        // РџСЂРё РЅР°Р¶Р°С‚РёРё РЅР° РєРЅРѕРїРєСѓ "РћР±Р·РѕСЂ..." РїРѕР·РІРѕР»РёРј РїРѕР»СЊР·РѕРІР°С‚РµР»СЋ РІС‹Р±СЂР°С‚СЊ РєР°С‚Р°Р»РѕРі
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Р”РёР°Р»РѕРі РІС‹Р±РѕСЂР° РєР°С‚Р°Р»РѕРіР°
                JFileChooser folderNameChooser = new JFileChooser();
                jFileChooserRussian();
                folderNameChooser.updateUI();
                // РњРѕР¶РЅРѕ РІС‹Р±РёСЂР°С‚СЊ С‚РѕР»СЊРєРѕ РєР°С‚Р°Р»РѕРі
                folderNameChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int ret = folderNameChooser.showDialog(null, "Р’С‹Р±РµСЂРёС‚Рµ РєР°С‚Р°Р»РѕРі");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = folderNameChooser.getSelectedFile();
                    folderNameTextField.setText(file.getAbsolutePath());
                }
            }
        });
        // РџСЂРё РЅР°Р¶Р°С‚РёРё РЅР° РєРЅРѕРїРєСѓ "Р�СЃРєР°С‚СЊ!"
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (folderNameTextField.getText().isEmpty()) {
                    showMessageDialog(null, "Р’С‹Р±РµСЂРёС‚Рµ РїР°РїРєСѓ РґР»СЏ РїРѕРёСЃРєР° С„Р°Р№Р»РѕРІ!", "РќРµР·Р°РїРѕР»РЅРµРЅС‹ РґР°РЅРЅС‹Рµ", ERROR_MESSAGE);
                    folderNameTextField.requestFocus();
                    return;
                }
                if (keywordTextField.getText().isEmpty()) {
                    showMessageDialog(null, "Р’РІРµРґРёС‚Рµ РєР»СЋС‡РµРІРѕРµ СЃР»РѕРІРѕ!", "РќРµР·Р°РїРѕР»РЅРµРЅС‹ РґР°РЅРЅС‹Рµ", ERROR_MESSAGE);
                    keywordTextField.requestFocus();
                    return;
                }
                final File dir = new File(folderNameTextField.getText());
                if (!dir.isDirectory()) {
                    showMessageDialog(null, "Р’С‹Р±РµСЂРёС‚Рµ РїР°РїРєСѓ РґР»СЏ РїРѕРёСЃРєР° С„Р°Р№Р»РѕРІ!", "РќРµР·Р°РїРѕР»РЅРµРЅС‹ РґР°РЅРЅС‹Рµ", ERROR_MESSAGE);
                    folderNameTextField.requestFocus();
                    return;
                }
                final DefaultListModel<String> listModel = new DefaultListModel<String>();
                resultList.setModel(listModel);
                // Р’С‹РІРѕРґРёС‚СЊ РЅР° СЌРєСЂР°РЅ СЃРїРёСЃРѕРє С„Р°Р№Р»РѕРІ (РїРѕР»РЅС‹Р№ РїСѓС‚СЊ Рє С„Р°Р№Р»Сѓ) СЃ СѓРєР°Р·Р°РЅРёРµРј РЅРѕРјРµСЂР° СЃС‚СЂРѕРєРё, РІ РєРѕС‚РѕСЂРѕР№ РЅР°Р№РґРµРЅРѕ РєР»СЋС‡РµРІРѕРµ СЃР»РѕРІРѕ
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        FileWalker walker = new FileWalker(new FileFoundListener() {
                            @Override
                            public void fileFound(File file) {
                                try {
                                    new FileScanner(file, keywordTextField.getText(), new KeywordFoundListener() {
                                        @Override
                                        public void keywordFound(File file, int line) {
                                            listModel.addElement(String.format("%s: %d", file.getAbsolutePath(), line));
                                            MySQLHelper.saveToDB(keywordTextField.getText(), file.getAbsolutePath(), line);
                                        }
                                    });
                                } catch (FileNotFoundException ex) {
                                    ex.printStackTrace();
                                }
                                mainPanel.repaint();
                            }
                        });
                        walker.walk(dir);
                        if (listModel.isEmpty()) {
                            listModel.addElement(String.format("Р’ РїР°РїРєРµ \"%s\" С„Р°Р№Р»РѕРІ СЃ РєР»СЋС‡РµРІС‹Рј СЃР»РѕРІРѕРј \"%s\" РЅРµ РЅР°Р№РґРµРЅРѕ!",
                                    folderNameTextField.getText(), keywordTextField.getText()));
                        }
                    }
                });

            }
        });
    }

    private void jFileChooserRussian() {
        UIManager.put("FileChooser.openButtonText", "РћС‚РєСЂС‹С‚СЊ");
        UIManager.put("FileChooser.cancelButtonText", "РћС‚РјРµРЅР°");
        UIManager.put("FileChooser.lookInLabelText", "РЎРјРѕС‚СЂРµС‚СЊ РІ");
        UIManager.put("FileChooser.fileNameLabelText", "Р�РјСЏ С„Р°Р№Р»Р°");
        UIManager.put("FileChooser.filesOfTypeLabelText", "РўРёРї С„Р°Р№Р»Р°");

        UIManager.put("FileChooser.saveButtonText", "РЎРѕС…СЂР°РЅРёС‚СЊ");
        UIManager.put("FileChooser.saveButtonToolTipText", "РЎРѕС…СЂР°РЅРёС‚СЊ");
        UIManager.put("FileChooser.openButtonText", "РћС‚РєСЂС‹С‚СЊ");
        UIManager.put("FileChooser.openButtonToolTipText", "РћС‚РєСЂС‹С‚СЊ");
        UIManager.put("FileChooser.cancelButtonText", "РћС‚РјРµРЅР°");
        UIManager.put("FileChooser.cancelButtonToolTipText", "РћС‚РјРµРЅР°");

        UIManager.put("FileChooser.lookInLabelText", "РџР°РїРєР°");
        UIManager.put("FileChooser.saveInLabelText", "РџР°РїРєР°");
        UIManager.put("FileChooser.fileNameLabelText", "Р�РјСЏ С„Р°Р№Р»Р°");
        UIManager.put("FileChooser.filesOfTypeLabelText", "РўРёРї С„Р°Р№Р»РѕРІ");

        UIManager.put("FileChooser.upFolderToolTipText", "РќР° РѕРґРёРЅ СѓСЂРѕРІРµРЅСЊ РІРІРµСЂС…");
        UIManager.put("FileChooser.newFolderToolTipText", "РЎРѕР·РґР°РЅРёРµ РЅРѕРІРѕР№ РїР°РїРєРё");
        UIManager.put("FileChooser.listViewButtonToolTipText", "РЎРїРёСЃРѕРє");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "РўР°Р±Р»РёС†Р°");
        UIManager.put("FileChooser.fileNameHeaderText", "Р�РјСЏ");
        UIManager.put("FileChooser.fileSizeHeaderText", "Р Р°Р·РјРµСЂ");
        UIManager.put("FileChooser.fileTypeHeaderText", "РўРёРї");
        UIManager.put("FileChooser.fileDateHeaderText", "Р�Р·РјРµРЅРµРЅ");
        UIManager.put("FileChooser.fileAttrHeaderText", "РђС‚СЂРёР±СѓС‚С‹");

        UIManager.put("FileChooser.acceptAllFileFilterText", "Р’СЃРµ С„Р°Р№Р»С‹");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("РџРѕРёСЃРє РєР»СЋС‡РµРІРѕРіРѕ СЃР»РѕРІР° РІРЅСѓС‚СЂРё С„Р°Р№Р»Р° РІ СѓРєР°Р·Р°РЅРЅРѕР№ РїР°РїРєРµ");
        MainForm mainForm = new MainForm();
        paramsFromCommandLine(args, mainForm);
        frame.setContentPane(mainForm.mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static void paramsFromCommandLine(String[] args, MainForm mainForm) {
        if (args.length >= 1) {
            mainForm.folderNameTextField.setText(args[0]);
        }
        if (args.length >= 2) {
            mainForm.keywordTextField.setText(args[1]);
        }
    }
}
