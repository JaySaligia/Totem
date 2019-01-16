parser grammar TotemParser;

options { tokenVocab = TotemLexer; }

root returns[String s]
    :sqlStatement{$s = $sqlStatement.s;}
    ;

sqlStatement returns[String s]
    :createStatement{$s = $createStatement.s;}
    ;

createStatement returns [String s]
    :CREATE createDeputyStatement
    |CREATE createSrcStatement
    ;
createSrcStatement returns [String s]
    locals[String ret = ""]
    :CLASS NAME{$ret += "id:0," + "classname:" + $NAME.text + ",";} LR_BARCKET NAME{$ret += "attr:" + $NAME.text + ",";} (ATTRTYPE {$ret += "type:" + $ATTRTYPE.text + ",";}COMMA NAME{$ret += "attr:" + $NAME.text + ",";})* ATTRTYPE {$ret += "type:" + $ATTRTYPE.text;} RR_BARCKET SEMI {$s = $ret;}
    ;

createDeputyStatement returns [String s]
    :SELECTDEPUTY
    ;



