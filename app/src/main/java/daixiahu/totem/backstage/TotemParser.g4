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
    locals[String ret = ""]
    :SELECTDEPUTY NAME{$ret += "id:1," + "classname:" + $NAME.text + ",";} SELECT NAME{$ret += "attr1:" + $NAME.text + ",";} ((AS NAME{$ret += "attr2:" + $NAME.text + ",";})? COMMA NAME{$ret += "attr1:" + $NAME.text + ",";})* (AS NAME{$ret += "attr2:" + $NAME.text + ",";})? FROM NAME{$ret += "srcclassname:" + $NAME.text + ",";} WHERE whereStatement{$ret += "cond:" + $whereStatement.s.replace("null", "") + ",";} SEMI {$s = $ret;}
    ;

insertStatement returns [String s]//2
    locals[String ret = ""]
    :INSERT INTO NAME{$ret += "id:2,classname:" + $NAME.text + ",";} VALUES LR_BARCKET (NAME COMMA{$ret += "attr:" + $NAME.text + ",";})* NAME{$ret += "attr:" + $NAME.text;} RR_BARCKET SEMI {$s = $ret;}
    ;

dropTableStatement returns [String s]
    locals[String ret = ""]
    :DROP CLASS NAME{} SEMI
    ;

deleteStatement returns [String s]
    locals[String ret = ""]
    :DELETE FROM NAME{} WHERE whereStatement
    ;

selectStatement returns [String s]
    :SELECT NAME (COMMA NAME)* FROM NAME WHERE whereStatement
    |SELECT NAME (CROSS NAME)+ FROM NAME WHERE whereStatement
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
    |LR_BARCKET whereStatement RR_BARCKET whereStatementplus{$s = "( " + $whereStatement.s + $whereStatementplus.s + " )";}
    ;

whereStatementplus returns [String s]
    :whereStatementdivide whereStatementplus(){$s = $whereStatementdivide.s + $whereStatementplus.s;}
    |{$s = "";}
    ;

whereStatementdivide returns [String s]
    :AND whereStatement {$s = "AND" + $whereStatement.s;}
    |OR whereStatement {$s = "OR" + $whereStatement.s;}
    ;

