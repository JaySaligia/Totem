parser grammar TotemParser;

options { tokenVocab = TotemLexer; }

root
    :sqlStatement
    ;

sqlStatement
    :createStatement
    ;

createStatement returns [String s]
    :CREATE CLASS NAME LR_BARCKET NAME (ATTRTYPE COMMA NAME)* ATTRTYPE  RR_BARCKET SEMI {$s = "test";}
    ;

