parser grammar TotemParser;

options { tokenVocab = TotemLexer; }

root returns[String s]
    :sqlStatement{$s = $sqlStatement.s;}
    ;

sqlStatement returns[String s]
    :createStatement{$s = $createStatement.s;}//0,1
    |insertStatement{$s = $insertStatement.s;}//2
    |dropTableStatement{$s = $dropTableStatement.s;}//4
    |deleteStatement{$s = $deleteStatement.s;}//3
    |selectStatement{$s = $selectStatement.s;}//5,6
    |updateStatement{$s = $updateStatement.s;}//7
    ;

createStatement returns [String s]
    :CREATE createDeputyStatement {$s = $createDeputyStatement.s;}
    |CREATE createSrcStatement {$s = $createSrcStatement.s;}
    ;
createSrcStatement returns [String s]//0
    locals[String ret = "",String attr = "",String type = ""]
    :CLASS NAME {$ret += "id:0,classname:" + $NAME.text; $attr = "attr: "; $type = "type: ";}LR_BARCKET NAME{$attr += $NAME.text + "-@-";} (ATTRTYPE{$type += $ATTRTYPE.text + "-@-";} COMMA NAME{$attr += $NAME.text + "-@-";})* ATTRTYPE{$type += $ATTRTYPE.text;}  RR_BARCKET SEMI {$s = $ret + "," + $attr + "," + $type;}
    ;

createDeputyStatement returns [String s]//1
    locals[String ret = "",String attrsrc = "",String attrdeputy=""]
    :SELECTDEPUTY NAME{$ret += "id:1," + "classname:" + $NAME.text + ",";$attrsrc = "attr: ";$attrdeputy = "attr:";} SELECT NAME{$attrsrc += $NAME.text + "-@-";} (AS NAME{$attrdeputy += $NAME.text + "-@-";} COMMA NAME{$attrsrc += $NAME.text + "-@-";})* AS NAME{$attrdeputy += $NAME.text;} FROM NAME{$ret += "srcclassname:" + $NAME.text + ",";} WHERE whereStatement{$ret += "cond:" + $whereStatement.s.replace("null", "") + ",";} SEMI {$s = $ret + $attrsrc + "," +$attrdeputy;}
    ;

insertStatement returns [String s]//2
    locals[String ret = ""]
    :INSERT INTO NAME{$ret += "id:2,classname:" + $NAME.text + ",attr:";} VALUES LR_BARCKET (NAME COMMA{$ret += $NAME.text + "-@-";})* NAME{$ret += $NAME.text;} RR_BARCKET SEMI {$s = $ret;}
    ;

deleteStatement returns [String s]//3
    locals[String ret = ""]
    :DELETE FROM NAME{$ret += "id:3,classname:" + $NAME.text + ",";} WHERE whereStatement{$ret += "cond:" + $whereStatement.s.replace("null","");} SEMI {$s = $ret;}
    ;

dropTableStatement returns [String s]//4
    locals[String ret = ""]
    :DROP CLASS NAME{$ret += "id:4,classname:" + $NAME.text;} SEMI {$s = $ret;}
    ;


selectStatement returns [String s]
    locals[String ret = ""]
    :SELECT {$ret += "id:5," + "attr:";}(NAME COMMA {$ret += $NAME.text + "-@-";})* NAME {$ret += $NAME.text + ",";}
     FROM NAME {$ret += "classname:" + $NAME.text + ",";}
     WHERE whereStatement {$ret += "cond:" + $whereStatement.s.replace("null","");}
     SEMI {$s = $ret;}
    |SELECT NAME CROSS {$ret += "id:6,"+ "crosspath:"+ $NAME.text;} NAME {$ret += "-@-" + $NAME.text;} (CROSS NAME {$ret += "-@-" + $NAME.text;})*  DOT NAME{ $ret += "," + "attr:" + $NAME.text + ",";}
     FROM NAME {$ret += "classname:" + $NAME.text + ",";}
     WHERE whereStatement {$ret += "cond:" + $whereStatement.s.replace("null","");}
     SEMI {$s = $ret;}
    ;

updateStatement returns [String s]
    locals[String ret = ""]
    :UPDATE NAME{$ret += "id:7, classname:" + $NAME.text + ",";} SET NAME{$ret += "attr:" + $NAME.text + ",";} EQ NAME{$ret += "val:" + $NAME.text + ",";} WHERE whereStatement SEMI{$ret += "cond:" + $whereStatement.s; $s = $ret;}
    ;

/*
whereStatement returns [String s]
    locals[String ret = ""]
    :COND{$s = $COND.text;}
    |whereStatement{$ret += $whereStatement.s;} AND whereStatement{$ret += $whereStatement.s;$s = $ret;}
    |whereStatement{$ret += $whereStatement.s;} OR whereStatement{$ret += $whereStatement.s;$s = $ret;}
    |LR_BARCKET whereStatement RR_BARCKET {$s = "( " + $whereStatement.s + " )";}
    ;
*/
whereStatement  returns [String s]
    :COND whereStatementplus{$s = $COND.text + $whereStatementplus.s;}
    |LR_BARCKET whereStatement RR_BARCKET whereStatementplus{$s = "(" + $whereStatement.s + $whereStatementplus.s + ")";}
    ;

whereStatementplus returns [String s]
    :whereStatementdivide whereStatementplus(){$s = $whereStatementdivide.s + $whereStatementplus.s;}
    |{$s = "";}
    ;

whereStatementdivide returns [String s]
    :AND whereStatement {$s = " AND " + $whereStatement.s;}
    |OR whereStatement {$s = " OR " + $whereStatement.s;}
    ;


