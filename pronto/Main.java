import control.Controller;
import javax.swing.UIManager;

void main() throws Exception {
    new Controller();
    String lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
    UIManager.setLookAndFeel(lookAndFeelClassName);
}
