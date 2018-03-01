package nu.esox.util;

public interface ObservableIF
{
    public void addObservableListener( ObservableListener l );
    public void removeObservableListener( ObservableListener l );
    public void clearObservableListeners();
    
    public boolean beginTransaction();
    public boolean beginTransaction( boolean collectTransaction );
    public void endTransaction( String info, Object data );
}
