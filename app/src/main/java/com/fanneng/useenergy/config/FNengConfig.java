package com.fanneng.useenergy.config;

import android.app.Application;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FNengConfig  {
    private static Application sApplication;

        public static synchronized Application getApplication() {
            if (sApplication == null) {
                sApplication = getSystemApp();
            }
            return sApplication;
        }

        private static Application getSystemApp() {
            try {
                Class<?> activitythread = Class.forName("android.app.ActivityThread");

                Method m_currentActivityThread = activitythread
                        .getDeclaredMethod("currentActivityThread");
                Field f_mInitialApplication = activitythread.getDeclaredField("mInitialApplication");
                f_mInitialApplication.setAccessible(true);
                Object current = m_currentActivityThread.invoke(null);

                Object app = f_mInitialApplication.get(current);

                return (Application) app;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
}

