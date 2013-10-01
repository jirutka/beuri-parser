Parser of Boolean Expressions for URI [![Build Status](https://travis-ci.org/jirutka/beuri-parser.png)](https://travis-ci.org/jirutka/beuri-parser) [![Coverage Status](https://coveralls.io/repos/jirutka/beuri-parser/badge.png)](https://coveralls.io/r/jirutka/beuri-parser)
=====================================

TODO


Grammar (EBNF)
--------------

```plain
expression   = union;
union        = intersection,
               { ",", intersection };
intersection = atom,
               { ";", atom };
complement   = "~", atom;
atom         = complement | parens | value;
parens       = "(", expression, ")";
value        = ? ( ~[ ",", ";", "~", "(", ")" ] )+ ?;
```


Usage examples
--------------

```java
BooleanExpressionProcessor processor = new BooleanExpressionProcessor();
BooleanExpression ast = processor.parse("foo,(bar;~baz)");
```


Maven
-----

Released versions are available in The Central Repository. Just add this artifact to your project:

```xml
<dependency>
    <groupId>cz.jirutka.beuri</groupId>
    <artifactId>beuri-parser</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

However if you want to use the last snapshot version, you have to add the Sonatype OSS repository:

```xml
<repository>
    <id>sonatype-snapshots</id>
    <name>Sonatype repository for deploying snapshots</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```


License
-------

This project is licensed under [MIT license](http://opensource.org/licenses/MIT).
