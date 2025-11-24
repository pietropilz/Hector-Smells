package view;

import control.*;
import model.Instrument;
import java.awt.*;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;

public class UserInterface extends JFrame {

    private Instrument currentInstrument = Instrument.PIANO;
    private int currentVolume = 50;
    private int currentOctave = 2;
    private int currentBPM = 120;
    JTextArea editText;

    public Instrument getCurrentInstrument() {
        return currentInstrument;
    }
    public int getCurrentBPM() {
        return currentBPM;
    }
    public int getCurrentOctave() {
        return currentOctave;
    }
    public int getCurrentVolume() {
        return currentVolume;
    }
    public JTextArea getEditText() {
        return editText;
    }

    //Configuraçao de Botoes

    private JButton createButtonTop(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(40, 40, 40));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        return button;
    }
    private JButton createButton(String text){
        JButton button = new JButton(text);
        button.setFont (new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground (new Color (60, 60, 60));
        button.setForeground (Color.WHITE);
        return button;
    }
    private JButton startButton() {
        JButton button = createButton("Start/Pause");
        button.setBackground(new Color(0, 150, 0));
        return button;
    }


    public UserInterface(Controller controller) {

        super("Just music");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(15, 15, 15));

        //Parte Superior

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        topPanel.setBackground(new Color(15, 15, 15));

        JButton chooseFileTxt = createButtonTop("Escolher Arquivo Texto");
        JButton saveFileTxt = createButtonTop("Salvar Arquivo Texto");
        JButton saveFileMidi = createButtonTop("Salvar Arquivo MIDI");

        topPanel.add(chooseFileTxt);
        topPanel.add(saveFileTxt);
        topPanel.add(saveFileMidi);

        add(topPanel, BorderLayout.NORTH);

        //Parte Central

        JPanel central = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(35,35,35));
                g2d.fillRoundRect(0,0, getWidth(), getHeight(), 30, 30);
            }
        };
        central.setOpaque(false);
        central.setLayout(new BorderLayout(20, 20));

        //titulo

        JLabel titulo = new JLabel("Just Music", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 45));
        titulo.setForeground(Color.WHITE);
        central.add(titulo, BorderLayout.NORTH);

        //area de texto de entrada

        editText = new JTextArea();
        editText.setBackground(Color.BLACK);
        editText.setForeground(Color.WHITE);
        editText.setCaretColor(Color.WHITE);
        editText.setLineWrap(true);
        editText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        editText.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        //Tratamento de scroll
        JScrollPane scroll = new JScrollPane(editText);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        central.add(scroll, BorderLayout.CENTER);

        //Botoes de start/pause e restart
        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10,10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        gbc.gridy = 0;
        JButton startPause = startButton();
        buttons.add(startPause, gbc);

        gbc.gridy = 1;
        JButton restart = createButton("Restart");
        buttons.add(restart, gbc);

        gbc.gridy = 2;

        JPanel octavePanel = new JPanel();
        octavePanel.setLayout(new BoxLayout(octavePanel, BoxLayout.Y_AXIS));
        octavePanel.setOpaque(false);

        JLabel octaveLabel = new JLabel("Oitava");
        octaveLabel.setForeground(Color.WHITE);
        octaveLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        octaveLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        String[] Octaves = { "2", "3", "4", "5", "6", "7", "8", "9"};
        JComboBox<String> octaveBox = new JComboBox<>(Octaves);
        octaveBox.setBackground(new Color(40,40,40));
        octaveBox.setForeground(Color.WHITE);
        octaveBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        octavePanel.add(octaveLabel);
        octavePanel.add(octaveBox);
        buttons.add(octavePanel, gbc);

        gbc.gridy = 3;

        JPanel bpmPanel = new JPanel();
        bpmPanel.setLayout(new BoxLayout(bpmPanel, BoxLayout.Y_AXIS));
        bpmPanel.setOpaque(false);

        JLabel bpmLabel = new JLabel("BPM");
        bpmLabel.setForeground(Color.WHITE);
        bpmLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        bpmLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        String[] BPMs = {"90", "120", "150", "180", "210", "240"};
        JComboBox<String> BPMBox = new JComboBox<>(BPMs);
        BPMBox.setBackground(new Color(40,40,40));
        BPMBox.setForeground(Color.WHITE);
        BPMBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bpmPanel.add(bpmLabel);
        bpmPanel.add(BPMBox);
        buttons.add(bpmPanel, gbc);

        gbc.gridy = 4;

        JPanel instrumentPanel = new JPanel();
        instrumentPanel.setLayout(new BoxLayout(instrumentPanel, BoxLayout.Y_AXIS));
        instrumentPanel.setOpaque(false);

        JLabel instrumentLabel = new JLabel("Instrumentos");
        instrumentLabel.setForeground(Color.WHITE);
        instrumentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        instrumentLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JComboBox<String> instrumentBox = new JComboBox<>();
        for (Instrument instr : Instrument.values()) {
            instrumentBox.addItem(instr.name());
        }
        instrumentBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        instrumentPanel.add(instrumentLabel);
        instrumentPanel.add(instrumentBox);

        buttons.add(instrumentPanel, gbc);

        gbc.gridy = 5;
        JButton documentation = createButton("Documentação");
        buttons.add(documentation, gbc);

        central.add(buttons, BorderLayout.WEST);

        //Volume Slider
        JPanel VolumePanel = new JPanel();
        VolumePanel.setOpaque (false);
        VolumePanel.setLayout (new BoxLayout (VolumePanel, BoxLayout.Y_AXIS));

        JLabel VolumeLabel = new JLabel("Volume:");
        VolumeLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        VolumeLabel.setForeground(Color.WHITE);
        VolumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setBackground(new Color(35, 35, 35));
        volumeSlider.setForeground(Color.WHITE);
        volumeSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);

        VolumePanel.add(VolumeLabel);
        VolumePanel.add(Box.createVerticalStrut(5));
        VolumePanel.add(volumeSlider);

        central.add(VolumePanel, BorderLayout.SOUTH);

        add(central);

        //Ações dos botões

        chooseFileTxt.addActionListener(_-> controller.buttonChooseFileTxt());

        saveFileTxt.addActionListener(_->{
            try {
                controller.buttonSaveFileTxt();
            } catch (IOException _) {}
        });

        saveFileMidi.addActionListener(_->{
            try {
                controller.buttonSaveFileMidi();
            } catch (IOException _) {}
        });

        startPause.addActionListener(_-> {
            try {
                controller.buttonStartPause();
            } catch (InvalidMidiDataException e) {
                throw new RuntimeException(e);
            }
        });
        restart.addActionListener(_-> {
            try {
                controller.buttonRestart();
            } catch (InvalidMidiDataException e) {
                throw new RuntimeException(e);
            }
        });

        documentation.addActionListener(_ -> {
            try {
                // Caminho do arquivo .txt (coloque o caminho real)
                StringBuilder sb = controller.buttonDocumentation();

                // Exibir no painel com scroll
                JTextArea area = new JTextArea(sb.toString());
                area.setEditable(false);
                area.setFont(new Font("Monospaced", Font.PLAIN, 14));

                JScrollPane scroll2 = new JScrollPane(area);
                scroll2.setPreferredSize(new Dimension(500, 400));

                JOptionPane.showMessageDialog(
                        null,
                        scroll2,
                        "Documentação do Trabalho",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Erro ao ler o arquivo de documentação.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        instrumentBox.addActionListener(_-> currentInstrument = Instrument.valueOf(instrumentBox.getSelectedItem().toString()));
        volumeSlider.addChangeListener(_-> currentVolume = volumeSlider.getValue());
        octaveBox.addActionListener(_-> currentOctave = Integer.parseInt(octaveBox.getSelectedItem().toString()));
        BPMBox.addActionListener(_-> currentBPM = Integer.parseInt(BPMBox.getSelectedItem().toString()));

        setVisible(true);
    }
    public void showMessageDialog(String string) {
        JOptionPane.showMessageDialog(this, string);
    }

}
