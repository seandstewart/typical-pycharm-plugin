from pydantic import BaseModel


class A(BaseModel):
    ab<caret>c: str # expected

class B(A):
    abc: str # expected

A(abc='cde') # expected
B(abc='cde') # expected
## count: 4