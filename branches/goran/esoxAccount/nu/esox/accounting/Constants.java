package nu.esox.accounting;

import java.util.*;
import java.text.*;


class Constants
{
    public static final DateFormat DATE_FORMAT =
        new SimpleDateFormat( "yyyy-MM-dd" )
        {
            public StringBuffer format( Date date, StringBuffer toAppendTo, FieldPosition pos )
            {
                return ( date == null ) ? toAppendTo : super.format( date, toAppendTo, pos );
            }
        };
}
