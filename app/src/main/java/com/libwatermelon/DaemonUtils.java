package com.libwatermelon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Process;

import com.libwatermelon.WaterConfigurations.DaemonConfiguration;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import wake.tim.mimic.com.mimictim.Info;

/**
 * Created by tf on 3/8/2018.
 */

public class DaemonUtils {

    private static final String SP_ENABLE_3P = "d_3p_f";
    private static final String SP_ENABLE_3P_KEY = "enable";
    private static final String WATER_OFF_FILENAME = "water_off";
    private static String sProcessName;

    private static void close(Closeable c) {
        if (null == c) {
            return;
        }
        try {
            c.close();
        } catch (IOException e) {
            Info.y(e);
        }
    }

    private static String getProcessName() {
        if (null == sProcessName) {
            String cmdlineFile = "/proc/" + Process.myPid() + "/cmdline";
            BufferedReader reader = null;
            String name;
            try {
                reader = new BufferedReader(new FileReader(cmdlineFile));
                String line;
                StringBuilder builder = new StringBuilder();
                while (null != (line = reader.readLine())) {
                    builder.append(line).append("\n");
                }
                sProcessName = builder.toString().trim();
            } catch (IOException e) {
                Info.y(e);
                sProcessName = null;
            } finally {
                close(reader);
            }
        }
        return sProcessName;
    }

    public static void startDaemon(Context cxt) {
        WaterConfigurations configurations = new WaterConfigurations(
                new DaemonConfiguration("wake.tim.mimic.com.mimictim:MSF", DaemonTimService.class.getCanonicalName()),
                new DaemonConfiguration("wake.tim.mimic.com.mimictim:Daemon", DaemonAssistService.class.getCanonicalName()),
                new DaemonConfiguration("wake.tim.mimic.com.mimictim:assist", DaemonAssistService2.class.getCanonicalName()));


        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.MANUFACTURER != null && Build.MANUFACTURER.toLowerCase().startsWith("xiaomi")
                    || Build.MODEL != null && Build.MODEL.toLowerCase().startsWith("oppo")) {
                configurations.ENABLE_3P = cxt.getSharedPreferences(SP_ENABLE_3P, Context.MODE_MULTI_PROCESS).getBoolean(SP_ENABLE_3P_KEY, true);
            }
        }

        if (isWaterOn(cxt)) {
            String processName = getProcessName();
            String mainProcess = cxt.getPackageName();

            Info.y("startDaemon:" + processName + "||" + mainProcess);

            if (processName.equals(mainProcess)) {
                IWaterStrategy.Fetcher.fetchStrategy(configurations).onInitialization(cxt, configurations);
            } else if (processName.equals(configurations.PERSISTENT_CONFIG.PROCESS_NAME)) {
                IWaterStrategy.Fetcher.fetchStrategy(configurations).onPersistentCreate(cxt, configurations);
            } else if (processName.equals(configurations.DAEMON_ASSISTANT_CONFIG.PROCESS_NAME)) {
                IWaterStrategy.Fetcher.fetchStrategy(configurations).onDaemonAssistantCreate(cxt, configurations);
            } else if (processName.equals(configurations.DAEMON_ASSISTANT_CONFIG2.PROCESS_NAME)) {
                IWaterStrategy.Fetcher.fetchStrategy(configurations).onDaemonAssistant2Create(cxt, configurations);
            }
        }
    }

    static boolean copyAssets(Context cxt, String binaryName, String chipMode, String binaryDir) {
        File des = new File(cxt.getDir(binaryDir, Context.MODE_PRIVATE), binaryName);
        File parent = des.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            return false;
        }

        InputStream is = null;
        FileOutputStream fos = null;
        boolean success;
        try {
            is = cxt.getAssets().open(chipMode + File.separator + binaryName);
            fos = new FileOutputStream(des);
            byte[] buffer = new byte[2048];
            int len;
            while (-1 != (len = is.read(buffer))) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            success = true;
        } catch (Exception e) {
            Info.y(e);
            success = false;
        } finally {
            close(is);
            close(fos);
        }

        if (success) {
            try {
                Runtime.getRuntime().exec("chmod 777 " + des.getAbsolutePath()).waitFor();
                Info.y("Success to change mode of " + des.getAbsolutePath());
                return true;
            } catch (IOException | InterruptedException e) {
                Info.y(e);
            }
        }
        return false;
    }

    static void setWaterState(Context cxt, boolean isOff) {
        File f = new File(cxt.getFilesDir(), WATER_OFF_FILENAME);
        boolean existed = f.exists();
        if (isOff && existed) {
            f.delete();
            return;
        }

        if (!isOff && !existed) {
            File parent = f.getParentFile();
            if (null != parent && !parent.exists()) {
                if (!parent.mkdirs()) {
                    return;
                }
            }

            try {
                f.createNewFile();
            } catch (IOException e) {
                Info.y(e);
            }
        }
    }

    static boolean isWaterOn(Context cxt) {
//        return !new File(cxt.getFilesDir(), WATER_OFF_FILENAME).exists();
        return true;
    }

    public static void setEnable3P(Context cxt, boolean enable) {
        SharedPreferences.Editor editor = cxt.getSharedPreferences(SP_ENABLE_3P, Context.MODE_MULTI_PROCESS).edit();
        editor.putBoolean(SP_ENABLE_3P_KEY, enable);
        editor.commit();
    }
}