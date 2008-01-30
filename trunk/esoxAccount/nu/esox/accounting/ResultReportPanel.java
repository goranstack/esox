package nu.esox.accounting;

import javax.swing.*;
import nu.esox.util.*;


public class ResultReportPanel extends ReportPanel
{
    public ResultReportPanel()
    {
        init( "result-table",
              new Account.Type []
              {
                  Account.TYPE_SALES_REVENUE,
                  Account.TYPE_OTHER_REVENUE,
                  Account.TYPE_MATERIAL_EXPENSES,
                  Account.TYPE_OTHER_EXPENSES,
                  Account.TYPE_WRITE_OFFS,
                  Account.TYPE_FINANCIAL_REVENUE,
                  Account.TYPE_FINANCIAL_EXPENSES,
                  Account.TYPE_EXTRAORDINARY_RESULT,
                  Account.TYPE_RESULT,
              },
              new Section []
              {
                  new TypeSection( 0 ),
                  new TypeSection( 1 ),
                  new SumSection( "Summa rörelseintäkter", new int [] { 0, 1 } ),
                  new TypeSection( 2 ),
                  new SumSection( "Bruttovinst", new int [] { 0, 1, 2 } ),
                  new TypeSection( 3 ),
                  new SumSection( "Resultat före avskrivningar", new int [] { 0, 1, 2, 3 } ),
                  new TypeSection( 4 ),
                  new SumSection( "Resultat efter avskrivningar", new int [] { 0, 1, 2, 3, 4 } ),
                  new TypeSection( 5 ),
                  new TypeSection( 6 ),
                  new TypeSection( 7 ),
                  new TypeSection( 8 ),
                  new SumSection( "Beräknat resultat", new int [] { 0, 1, 2, 3, 4, 5, 6, 7 ,8 } ),
              }
              );
    }

    protected JTable createTable( Account account )
    {
        return new ResultReportTable( account );
    }
    
    protected JTable createTable( ObservableList accounts )
    {
        return new ResultReportTable( accounts );
    }
}

