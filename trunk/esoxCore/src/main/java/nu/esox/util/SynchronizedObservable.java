package nu.esox.util;

public class SynchronizedObservable extends Observable
{
    public SynchronizedObservable()
    {
        super();
    }

    protected synchronized boolean checkForThenSetTransactionInProgress()
    {
        return super.checkForThenSetTransactionInProgress();
    }

    static final long serialVersionUID = -6455807394998526306L;
}
