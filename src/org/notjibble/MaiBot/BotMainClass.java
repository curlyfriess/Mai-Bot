package org.notjibble.MaiBot;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.ConnectionSettings;
import org.jibble.pircbot.PircBot;
import java.io.*;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.daemon.*;

public class BotMainClass extends PircBot implements Daemon {
    Thread channelPrint;
    Timer timer = new Timer();
    boolean timerRunning = false;

    public static void main(String[] args) throws Exception {
        BotMainClass bot = new BotMainClass();
        bot.setName(bot.parseUserOptions().getBot_Name());
        bot.setVerbose(bot.parseUserOptions().getVerbose());
        bot.connect(bot.parseUserOptions().getNetwork());
        final String channel = bot.parseUserOptions().getChannel();
        bot.joinChannel(channel);
        bot.onMessage(channel, null, null, null, null);
    }

    private void exceptionHandler(IOException e) {
        System.out.println(e.getMessage());
        System.exit(0);
    }

    public userOptions parseUserOptions() throws IOException {
        final userOptions userOptions;
        final Properties prop = new Properties();
        StringBuilder filePath = new StringBuilder();
        final String propFileName = ".config.properties";
        filePath = filePath.append(System.getProperty("user.home")).append(File.separatorChar).append(propFileName);
        final FileInputStream file = new FileInputStream(filePath.toString());
        prop.load(file);
        final String server = null;
        final String bot_name = prop.getProperty("bot_name");
        final boolean verbose = Boolean.valueOf(prop.getProperty("verbose"));
        final String network = prop.getProperty("network");
        final String channel = prop.getProperty("channel");
        final int port = Integer.parseInt(prop.getProperty("port"));
        final boolean useSSL = Boolean.valueOf(prop.getProperty("useSSL"));
        final boolean verifySSL = Boolean.valueOf(prop.getProperty("verfiySSL"));
        final String password = prop.getProperty("password");
        userOptions = new userOptions(server, bot_name, verbose, network, channel, port, useSSL, verifySSL,
                password);
        return userOptions;
    }

    @Override
    protected ConnectionSettings clone() {
        ConnectionSettings var1 = null;
        try {
            var1 = new ConnectionSettings((this.parseUserOptions().getServer()));
        } catch (IOException e) {
            exceptionHandler(e);
        }
        try {
            var1.port = this.parseUserOptions().getPort();
        } catch (IOException e) {
            exceptionHandler(e);
        }
        try {
            var1.useSSL = this.parseUserOptions().getUseSSL();
        } catch (IOException e) {
            exceptionHandler(e);
        }
        try {
            var1.verifySSL = this.parseUserOptions().getVerifySSL();
        } catch (IOException e) {
            exceptionHandler(e);
        }
        try {
            var1.password = this.parseUserOptions().getPassword();
        } catch (IOException e) {
            exceptionHandler(e);
        }
        return var1;
    }

    @Override
    protected void onMessage(String s, String s1, String s2, String s3, String s4) {
        Modules_Interface.modules.module module = new Modules_Interface.modules.module();
        final int wordLengthLimit = 7;
        final int timerLength = 300000;
        final String version = getVersion();

        channelPrint = new Thread() {
            public void run() {
                if (!timerRunning) {
                    String[] splitString = s4.split(" ");
                    String word = splitString[1];
                    int wordLength = word.length();
                    if (!word.startsWith("-") && wordLength <= wordLengthLimit && !word.isEmpty()) {
                        if (splitString[0].equals(".big") || splitString[0].equals(".gay")
                                || splitString[0].equals(".cow")) {
                            String option = null;
                            if (s4.startsWith(".big")) {
                                option = "";
                            } else if (s4.startsWith(".gay")) {
                                option = "--gay";
                            }
                            timerRunning = true;
                            timer.schedule(new setTimer(), timerLength);
                            String line;
                            BufferedReader stdOutput = module.terminalIO.stdReader(s4, option, word);
                            try {
                                while ((line = stdOutput.readLine()) != null) {
                                    if (!s4.equals(".gay")) {
                                        sendMessage(s, Colors.GREEN.concat(line));
                                    } else {
                                        sendMessage(s, line);
                                    }
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }
                if (s4.startsWith(".bots")) {
                    String info = module.bots_module.bot_info(version);
                    sendMessage(s, info);
                }
            }
        };
        if (!channelPrint.isAlive()) {
            channelPrint.start();
        }
    }

    private class setTimer extends TimerTask {
        @Override
        public void run() {
            timerRunning = false;
        }
    }

    private class userOptions {
        final String server;
        final String bot_name;
        final boolean verbose;
        final String network;
        final String channel;
        final int port;
        final boolean useSSL;
        final boolean verifySSL;
        final String password;

        private userOptions(String server, String bot_name, boolean verbose, String network, String channel, int port,
                            boolean useSSL, boolean verifySSL, String password) {
            this.server = server;
            this.bot_name = bot_name;
            this.verbose = verbose;
            this.network = network;
            this.channel = channel;
            this.port = port;
            this.useSSL = useSSL;
            this.verifySSL = verifySSL;
            this.password = password;
        }

        private String getServer() {
            return server;
        }

        private String getBot_Name() {
            return bot_name;
        }

        private boolean getVerbose() {
            return verbose;
        }

        private String getNetwork() {
            return network;
        }

        private String getChannel() {
            return channel;
        }

        private int getPort() {
            return port;
        }

        private boolean getUseSSL() {
            return useSSL;
        }

        private boolean getVerifySSL() {
            return verifySSL;
        }

        private String getPassword() {
            return password;
        }
    }

    @Override
    public void init(DaemonContext dc) throws Exception {
    }

    @Override
    public void start() throws Exception {
        main(null);
    }

    @Override
    public void stop() throws Exception {
        channelPrint.interrupt();
        timer.cancel();
    }

    @Override
    public void destroy() {
    }
}
