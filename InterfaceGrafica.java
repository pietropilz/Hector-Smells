import javax.swing.*;
import java.awt.event.*;

public class InterfaceGrafica {

    public static void main(String[] args) {
        // Chama o método da interface gráfica dentro da thread de eventos do Swing
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Janela(); // Cria uma nova instância da janela
            }
        });
    }
}

class Janela extends JFrame {

    public Janela() {
        // Definindo o título da janela
        setTitle("Minha Interface Gráfica");
        
        // Definindo o tamanho da janela
        setSize(400, 300);
        
        // Definindo o comportamento ao fechar a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Criando um botão simples
        JButton botao = new JButton("Clique aqui");
        
        // Adicionando um ActionListener para o botão
        botao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Exibe uma mensagem quando o botão é clicado
                JOptionPane.showMessageDialog(null, "Você clicou no botão!");
            }
        });

        // Adicionando o botão na janela
        add(botao);

        // Centralizando a janela na tela
        setLocationRelativeTo(null);
        
        // Tornando a janela visível
        setVisible(true);
    }
}
