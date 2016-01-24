package org.notjibble.MaiBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalIO_Implementer implements Modules_Interface.modules.terminalIO{
    public BufferedReader stdReader(String command, String option, String word) {
        String[] commands = null;
        BufferedReader stdOutput;
        Runtime runtime = Runtime.getRuntime();
        if (command.startsWith(".big") || command.startsWith(".gay")) {
            commands = new String[] {"toilet", "-F", "crop", "-E", "irc", option, word};
        } else if (command.startsWith(".cow")) {
            commands = new String[] {"cowsay", word};
        }
        Process process = null;
        try {
            process = runtime.exec(commands);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return stdOutput;
    }
}
