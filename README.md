## Github log generator

This is a git log generator working with commits in this format:
```
    {Trello Board}-{Card ID}: {Improvement}
```

for example

```
    GEN-123: Make the world smile again
```    

Execute **GitLogApp** with four params:

```
 token owner project base head
```

for example

```
40e2d08004fc27ba4bde07ad1000f70fce0d0733 bob983 myrepo v1.0 v1.1
```

this will get you

```
## Board X##
  **X-2** - [2015-06-10] "X-2: do something" e70c2d1f679e42ab1e84db4979fd52ffdd85949c
  
## Board Y##
   **Y-10** - [2015-06-11] "Y-10: fix me" e70c2d2f678e42ab1e84db4979fd52ffdd85949c
  
```  

that renders as (the commit ids will change to proper links when used in release notes)

## Board X##
  **X-2** - [2015-06-10] "X-2: do something" e70c2d1f679e42ab1e84db4979fd52ffdd85949c
  
## Board Y##
   **Y-10** - [2015-06-11] "Y-10: fix me" e70c2d2f678e42ab1e84db4979fd52ffdd85949c
