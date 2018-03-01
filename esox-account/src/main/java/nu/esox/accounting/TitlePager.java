package nu.esox.accounting;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import nu.esox.util.*;

public class TitlePager
{
    public static final class Record
    {
        private final int m_number;
        private final String m_date;
        private final List<Row> m_debets = new ArrayList<Row>();
        private final List<Row> m_credits = new ArrayList<Row>();

        private Record( Verification v ) throws IOException
        {
            m_number = v.getNumber();
            m_date = Constants.DATE_FORMAT.format( v.getDate() );

            for
                ( Transaction t : v.getTransactions() )
            {
                if ( t.getAccount() == null ) continue;
                
                int account = t.getAccount().getNumber();
                double value = t.getAmount();
                if
                    ( value < 0 )
                {
                    m_credits.add( new Row( account, -value ) );
                } else {
                    m_debets.add( new Row( account, value ) );
                }

            }

            validateValues( m_credits, validateValues( m_debets ) );
        }

        public String getInfix()
        {
            String tmp = INFIX;
            tmp = tmp.replaceFirst( "%VN%", "" + m_number );
            tmp = tmp.replaceFirst( "%DATE%", "" + m_date );
            tmp = replaceValues( m_debets, "1", "2", tmp );
            tmp = replaceValues( m_credits, "3", "4", tmp );
            tmp = tmp.replaceAll( "%1?[0-9]%[1-4]%", "" );
            return tmp;
        }


        private double validateValues( List<Row> rows )
        {
            return validateValues( rows, 0 );
        }
        
        private double validateValues( List<Row> rows, double expectedSum )
        {
            if ( rows.size() > 15 ) System.err.println( "Warning: table oveflow on sheet " + m_number );
            double sum = 0;
            for
                ( int y = 0; y < rows.size(); y++ )
            {
                sum += rows.get( y ).getValue();
            }

            if
                ( expectedSum > 0 )
            {
                if ( sum != expectedSum ) System.err.println( "Warning: inconsistency (" + sum + " != " + expectedSum + ") on sheet " + m_number );
            }
            
            return sum;
        }

        private String replaceValues( List<Row> rows, String accountTag, String valueTag, String str )
        {
            for
                ( int y = 0; y < rows.size(); y++ )
            {
                str = str.replaceFirst( "%" + ( y + 1 ) + "%" + accountTag + "%", "" + rows.get( y ).getAccount() );
                str = str.replaceFirst( "%" + ( y + 1 ) + "%" + valueTag + "%", AmountTextField.FORMAT.format( rows.get( y ).getValue() ) );
            }
            return str;
        }
        
        
        public final class Row
        {
            private final int m_account;
            private final double m_value;
            
            private Row( int account, double value )
            {
                m_account = account;
                m_value = value;
            }

            public final int getAccount() { return m_account; }
            public final double getValue() { return m_value; }
        }

    }


    





    public static void doit( Collection<Verification> vs, File file ) throws Exception
    {
        JarInputStream jis = new JarInputStream( TitlePager.class.getResourceAsStream( "ver.odt" ) );
        JarOutputStream jos = new JarOutputStream( new FileOutputStream( file ) );

        JarEntry e = jis.getNextJarEntry();
        while
            ( e != null )
        {
            if
                ( ! "content.xml".equals( e.getName() ) )
            {
                jos.putNextEntry( e );

                byte [] buffer = new byte [ 1024 ];
                int len = jis.read( buffer, 0, 1024 );
                while
                    ( len > 0 )
                {
                    jos.write( buffer, 0, len );
                    len = jis.read( buffer, 0, 1024 );
                }
            }
            e = jis.getNextJarEntry();
        }

        jos.putNextEntry( new JarEntry( "content.xml" ) );
        
        PrintStream ps = new PrintStream( jos );
        ps.print( PREFIX );
        
        for
            ( Verification v : vs )
        {
            Record record = new Record( v );
            ps.print( record.getInfix() );
        }

        ps.print( POSTFIX );
        jos.closeEntry();

        jis.close();
        jos.close();
    }






    public static void main( String [] args ) throws Exception
    {
        Accounting a = new Accounting();
        new nu.esox.xml.XmlReader( new FileInputStream( "db.xml" ), a, new HashMap() );

        VerificationSet vs = a.getYears().getYear( Integer.parseInt( args[ 0 ] ) ).getVerifications();

        doit( vs, new File( args.length > 1 ? args[ 1 ] : "out.odt" ) );
    }



    private static final String PREFIX = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\" xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" xmlns:ooow=\"http://openoffice.org/2004/writer\" xmlns:oooc=\"http://openoffice.org/2004/calc\" xmlns:dom=\"http://www.w3.org/2001/xml-events\" xmlns:xforms=\"http://www.w3.org/2002/xforms\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" office:version=\"1.0\"><office:scripts/><office:font-face-decls><style:font-face style:name=\"Lucidasans1\" svg:font-family=\"Lucidasans\"/><style:font-face style:name=\"Bitstream Vera Sans1\" svg:font-family=\"&apos;Bitstream Vera Sans&apos;\" style:font-pitch=\"variable\"/><style:font-face style:name=\"Lucidasans\" svg:font-family=\"Lucidasans\" style:font-pitch=\"variable\"/><style:font-face style:name=\"Mincho\" svg:font-family=\"Mincho\" style:font-pitch=\"variable\"/><style:font-face style:name=\"Bitstream Vera Serif\" svg:font-family=\"&apos;Bitstream Vera Serif&apos;\" style:font-family-generic=\"roman\" style:font-pitch=\"variable\"/><style:font-face style:name=\"Bitstream Vera Sans\" svg:font-family=\"&apos;Bitstream Vera Sans&apos;\" style:font-family-generic=\"swiss\" style:font-pitch=\"variable\"/></office:font-face-decls><office:automatic-styles><style:style style:name=\"Table1\" style:family=\"table\"><style:table-properties style:width=\"3.1896in\" fo:break-before=\"page\" table:align=\"right\"/></style:style><style:style style:name=\"Table1.A\" style:family=\"table-column\"><style:table-column-properties style:column-width=\"1.9285in\"/></style:style><style:style style:name=\"Table1.B\" style:family=\"table-column\"><style:table-column-properties style:column-width=\"1.2611in\"/></style:style><style:style style:name=\"Table1.A1\" style:family=\"table-cell\"><style:table-cell-properties style:vertical-align=\"bottom\" fo:padding=\"0.0382in\" fo:border-left=\"0.0139in solid #000000\" fo:border-right=\"none\" fo:border-top=\"0.0139in solid #000000\" fo:border-bottom=\"0.0139in solid #000000\"/></style:style><style:style style:name=\"Table1.B1\" style:family=\"table-cell\"><style:table-cell-properties fo:padding=\"0.0382in\" fo:border=\"0.0139in solid #000000\"/></style:style><style:style style:name=\"Table1.A2\" style:family=\"table-cell\"><style:table-cell-properties fo:padding=\"0.0382in\" fo:border-left=\"0.0139in solid #000000\" fo:border-right=\"none\" fo:border-top=\"none\" fo:border-bottom=\"0.0139in solid #000000\"/></style:style><style:style style:name=\"Table1.B2\" style:family=\"table-cell\"><style:table-cell-properties fo:padding=\"0.0382in\" fo:border-left=\"0.0139in solid #000000\" fo:border-right=\"0.0139in solid #000000\" fo:border-top=\"none\" fo:border-bottom=\"0.0139in solid #000000\"/></style:style><style:style style:name=\"Table2\" style:family=\"table\"><style:table-properties style:width=\"3.55in\" table:align=\"center\"/></style:style><style:style style:name=\"Table2.A\" style:family=\"table-column\"><style:table-column-properties style:column-width=\"0.8875in\"/></style:style><style:style style:name=\"Table2.A1\" style:family=\"table-cell\"><style:table-cell-properties fo:padding=\"0.0382in\" fo:border-left=\"0.0007in solid #000000\" fo:border-right=\"none\" fo:border-top=\"0.0007in solid #000000\" fo:border-bottom=\"0.0007in solid #000000\"/></style:style><style:style style:name=\"Table2.D1\" style:family=\"table-cell\"><style:table-cell-properties fo:padding=\"0.0382in\" fo:border=\"0.0007in solid #000000\"/></style:style><style:style style:name=\"Table2.A2\" style:family=\"table-cell\"><style:table-cell-properties fo:padding=\"0.0382in\" fo:border-left=\"0.0007in solid #000000\" fo:border-right=\"none\" fo:border-top=\"none\" fo:border-bottom=\"0.0007in solid #000000\"/></style:style><style:style style:name=\"Table2.D2\" style:family=\"table-cell\"><style:table-cell-properties fo:padding=\"0.0382in\" fo:border-left=\"0.0007in solid #000000\" fo:border-right=\"0.0007in solid #000000\" fo:border-top=\"none\" fo:border-bottom=\"0.0007in solid #000000\"/></style:style><style:style style:name=\"Table3\" style:family=\"table\"><style:table-properties style:width=\"3.5472in\" fo:margin-left=\"1.5722in\" fo:margin-right=\"1.5729in\" table:align=\"margins\"/></style:style><style:style style:name=\"Table3.A\" style:family=\"table-column\"><style:table-column-properties style:column-width=\"1.1778in\" style:rel-column-width=\"21755*\"/></style:style><style:style style:name=\"Table3.B\" style:family=\"table-column\"><style:table-column-properties style:column-width=\"1.1938in\" style:rel-column-width=\"22050*\"/></style:style><style:style style:name=\"Table3.C\" style:family=\"table-column\"><style:table-column-properties style:column-width=\"1.1764in\" style:rel-column-width=\"21730*\"/></style:style><style:style style:name=\"Table3.A1\" style:family=\"table-cell\"><style:table-cell-properties fo:padding=\"0.0382in\" fo:border-left=\"0.0007in solid #000000\" fo:border-right=\"none\" fo:border-top=\"0.0007in solid #000000\" fo:border-bottom=\"0.0007in solid #000000\"/></style:style><style:style style:name=\"Table3.C1\" style:family=\"table-cell\"><style:table-cell-properties fo:padding=\"0.0382in\" fo:border=\"0.0007in solid #000000\"/></style:style><style:style style:name=\"P1\" style:family=\"paragraph\" style:parent-style-name=\"Table_20_Contents\"><style:paragraph-properties fo:text-align=\"end\" style:justify-single-word=\"false\"/></style:style><style:style style:name=\"P2\" style:family=\"paragraph\" style:parent-style-name=\"Table_20_Contents\"><style:paragraph-properties fo:text-align=\"start\" style:justify-single-word=\"false\"/></style:style><style:style style:name=\"P3\" style:family=\"paragraph\" style:parent-style-name=\"Table_20_Contents\"><style:paragraph-properties fo:text-align=\"center\" style:justify-single-word=\"false\"/></style:style><style:style style:name=\"P4\" style:family=\"paragraph\" style:parent-style-name=\"Table_20_Contents\"><style:paragraph-properties fo:text-align=\"start\" style:justify-single-word=\"false\"/><style:text-properties style:text-position=\"super 58%\" fo:font-size=\"14pt\" style:font-size-asian=\"14pt\" style:font-size-complex=\"14pt\" style:font-weight-complex=\"normal\"/></style:style><style:style style:name=\"P5\" style:family=\"paragraph\" style:parent-style-name=\"Table_20_Contents\"><style:paragraph-properties fo:text-align=\"start\" style:justify-single-word=\"false\"/><style:text-properties style:text-position=\"super 58%\" fo:font-size=\"14pt\" style:font-size-asian=\"14pt\" style:font-size-complex=\"14pt\"/></style:style></office:automatic-styles><office:body><office:text><office:forms form:automatic-focus=\"false\" form:apply-design-mode=\"false\"/><text:sequence-decls><text:sequence-decl text:display-outline-level=\"0\" text:name=\"Illustration\"/><text:sequence-decl text:display-outline-level=\"0\" text:name=\"Table\"/><text:sequence-decl text:display-outline-level=\"0\" text:name=\"Text\"/><text:sequence-decl text:display-outline-level=\"0\" text:name=\"Drawing\"/></text:sequence-decls>";
    private static final String INFIX = "<table:table table:name=\"Table1\" table:style-name=\"Table1\"><table:table-column table:style-name=\"Table1.A\"/><table:table-column table:style-name=\"Table1.B\"/><table:table-row><table:table-cell table:style-name=\"Table1.A1\" office:value-type=\"string\"><text:p text:style-name=\"Table_20_Contents\">Verifikationsnummer</text:p></table:table-cell><table:table-cell table:style-name=\"Table1.B1\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%VN%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table1.A2\" office:value-type=\"string\"><text:p text:style-name=\"P2\">Datum</text:p></table:table-cell><table:table-cell table:style-name=\"Table1.B2\" office:value-type=\"string\"><text:p text:style-name=\"P3\">%DATE%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table1.A2\" office:value-type=\"string\"><text:p text:style-name=\"P2\">Kontoutdrag</text:p></table:table-cell><table:table-cell table:style-name=\"Table1.B2\" office:value-type=\"string\"><text:p text:style-name=\"P3\"/></table:table-cell></table:table-row></table:table><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><text:p text:style-name=\"Standard\"/><table:table table:name=\"Table2\" table:style-name=\"Table2\"><table:table-column table:style-name=\"Table2.A\" table:number-columns-repeated=\"4\"/><table:table-header-rows><table:table-row><table:table-cell table:style-name=\"Table2.A1\" office:value-type=\"string\"><text:p text:style-name=\"P3\">Konto</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A1\" office:value-type=\"string\"><text:p text:style-name=\"P3\">Debet</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A1\" office:value-type=\"string\"><text:p text:style-name=\"P3\">Konto</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D1\" office:value-type=\"string\"><text:p text:style-name=\"P3\">Kredit</text:p></table:table-cell></table:table-row></table:table-header-rows><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%1%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%1%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%1%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%1%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%2%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%2%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%2%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%2%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%3%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%3%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%3%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%3%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%4%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%4%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%4%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%4%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%5%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%5%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%5%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%5%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%6%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%6%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%6%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%6%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%7%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%7%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%7%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%7%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%8%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%8%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%8%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%8%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%9%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%9%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%9%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%9%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%10%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%10%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%10%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%10%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%11%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%11%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%11%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%11%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%12%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%12%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%12%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%12%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%13%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%13%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%13%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%13%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%14%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%14%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%14%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%14%4%</text:p></table:table-cell></table:table-row><table:table-row><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%15%1%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%15%2%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.A2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%15%3%</text:p></table:table-cell><table:table-cell table:style-name=\"Table2.D2\" office:value-type=\"string\"><text:p text:style-name=\"P1\">%15%4%</text:p></table:table-cell></table:table-row></table:table><table:table table:name=\"Table3\" table:style-name=\"Table3\"><table:table-column table:style-name=\"Table3.A\"/><table:table-column table:style-name=\"Table3.B\"/><table:table-column table:style-name=\"Table3.C\"/><table:table-header-rows><table:table-row><table:table-cell table:style-name=\"Table3.A1\" office:value-type=\"string\"><text:p text:style-name=\"P4\">Kont.</text:p></table:table-cell><table:table-cell table:style-name=\"Table3.A1\" office:value-type=\"string\"><text:p text:style-name=\"P5\">Bokf.</text:p></table:table-cell><table:table-cell table:style-name=\"Table3.C1\" office:value-type=\"string\"><text:p text:style-name=\"P5\">Rev.</text:p></table:table-cell></table:table-row></table:table-header-rows></table:table>";
    private static final String POSTFIX = "<text:p text:style-name=\"Standard\"/></office:text></office:body></office:document-content>";
}
