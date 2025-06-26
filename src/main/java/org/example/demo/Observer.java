package org.example.demo;

public interface Observer {

    default public void openApp(){};
    default public void othershit(){};
    default public void closeApp(){};
    default public void appShown(){};
}
