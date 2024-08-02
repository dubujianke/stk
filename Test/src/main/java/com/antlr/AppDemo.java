package com.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class AppDemo {
    public static void main(String[] args) {
        String input = null;
        // 此处把输入的参数，直接赋值了
        args = new String[2];
        args[0] = "-input";
        args[1] = "1+2+3";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-input")) {
                input = args[++i];
            }
        }

        if (input == null) {
            System.out.println("args:  -input <expression>");
            return;
        }

        CodePointCharStream charStream = CharStreams.fromString(input);
        ExprLexer lexer = new ExprLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        ParseTree tree = parser.expr();

        int len = tree.getChildCount();

        EvalVisitor visitor = new EvalVisitor();

        Object result = visitor.visit(tree);
        System.out.println("output=" + result);
    }
}

