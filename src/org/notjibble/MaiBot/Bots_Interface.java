package org.notjibble.MaiBot;

/*
 *@see Modules_Interface.modules
 */
public class Bots_Interface implements Modules_Interface.modules.bots {
    public String bot_info(String pircBotInfo) {
        final String bot_info;
        bot_info = "Reporting in! [Java] " + pircBotInfo;
        return bot_info;
    }
}