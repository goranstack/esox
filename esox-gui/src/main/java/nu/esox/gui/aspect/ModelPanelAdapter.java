package nu.esox.gui.aspect;

import nu.esox.util.*;
import nu.esox.gui.*;


public class ModelPanelAdapter extends SubModelAdapter
{
    public ModelPanelAdapter( ModelPanel modelPanel,
                              ModelOwnerIF modelOwner,
                              Class modelClass,
                              String getAspectMethodName,
                              String aspectName )
    {
        super( modelPanel,
               "setModel",
               ObservableIF.class,
               modelOwner,
               modelClass,
               getAspectMethodName,
               aspectName );
    }

    
}
