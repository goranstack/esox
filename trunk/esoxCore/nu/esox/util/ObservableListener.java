package nu.esox.util;


import java.util.EventListener;


public interface ObservableListener extends EventListener
{
    void valueChanged( ObservableEvent e );
}
