import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Программа поиска ключевого слова внутри файла в указанной папке
 */
public class MainForm {
    private JTextField keywordTextField;
    private JButton searchButton;
    private JList resultList;
    private JPanel mainPanel;
    private JTextField folderNameTextField;
    private JButton browseButton;

    public MainForm() {
        // При нажатии на кнопку "Обзор..." позволим пользователю выбрать каталог
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Диалог выбора каталога
                JFileChooser folderNameChooser = new JFileChooser();
                jFileChooserRussian();
                folderNameChooser.updateUI();
                // Можно выбирать только каталог
                folderNameChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int ret = folderNameChooser.showDialog(null, "Выберите каталог");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = folderNameChooser.getSelectedFile();
                    folderNameTextField.setText(file.getAbsolutePath());
                }
            }
        });
        // При нажатии на кнопку "Искать!"
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (folderNameTextField.getText().isEmpty()) {
                    showMessageDialog(null, "Выберите папку для поиска файлов!", "Незаполнены данные", ERROR_MESSAGE);
                    folderNameTextField.requestFocus();
                    return;
                }
                if (keywordTextField.getText().isEmpty()) {
                    showMessageDialog(null, "Введите ключевое слово!", "Незаполнены данные", ERROR_MESSAGE);
                    keywordTextField.requestFocus();
                    return;
                }
                final File dir = new File(folderNameTextField.getText());
                if (!dir.isDirectory()) {
                    showMessageDialog(null, "Выберите папку для поиска файлов!", "Незаполнены данные", ERROR_MESSAGE);
                    folderNameTextField.requestFocus();
                    return;
                }
                final DefaultListModel<String> listModel = new DefaultListModel<String>();
                resultList.setModel(listModel);
                // Выводить на экран список файлов (полный путь к файлу) с указанием номера строки, в которой найдено ключевое слово
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
                            listModel.addElement(String.format("В папке \"%s\" файлов с ключевым словом \"%s\" не найдено!",
                                    folderNameTextField.getText(), keywordTextField.getText()));
                        }
                    }
                });

            }
        });
    }

    private void jFileChooserRussian() {
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.lookInLabelText", "Смотреть в");
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип файла");

        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить");
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.openButtonToolTipText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Отмена");

        UIManager.put("FileChooser.lookInLabelText", "Папка");
        UIManager.put("FileChooser.saveInLabelText", "Папка");
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип файлов");

        UIManager.put("FileChooser.upFolderToolTipText", "На один уровень вверх");
        UIManager.put("FileChooser.newFolderToolTipText", "Создание новой папки");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Таблица");
        UIManager.put("FileChooser.fileNameHeaderText", "Имя");
        UIManager.put("FileChooser.fileSizeHeaderText", "Размер");
        UIManager.put("FileChooser.fileTypeHeaderText", "Тип");
        UIManager.put("FileChooser.fileDateHeaderText", "Изменен");
        UIManager.put("FileChooser.fileAttrHeaderText", "Атрибуты");

        UIManager.put("FileChooser.acceptAllFileFilterText", "Все файлы");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Поиск ключевого слова внутри файла в указанной папке");
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
