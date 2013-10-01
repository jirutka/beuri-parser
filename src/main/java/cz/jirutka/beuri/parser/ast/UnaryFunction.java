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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

/**
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public abstract class UnaryFunction extends Function {

    public BooleanExpression argument;


    protected UnaryFunction(Operator operator) {
        super(operator);
    }

    protected UnaryFunction(Operator operator, BooleanExpression argument) {
        super(operator);
        this.argument = argument;
    }


    public Set<BooleanExpression> getArguments() {
        return unmodifiableSet(new HashSet<>(asList(argument)));
    }

    public BooleanExpression getArgument() {
        return argument;
    }

    public boolean setArgument(BooleanExpression argument) {
        this.argument = argument;
        return true;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final UnaryFunction other = (UnaryFunction) obj;
        return new EqualsBuilder()
                .append(operator, other.operator)
                .append(argument, other.argument)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(5, 83).append(operator).append(argument).toHashCode();
    }
}
