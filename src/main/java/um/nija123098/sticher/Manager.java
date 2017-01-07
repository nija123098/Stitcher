package um.nija123098.sticher;

import java.awt.*;
import java.io.Console;
import java.io.File;

/**
 * Made by nija123098 on 1/5/2017
 */
public class Manager {
    public static void main(String[] args) {
        try{Console console = System.console();
            if (console == null && !GraphicsEnvironment.isHeadless()) {
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "cmd", "/k", "java -jar \"" + Manager.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6) + "\""});
            }
            console = System.console();
            while (true){
                String s = console.readLine();
                if (!s.startsWith("sth")){
                    continue;
                }
                s = s.substring("sth".length());
                if (s.startsWith(" ")){
                    s = s.substring(1);
                }
                boolean clean = false;
                if (s.startsWith("c")){
                    clean = true;
                    s = s.substring(1);
                }
                if (s.startsWith(" ")){
                    s = s.substring(1);
                }
                String[] ss = s.split(" ");
                sw:
                switch (ss[0]){
                    case "exit":
                        System.exit(0);
                    case "":
                        Stitcher.operateStitch(Reference.CONTAINER, clean);
                        break;
                    case "u":
                        if (ss.length > 1){
                            if (new File(ss[1]).getName().endsWith("." + Reference.ENDING)){
                                Stitcher.operateUnstitch(Reference.CONTAINER + "\\" + ss[1], clean);
                            }else{
                                console.writer().write("That is not a ." + Reference.ENDING + " file.");
                            }
                        }else{
                            String path = null;
                            boolean found = false;
                            for (File file : new File(Reference.CONTAINER).listFiles()) {
                                if (file.getName().endsWith("." + Reference.ENDING)){
                                    if (found){
                                        console.writer().write("There are multiple ." + Reference.ENDING + " files in this directory.  Please specify one");
                                        break sw;
                                    }else{
                                        found = true;
                                        path = file.getPath();
                                    }
                                }
                            }
                            if (path == null){
                                console.writer().write("No ." + Reference.ENDING + " file found in this directory.");
                            }else{
                                Stitcher.operateUnstitch(path, clean);
                            }
                        }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
