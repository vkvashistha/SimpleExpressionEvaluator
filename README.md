# SimpleExpressionEvaluator
Evaluate boolean expressions with symbol table support
## Usage
```
SimpleExpressionEvaluator.evaluate("1+2"); // returns "3.0"
SimpleExpressionEvaluator.evaluate("2>3"); // returns "false"
SimpleExpressionEvaluator.evaluate("(3>2)||((2<4)&&(2>1))"); // returns "true"
```

### With Symbol Table
```
HashMap<String, Object> st = new HashMap<String, Object>();  
st.put("a",1);  
st.put("b",2);  
st.put("c",3);  
st.put("d",4);  
SimpleExpressionEvaluator.evaluate("a+b", st);  
SimpleExpressionEvaluator.evaluate("a>b",st);  
SimpleExpressionEvaluator.evaluate("(c>b)||((b<d)&&(b>a))",st);
```