# Simple Expression Evaluator
Evaluate boolean expressions or simple mathematical expressions with variables support
## Usage
```
Expression.eval("1+2").asString(); // returns "3.0"
Expression.eval("2>3"); // returns "false"
Expression.eval("(3>2)||((2<4)&&(2>1))"); // returns "true"
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
```