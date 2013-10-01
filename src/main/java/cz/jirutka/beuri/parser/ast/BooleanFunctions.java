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
package cz.jirutka.beuri.parser.ast;

/**
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public class BooleanFunctions {

    private BooleanFunctions() {}


    public static BinaryFunction union(Object... arguments) {
        return fillBinaryFunction(arguments, new UnionFunction());
    }

    public static BinaryFunction or(Object... arguments) {
        return union(arguments);
    }

    public static BinaryFunction intersect(Object... arguments) {
        return fillBinaryFunction(arguments, new IntersectFunction());
    }

    public static BinaryFunction and(Object... arguments) {
        return intersect(arguments);
    }

    public static UnaryFunction complement(Object argument) {
        if (argument instanceof BinaryFunction) {
            return new ComplementFunction((BinaryFunction) argument);
        } else {
            return new ComplementFunction(argument.toString());
        }
    }

    public static UnaryFunction not(Object argument) {
        return complement(argument);
    }


    private static BinaryFunction fillBinaryFunction(Object[] arguments, BinaryFunction func) {

        for (Object arg : arguments) {
            if (arg instanceof BooleanExpression) {
                func.add((BooleanExpression) arg);

            } else {
                func.add(arg.toString());
            }
        }
        return func;
    }
}
