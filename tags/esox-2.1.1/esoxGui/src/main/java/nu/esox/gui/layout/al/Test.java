package nu.esox.gui.layout.al;


import java.awt.*;
import java.awt.event.*;
import nu.esox.al.*;
import javax.swing.*;

  /**
   * @author  Dennis Malmström
   * @version 2.0
   */
class Test extends JFrame
{
    private static int m_count = 0;

    private AttachmentPanel m_panel;

    
    public Test()
    {
        m_count++;

        addWindowListener(
                          new java.awt.event.WindowAdapter()
                          {
                              public void windowClosing( java.awt.event.WindowEvent e )
                              {
                                  m_count--;
                                  if ( m_count == 0 ) System.exit( 0 );
                              }
                          }
                          );

        m_panel = new AttachmentPanel();
        m_panel.setBackground( Color.white );
        getContentPane().add( m_panel );

        new AttachmentPanelEditor( m_panel );
    }

    public void add( Component component, Constraint constraint )
    {
        m_panel.add( component, constraint );
    }

    public static void case1()
    {
        Test f = new Test();

        JButton b1,b2,b3,b4;

        f.add(
              b1 = new JButton( "aaa" ),
              new Constraint( 
                             ContainerAttachment.create( Edge.TOP, 50 )
                             , ContainerAttachment.create( Edge.LEFT, 5 )
                             )
              );

        f.add(
              b2 = new JButton( "bbb" ),
              new Constraint(
                             ContainerAttachment.create( Edge.TOP, 100 ),
                             ContainerAttachment.create( Edge.RIGHT, -5 )
                             )
              );

        f.add(
              b3 = new JButton( "ccc" ),
              new Constraint(
                             ContainerAttachment.create( Edge.TOP, 150 ),
                             ComponentAttachment.create( Edge.LEFT, b1, Edge.RIGHT, 0 ),
                             ComponentAttachment.create( Edge.RIGHT, b2, Edge.LEFT, 0 )
                             )
              );

        f.add(
              b4 = new JButton( "ddd" ),
              new Constraint(
                             ContainerAttachment.create( Edge.TOP, 0 ),
                             ComponentAttachment.create( Edge.LEFT, b2, Edge.LEFT, -150 ),
                             ComponentAttachment.create( Edge.RIGHT, b1, Edge.RIGHT, 50 )
                             )
              );

        b1.setName( "b1" );
        b2.setName( "b2" );
        b3.setName( "b3" );
        b4.setName( "b4" );

        f.pack();
        f.setVisible( true );
    }

    public static void case2()
    {
        Test f = new Test();

        JButton b1,b2,b3,b4;

        f.add(
              b1 = new JButton( "1234567890" ),
              new Constraint( 
                             ContainerAttachment.create( Edge.TOP, 5 )
                             , RelativeAttachment.create( Edge.LEFT, 0.5f )
                             )
              );

        f.add(
              b2 = new JButton( "1234567890" ),
              new Constraint(
                             ContainerAttachment.create( Edge.TOP, 50 ),
                             ContainerAttachment.create( Edge.BOTTOM, -50 ),
                             ComponentAttachment.create( Edge.LEFT, b1, Edge.LEFT, 10 )
                             )
              );
	
	
        f.pack();
        f.setVisible( true );
    }

    public static void case3()
    {
        Test f = new Test();

        JButton b1,b2,b3,b4;

        f.add(
              b1 = new JButton( "111" ),
              new Constraint( ContainerAttachment.create( Edge.TOP, 5 ),
                              RelativeAttachment.create( Edge.LEFT, 0.25f, 5 ),
                              RelativeAttachment.create( Edge.RIGHT, 0.75f, -5 ) )
              );

        f.add(
              b2 = new JButton( "222" ),
              new Constraint( ComponentAttachment.create( Edge.TOP, b1, Edge.BOTTOM, 35 ),
                              ComponentAttachment.create( Edge.LEFT, b1, Edge.LEFT )
                              )
              );
	
        f.add(
              b3 = new JButton( "333" ),
              new Constraint( ContainerAttachment.create( Edge.BOTTOM, -5 ),
                              ContainerAttachment.create( Edge.RIGHT, -5 ),
                              ComponentAttachment.create( Edge.LEFT, b2, Edge.RIGHT, 10 ) )
              );

        f.add(
              b4 = new JButton( "444" ),
              new Constraint( ComponentAttachment.create( Edge.LEFT, b3, Edge.LEFT ),
                              ComponentAttachment.create( Edge.BOTTOM, b3, Edge.TOP ),
                              ContainerAttachment.create( Edge.RIGHT ),
                              RelativeAttachment.create( Edge.TOP, 0.5f ) )
              );

        f.pack();
        f.setVisible( true );
    }

    
    public static void case4()
    {
        Test f = new Test();

        final JLabel l1 = new JLabel( "Namn" );
        JLabel l2 = new JLabel( "Ålder" );
        JLabel l3 = new JLabel( "Kön" );
        JLabel l4 = new JLabel( "Skonummer" );
        
        JTextField t1 = new JTextField( 20 );
        JTextField t2 = new JTextField( 5 );
        JTextField t3 = new JTextField( 7 );
        JTextField t4 = new JTextField( 3 );
//         t1.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder( 10, 0, 10, 0 ), t1.getBorder() ) );
//         t2.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder( 10, 0, 10, 0 ), t2.getBorder() ) );
//         t3.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder( 10, 0, 10, 0 ), t3.getBorder() ) );
//         t4.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder( 10, 0, 10, 0 ), t4.getBorder() ) );
        t1.addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent ev )
                {
                    l1.setText( ev.getActionCommand() );
                }
            } );
                             
        f.add(
              l1,
              new Constraint(
                             ContainerAttachment.create( Edge.LEFT, 0 ),
                             ComponentAttachment.create( Edge.TOP, t1, Edge.TOP, 0 ),
                             ComponentAttachment.create( Edge.BOTTOM, t1, Edge.BOTTOM, 0 )
                             )
              );
        
        f.add(
              l2,
              new Constraint(
                             ContainerAttachment.create( Edge.LEFT, 0 ),
                             ComponentAttachment.create( Edge.TOP, t2, Edge.TOP, 0 ),
                             ComponentAttachment.create( Edge.BOTTOM, t2, Edge.BOTTOM, 0 )
                             )
              );
        
        f.add(
              l3,
              new Constraint(
                             ContainerAttachment.create( Edge.LEFT, 0 ),
                             ComponentAttachment.create( Edge.TOP, t3, Edge.TOP, 0 ),
                             ComponentAttachment.create( Edge.BOTTOM, t3, Edge.BOTTOM, 0 )
                             )
              );
        
        f.add(
              l4,
              new Constraint(
                             ContainerAttachment.create( Edge.LEFT, 0 ),
                             ComponentAttachment.create( Edge.TOP, t4, Edge.TOP, 0 ),
                             ComponentAttachment.create( Edge.BOTTOM, t4, Edge.BOTTOM, 0 )
                             )
              );
        
        f.add(
              t1,
              new Constraint( 
                             ContainerAttachment.create( Edge.TOP, 0 ),
                             ComponentAttachment.create( Edge.LEFT, new Component [] { l1, l2, l3, l4 }, SiblingAttachment.MAX, Edge.RIGHT, 0 )
                             )
              );
        
        f.add(
              t2,
              new Constraint( 
                             ComponentAttachment.create( Edge.TOP, t1, Edge.BOTTOM, 0 ),
                             ComponentAttachment.create( Edge.LEFT, new Component [] { l1, l2, l3, l4 }, SiblingAttachment.MAX, Edge.RIGHT, 0 )
                             )
              );
        
        f.add(
              t3,
              new Constraint( 
                             ComponentAttachment.create( Edge.TOP, t2, Edge.BOTTOM, 0 ),
                             ComponentAttachment.create( Edge.LEFT, new Component [] { l1, l2, l3, l4 }, SiblingAttachment.MAX, Edge.RIGHT, 0 )
                             )
              );
        
        f.add(
              t4,
              new Constraint( 
                             ComponentAttachment.create( Edge.TOP, t3, Edge.BOTTOM, 0 ),
                             ComponentAttachment.create( Edge.LEFT, new Component [] { l1, l2, l3, l4 }, SiblingAttachment.MAX, Edge.RIGHT, 0 )
                             )
              );

	
        f.pack();
        f.setVisible( true );
    }

    
    public static void main(java.lang.String[] args)
    {
        case1();
        case2();
        case3();
        case4();
    }

    static final long serialVersionUID = 42;
}
