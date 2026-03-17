package nu.esox.gui.aspect;

import java.util.function.*;
import nu.esox.util.*;
import nu.esox.gui.*;


public class ModelPanelAdapter extends SubModelAdapter
{
    public <M> ModelPanelAdapter( ModelPanel modelPanel,
                                  ModelOwnerIF modelOwner,
                                  Function<M, ?> getter,
                                  String aspectName )
    {
        super( modelPanel,
               (BiConsumer<ModelPanel, Object>) (p, v) -> p.setModel( (ObservableIF) v ),
               modelOwner,
               getter,
               aspectName );
    }

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
