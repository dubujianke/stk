package com.antlr;


public class EvalVisitor extends ExprBaseVisitor<Integer> {
    @Override
    public Integer visitAddSub(ExprParser.AddSubContext ctx) {
        Integer left = visit(ctx.expr(0));  // should call "visit", not "visitChildren"
        Integer right = visit(ctx.expr(1));
        if (ctx.op.getType() == ExprLexer.ADD) {
            return left + right;
        } else {
            return left - right;
        }
    }

    @Override
    public Integer visitInt(ExprParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }
}
