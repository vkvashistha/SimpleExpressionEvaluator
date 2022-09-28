# Simple Expression Evaluator
Similar to Javascript eval(), this library evaluate boolean expressions or simple mathematical expressions with variables (or Symbol table) support. Following operators are supported:-  
'<', '>', '<=', '>=', '==', '!=', '+', '-', '*', '/', '&&', '||', '!'
## Usage
```
Expression.eval("1+2").asString(); // returns "3.0"
Expression.eval("1+2").asInt(); // returns 3
Expression.eval("2>3").asString(); // returns "false"
Expression.eval("2>3").asBoolean(); // returns false
Expression.eval("(3>2)||((2<4)&&(2>1))").asString(); // returns "true"
```

### With Symbol Table
```
HashMap<String, Object> st = new HashMap<String, Object>();  
st.put("a",1);  
st.put("b",2);  
st.put("c",3);  
st.put("d",4);  
Expression.eval("a+b", st).asInt();  // or simply asString()
Expression.eval("a>b",st).asBoolean();  // or simply asString()
Expression.eval("(c>b)||((b<d)&&(b>a))",st).asBoolean();  // or simply asString()
Expression.eval("(c>2)||((2<d)&&(b>1))",st).asBoolean();  // or simply asString()
```
### Create Expression using Expression Builder
``
Expression.expressionBuilder().putSymbol("a",2).putSymbol("b",3).build("(b>a)").evaluate();
``