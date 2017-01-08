package um.nija123098.sticher;

import javafx.util.Pair;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Stitcher {
    /**
     * Gets all file paths from a directory and its subdirectories
     * @param container the directory that all files will be got from
     * @return a list of all file locations excluding this Jar
     */
    public static List<String> getPaths(String container){
        List<String> strings = new ArrayList<String>();
        for (File file : new File(container).listFiles()) {
            if (file.isDirectory()){
                strings.addAll(getPaths(file.getPath()));
            }else{
                strings.add(file.getPath());
            }
        }
        strings.remove(Reference.LOC);
        return strings;
    }
    /**
     * Gets the lines of a part of a .sth file
     * @param filePath the full file path of the file that will be made a part
     * @return the strings including location and key of the file
     * @throws Exception if any exceptions are thrown reading the file
     */
    public static List<String> getPart(String filePath) throws Exception {
        List<String> file = new ArrayList<String>();
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        file.add(new String(bytes));
        List<String> strings = new ArrayList<String>(file.size() + 3);
        strings.add(filePath.substring(Reference.CONTAINER.length()));
        String unqique = "" + Reference.RANDOM.nextInt();
        while (true){
            if (!file.contains(unqique)){
                break;
            }
            unqique += Reference.RANDOM.nextInt();
        }
        strings.add(unqique);
        strings.addAll(file);
        strings.add(unqique);
        return strings;
    }
    /**
     * Parses the contents of the .sth file
     * @param strings the strings of the file to unstitch
     * @return a list of pairs of relative paths and its strings
     */
    public static List<Pair<String, List<String>>> unstitch(List<String> strings){
        List<Pair<String, List<String>>> parts = new ArrayList<Pair<String, List<String>>>();
        List<String> active = new ArrayList<String>();
        String key, path;
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()){
            path = iterator.next();
            key = iterator.next();
            while (true){
                String s = iterator.next();
                if (s.equals(key)){
                    break;
                }
                active.add(s);
            }
            parts.add(new Pair<String, List<String>>(path, new ArrayList<String>(active)));
            active.clear();
        }
        return parts;
    }
    /**
     *
     * @param container the full path of a container to stitch together
     * @return the list of strings of the stitched file
     * @throws Exception if any IO operations go wrong
     */
    public static List<String> stitch(String container) throws Exception{
        List<String> strings = new ArrayList<String>();
        for (String s : getPaths(container)) {
            strings.addAll(getPart(s));
        }
        return strings;
    }
    /**
     * Operates the actual stitching
     * @param container the full path of the cantainer to stitch together
     * @throws Exception if any IO operations go wrong
     */
    public static void operateStitch(String container, boolean clean) throws Exception{
        Path p = Paths.get(new File(container).getName() + "-" + System.currentTimeMillis() + "." + Reference.ENDING);
        p.toFile().getParentFile().mkdirs();
        Files.createFile(p);
        List<String> paths = null;
        if (clean){
            paths = getPaths(container);
        }
        Files.write(p, stitch(container));// todo optimize
        if (clean){
            Path th = Paths.get(Reference.LOC);
            for (String path : paths) {
                if (!p.equals(th)){
                    Files.deleteIfExists(Paths.get(path));
                }
            }
        }
    }
    /**
     * Operates the actual unstitching
     * @param file the full path of the file to be unstitched
     * @throws Exception if any IO operations go wrong
     */
    public static void operateUnstitch(String file, boolean clean) throws Exception{
        String parent = new File(file).getParent();
        Path active;
        for (Pair<String, List<String>> pair : unstitch(Files.readAllLines(Paths.get(file)))) {
            active = Paths.get(parent + "\\" + pair.getKey());
            active.toFile().getParentFile().mkdirs();
            Files.createFile(active);
            Files.write(active, pair.getValue());
        }
        if (clean){
            Files.deleteIfExists(Paths.get(file));
        }
    }
}
