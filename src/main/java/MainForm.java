import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
                // Можно выбирать только каталог
                folderNameChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int ret = folderNameChooser.showDialog(null, "Выберите каталог");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = folderNameChooser.getSelectedFile();
                    folderNameTextField.setText(file.getAbsolutePath());
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Поиск ключевого слова внутри файла в указанной папке");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
