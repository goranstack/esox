package nu.esox.gui.layout.al;


import java.awt.*;
import nu.esox.al.*;

  /**
   * Utility functions for attaching components to other components.
   * 
   * @author  Dennis Malmström
   * @version 2.0
   */
public class ComponentAttachment
{
    private ComponentAttachment()
    {
    }
    
      /**
       * Creates an instance of SiblingAttachment attaching to a Component.
       * 
       * @param edge The attached edge.
       * @param component Component to which the edge is attached.
       * @param componentEdge The edge of component to which the edge is attached.
       */
    public static SiblingAttachment create( Edge edge, Component component, Edge componentEdge )
    {
        return SiblingAttachment.create( edge, create( component ), componentEdge );
    }

    
      /**
       * Creates an instance of SiblingAttachment attaching to a Component.
       * 
       * @param edge The attached edge.
       * @param component Component to which the edge is attached.
       * @param componentEdge The edge of component to which the edge is attached.
       * @param offset Offset from componentEdge.
       */
    public static SiblingAttachment create( Edge edge, Component component, Edge componentEdge, int offset )
    {
        return SiblingAttachment.create( edge, create( component ), componentEdge, offset );
    }

    
      /**
       * Creates an instance of SiblingAttachment attaching to a set of Components.
       * 
       * @param edge The attached edge.
       * @param components Components to which the edge is attached.
       * @param pos Max or min position among components to which the edge is attached.
       * @param componentEdge The edge of component to which the edge is attached.
       */
    public static SiblingAttachment create( Edge edge, Component [] components, SiblingAttachment.Position pos, Edge componentEdge )
    {
        return SiblingAttachment.create( edge, create( components ), pos, componentEdge );
    }

    
      /**
       * Creates an instance of SiblingAttachment attaching to a set of Components.
       * 
       * @param edge The attached edge.
       * @param components Components to which the edge is attached.
       * @param pos Max or min position among components to which the edge is attached.
       * @param offset Offset from componentEdge.
       */
    public static SiblingAttachment create( Edge edge, Component [] components, SiblingAttachment.Position pos, Edge componentEdge, int offset )
    {
        return SiblingAttachment.create( edge, create( components ), pos, componentEdge, offset );
    }


    
    private static Attachable create( Component c )
    {
        return new ComponentAttachable( c );
    }

    private static Attachable [] create( Component [] c )
    {
        Attachable [] a = new Attachable [ c.length ];
        for
            ( int i = 0; i < a.length; i++ )
        {
            a[ i ] = new ComponentAttachable( c[ i ] );
        }
        return a;
    }
    
}
