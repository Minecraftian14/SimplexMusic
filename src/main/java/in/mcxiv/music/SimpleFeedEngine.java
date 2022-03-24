package in.mcxiv.music;

import javax.sound.midi.*;
import java.util.function.Supplier;

public class SimpleFeedEngine {

    public static final int NT_MID_C = 60;

    public static final int EV_NOTE_ON = 144;
    public static final int EV_NOTE_OFF = 128;

    /**
     * For things like changing default instrument.
     */
    public static final int EV_PROGRAM_CHANGE = 192;

    /**
     * Control change for sending events
     */
    public static final int EV_CONTROL_CHANGE = 176;

    public static final int EV_PITCH_BEND = 244;

    Synthesizer synth;
    Sequencer sequencer;
    Sequence sequence;
    Track track;

    int position = 4;

    public SimpleFeedEngine() {
        try {
            construct();
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    protected void construct() throws MidiUnavailableException, InvalidMidiDataException {
        synth = MidiSystem.getSynthesizer();
        synth.loadAllInstruments(synth.getDefaultSoundbank());

        sequencer = MidiSystem.getSequencer();
        sequencer.open();

        sequence = new Sequence(Sequence.PPQ, 4);
        track = sequence.createTrack();

        sequencer.setSequence(sequence);
        sequencer.setTempoInBPM(80);
    }

    public NoteSequencer getSequencer(int[] sequence) {
        return new NoteSequencer(sequence);
    }

    public void playNote(int channel, int note, int velocity, int length) {
        track.add(makeEvent(EV_NOTE_ON, channel, note, velocity, position));
        track.add(makeEvent(EV_NOTE_OFF, channel, note, velocity, position + length));
        position += length;
        if (!sequencer.isRunning()) start();
    }

    public void start() {
        sequencer.start();
    }

    private MidiEvent makeEvent(int command, int channel, int note, int velocity, int tick) {
        MidiEvent event = null;

        try {
            ShortMessage message = new ShortMessage();
            message.setMessage(command, channel, note, velocity);
            event = new MidiEvent(message, tick);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

        return event;
    }

    public final class NoteSequencer {

        public static final int[] SEQ_1111 = {1, 1, 1, 1};
        public static final int[] SEQ_1212 = {1, 2, 1, 2, 1, 2, 1, 2};

        int[] sequence;
        int index = 4;

        public NoteSequencer(int[] sequence) {
            this.sequence = sequence;
            assert ((Supplier<Integer>) () -> {
                int sum = 0;
                for (int length : sequence) sum += length;
                return sum;
            }).get() % 4 == 0;
        }

        public void playNote(int channel, int note, int velocity) {
            SimpleFeedEngine.this.playNote(channel, note, velocity, sequence[index++]);
            if (index >= sequence.length) index = 0;
        }
    }
}
