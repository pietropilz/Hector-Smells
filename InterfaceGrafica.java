package music;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import javax.sound.midi.Track;
import javax.swing.*;

public class InterfaceGrafica extends JFrame {

    private MusicPlayer Player;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private File file;
    private Instrument CurrentInstrument = Instrument.PIANO;
    private int CurrentVolume = 50;
    private int CurrentOctave = 2;
    private int CurrentBPM = 120;
    JTextArea editText;

    //Configuraçao de Botoes

    private JButton CreateButtonTop (String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(40, 40, 40));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(160, 40));
        return button;
    }
    private JButton CreateButton (String text){
        JButton button = new JButton(text);
        button.setFont (new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground (new Color (60, 60, 60));
        button.setForeground (Color.WHITE);
        return button;
    }
    private JButton StartButton (String text) {
        JButton button = CreateButton(text);
        button.setBackground(new Color(0, 150, 0));
        return button;
    }


    public InterfaceGrafica() {
        super("Mauri music");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(15, 15, 15));

        //Parte Superior

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        topPanel.setBackground(new Color(15, 15, 15));

        JButton ChooseFile = CreateButtonTop("Escolher Arquivo");
        JButton SaveFile = CreateButtonTop("Salvar Arquivo");

        topPanel.add(ChooseFile);
        topPanel.add(SaveFile);

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

        JLabel titulo = new JLabel("Mauri Music");
        titulo.setFont(new Font("TimesNewRoman", Font.BOLD, 36));
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
        JButton startPause = StartButton("Start/Pause");
        buttons.add(startPause, gbc);

        gbc.gridy = 1;
        JButton restart = CreateButton("Restart");
        buttons.add(restart, gbc);

        gbc.gridy = 2;
        String[] Octaves = { "2", "3", "4", "5", "6", "7", "8", "9"};
        JComboBox<String> octaveBox = new JComboBox<>(Octaves);
        octaveBox.setBackground(new Color(40,40,40));
        octaveBox.setForeground(Color.WHITE);
        octaveBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        buttons.add(octaveBox, gbc);

        gbc.gridy = 3;
        String[] BPMs = { "120", "150", "180", "190", "210"};
        JComboBox<String> BPMBox = new JComboBox<>(BPMs);
        BPMBox.setBackground(new Color(40,40,40));
        BPMBox.setForeground(Color.WHITE);
        BPMBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        buttons.add(BPMBox, gbc);

        gbc.gridy = 4;
        JComboBox<String> instrumentBox = new JComboBox<>();
        for (Instrument instr : Instrument.values()) {
            instrumentBox.addItem(instr.name());
        }
        instrumentBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        buttons.add(instrumentBox, gbc);

        central.add(buttons, BorderLayout.WEST);

        //Volume Slider
        JPanel VolumePanel = new JPanel();
        VolumePanel.setOpaque (false);
        VolumePanel.setLayout (new BoxLayout (VolumePanel, BoxLayout.Y_AXIS));

        JLabel VolumeLabel = new JLabel("Volume:");
        VolumeLabel.setForeground(Color.WHITE);
        VolumeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSlider volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setBackground(new Color(35, 35, 35));
        volumeSlider.setForeground(Color.WHITE);
        volumeSlider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        VolumePanel.add(VolumeLabel);
        VolumePanel.add(Box.createVerticalStrut(5));
        VolumePanel.add(volumeSlider);

        central.add(VolumePanel, BorderLayout.SOUTH);
        
        add(central);

        //Ações dos botões

        ChooseFile.addActionListener(e-> {
            JFileChooser filechoose = new JFileChooser();
            if (filechoose.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                file = filechoose.getSelectedFile();
                LoadFile(file);
            }
        });

        SaveFile.addActionListener(e->{
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Arquivo");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivo de texto (*.txt)", "txt"));

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(editText.getText());
                JOptionPane.showMessageDialog(this, "Arquivo salvo com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo: " + ex.getMessage());
            }
        }
    });

        startPause.addActionListener(e-> {
            String text = editText.getText();
            if (text.isEmpty()){
                JOptionPane.showMessageDialog(this, "Digite algo para tocar!");
            }
            if(Player != null){
                if(isPlaying){
                    Player.pause();
                    isPlaying = false;
                    isPaused = true;
                }   
                    else{
                     isPlaying = true;
                        if(isPaused){
                            isPaused = false;
                            Player.play();
                        }
                            else{
                    
                                new Thread(() -> {
                                Player.stop();
                                for (Track t : Player.getSequence().getTracks()) {
                                Player.deleteTrack(t);
                                }
                                ReadText(text);
                                Player.play();  
                                }).start();
                            }
                     }
            }
        });

        


        restart.addActionListener(e->{
           String text = editText.getText();
    if (text.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Digite algo para tocar!");
        return;
    }

    if (Player != null) {
        new Thread(() -> {
            Player.stop();

            for (Track t : Player.getSequence().getTracks()) {
                Player.deleteTrack(t);
            }

            ReadText(text);

            Player.play();

          
            isPlaying = true;
            isPaused = false;
        }).start();
    }
});

        instrumentBox.addActionListener(e->{
            CurrentInstrument = Instrument.valueOf(instrumentBox.getSelectedItem().toString());
        });

        volumeSlider.addChangeListener(e->{
            CurrentVolume = volumeSlider.getValue();
        });

        octaveBox.addActionListener(e-> {
            CurrentOctave = Integer.parseInt(octaveBox.getSelectedItem().toString());
        });

         BPMBox.addActionListener(e-> {
            CurrentBPM = Integer.parseInt(BPMBox.getSelectedItem().toString());
        });

        setVisible(true);
        }

        //Metodos

        public void setPlayer(MusicPlayer player) {
            this.Player = player;

        }

        private void LoadFile(File file){
            try {
               String content = Files.readString(file.toPath());
               editText.setText(content); 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar o arquivo: ");
            }
        } 

        private void ReadText(String text){
            try {
                
                for(Track t : Player.getSequence().getTracks()){
                    Player.deleteTrack(t);
                }

                SoundTrack Track = Player.createTrack(CurrentInstrument, CurrentVolume, CurrentOctave, CurrentBPM);
                Track.setController(Player.getController());

                FileTreatment Reader = new FileTreatment(Track);
                Reader.readString(text);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao ler o texto: ");
            }
        }
        public void run() {
            SwingUtilities.invokeLater(InterfaceGrafica::new);
    }

      
    }

       



       
