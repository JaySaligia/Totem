lexer grammar TotemLexer;

CREATE: [Cc][Rr][Ee][Aa][Tt][Ee];
CLASS: [Cc][Ll][Aa][Ss][Ss];
ATTRTYPE:  ([Ss][Tt][Rr][Ii][Nn][Gg]) | ([Ii][Nn][Tt]);
NAME: [A-Za-z0-9]+;
LR_BARCKET: '(';
RR_BARCKET: ')';
COMMA: ',';
SEMI: ';';
WS:  [ \t\r\n] + ->skip;