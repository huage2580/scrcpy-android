package org.las2mile.scrcpy;

import java.io.IOException;

public final class Server {

    private static String ip = null;

    private Server() {
        // not instantiable
    }

    private static void scrcpy(Options options) throws IOException {
        final Device device = new Device(options);
        try (DroidConnection connection = DroidConnection.open(ip)) {
            ScreenEncoder screenEncoder = new ScreenEncoder(options.getBitRate());

            // asynchronous
            startEventController(device, connection, options);

            try {
                // synchronous
                screenEncoder.streamScreen(device, connection.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                // this is expected on close
                Ln.d("Screen streaming stopped");

            }
        }finally {
            Device.setScreenPowerMode(Device.POWER_MODE_NORMAL);
        }
    }

    private static void startEventController(final Device device, final DroidConnection connection, Options options) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new EventController(device, connection).control(options);
                } catch (IOException e) {
                    // this is expected on close
                    Ln.d("Event controller stopped");
                }
            }
        }).start();
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private static Options createOptions(String... args) {
        Options options = new Options();

        if (args.length < 1) {
            return options;
        }
        ip = String.valueOf(args[0]);


        if (args.length < 2) {
            return options;
        }
        int maxSize = Integer.parseInt(args[1]) & ~7; // multiple of 8
        options.setMaxSize(maxSize);

        if (args.length < 3) {
            return options;
        }
        int bitRate = Integer.parseInt(args[2]);
        options.setBitRate(bitRate);

        if (args.length < 4) {
            return options;
        }

        boolean turnScreenOff = Boolean.parseBoolean(args[3]);
        options.setTurnScreenOff(turnScreenOff);

        if (args.length < 5) {
            return options;
        }

        // use "adb forward" instead of "adb tunnel"? (so the server must listen)
        boolean tunnelForward = Boolean.parseBoolean(args[4]);
        options.setTunnelForward(tunnelForward);
        return options;
    }

    public static void main(String... args) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Ln.e("Exception on thread " + t, e);
            }
        });

        try {
            Process cmd = Runtime.getRuntime().exec("rm /data/local/tmp/scrcpy-server.jar");
            cmd.waitFor();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        Options options = createOptions(args);
        scrcpy(options);
    }
}

