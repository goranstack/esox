package nu.esox.gui.list;


import javax.swing.*;
import javax.swing.event.*;
import nu.esox.util.*;


public class ListSelectionPredicate extends Predicate implements ListSelectionListener
{
    public interface Test
    {
        boolean test( ListSelectionModel selectionModel );
    }


    public static final Test TEST_NONE = new CountTest( 0, 0 );
    public static final Test TEST_ONE = new CountTest( 1, 1 );
    public static final Test TEST_SOME = new CountTest( 1, Integer.MAX_VALUE );

    
    private final ListSelectionModel m_selectionModel;
    private final Test m_test;

    
    public ListSelectionPredicate( ListSelectionModel selectionModel, Test test )
    {
        m_selectionModel = selectionModel;
        m_test = test;
        m_selectionModel.addListSelectionListener( this );
        
        update();
    }

    public void valueChanged( ListSelectionEvent ev )
    {
        if ( ! ev.getValueIsAdjusting() ) update();
    }
   

    protected final void update()
    {
        set( m_test.test( m_selectionModel ) && pass() );
    }

    protected boolean pass() { return true; }



    
    public static class CountTest implements Test
    {
        private final int m_min;
        private final int m_max;

        public CountTest( int min, int max )
        {
            m_min = min;
            m_max = max;
        }

        public boolean test( ListSelectionModel selectionModel )
        {
            int count = 0;
            if
                ( ! selectionModel.isSelectionEmpty() )
            {
                for
                    ( int i = selectionModel.getMinSelectionIndex();
                      i <= selectionModel.getMaxSelectionIndex();
                      i++ )
                {
                    if ( selectionModel.isSelectedIndex( i ) ) count++;
                }
            }

            return count <= m_max && count >= m_min;
        }
    }

    static final long serialVersionUID = 42;
}
