package in.mcxiv.music;

import in.mcxiv.music.SimpleFeedEngine.NoteSequencer;
import in.mcxiv.threads.ThreadMan;
import pd.OpenSimplex2;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MusicBox {

    private static final long SEED = new Random().nextLong();
    private static final double FACTOR = 0.02;

    SimpleFeedEngine engine;
    NoteSequencer sequencer;
    boolean is_proc_running;
    int just_some_feed_numb;
    Scale mcxiv_scale_on_Cm;
    Consumer<Double> consmr;

    public MusicBox() {
        engine = new SimpleFeedEngine();
        mcxiv_scale_on_Cm = new Scale();
        sequencer = engine.getSequencer(new int[]{
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 2, 2, 1, 1, 1,
                1, 2, 1, 1, 2, 1,
                2, 1, 1, 1, 2, 1,
                3, 1, 1, 2, 1
        });
        is_proc_running = true;
        ThreadMan.getInstance().scheduleAtFixedRate(this::addNext, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void callBack(Consumer<Double> consumer) {
        consmr = consumer;
    }

    private void addNext() {
        if (is_proc_running) while (engine.track.ticks() - engine.sequencer.getTickPosition() < 50) {
            double noise = OpenSimplex2.noise2(SEED, (just_some_feed_numb++) * FACTOR, 0);
            consmr.accept(noise);
            sequencer.playNote(1,
                    mcxiv_scale_on_Cm.reScale((int) (SimpleFeedEngine.NT_MID_C + 12 * noise))
                    , 64);
        }
    }
}

