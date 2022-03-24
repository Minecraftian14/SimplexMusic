package in.mcxiv.music;

import java.util.Arrays;

import static in.mcxiv.music.SimpleFeedEngine.NT_MID_C;

public class Scale {

    //  private static final int[] normal_scale = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11};
    private static final int[] mcxivC_aaroh = {0, 2, 3, 5, 6, 8, 11};
    private static final int[] mcxivC_avroh = {0, 2, 3, 5, 7, 8, 11};

    int lastNote = NT_MID_C;

    public int reScale(int n) {
        if (n == lastNote) return n;
        if (n > lastNote) /* movin' up */ {
            int delta = n - lastNote;
            int root = downToIndex(lastNote, mcxivC_aaroh) + delta;
            int height = height(lastNote);
            n = getNoteOf(root, mcxivC_aaroh);
            lastNote = 12 * height + n;
            return lastNote;
        } else {
            int delta = n - lastNote;
            int root = downToIndex(lastNote, mcxivC_avroh) + delta;
            int height = height(lastNote);
            n = getNoteOf(root, mcxivC_avroh);
            lastNote = 12 * height + n;
            return lastNote;
        }
    }

    private int getNoteOf(int note, int[] scale) {
        while (note < 0) note += 7;
        return scale[note % 7];
    }

    private int downToIndex(int note, int[] scale) {
//      note -= NT_MID_C;
        note %= 12;
        int index = Arrays.binarySearch(scale, note);
        if (index < 0) index = -index - 1;
        return index;
    }

    private int height(int note) {
        return (int) ((note - NT_MID_C) / 12f) + 5;
    }
}
