package nu.esox.gui.aspect;


public interface AspectIF
{
    Object getAspectValue( Object model );
    void setAspectValue( Object model, Object aspectValue );
}
