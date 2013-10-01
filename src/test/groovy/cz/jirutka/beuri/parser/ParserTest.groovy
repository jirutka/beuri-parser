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
package cz.jirutka.beuri.parser

import cz.jirutka.beuri.parser.ast.BooleanExpression
import cz.jirutka.beuri.parser.ast.Value
import org.parboiled.Parboiled
import org.parboiled.Rule
import org.parboiled.parserunners.ParseRunner
import org.parboiled.parserunners.ReportingParseRunner
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static cz.jirutka.beuri.parser.ast.BooleanFunctions.*

/**
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
@Unroll
class ParserTest extends Specification {

    @Shared Parser parser

    def setupSpec() {
        parser = Parboiled.createParser(Parser.class, new Parser.ParseRunnerProvider() {
            public ParseRunner<BooleanExpression> get(Rule rule) {
                return new ReportingParseRunner<>(rule);
            }
        });
    }

    def 'should parse input: #input'() {
        when:
            def actual = parser.parse(input.toCharArray())
        then:
            actual == expected
        where:
            input                       | expected
            'foo'                       | new Value('foo')
            '~foo'                      | complement('foo')
            'foo,bar'                   | union('foo', 'bar')
            'foo;bar'                   | intersect('foo', 'bar')
            'foo;(bar,baz)'             | intersect('foo', union('bar', 'baz'))
            '(foo;bar),baz'             | union(intersect('foo', 'bar'), 'baz')
            'foo;~(bar,baz)'            | intersect('foo', complement(union('bar', 'baz')))
            'foo,(bar;~(baz,foo);qux)'  | union('foo', intersect('bar', complement(union('baz', 'foo')), 'qux'))
    }
}
