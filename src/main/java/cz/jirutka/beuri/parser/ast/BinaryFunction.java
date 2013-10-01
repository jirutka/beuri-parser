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

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

/**
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public abstract class BinaryFunction extends Function {

    private final Set<BooleanExpression> arguments = new LinkedHashSet<>();


    protected BinaryFunction(Operator operator) {
        super(operator);
    }


    public Set<BooleanExpression> getArguments() {
        return unmodifiableSet(arguments);
    }

    public boolean addArgument(BooleanExpression argument) {
        arguments.add(argument);
        return true;
    }

    public BinaryFunction add(BooleanExpression... arguments) {
        this.arguments.addAll(asList(arguments));
        return this;
    }

    public BinaryFunction add(String... arguments) {
        for (String arg : arguments) {
            this.arguments.add(new Value(arg));
        }
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final BinaryFunction other = (BinaryFunction) obj;
        return new EqualsBuilder()
                .append(operator, other.operator)
                .append(arguments, other.arguments)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 17).append(operator).append(arguments).toHashCode();
    }
}
