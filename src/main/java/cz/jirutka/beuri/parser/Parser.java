/*
 * The MIT License
 *
 * Copyright 2013 Jakub Jirutka <jakub@jirutka.cz>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cz.jirutka.beuri.parser;

import cz.jirutka.beuri.parser.ast.*;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.Cached;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.Var;

import static org.parboiled.errors.ErrorUtils.printParseErrors;

/**
 * Parboiled parser for the Boolean Expressions for URI.
 *
 * EBNF grammar:
 *
 * <pre>{@code
 * expression 	= union;
 * union 		= intersection,
 *                { ",", intersection };
 * intersection = atom,
 *                { ";", atom };
 * complement   = "~", atom;
 * atom         = complement | parens | value;
 * parens       = "(", expression, ")";
 * value        = ? ( ~[ ",", ";", "~", "(", ")" ] )+ ?;
 * }</pre>
 *
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public class Parser extends BaseParser<BooleanExpression> {

    public interface ParseRunnerProvider {
        ParseRunner<BooleanExpression> get(Rule rule);
    }

    private static final String
            INTERSECT = ";",
            UNION = ",",
            COMPLEMENT = "~",
            LPAREN = "(",
            RPAREN = ")";


    private final ParseRunnerProvider parseRunnerProvider;


    public Parser(ParseRunnerProvider parseRunnerProvider) {
        this.parseRunnerProvider = parseRunnerProvider;
    }


    public BooleanExpression parse(char[] source) {
        return parseInternal(source).resultValue;
    }

    protected ParsingResult<BooleanExpression> parseInternal(char[] source) {
        ParsingResult<BooleanExpression> result = parseRunnerProvider.get(Expression()).run(source);

        if (result.hasErrors()) {
            throw new RuntimeException(
                    "Internal error during expression parsing:\n--- ParseErrors ---\n"
                    + printParseErrors(result)
            );
        }

        return result;
    }


    //////// Rules ////////

    public Rule Expression() {
        return Union();
    }

    public Rule Union() {
        return BinaryOperatorRule(Intersection(), UNION);
    }

    public Rule Intersection() {
        return BinaryOperatorRule(Atom(), INTERSECT);
    }

    public Rule Complement() {
        return Sequence(
                COMPLEMENT,
                Atom(), push( new ComplementFunction(pop()) )
        );
    }

    public Rule Atom() {
        return FirstOf(
                Complement(),
                Parens(),
                Value()
        );
    }

    public Rule Parens() {
        return Sequence(
                '(',
                Union(),
                ')'
        );
    }

    public Rule Value() {
        return Sequence(
                OneOrMore(
                        TestNot(SpecialChars()),
                        ANY
                ), push(new Value(match()))
        );
    }

    @Cached
    public Rule BinaryOperatorRule(Rule subRule, String operator) {
        Var<BinaryFunction> node = new Var<>();

        return Sequence(
                subRule,
                Optional(
                        Test(operator),
                        node.set(newBinaryFunc(operator, pop())),
                        ZeroOrMore(
                                operator,
                                subRule, node.get().addArgument(pop())
                        ),
                        push(node.getAndClear())
                )
        );
    }

    public Rule SpecialChars() {
        return FirstOf(INTERSECT, UNION, COMPLEMENT, LPAREN, RPAREN);
    }


    //////// Helpers ////////

    protected BinaryFunction newBinaryFunc(String operator, BooleanExpression argument) {
        switch (operator) {
            case UNION:
                return new UnionFunction().add(argument);
            case INTERSECT:
                return new IntersectFunction().add(argument);
            default:
                throw new IllegalArgumentException("Unknown operator");
        }
    }
}
