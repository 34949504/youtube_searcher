package org.example.demo;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.dispatcher.SwingDispatchService;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javafx.application.Platform;

import java.util.ArrayList;

public class KeyListener implements NativeKeyListener,Observer {

    private final ArrayList<Integer> keyStack = new ArrayList<>();
    private static final int CTRL_KEY_CODE = NativeKeyEvent.VC_CONTROL;
    private static final int ALT_KEY_CODE = NativeKeyEvent.VC_ALT;
    private ArrayList<Observer> observer_list = new ArrayList<>();

    public KeyListener() {
        GlobalScreen.setEventDispatcher(new SwingDispatchService());

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            ex.printStackTrace();
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(this);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int keycode = e.getKeyCode();
//        System.out.println("Pressed: " + keycode + " (" + NativeKeyEvent.getKeyText(keycode) + ")");

        if (keycode == 1)
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    for (Observer observer:observer_list)
                    {
                        observer.closeApp();
                    }
                    keyStack.clear();
                }
            });
            return;
        }

        keyStack.add(keycode);

        // Keep stack small
        if (keyStack.size() > 2) {
            keyStack.remove(0);
        }

        // Check for double Ctrl
        if (keyStack.size() == 2 &&
                keyStack.get(0) == CTRL_KEY_CODE &&
                keyStack.get(1) == ALT_KEY_CODE) {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    for (Observer observer:observer_list){
                        observer.openApp();
                    }
                }
            });
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    for (Observer observer:observer_list){
                        observer.othershit();
                    }
                }
            });

            System.out.println("Double Ctrl detected!");
            keyStack.clear(); // Reset the stack
        }
    }
    public void addObserver(Observer observer){
        observer_list.add(observer);
    }


}
