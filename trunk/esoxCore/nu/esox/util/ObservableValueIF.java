package nu.esox.util;


public interface ObservableValueIF<T> extends ObservableIF
{
    T get();
    void set( T value );
}
