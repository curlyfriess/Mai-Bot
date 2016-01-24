package org.notjibble.MaiBot;

import java.io.BufferedReader;

public class Modules_Interface {
    interface modules {
        interface terminalIO {
            BufferedReader stdReader(String command, String option, String word);
        }
        interface bots {
            String bot_info(String pircBotInfo);
        }
        class module {
            Modules_Interface.modules.terminalIO terminalIO = new TerminalIO_Implementer();
            Modules_Interface.modules.bots bots_module = new Bots_Interface();
        }
    }
}