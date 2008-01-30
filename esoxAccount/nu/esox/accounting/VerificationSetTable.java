package nu.esox.accounting;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import nu.esox.util.*;
import nu.esox.gui.*;
import nu.esox.gui.aspect.*;
import nu.esox.gui.list.*;
import nu.esox.gui.model.*;
import nu.esox.gui.layout.*;


public class VerificationSetTable extends JTable
{
    private final Action m_addAction;
    private final Action m_deleteAction;
    private final Action m_upAction;
    private final Action m_downAction;
    private final Predicate m_hasModel = new Predicate();


    private class VerificationNumberRenderer implements TableCellRenderer
    {
        public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
        {
            Component c = VerificationSetTable.this.getDefaultRenderer( value.getClass() ).getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
            if
                ( ( (Verification) getVerificationSet().get( row ) ).getAlert().length() > 0 )
            {
                c.setForeground( Color.red );
            } else {
                c.setForeground( getForeground() );
            }
            return c;
        }
    }

    public VerificationSetTable( String addLabel, String deleteLabel )
    {
        super( new TableModel() );

        setAutoCreateColumnsFromModel( false );
        setDefaultRenderer( Double.class, new AmountTableRenderer() );
        getColumn( "Nr" ).setCellRenderer( new VerificationNumberRenderer() );
        
        m_addAction = new AbstractAction( addLabel ) { public void actionPerformed( ActionEvent ev ) { add(); } };
        m_deleteAction = new AbstractAction( deleteLabel ) { public void actionPerformed( ActionEvent ev ) { delete(); } };
        m_upAction = new AbstractAction( "Upp" ) { public void actionPerformed( ActionEvent ev ) { up(); } };
        m_downAction = new AbstractAction( "Ner" ) { public void actionPerformed( ActionEvent ev ) { down(); } };
        
        new EnablePredicateAdapter( this,
                                    null,
                                    m_addAction,
                                    null,
                                    m_hasModel );
        
        new EnablePredicateAdapter( null,
                                    null,
                                    m_deleteAction,
                                    null,
                                    new AndPredicate( m_hasModel,
                                                      new ListSelectionPredicate( getSelectionModel(), ListSelectionPredicate.TEST_SOME ) ) );

        getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_INSERT, 0 ), m_addAction.getValue( Action.NAME ) );
        getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 ), m_deleteAction.getValue( Action.NAME ) );
        getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0 ), m_deleteAction.getValue( Action.NAME ) );
        getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_UP, InputEvent.ALT_MASK ), m_upAction.getValue( Action.NAME ) );
        getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, InputEvent.ALT_MASK ), m_downAction.getValue( Action.NAME ) );

        getActionMap().put( m_addAction.getValue( Action.NAME ), m_addAction );
        getActionMap().put( m_deleteAction.getValue( Action.NAME ), m_deleteAction );
        getActionMap().put( m_upAction.getValue( Action.NAME ), m_upAction );
        getActionMap().put( m_downAction.getValue( Action.NAME ), m_downAction );

        new EnablePredicateAdapter( null,
                                    null,
                                    new Action [] { m_upAction, m_downAction },
                                    null,
                                    new ListSelectionPredicate( getSelectionModel(), ListSelectionPredicate.TEST_SOME ) );
    }


    public final void setVerificationSet( VerificationSet ts )
    {
        if ( ts == ( (TableModel) getModel() ).getData() ) return;
        ( (TableModel) getModel() ).setData( ts );
        m_hasModel.set( ts != null );
    }

    public final VerificationSet getVerificationSet()
    {
        return (VerificationSet) ( (TableModel) getModel() ).getData();
    }



    public PredicateIF getHasModel() { return m_hasModel; }
    public final Action getUpAction() { return m_upAction; }
    public final Action getDownAction() { return m_downAction; }
    public final Action getAddAction() { return m_addAction; }
    public final Action getDeleteAction() { return m_deleteAction; }

    private void up()
    {
        VerificationSet vs = getVerificationSet();
        if ( vs == null ) return;

        int [] rows = getSelectedRows();
        if ( rows.length == 0 ) return;
        if ( rows[ 0 ] == 0 ) return;

        vs.beginTransaction();
        for
            ( int i = 0; i < rows.length; i++ )
        {
            vs.add( rows[ i ] - 1, vs.remove( rows[ i ] ) );
        }
        vs.endTransaction( null, null );

        getSelectionModel().clearSelection();
        for
            ( int i = 0; i < rows.length; i++ )
        {
            getSelectionModel().addSelectionInterval( rows[ i ] - 1, rows[ i ] - 1 );
        }
    }

    private void down()
    {
        VerificationSet vs = getVerificationSet();
        if ( vs == null ) return;

        int [] rows = getSelectedRows();
        if ( rows.length == 0 ) return;
        if ( rows[ rows.length - 1 ] == vs.size() - 1 ) return;

        vs.beginTransaction();
        for
            ( int i = rows.length - 1; i >= 0; i-- )
        {
            vs.add( rows[ i ] + 1, vs.remove( rows[ i ] ) );
        }
        vs.endTransaction( null, null );

        getSelectionModel().clearSelection();
        for
            ( int i = rows.length - 1; i >= 0; i-- )
        {
            getSelectionModel().addSelectionInterval( rows[ i ] + 1, rows[ i ] + 1 );
        }
    }

    private void add()
    {
        getVerificationSet().create();
        int i = getRowCount() - 1;
        setRowSelectionInterval( i, i );
    }

    private void delete()
    {
        int [] rows = getSelectedRows();
        clearSelection();
        
        for
            ( int i = rows.length - 1; i >= 0; i-- )
        {
            Verification v = (Verification) getVerificationSet().remove( rows[ i ] );
            v.dispose();
        }

        if
            ( rows[ rows.length - 1 ] < getVerificationSet().size() )
        {
            int i = rows[ rows.length - 1 ];
            setRowSelectionInterval( i, i );
        } else if
              ( ! getVerificationSet().isEmpty() )
        {
            int i = getRowCount() - 1;
            setRowSelectionInterval( i, i );
        }
    }
    
    
    private static class TableModel extends ObservableListTableModel
    {
        TableModel()
        {
            super( null );
        }
        
        protected Column [] getColumns() { return m_columns; }

        private static Verification getVerification( Object o ) { return (Verification) o; }
        
        private static Column [] m_columns =
            new Column []
            {
                new Column( "Nr", Number.class, false )
                {
                    public Object getValue( Object target ) { return getVerification( target ).getNumber(); }
                },
                
                new Column( "Text", String.class, false )
                {
                    public Object getValue( Object target ) { return getVerification( target ).getName(); }
                },
                
                new Column( "Datum", String.class, false )
                {
                    public Object getValue( Object target ) { return Constants.DATE_FORMAT.format( getVerification( target ).getDate() ); }
                },
                
//                 new Column( "Summa", Double.class, false )
//                 {
//                     public Object getValue( Object target ) { return (Double) getVerification( target ).getAmount(); }
//                 },
                
                new Column( "Omslutning", Double.class, false )
                {
                    public Object getValue( Object target ) { return (Double) getVerification( target ).getSum(); }
                },
            };
    }


}

