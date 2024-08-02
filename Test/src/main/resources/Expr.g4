grammar Expr;

expression
 : predicate
 ;

predicate
 : left=predicate logicalOperator right=predicate             #logicalPredicate
 | expr                                             #exprAtomPredicate
 ;

expr:
INT                           # int
| ID                            # id
| '(' expression ')'                  # parens
;

logicalOperator
 : AND | '&' '&' | OR | '|' '|'
 ;
MUL : '*' ; // assigns token name to '*' used above in grammar
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;
ID : [a-zA-Z]+ ;
INT : [0-9]+ ;
NEWLINE:'\r'? '\n' ;
WS : [ \t]+ -> skip;