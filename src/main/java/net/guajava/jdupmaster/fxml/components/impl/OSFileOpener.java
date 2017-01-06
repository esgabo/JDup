package net.guajava.jdupmaster.fxml.components.impl;

import net.guajava.jdupmaster.fxml.components.FileOpener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Component("os-file-opener")
public class OSFileOpener implements FileOpener {
    @Override
    public boolean open(File file) {
        return openWithOSCommand(file);
    }

    private boolean openWithOSCommand(File file) {
        OS os = OS.valueContained(System.getProperty("os.name"));
        return os.openFile(file);
    }

    enum OS {
        WIN("win", file -> {
            return runCommand("explorer", "%s", file.getPath());
        }),
        MAC("mac", file -> {
            return runCommand("open", "%s", file.getPath());
        }),
        LINUX("linux", file -> {
            List<String> commands = Arrays.asList("kde-open", "gnome-open", "xdg-open");
            return commands.stream()
                    .filter(command -> runCommand(command, "%s", file.getPath()))
                    .findFirst()
                    .isPresent();
        }),
        SOLARIS("sunos", LINUX.openFileLambda),
        UNKNOWN("unknown", file -> false);

        private String osId;
        private Function<File, Boolean> openFileLambda;

        private OS(String osId, Function<File, Boolean> openFile) {
            this.osId = osId;
            this.openFileLambda = openFile;
        }

        @Override
        public String toString() {
            return osId;
        }

        public boolean openFile(File file) {
            return openFileLambda.apply(file);
        }

        public static boolean runCommand(String command, String args, String file) {

//            logOut("Trying to exec:\n   cmd = " + command + "\n   args = " + args + "\n   %s = " + file);

            String[] parts = prepareCommand(command, args, file);

            try {
                Process p = Runtime.getRuntime().exec(parts);
                if (p == null) return false;

                try {
                    int retval = p.exitValue();
                    if (retval == 0) {
//                        logErr("Process ended immediately.");
                        return false;
                    } else {
//                        logErr("Process crashed.");
                        return false;
                    }
                } catch (IllegalThreadStateException itse) {
//                    logErr("Process is running.");
                    return true;
                }
            } catch (IOException e) {
//                logErr("Error running command.", e);
                return false;
            }
        }

        private static String[] prepareCommand(String command, String args, String file) {

            List<String> parts = new ArrayList<String>();
            parts.add(command);

            if (args != null) {
                for (String s : args.split(" ")) {
                    s = String.format(s, file); // put in the filename thing

                    parts.add(s.trim());
                }
            }

            return parts.toArray(new String[parts.size()]);
        }

        static OS valueContained(String name) {
            String lowerName = name.toLowerCase();
            return Arrays.stream(OS.values())
                    .filter(os -> lowerName.contains(os.toString()))
                    .findFirst()
                    .orElse(OS.UNKNOWN);
        }
    }
}