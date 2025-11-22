package model;

public enum Instrument {
    PIANO(0),
    ORGAO(16),
    XILOFONE(13),
    ACORDEON(21),
    VIOLAO(24),
    GUITARRA(26),
    BAIXO(32),
    VIOLINO(40),
    HARPA(46),
    STRINGS(48),
    TROMPETE(56),
    TUBA(58),
    PICCOLO(72),
    FLAUTA(73),
    ASSOBIO(78),
    GOBLIN(101),
    TEMAESTRELA(103),
    PASSARINHO(123),
    TELEFONE(124),
    TIRO(127);

    private final int instrumentCode;

    Instrument(int instrumentCode) {
        this.instrumentCode = instrumentCode;
    }

    public int getInstrumentCode() {
        return instrumentCode;
    }

    public static Instrument random() {
        Instrument[] instrumentos = values();
        int index = java.util.concurrent.ThreadLocalRandom.current().nextInt(instrumentos.length);
        return instrumentos[index];
    }
}