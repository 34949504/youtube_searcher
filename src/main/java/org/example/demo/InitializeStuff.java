package org.example.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class InitializeStuff {

    private File appDataDir;
    private File lockFile;
    private FileLock lock;

    public void init() {
        setAppDataDirectory();
        createAppDataDirectoryIfNeeded();
        createLockFileIfNeeded();
        lockFileIfPossible();
    }

    private void setAppDataDirectory() {
        String appDataDirName = "youtube_searcher";
        String baseDir = System.getenv("APPDATA");

        if (baseDir == null || baseDir.isEmpty()) {
            baseDir = System.getProperty("user.home");
        }

        appDataDir = new File(baseDir, appDataDirName);
        lockFile = new File(appDataDir, "lock.lock");
    }

    private void createAppDataDirectoryIfNeeded() {
        if (!appDataDir.exists()) {
            if (appDataDir.mkdirs()) {
                System.out.println("Created app data directory: " + appDataDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create app data directory: " + appDataDir.getAbsolutePath());
            }
        }
    }

    private void createLockFileIfNeeded() {
        if (!lockFile.exists()) {
            try {
                if (lockFile.createNewFile()) {
                    System.out.println("Created lock file: " + lockFile.getAbsolutePath());
                } else {
                    System.err.println("Could not create lock file.");
                }
            } catch (IOException e) {
                System.err.println("Error while creating lock file:");
                e.printStackTrace();
            }
        }
    }

    private void lockFileIfPossible() {
        try {
            FileChannel channel = new FileOutputStream(lockFile, true).getChannel();
            lock = channel.tryLock();

            if (lock != null) {
                System.out.println("Lock acquired on: " + lockFile.getAbsolutePath());
                // Keep reference to lock so it doesn't get GC'ed
            } else {
                System.err.println("Could not acquire lock; file is already locked.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Error trying to lock file:");
            System.exit(1);
            e.printStackTrace();
        }
    }
}
