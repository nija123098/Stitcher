package um.nija123098.sticher;

import java.io.File;
import java.util.Random;

/**
 * Made by nija123098 on 1/5/2017
 */
public class Reference {
    public static String ENDING = "sth";
    public static Random RANDOM = new Random();
    public static final String CONTAINER = new File(Stitcher.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
    public static final String LOC = Stitcher.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    public static void main(String[] args) {
        try {
            Stitcher.operateUnstitch("C:\\Users\\Dev.home-PC\\GitRepos\\Sticher\\target\\target-1483742117588.sth", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
